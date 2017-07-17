package org.sol.script.grenwall.node;

import org.powerbot.script.rt6.Component;
import org.powerbot.script.rt6.Widget;
import org.sol.api.methods.Lodestone2;
import org.sol.api.path.Path;
import org.sol.core.node.Node;
import org.sol.core.script.Methods;
import org.sol.script.grenwall.OmniGrenwalls;
import org.sol.script.grenwall.util.TeleportMethod;
import org.sol.util.Condition;

public class Walk extends Node {
    private final Path path;

    public Walk(Methods methods) {
        super(methods);

        path = new Path(methods, OmniGrenwalls.teleportMethod.getMethod().getReturnPath(methods));
    }

    @Override
    public boolean activate() {
        return !path.isComplete();
    }

    @Override
    public void run() {
        if (OmniGrenwalls.teleportMethod.getMethod().equals(TeleportMethod.Method.VARROCK)) {
            final Widget widget = methods.interaction.getWidgetContaining("where would you like to go");

            if (widget != null && widget.valid()) {
                final Component child = methods.interaction.getChildContaining(widget, "tree gnome stronghold");

                if (child != null && child.valid() && child.visible()) {
                    methods.status = "Teleporting to TGS...";
                    if (child.click(true)) {
                        methods.sleep.sleep(new Condition() {
                            @Override
                            public boolean accept() {
                                return child.valid() && child.visible();
                            }
                        }, 2000);
                    }
                    return;
                }
            }
        } else {
            if (OmniGrenwalls.teleportMethod.getBankArea().contains(methods.context.players.local().tile())) {
                if (methods.context.bank.opened()) {
                    methods.context.bank.close();
                } else {
                    methods.status = "Teleporting to hunting spot";
                    Lodestone2.TIRANNWN.teleport(methods.context);
                    //methods.lodestone.teleport(Lodestone.Lodestones.TIRANNWN);
                }
                return;
            }
        }

        if (methods.context.combatBar.expanded()) {
            if (methods.context.combatBar.expanded(false)) {
                methods.sleep.sleep(new Condition() {
                    @Override
                    public boolean accept() {
                        return methods.actionBar.isExpanded();
                    }
                }, 2000);
            }
            return;
        }

        path.run();
    }
}