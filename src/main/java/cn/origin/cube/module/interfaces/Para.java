package cn.origin.cube.module.interfaces;

import cn.origin.cube.module.Category;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Para {

    ParaMode para();

    public enum ParaMode{
        Full,Light,Test
    }
}

