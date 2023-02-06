package cn.origin.cube.core.events.event.concurrent.blocking;

import cn.origin.cube.core.events.event.concurrent.task.Task;

public interface BlockingTask extends Task<BlockingContent> {
    @Override
    void invoke(BlockingContent unit);
}
