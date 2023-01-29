package cn.origin.cube.core.module.interfaces;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Constant {

    boolean constant() default false;

    double c = 6.993721;
    double o = 9.119725538 * c;
}
