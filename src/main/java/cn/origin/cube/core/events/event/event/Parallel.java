package cn.origin.cube.core.events.event.event;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Parallel {
    boolean loadable() default true;

    boolean runnable() default false;
}
