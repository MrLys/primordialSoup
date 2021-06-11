package gfx;

import javax.swing.*;
import java.awt.*;

public class SimulationWindow extends JFrame {

    public SimulationWindow(String title) throws HeadlessException {
        super(title);
        init();
    }

    private void init() {
        add(new Surface());
        setSize(800, 600);
        setLocationRelativeTo(null); // place in center of screen
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
