package org.sol.core.script;

import org.powerbot.script.Random;
import org.sol.core.concurrent.LoopTask;

public class CameraMovement extends LoopTask {
    public CameraMovement(Methods methods) {
        super(methods);
    }

    @Override
    public int loop() {
        int randAngle = Random.nextInt(-60, 60);
        methods.context.camera.angle(randAngle);
        return Random.nextInt(2000, 4000);
    }
}
