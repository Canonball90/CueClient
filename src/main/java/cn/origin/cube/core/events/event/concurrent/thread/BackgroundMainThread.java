package cn.origin.cube.core.events.event.concurrent.thread;

import cn.origin.cube.core.events.event.concurrent.ConcurrentTaskManager;
import cn.origin.cube.core.events.event.concurrent.utils.ThreadUtil;

/**
 * Created by B_312 on 05/01/2021
 */
public class BackgroundMainThread extends Thread {

    @SuppressWarnings("InfiniteLoopStatement")
    @Override
    public void run() {
        while (true) {
            try {
                ConcurrentTaskManager.updateBackground();
            } catch (Exception exception) {
                System.out.println("[TaskManager-Background]Running an unsafe task in background main thread!Please check twice!" +
                        "You'd better run an unsafe task by launching a new task or surrounding with try catch " +
                        "instead of running directly in background main thread!");
                exception.printStackTrace();
            }
            ThreadUtil.delay();
        }
    }

}
