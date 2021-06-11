import gfx.SimulationParameters;
import gfx.SimulationWindow;
import gfx.WorldGfx;

import javax.swing.*;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main {

    public static void main(String[] args) {

        WorldGfx worldGfx = new WorldGfx();

        SimulationTickListener tickListener = new SimulationTickListener(worldGfx);

        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                SimulationWindow window = new SimulationWindow(
                        "prims - primordial soup simulator",
                        worldGfx);
                window.setVisible(true);
            }
        });

        new Timer(50, tickListener).start();
    }
}

class SimulationTickListener implements ActionListener {
    private WorldGfx worldGfx;

    public SimulationTickListener(WorldGfx worldGfx) {
        this.worldGfx = worldGfx;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        // make sure our GUI is not null and is displayed
        if (worldGfx != null && worldGfx.isDisplayable()) {
            // call method to animate.
            worldGfx.tick(); // this method calls repaint
        }
    }
}
