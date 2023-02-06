package cn.origin.cube.core.events.event.concurrent.task;

/**
 * Created by B_312 on 05/01/2021
 */
public interface ObjectTask extends Task<Object> {
    @Override
    void invoke(Object valueIn);
}
