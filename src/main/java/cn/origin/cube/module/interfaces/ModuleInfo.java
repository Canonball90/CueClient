package cn.origin.cube.module.interfaces;

import cn.origin.cube.core.module.Category;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface ModuleInfo {
    String name();

    String descriptions();

    Category category();

    int defaultKeyBind() default 0;

    boolean defaultEnable() default false;
}
