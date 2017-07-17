package org.sol.script.agility;

import org.powerbot.script.MessageEvent;
import org.powerbot.script.MessageListener;
import org.powerbot.script.Script;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.GameObject;
import org.powerbot.script.rt6.TileMatrix;
import org.sol.core.script.OmniScript;
import org.sol.script.agility.branch.Eat;
import org.sol.script.agility.branch.Navigation;
import org.sol.script.agility.course.Course;
import org.sol.script.agility.course.Courses;

import javax.swing.*;
import java.awt.*;

//todo: add pots option
//todo: add other foods
//todo: add other courses

@Script.Manifest(description = "Effortlessly traverses all agility courses!",
        name = "OmniAgility",
        properties = "hidden=true")
public class OmniAgility extends OmniScript<ClientContext> implements Script, MessageListener {
    public static Course course;

    public OmniAgility() {
        course = null;
    }

    @Override
    protected void setup() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                Courses selection = (Courses) JOptionPane.showInputDialog(null,
                        "Please select your course. Script uses pies to eat.",
                        "OmniAgility Course Selection",
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        Courses.values(),
                        Courses.values()[0]);
                course = new Course(selection, methods);

                provide(new Eat(methods), new Navigation(methods, course));

                ready = true;
            }
        });
    }

    @Override
    protected void addSkillsTrained() {
        this.skillsTrained.add("Agility");
    }

    @Override
    public void repaint(Graphics g) {
        super.repaint(g);
        if (ready && !hidePaint) {
            final Graphics2D graphics = (Graphics2D) g;
            graphics.setStroke(new BasicStroke(3));

            graphics.setColor(Color.RED);
            GameObject currentObject = course.getCurrentObstacle().getObject(methods);
            if(currentObject != null) {
                TileMatrix currentMatrix = currentObject.tile().matrix(methods.context);
                if (currentMatrix != null) {
                    System.out.println("here");
                    currentMatrix.draw(graphics);
                }
                currentObject.draw(graphics);
            }
            graphics.setColor(Color.GREEN);
            GameObject nextObject = course.getNextObstacle().getObject(methods);
            if(nextObject != null) {
                TileMatrix nextMatrix = nextObject.tile().matrix(methods.context);
                if(nextMatrix != null) {
                    nextMatrix.draw(graphics);
                }
                nextObject.draw(graphics);
            }
        }
    }

    @Override
    public void messaged(MessageEvent messageEvent) {
        //todo: upon failure maybe
        //todo: upon banking
        //todo: upon needing to eat
        //todo: upon getting experience or passing an obstacle
        //todo: add camera antipattern
        //todo: one obstacle can belong to multiple courses (think basic/advanced)

        /*final String string = event.getMessage().toLowerCase();
        if (event.getId() == 0 || event.getSender() == null || event.getSender().equals("")) {*/
    }
}