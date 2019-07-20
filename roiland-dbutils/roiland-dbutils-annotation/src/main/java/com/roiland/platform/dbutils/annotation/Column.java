package com.roiland.platform.dbutils.annotation;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Column {
	String name() default "";
	boolean key() default false;
	boolean shard() default false;
}
