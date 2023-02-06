package cn.origin.cube.core.events.event.event;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface ParallelListener {
    int priority() default Priority.Medium;
}
