package cn.origin.cube.core.events.event.concurrent.task;

import cn.origin.cube.core.events.event.concurrent.utils.Syncer;

abstract class Syncable implements Runnable {
    protected Syncer syncer;
}
