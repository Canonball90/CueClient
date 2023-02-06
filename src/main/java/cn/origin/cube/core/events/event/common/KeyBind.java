package cn.origin.cube.core.events.event.common;

import cn.origin.cube.core.events.event.concurrent.task.VoidTask;

public class KeyBind {

    private int keyCode;
    private final VoidTask action;

    public KeyBind(int keyCode, VoidTask action) {
        this.keyCode = keyCode;
        this.action = action;
    }

    public void test(int keyCode) {
        if (this.keyCode == keyCode) action.invoke();
    }

    public void setKeyCode(int keyCode) {
        this.keyCode = keyCode;
    }

    public int getKeyCode() {
        return keyCode;
    }

}
