package gfx;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

import static java.lang.Math.sin;
import static java.lang.Math.cos;

/**
 * Default orientation is facing up.
 */
public class CreatureGfx {

    /**
     * The angle the creature is facing in degrees.
     * 0 degrees corresponds to facing right, and 90 degrees is facing down.
     */
    private double angle = -90;

    /**
     * How many degrees from the facing angle the tail should be drawn
     */
    private static final double DRAW_ANGLE_TAIL = 180;

    Ellipse2D.Double body = new Ellipse2D.Double(100, 100, 30, 30);

    Line2D.Double tail;

    BasicStroke limbsStroke = new BasicStroke(2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
    Color limbsColor = new Color(100, 200, 100);
    Color bodyColor = new Color(255, 100, 100);

    public CreatureGfx() {
        Point2D.Double origin = new Point2D.Double(
                body.x + (body.width / 2),
                body.y + (body.height / 2)
        );

        // this line is going straight up
        Point2D.Double end = new Point2D.Double(
                origin.x,
                origin.y + 50
        );

        tail = new Line2D.Double(origin, end);
    }

    public void draw(Graphics2D g2d) {
        Stroke defaultStroke = g2d.getStroke();

        g2d.setStroke(limbsStroke);
        g2d.setColor(limbsColor);
        g2d.draw(tail);

        g2d.setStroke(defaultStroke); // reset stroke before filling body
        g2d.setPaint(bodyColor);
        g2d.fill(body);
    }

    public void tick(double rotateSignal, double translateSignal) {

        double deltaAngle = rotateSignal * SimulationParameters.ROTATION_RATE_FACTOR;
        angle += deltaAngle;

        Point2D.Double rotated = rotate(tail.getP1(), tail.getP2(), deltaAngle);

        tail.setLine(tail.getP1(), rotated);
    }

    /**
     * Rotate a point about the given origin
     */
    private Point2D.Double rotate(Point2D pointOfOrigin, Point2D point, double angle) {
        double radians = Math.toRadians(-angle);

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

    public Rectangle getBounds() {
        return body.getBounds();
    }
}
