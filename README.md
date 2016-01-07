# easy-trace
用于跟踪应用中所有方法的执行，可以用于调试，也可以用于收集性能数据等（需扩展）

### 原理
通过java.lang.instrument提供的premain拦截Class的字节码， 通过javassist对字节码进行修改，在需要的方法中插入trace点。
目前支持的trace点：1、before (static)method execute; 2、after (static)method execute; 3、after constructor execute。
为了尽量减少依赖，该项目只依赖javassist 3.20.0-GA版本，该版本支持java8。  

目前主要有三个trace的实现类：

ConsoleTraceRecord：trace信息打印到控制台，默认的方式

FileTraceRecord: trace信息写入到同一个文件，路径为./logs/trace.log

FilePerThreadTraceRecord: 每个线程记录自己的trace信息，路径为./log/trace_{thread_name}.log

### 使用方式
1、将easy-trace和javassist的包加入的你class path；

2、在源码目录（如src/main/resources)下增加easy-trace.xml文件，并根据你要分析的应用进行配置；

3、启动应用时：java -javaagent:{easy trace jar path}\easy-trace-0.0.1-SNAPSHOT.jar -jar {your jar}。
如果需要调试jdk的类则需要加：
-Xbootclasspath/a:"{你的路径}\javassist-3.20.0-GA.jar;{你的路径}\easy-trace-0.0.1-SNAPSHOT.jar"

注意：该版本目前主要用来查看执行路径，方便源码分析，如果需要收集性能数据需要进行扩展。

### 配置文件说明

```
<?xml version="1.0" encoding="UTF-8"?>
<configuration>
	<!-- 类和方法的过滤配置 -->
	<class-methods>
		<!-- 排除的系统类 -->
		<exclude-system-class>
		</exclude-system-class>
		<!-- 包括的系统类，优先级高于exclude-system-class -->
		<include-system-class>
		</include-system-class>
		<!-- 排除的用户类，优先级高于include-user-class -->
		<exclude-user-class>
		</exclude-user-class>
		<!-- 包含的用户类 -->
		<include-user-class>
			<class-method>
				<class-regex>^io/netty/</class-regex>
				<!-- 不配置则所有方法都可以 -->
				<method-regex></method-regex>
				<!-- 不配置表示所有点都关注,半角逗号分隔,可以配置的范围: CONSTRUCTOR,METHOD_BEFORE,METHOD_AFTER,STATIC_BEFORE,STATIC_AFTER -->
				<interst>ONSTRUCTOR,METHOD_BEFORE,METHOD_AFTER,STATIC_BEFORE,STATIC_AFTER</interst>
				<!-- 可以将生产的class输出到该目录下,用反编译工具查看: 如D:\code -->
				<wirte-file></wirte-file>
			</class-method>
		</include-user-class>
	</class-methods>
	
	<!-- trace记录类 -->
	<trace-records>
		<!-- 不配置默认为ConsoleTraceRecord，可以根据情况配置 -->
		<record-class>com.yam.trace.core.trace.record.ConsoleTraceRecord</record-class>
		<record-class>com.yam.trace.core.trace.record.FileTraceRecord</record-class>
		<!-- 第二种配置方式,默认的消息格式化类是StringMessageFormatter -->
		<record-class-info>
			<record-class>com.yam.trace.core.trace.record.FilePerThreadTraceRecord</record-class>
			<message-formatter-class>com.yam.trace.core.formatter.StringMessageFormatter</message-formatter-class>
		</record-class-info>
	</trace-records>
	
	<message-filters>
		<!-- 检测消息的合法性，只能配一项，默认为DefaultMessageCheck -->
		<message-check>com.yam.trace.core.intercept.DefaultMessageCheck</message-check>
		<!-- 后面的几个配置可以配置多项，内容为正则表达式,exclude优先级高于include -->
		<include-thread-name></include-thread-name>
		<exclude-thread-name></exclude-thread-name>
		<include-message></include-message>
		<exclude-message></exclude-message>
	</message-filters>
</configuration>
```

### 输出示例
以下是netty MpscLinkedQueue类的poll方法执行流程：
```
2016-01-02 17:10:24.024	nioEventLoopGroup-0-0 ----------------------->begin	io.netty.util.internal.MpscLinkedQueue.poll ()Ljava/lang/Object;
2016-01-02 17:10:24.024	nioEventLoopGroup-0-0 ------------------------->begin	io.netty.util.internal.MpscLinkedQueue.peekNode ()Lio/netty/util/internal/MpscLinkedQueueNode;
2016-01-02 17:10:24.024	nioEventLoopGroup-0-0 --------------------------->begin	io.netty.util.internal.MpscLinkedQueue.headRef ()Lio/netty/util/internal/MpscLinkedQueueNode;
2016-01-02 17:10:24.024	nioEventLoopGroup-0-0 --------------------------->end	io.netty.util.internal.MpscLinkedQueue.headRef ()Lio/netty/util/internal/MpscLinkedQueueNode; return io.netty.util.internal.MpscLinkedQueue$DefaultNode
2016-01-02 17:10:24.024	nioEventLoopGroup-0-0 --------------------------->begin	io.netty.util.internal.MpscLinkedQueue$DefaultNode.next ()Lio/netty/util/internal/MpscLinkedQueueNode;
2016-01-02 17:10:24.024	nioEventLoopGroup-0-0 --------------------------->end	io.netty.util.internal.MpscLinkedQueue$DefaultNode.next ()Lio/netty/util/internal/MpscLinkedQueueNode; return null
2016-01-02 17:10:24.024	nioEventLoopGroup-0-0 --------------------------->begin	io.netty.util.internal.MpscLinkedQueue.tailRef ()Lio/netty/util/internal/MpscLinkedQueueNode;
2016-01-02 17:10:24.024	nioEventLoopGroup-0-0 --------------------------->end	io.netty.util.internal.MpscLinkedQueue.tailRef ()Lio/netty/util/internal/MpscLinkedQueueNode; return io.netty.util.internal.MpscLinkedQueue$DefaultNode
2016-01-02 17:10:24.024	nioEventLoopGroup-0-0 ------------------------->end	io.netty.util.internal.MpscLinkedQueue.peekNode ()Lio/netty/util/internal/MpscLinkedQueueNode; return null
2016-01-02 17:10:24.024	nioEventLoopGroup-0-0 ----------------------->end	io.netty.util.internal.MpscLinkedQueue.poll ()Ljava/lang/Object; return null
```
