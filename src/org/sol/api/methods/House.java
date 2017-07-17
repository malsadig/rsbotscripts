package org.sol.api.methods;

import org.powerbot.script.rt6.Component;
import org.powerbot.script.rt6.GameObject;
import org.powerbot.script.rt6.Item;
import org.powerbot.script.rt6.LocalPath;
import org.sol.core.script.Contextable;
import org.sol.core.script.Methods;
import org.sol.util.Condition;

public class House extends Contextable {
    private static final int PORTAL_INSIDE_ID = 13405,
            PORTAL_OUTSIDE_ID = 15531;

    private static final int PORTAL_WIDGET = 1188,
            BUILDING_MODE_COMPONENT = 18,
            NORMAL_MODE_COMPONENT = 12;

    public static final int HOME_TELETAB_ID = 8013;

    private LocalPath path;

    public House(Methods methods) {
        super(methods);
    }

    public boolean isInSide() {
        return !methods.context.objects.select(10).id(PORTAL_INSIDE_ID).isEmpty();
    }

    public boolean isRightOutside() {
        return !methods.context.objects.select(10).id(PORTAL_OUTSIDE_ID).isEmpty();
    }

    public void leave() {
        if (!isInSide()) return;

        GameObject portal = methods.context.objects.poll();

        if (methods.context.movement.distance(methods.context.players.local(), portal) < 5) {
            path = null;

            if (portal.inViewport()) {
                if (portal.interact("Enter")) {
                    methods.sleep.sleep(new Condition() {
                        @Override
                        public boolean accept() {
                            return isInSide();
                        }
                    }, 2500);
                }
            } else {
                methods.context.camera.turnTo(portal);
            }
        } else {
            if (path == null) {
                methods.context.movement.findPath(methods.context.objects.poll());
            }

            path.traverse();
        }
    }

    public void enter(final boolean buildingMode) {
        if (!isRightOutside()) return;

        final Component component = methods.context.widgets.widget(PORTAL_WIDGET).component(BUILDING_MODE_COMPONENT);

        if (component != null && component.valid() && component.visible()) {
            if (buildingMode) {
                if (methods.context.input.sendln("2")) {
                    methods.sleep.sleep(new Condition() {
                        @Override
                        public boolean accept() {
                            return !isInSide();
                        }
                    }, 3000);
                }
            } else {
                if (methods.context.input.sendln("1")) {
                    methods.sleep.sleep(new Condition() {
                        @Override
                        public boolean accept() {
                            return !isInSide();
                        }
                    }, 3000);
                }
            }
        } else {
            final GameObject portal = methods.context.objects.poll();
            if (portal.click()) {
                methods.sleep.sleep(new Condition() {
                    @Override
                    public boolean accept() {
                        return !(component != null && component.valid() && component.visible());
                    }
                }, 1000);
            }
        }
    }

    public void homeTele() {
        if (!methods.context.backpack.select().id(HOME_TELETAB_ID).isEmpty()) {
            final Item tab = methods.context.backpack.poll();
            if (tab.interact("Break")) {
                methods.sleep.sleep(new Condition() {
                    @Override
                    public boolean accept() {
                        return !isInSide();
                    }
                }, 5000);
            }
        }
    }

    public boolean isInBuildingMode() {
        return !methods.context.objects.select(10).name("Door hotspot").isEmpty();
    }

    public void teleport(final String location) {
        if (!isInSide()) return;
        if (!methods.context.objects.select(10).name(location + " Portal").isEmpty()) {
            GameObject portal = methods.context.objects.poll();

            if (portal.inViewport()) {
                if (portal.interact("Enter")) {
                    path = null;
                    methods.sleep.sleep(new Condition() {
                        @Override
                        public boolean accept() {
                            return isInSide();
                        }
                    }, 2500);
                }
            } else {
                if (methods.context.movement.step(portal)) {
                    methods.sleep.sleep(new Condition() {
                        @Override
                        public boolean accept() {
                            return methods.context.players.local().idle();
                        }
                    }, 750);
                }
            }
        }
    }

    public void pray() {
        if (!isInSide() || methods.prayer.isFull()) return;
        if (!methods.context.objects.select(30).name("Altar").isEmpty()) {
            GameObject portal = methods.context.objects.poll();

            if (portal.inViewport()) {
                if (portal.interact("Pray")) {
                    path = null;
                    methods.sleep.sleep(new Condition() {
                        @Override
                        public boolean accept() {
                            return !methods.prayer.isFull();
                        }
                    }, 2500);
                }
            } else {
                if (methods.context.movement.step(portal)) {
                    methods.sleep.sleep(new Condition() {
                        @Override
                        public boolean accept() {
                            return methods.context.players.local().idle();
                        }
                    }, 750);
                }
            }
        }
    }
}