import model.Creature;

import gfx.SimulationWindow;

import java.awt.EventQueue;

public class Main {
    public static void main(String[] args) {
        //Define a simple MultiLayerNetwork:
        CreateService service = new CreateService();
        Creature creature1 = service.createCreature(1);
        Creature creature2 = service.createCreature(2);
        service.saveCreature(creature1);
        service.saveCreature(creature2);
        Creature c1 = service.restoreCreature(1);
        Creature c2 = service.restoreCreature(2);
        System.out.println("Saved and loaded parameters are equal:      " + creature1.getBrain().getNet().params().equals(c1.getBrain().getNet().params()));
        System.out.println("Saved and loaded configurations are equal:  " + creature1.getBrain().getNet().getLayerWiseConfigurations().equals(c1.getBrain().getNet().getLayerWiseConfigurations()));
        System.out.println("Saved and loaded parameters are equal:      " + creature2.getBrain().getNet().params().equals(c2.getBrain().getNet().params()));
        System.out.println("Saved and loaded configurations are equal:  " + creature2.getBrain().getNet().getLayerWiseConfigurations().equals(c2.getBrain().getNet().getLayerWiseConfigurations()));
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                SimulationWindow window = new SimulationWindow("prims - primordial soup simulator");
                window.setVisible(true);
            }
        });
    }

}
