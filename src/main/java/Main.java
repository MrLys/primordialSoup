import model.Creature;
import model.Food;
import model.World;

import gfx.SimulationWindow;
import gfx.WorldGfx;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.api.preprocessor.NormalizerMinMaxScaler;
import org.nd4j.linalg.factory.Nd4j;
import service.CreatureService;

import javax.swing.*;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;

public class Main {


    public static void main(String[] args) {
        //new Main().test();
        CreatureService service = new CreatureService();
        //service.printInitialWeights();
        ArrayList<Creature> creatures = new ArrayList<>();
        ArrayList<Food> foodList = new ArrayList<>();
        Creature creature1 = service.createCreature(1);
        Creature creature2 = service.createCreature(2);
        //Creature creature2 = service.createCreature(2);
        creatures.add(creature1);
        creatures.add(creature2);
        foodList.add(new Food(50));
        foodList.add(new Food(50));
        foodList.add(new Food(50));
        World world = new World(creatures, foodList);
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

        new Timer(50, tickListener).start();
    }

    private void test() {
        CreatureService service = new CreatureService();
        Creature c = service.createCreature(0);
        service.calculateColor(c);
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
