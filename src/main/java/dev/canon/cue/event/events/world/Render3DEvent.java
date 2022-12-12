package dev.canon.cue.event.events.world;

import dev.canon.cue.event.events.EventStage;
import dev.canon.cue.event.events.EventStage;

public class Render3DEvent
        extends EventStage {
    private final float partialTicks;

    public Render3DEvent(float partialTicks) {
        this.partialTicks = partialTicks;
    }

    public float getPartialTicks() {
        return this.partialTicks;
    }
}

