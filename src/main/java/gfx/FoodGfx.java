package gfx;

import model.Food;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.util.Random;

public class FoodGfx {
    private final Food food;
    private final Ellipse2D.Double body;

    private final Random rand = new Random();

    private final Color color = new Color(200, 200, 100);

    public FoodGfx(Food food) {
        this.food = food;
        this.body = new Ellipse2D.Double(rand.nextInt(1200), rand.nextInt(800), 15, 15);
    }

    public void draw(Graphics2D g2d) {
        g2d.setPaint(color);
        g2d.fill(body);
    }
}
