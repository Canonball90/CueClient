package cn.origin.cube.core.events.client;

import cn.origin.cube.core.module.AbstractModule;
import net.minecraftforge.fml.common.eventhandler.Event;

public class ModuleToggleEvent extends Event {

    public static class Enable extends ModuleToggleEvent {
        AbstractModule module;

        public Enable(AbstractModule module) {
            this.module = module;
        }

        public AbstractModule getModule() {
            return module;
        }
    }

    public static class Disable extends ModuleToggleEvent {
        AbstractModule module;

        public Disable(AbstractModule module) {
            this.module = module;
        }

        public AbstractModule getModule() {
            return module;
        }
    }
}
