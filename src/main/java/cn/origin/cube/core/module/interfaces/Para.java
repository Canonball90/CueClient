package cn.origin.cube.core.module.interfaces;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Para {

    ParaMode para();

     enum ParaMode{
        Full,Light,Test
    }
}

