/**
 * Copyright: Copyright (c) 2015 
 * 
 * @author youaremoon
 * @date 2016年1月1日
 * @version V1.0
 */
package com.yam.trace.core.proxy;

import java.io.ByteArrayInputStream;
import java.security.ProtectionDomain;
import java.util.HashSet;
import java.util.Set;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtBehavior;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtField;
import javassist.CtMethod;
import javassist.Modifier;
import javassist.NotFoundException;

import com.yam.trace.core.config.ClassMethodConfig;
import com.yam.trace.core.trace.TraceEntry;
import com.yam.trace.core.util.PatternUtil;
import com.yam.trace.core.util.TraceLogger;

/**
 * @Description: 根据拦截信息为类创建代理
 * @author youaremoon
 * @date 2016年1月1日 下午2:40:06
 *
 */
public class ByteCodeProxy implements IInterceptProxy {
	private static final String INSTANCE_FIELD_NAME = "$interceptEntry";
	private static final String INSTANCE_METHOD_NAME = "$getInterceptEntry";
	private static final String INSTANCE_FIELD_SRC = String.format("private %s %s;", 
			TraceEntry.TRACE_CLASSNAME, INSTANCE_FIELD_NAME);
	private static final String INSTANCE_METHOD_SRC = String.format("protected %s %s(){if(null==%s){%s=%s;}return %s;}", 
			TraceEntry.TRACE_CLASSNAME, INSTANCE_METHOD_NAME, INSTANCE_FIELD_NAME, INSTANCE_FIELD_NAME, TraceEntry.TRACE_NEW_INSTANCE, INSTANCE_FIELD_NAME); 

	private static final Set<String> EXCLUDE_METHODS = new HashSet<String>();
	static {
		EXCLUDE_METHODS.add("main");
		EXCLUDE_METHODS.add("wait");
		EXCLUDE_METHODS.add("equals");
		EXCLUDE_METHODS.add("toString");
		EXCLUDE_METHODS.add("hashCode");
		EXCLUDE_METHODS.add("getClass");
		EXCLUDE_METHODS.add("notify");
		EXCLUDE_METHODS.add("notifyAll");
		EXCLUDE_METHODS.add(INSTANCE_METHOD_NAME);
	}
	
	/**
	 * 根据配置信息为类生成代理
	 */
	@Override
	public byte[] proxyFor(ClassLoader loader, ProtectionDomain protectionDomain, String className, byte[] classfileBuffer, ClassMethodConfig classMethodConfig) {
		ClassPool pool = ClassPool.getDefault();
		CtClass cls = null;
		try {
			cls = pool.makeClass(new ByteArrayInputStream(classfileBuffer));
			if (!canProxy(cls)) {
				return classfileBuffer;
			}
			
			addTraceEntry(cls);

			// 拦截构造方法
			boolean interceptConstructor = interceptConstructor(cls, classMethodConfig);
			// 拦截普通方法
			boolean interceptMethod = interceptMethod(cls, classMethodConfig);
			
			// 没有执行过拦截则直接返回原类
			if (!interceptConstructor && !interceptMethod) {
				return classfileBuffer;
			}

			// 输出代理后的代码，可使用反编译工具查看
			String writeFile = classMethodConfig.getWriteFile();
			if (null != writeFile && writeFile.length() > 0) {
				cls.writeFile(writeFile);
			}
			
			TraceLogger.printToConsole("create proxy success: " + className, true);
			return cls.toBytecode();
		} catch (Exception e) {
			// 直接打印出来，如果看到这种信息，请联系作者
			TraceLogger.printToConsole("try to create proxy fail, class name:" + className, e);
		} finally {
			if (null != cls) {
				cls.detach();
			}
		}
		
		return null;
	}
	
	private void addTraceEntry(CtClass cls) throws CannotCompileException {
		CtMethod[] ms = cls.getMethods();
		
		// 如果父类中存在，且能访问则不用再加
		try {
			for (CtMethod m : ms) {
				int modifiers = m.getModifiers();
				if (INSTANCE_METHOD_NAME.equals(m.getName()) && m.getParameterTypes().length == 0
						&& (Modifier.isProtected(modifiers) || Modifier.isPublic(modifiers))) {
					return;
				}
			}
		} catch (NotFoundException e) {
			TraceLogger.printToConsole(e);
		}
		
		// 添加字段
		CtField f = CtField.make(INSTANCE_FIELD_SRC, cls);
		cls.addField(f);
		
		// 添加方法
		CtMethod m = CtMethod.make(INSTANCE_METHOD_SRC, cls);
		cls.addMethod(m);
	}
	
	/**
	 * 对构造方法生成代理
	 * @param cls
	 * @param interceptInfo
	 * @return
	 * @throws CannotCompileException
	 * @throws NotFoundException
	 */
	private boolean interceptConstructor(CtClass cls, ClassMethodConfig classMethodConfig) throws CannotCompileException, NotFoundException {
		if (!classMethodConfig.interstAt(ClassMethodConfig.Interst.CONSTRUCTOR)) {
			return false;
		}
		
		boolean intercept = false;
		CtConstructor[] ccs = cls.getDeclaredConstructors();
		for (CtConstructor cc : ccs) {
			if (!shouldProxy(classMethodConfig, cc)) {
				continue;
			}
			
//			cc.insertBefore(createInterceptCall(cc, InterceptEntry.INTERCEPT_CONSTRUCTOR_BEFORE_PREFIX, false, true));
			cc.insertAfter(createInterceptCall(cc, INSTANCE_METHOD_NAME + "()." + TraceEntry.TRACE_CONSTRUCTOR_AFTER_PREFIX, 
					true, false, false, false));
			
			intercept = true;
		}
		
		return intercept;
	}
	
	/**
	 * 对普通方法生成代理
	 * @param cls
	 * @param interceptInfo
	 * @return
	 * @throws CannotCompileException
	 * @throws NotFoundException
	 */
	private boolean interceptMethod(CtClass cls, ClassMethodConfig classMethodConfig) throws CannotCompileException, NotFoundException {
		boolean intercept = false;
		
		CtMethod[] methods = cls.getDeclaredMethods();
		for (CtMethod method : methods) {
			if (!shouldProxy(classMethodConfig, method)) {
				continue;
			}
			
			// 静态方法与非静态方法调用不同的方法
			if (Modifier.isStatic(method.getModifiers())) {
				boolean traceBefore = classMethodConfig.interstAt(ClassMethodConfig.Interst.STATIC_BEFORE);
				boolean traceAfter = classMethodConfig.interstAt(ClassMethodConfig.Interst.STATIC_AFTER);
				if (traceBefore || traceAfter) {
					method.insertBefore(createInterceptCall(method, 
							TraceEntry.TRACE_DEFAULT_INSTANCE + "." + TraceEntry.TRACE_STATIC_METHOD_BEFORE_PREFIX, 
							traceBefore, true, true, false));
					method.insertAfter(createInterceptCall(method, 
							TraceEntry.TRACE_DEFAULT_INSTANCE + "." + TraceEntry.TRACE_STATIC_METHOD_AFTER_PREFIX, 
						traceAfter, true, true, true));
					intercept = true;
				}
			} else {
				boolean traceBefore = classMethodConfig.interstAt(ClassMethodConfig.Interst.METHOD_BEFORE);
				boolean traceAfter = classMethodConfig.interstAt(ClassMethodConfig.Interst.METHOD_AFTER);
				if (traceBefore || traceAfter) {
					method.insertBefore(createInterceptCall(method, 
							INSTANCE_METHOD_NAME + "()." + TraceEntry.TRACE_METHOD_BEFORE_PREFIX,
							traceBefore, true, false, false));
					method.insertAfter(createInterceptCall(method, 
							INSTANCE_METHOD_NAME + "()." + TraceEntry.TRACE_METHOD_AFTER_PREFIX,
						traceAfter, true, false, true));
					intercept = true;
				}
			}
		}
		
		return intercept;
	}
	
	private String createInterceptCall(CtBehavior behavior, String prefix, boolean trace, boolean needMethodName, boolean isStatic, boolean needResult)
			throws NotFoundException {
		StringBuilder sb = new StringBuilder();

		// 添加前缀-调用语句的语句
		sb.append(prefix);
		
		// trace
		sb.append(trace);
		
		// 实例传this, 否则传class
		if (isStatic) {
			sb.append(",").append(behavior.getDeclaringClass().getName()).append(".class");
		} else {
			sb.append(", $0");
		}
		
		// 构造方法不需要methodName
		if (needMethodName) {
			sb.append(", \"").append(behavior.getMethodInfo().toString()).append("\"");
		}
		
		// 传入方法调用时的传参
		sb.append(", $args");
		// 结果
		if (needResult) {
			// $w的作用是把基础类型转换为包装类型，如果不是基础类型则忽略
			sb.append(", ($w)$_");
		}
		sb.append(");\r\n");
		
		return sb.toString();
	}
	
	private boolean shouldProxy(ClassMethodConfig classMethodConfig, CtBehavior method) throws NotFoundException {
		int modifiers = method.getModifiers();
		if (Modifier.isNative(modifiers)) {
			// 本地方法不能代理
			return false;
		} else if (Modifier.isAbstract(modifiers)) {
			// 抽象方法不能代理
			return false;
		} else if (EXCLUDE_METHODS.contains(method.getName())) {
			return false;
		}
		
		// 未配置需要代理的方法则所有方法都代理
		String regex = classMethodConfig.getMethodRegex();
		if (null == regex) {
			return true;
		}
		
		return PatternUtil.checkRegex(regex, method.getName());
	}
	
	private boolean canProxy(CtClass cls) {
		if (null == cls) {
			return false;
		}
		
		// 不代理的包括：接口、注解、数组、枚举、基础类
		return !cls.isInterface() && !cls.isAnnotation() && !cls.isArray() && !cls.isEnum()
				&& !cls.isPrimitive();
	}
}
