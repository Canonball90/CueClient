package cn.origin.cube.utils.client.event.concurrent.task;

import cn.origin.cube.utils.client.event.concurrent.utils.Syncer;

abstract class Syncable implements Runnable {
    protected Syncer syncer;
}
