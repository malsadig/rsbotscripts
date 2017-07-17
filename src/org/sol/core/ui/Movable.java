package org.sol.core.ui;

import org.sol.core.script.Contextable;
import org.sol.core.script.Methods;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.LinkedList;
import java.util.List;

public abstract class Movable extends Contextable implements MouseMotionListener, MouseListener {
    int x, y;

    private boolean mousePressed = false;

    int xMod, yMod;

    private int xLast, yLast;

    public List<String> texts;

    Shape shape;

    public Movable(Methods methods) {
        super(methods);

        this.texts = new LinkedList<String>();
    }

    public abstract void draw(final Graphics2D graphics, final Methods methods);

    int getWidth(final Graphics2D graphics) {
        int biggest = 0;
        for (final String text : texts) {
            int size = graphics.getFontMetrics().stringWidth(text);
            if (size > biggest) {
                biggest = size;
            }
        }
        return biggest;
    }

    @Override
    public void mouseReleased(final MouseEvent e) {
        mousePressed = false;
    }

    @Override
    public void mousePressed(final MouseEvent e) {
        if (shape != null && shape.contains(e.getPoint())) {
            xLast = e.getPoint().x;
            yLast = e.getPoint().y;
            mousePressed = true;
        }
    }

    @Override
    public void mouseDragged(final MouseEvent e) {
        if (mousePressed) {
            int x = e.getPoint().x;
            int y = e.getPoint().y;
            xMod += x - xLast;
            yMod += y - yLast;
            xLast = x;
            yLast = y;
            this.x = xMod;
            this.y = yMod;
        }
    }

    @Override
    public void mouseClicked(final MouseEvent e) {
    }

    @Override
    public void mouseEntered(final MouseEvent e) {
    }

    @Override
    public void mouseExited(final MouseEvent e) {
    }

    @Override
    public void mouseMoved(final MouseEvent e) {
    }
}