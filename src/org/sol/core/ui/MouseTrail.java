package org.sol.core.ui;

import java.awt.*;

public class MouseTrail {
    private final static int SIZE = 25;

    private final Point[] points;
    private int index;

    public MouseTrail() {
        points = new Point[SIZE];
        index = 0;
    }

    public void add(Point p) {
        points[index++] = p;
        index %= SIZE;
    }

    public void draw(Graphics graphics) {
        double alpha = 0;

        for (int i = index; i != (index == 0 ? SIZE - 1 : index - 1); i = (i + 1) % SIZE) {
            if (points[i] != null && points[(i + 1) % SIZE] != null) {
                graphics.setColor(new Color(graphics.getColor().getRed(), graphics.getColor().getGreen(), graphics.getColor().getBlue(), (int) alpha));
                graphics.drawLine(points[i].x, points[i].y, points[(i + 1) % SIZE].x, points[(i + 1) % SIZE].y);
                alpha += (255.0 / SIZE);
            }
        }
    }
}