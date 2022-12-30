package cn.origin.cube.utils.client.event.event;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface ParallelListener {
    int priority() default Priority.Medium;
}
