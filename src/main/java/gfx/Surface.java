package gfx;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;

public class Surface extends JPanel {

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        BasicStroke stroke = new BasicStroke(3f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
        g2d.setStroke(stroke);

        Ellipse2D.Float body = new Ellipse2D.Float(100, 100, 33, 33);

        g2d.draw(body);
    }
}
