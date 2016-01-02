/**
 * Copyright: Copyright (c) 2015 
 * 
 * @author youaremoon
 * @date 2016年1月2日
 * @version V1.0
 */
package com.yam.trace.core.trace;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Description: TODO
 * @author youaremoon
 * @date 2016年1月2日 下午12:03:24
 *
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Share {

}
