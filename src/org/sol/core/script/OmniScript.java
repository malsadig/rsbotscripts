package org.sol.core.script;

import org.powerbot.script.PaintListener;
import org.powerbot.script.PollingScript;
import org.powerbot.script.rt6.ClientContext;
import org.sol.core.node.Node;
import org.sol.core.node.Tree;
import org.sol.core.ui.Movable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public abstract class OmniScript<C extends ClientContext> extends PollingScript<C> implements PaintListener, MouseListener, MouseMotionListener {
    private Tree tree;

    protected InventoryMonitor inventory;

    final String TITLE;

    protected final List<String> skillsTrained;

    protected boolean hidePaint = false;

    protected Methods methods;

    protected JFrame gui;

    protected boolean ready = false;

    protected OmniScript() {
        TITLE = this.getName();
        this.skillsTrained = new LinkedList<String>();
    }

    @Override
    public void start() {
        super.start();

        addSkillsTrained();

        methods = new Methods(super.ctx, this);

        methods.status = "Initializing...";

        tree = new Tree();

        inventory = new InventoryMonitor(methods);

        setup();

        methods.getContainer().submit(inventory);

        methods.getContainer().submit(new CameraMovement(methods));
    }

    @Override
    public void poll() {
        if (ready) tree.run();
    }

    @Override
    public void stop() {
        methods.shutdownContainer();
        super.stop();
    }

    @Override
    public void repaint(final Graphics g) {
        if (ready) {
            final Graphics2D graphics = (Graphics2D) g;
            graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) 0.8));
            graphics.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 17));

            try {
                if (!hidePaint) {
                    methods.omniPaint.draw(graphics, skillsTrained());
                }
            } catch (final Throwable ignored) {
            }
        }
    }

    protected abstract void setup();

    List<String> skillsTrained() {
        return skillsTrained;
    }

    protected abstract void addSkillsTrained();

    protected void provide(final Node... nodes) {
        Collections.addAll(tree, nodes);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() == 2) {
            hidePaint = !hidePaint;
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        for (final Movable movable : methods.omniPaint.movables.values()) {
            movable.mouseReleased(e);
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        for (final Movable movable : methods.omniPaint.movables.values()) {
            movable.mousePressed(e);
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        for (final Movable movable : methods.omniPaint.movables.values()) {
            movable.mouseDragged(e);
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }
}