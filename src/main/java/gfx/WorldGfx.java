package gfx;

import javax.swing.*;
import java.awt.*;

public class WorldGfx extends JPanel {

    CreatureGfx creatureGfx = new CreatureGfx();

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        creatureGfx.draw(g2d);
    }

    public void tick() {
        creatureGfx.tick(0.5, 0);
        //repaint(creatureGfx.getBounds());
        repaint();
    }
}
