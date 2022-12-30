package cn.origin.cube.utils.client.event.concurrent.blocking;

import cn.origin.cube.utils.client.event.concurrent.task.Task;

public interface BlockingTask extends Task<BlockingContent> {
    @Override
    void invoke(BlockingContent unit);
}
