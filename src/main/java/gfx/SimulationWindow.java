package gfx;

import javax.swing.*;
import java.awt.*;

public class SimulationWindow extends JFrame {

    public SimulationWindow(String title, WorldGfx worldGfx) throws HeadlessException {
        super(title);
        init(worldGfx);
    }

    private void init(WorldGfx worldGfx) {
        add(worldGfx);
        setSize(1200, 800);
        setLocationRelativeTo(null); // place in center of screen
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
