package cn.origin.cube.core.events.event.event.decentralization;

import cn.origin.cube.core.events.event.concurrent.task.Task;

import java.util.concurrent.ConcurrentHashMap;

public interface Listenable {

    ConcurrentHashMap<DecentralizedEvent<? extends EventData>, Task<? extends EventData>> listenerMap();

    void subscribe();

    void unsubscribe();

}
