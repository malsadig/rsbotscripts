package org.sol.api.methods;

import org.powerbot.script.Random;
import org.powerbot.script.rt6.Component;
import org.sol.core.script.Contextable;
import org.sol.core.script.Methods;
import org.sol.util.Condition;

public class Lodestone extends Contextable {
    public Lodestone(Methods methods) {
        super(methods);
    }

    public void teleport(final Lodestones lodestone) {
        if (methods.actionBar.isExpanded()) {
            if (methods.actionBar.isLocked()) {
                final Component destinationScreen = methods.context.widgets.widget(1092).component(0);
                if (destinationScreen.valid() && destinationScreen.visible()) {
                    final Component destination = lodestone.getWidgetChild(methods);
                    if (destination.valid() && destination.visible() && destination.interact("Teleport")) {
                        methods.sleep.sleep(new Condition() {
                            @Override
                            public boolean accept() {
                                return true;
                            }
                        }, Random.nextInt(550, 750));
                        methods.sleep.sleep(new Condition() {
                            @Override
                            public boolean accept() {
                                return methods.context.players.local().animation() != -1 || destination.valid() || destination.visible();
                            }
                        }, 10000);
                    }
                } else {
                    final Component node = methods.actionBar.getNode(11);
                    methods.sleep.sleep(new Condition() {
                        @Override
                        public boolean accept() {
                            return true;
                        }
                    }, Random.nextInt(750, 950));
                    if (methods.context.players.local().animation() == -1 && node != null && node.interact("Cast")) {
                        methods.sleep.sleep(new Condition() {
                            @Override
                            public boolean accept() {
                                return !destinationScreen.visible();
                            }
                        }, 2000);
                    }
                }
            } else {
                if (methods.actionBar.lock(true)) {
                    methods.sleep.sleep(new Condition() {
                        @Override
                        public boolean accept() {
                            return !methods.actionBar.isLocked();
                        }
                    }, 2000);
                }
            }
        } else {
            if (methods.actionBar.expand(true)) {
                methods.sleep.sleep(new Condition() {
                    @Override
                    public boolean accept() {
                        return !methods.actionBar.isExpanded();
                    }
                }, 2000);
            }
        }
    }

    public enum Lodestones {
        AL_KHARID(0),
        ARDOUGNE(1),
        BURTHROPE(2),
        CATHERBY(3),
        DRAYNOR(4),
        EDGEVILLE(5),
        FALADOR(6),
        LUMBRIDGE(7),
        PORT_SARIM(8),
        SEERS_VILLAGE(9),
        TAVERLY(10),
        VARROCK(11),
        YANILLE(12),
        TIRANNWN(20);
        private final int shift;
        private int widgetChild = -1;

        private Lodestones(final int shift) {
            this.shift = shift;
            if (shift <= 12) {
                widgetChild = 40 + shift;
            } else {
                widgetChild = 38 + shift;
            }
        }

        public boolean isActivated(final Methods methods) {
            return (methods.context.varpbits.varpbit(3) >> shift & 1) == 1;
        }

        public Component getWidgetChild(final Methods methods) {
            return methods.context.widgets.widget(1092).component(widgetChild);
        }
    }
}