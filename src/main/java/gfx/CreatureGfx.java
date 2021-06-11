package gfx;

import model.Creature;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.Random;

import static java.lang.Math.sin;
import static java.lang.Math.cos;
import static java.lang.Math.toRadians;

/**
 * Default orientation is facing up.
 */
public class CreatureGfx {
    private final Creature creature;
    /**
     * The angle the creature is facing in degrees.
     * 0 degrees corresponds to facing right, and 90 degrees is facing down.
     */
    private double angle = -90;

    /**
     * How many degrees from the facing angle the tail should be drawn
     */
    private static final double DRAW_ANGLE_TAIL = 180;

    private static final double DRAW_ANGLE_LEFT_ANTENNA = -40;
    private static final double DRAW_ANGLE_RIGHT_ANTENNA = 40;

    Ellipse2D.Double body;
    Line2D.Double tail;
    Line2D.Double leftAntenna;
    Line2D.Double rightAntenna;

    BasicStroke limbsStroke = new BasicStroke(2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
    Color limbsColor = new Color(100, 200, 100);
    Color bodyColor = new Color(255, 100, 100);

    Random rand = new Random();

    public CreatureGfx(Creature creature) {
        this.creature = creature;

        body = new Ellipse2D.Double(rand.nextInt(1200), rand.nextInt(800), 30, 30);

        Point2D.Double bodyCenter = new Point2D.Double(body.getCenterX(), body.getCenterY());

        { // tail
            Point2D.Double end = translate(bodyCenter.x, bodyCenter.y, 40, angle + DRAW_ANGLE_TAIL);
            tail = new Line2D.Double(bodyCenter, end);
        }

        { // left antenna
            Point2D.Double end = translate(bodyCenter.x, bodyCenter.y, 25, angle + DRAW_ANGLE_LEFT_ANTENNA);
            leftAntenna = new Line2D.Double(bodyCenter, end);
        }

        { // right antenna
            Point2D.Double end = translate(bodyCenter.x, bodyCenter.y, 25, angle + DRAW_ANGLE_RIGHT_ANTENNA);
            rightAntenna = new Line2D.Double(bodyCenter, end);
        }
    }

    public void draw(Graphics2D g2d) {
        Stroke defaultStroke = g2d.getStroke();

        g2d.setStroke(limbsStroke);
        g2d.setColor(limbsColor);
        g2d.draw(tail);
        g2d.draw(leftAntenna);
        g2d.draw(rightAntenna);

        g2d.setStroke(defaultStroke); // reset stroke before filling body
        g2d.setPaint(bodyColor);
        g2d.fill(body);
    }

    public void tick(double rotateSignal, double translateSignal) {

        double translationMagnitude = translateSignal * SimulationParameters.TRANSLATION_RATE_FACTOR;
        double deltaAngle = rotateSignal * SimulationParameters.ROTATION_RATE_FACTOR;
        angle -= deltaAngle;

        {
            Point2D.Double translated = translateSaturated(body.getX(), body.getY(), translationMagnitude, angle);
            body.setFrame(translated.x, translated.y, body.width, body.height);
        }

        Point2D.Double newP1 = new Point2D.Double(body.getCenterX(), body.getCenterY());

        {
            Point2D.Double newP2 = translate(body.getCenterX(), body.getCenterY(), 40, angle + DRAW_ANGLE_TAIL);
            Point2D.Double rotatedP2 = rotate(newP1, newP2, deltaAngle);
            tail.setLine(newP1, rotatedP2);
        }

        {
            Point2D.Double newP2 = translate(body.getCenterX(), body.getCenterY(), 25, angle + DRAW_ANGLE_LEFT_ANTENNA);
            Point2D.Double rotatedP2 = rotate(newP1, newP2, deltaAngle);
            leftAntenna.setLine(newP1, rotatedP2);
        }

        {
            Point2D.Double newP2 = translate(body.getCenterX(), body.getCenterY(), 25, angle + DRAW_ANGLE_RIGHT_ANTENNA);
            Point2D.Double rotatedP2 = rotate(newP1, newP2, deltaAngle);
            rightAntenna.setLine(newP1, rotatedP2);
        }
    }

    /**
     * Rotate a point about the given origin
     */
    private Point2D.Double rotate(Point2D pointOfOrigin, Point2D point, double angle) {
        double radians = toRadians(-angle);

        // copy values to local vars to prevent accidental mutation of inputs
        double x = point.getX();
        double y = point.getY();

        // convert the point to a vector originating at (0, 0)
        x -= pointOfOrigin.getX();
        y -= pointOfOrigin.getY();

        double xRotated = (x * cos(radians)) - (y * sin(radians));
        double yRotated = (x * sin(radians)) + (y * cos(radians));

        // convert it from vector back to point
        xRotated += pointOfOrigin.getX();
        yRotated += pointOfOrigin.getY();

        return new Point2D.Double(xRotated, yRotated);
    }

    private Point2D.Double translateSaturated(double x, double y, double magnitude, double angle) {
        double radians = toRadians(angle);
        double deltaX = magnitude * cos(radians);
        double deltaY = magnitude * sin(radians);

        x += deltaX;
        y += deltaY;

        x = Math.max(0, Math.min(1200, x));
        y = Math.max(0, Math.min(800, y));

        return new Point2D.Double(x, y);
    }

    private Point2D.Double translate(double x, double y, double magnitude, double angle) {
        double radians = toRadians(angle);
        double deltaX = magnitude * cos(radians);
        double deltaY = magnitude * sin(radians);

        x += deltaX;
        y += deltaY;

        return new Point2D.Double(x, y);
    }

    public Rectangle getBounds() {
        return body.getBounds();
    }

    public Creature getCreature() {
        return creature;
    }
}
