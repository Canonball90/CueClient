package cn.origin.cube.core.plugin;

import java.io.IOException;

public interface PluginManagerImpl<T> {
    java.io.File getFile();

    void onLoad() throws IOException;

    boolean needsUpdate();

    default boolean needsUpdate(T type) {
        return this.needsUpdate();
    }

    default void onLoad(T type) throws IOException {
        this.onLoad();
    }
}

