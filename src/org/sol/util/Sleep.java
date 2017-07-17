package org.sol.util;

import org.sol.core.script.Contextable;
import org.sol.core.script.Methods;

public class Sleep extends Contextable {
    public Sleep(Methods methods) {
        super(methods);
    }

    /*public boolean omniSleep(final Condition condition, final int waitLength) {
        return org.powerbot.script.util.Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return condition.accept();
            }
        }, waitLength / 10, 10);
    }*/

    //if condition given is true: keep sleeping unless told by the

    /*sleepwhile*/
    public boolean sleep(final Condition condition, final int waitLength) {
        final Timer timer = new Timer(waitLength);
        while (timer.isRunning()) {
            if (!condition.accept()) {
                return true;
            }
        }
        return false;
    }
}