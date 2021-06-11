import gfx.SimulationWindow;

import java.awt.EventQueue;

public class Main {
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                SimulationWindow window = new SimulationWindow("prims - primordial soup simulator");
                window.setVisible(true);
            }
        });
    }
}
