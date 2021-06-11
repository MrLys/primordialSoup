import model.Creature;
import model.Food;
import model.World;

import gfx.SimulationWindow;
import gfx.WorldGfx;
import service.CreatureService;

import javax.swing.*;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        CreatureService service = new CreatureService();
        //service.printInitialWeights();
        Creature creature1 = service.createCreature(1);
        //Creature creature2 = service.createCreature(2);
        List<Creature> creatures = Arrays.asList(creature1);
        Food food = new Food();
        List<Food> foodList = new ArrayList<>();
        World world = new World(creatures,foodList);
        WorldGfx worldGfx = new WorldGfx(world, service);

        SimulationTickListener tickListener = new SimulationTickListener(worldGfx);


        //service.saveCreature(creature1);
        //service.saveCreature(creature2);
        //Creature c1 = service.restoreCreature(1);
        //Creature c2 = service.restoreCreature(2);
        //System.out.println("Saved and loaded parameters are equal:      " + creature1.getBrain().getNet().params().equals(c1.getBrain().getNet().params()));
        //System.out.println("Saved and loaded configurations are equal:  " + creature1.getBrain().getNet().getLayerWiseConfigurations().equals(c1.getBrain().getNet().getLayerWiseConfigurations()));
        //System.out.println("Saved and loaded parameters are equal:      " + creature2.getBrain().getNet().params().equals(c2.getBrain().getNet().params()));
        //System.out.println("Saved and loaded configurations are equal:  " + creature2.getBrain().getNet().getLayerWiseConfigurations().equals(c2.getBrain().getNet().getLayerWiseConfigurations()));
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                SimulationWindow window = new SimulationWindow(
                        "prims - primordial soup simulator",
                        worldGfx);
                window.setVisible(true);
            }
        });

        new Timer(1000, tickListener).start();
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
