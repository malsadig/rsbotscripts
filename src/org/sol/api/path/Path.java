package org.sol.api.path;

import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.Game;
import org.powerbot.script.rt6.GameObject;
import org.sol.core.concurrent.Task;
import org.sol.core.script.Methods;
import org.sol.util.Condition;

import java.util.EnumSet;

public class Path extends Task {
    private final Obstacle[] obstacles;

    private boolean complete;

    public Path(Methods methods, final Obstacle... obstacles) {
        super(methods);

        this.obstacles = obstacles;
    }

    @Override
    public void run() {
        Obstacle current = null;
        for (final Obstacle ob : obstacles) {
            if (ob.accept()) {
                current = ob;
                break;
            }
        }
        if (current == null) {
            complete = true;
            return;
        }

        runRt6(current, methods.context);
    }

    private void runRt6(Obstacle current, final ClientContext context) {
        final GameObject object;
        if (current.getId() == -1) {
            context.objects.select(10).name(current.getName()).nearest().first();
        } else {
            context.objects.select(10).id(current.getId()).nearest().first();
        }
        if (!context.objects.isEmpty() &&
                (object = context.objects.poll()) != context.objects.nil() && object.valid() && object.inViewport()
                && context.players.local().idle()) {
            context.camera.turnTo(object);
            methods.status = "Maneuvering " + current.getName();
            if (object.interact(current.getAction())) {
                final Obstacle finalCurrent = current;

                methods.sleep.sleep(new Condition() {
                    @Override
                    public boolean accept() {
                        return context.game.crosshair() != Game.Crosshair.ACTION || finalCurrent.accept() || context.players.local().idle();
                    }
                }, 2500);
            }
        } else {
            if (current.containsPath()) {
                methods.status = "Walking to " + current.getName();
                current.getPath().traverse(EnumSet.of(org.powerbot.script.rt6.Path.TraversalOption.HANDLE_RUN));
            }
        }
    }

    public boolean isComplete() {
        return complete;
    }
}
