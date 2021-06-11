import model.Creature;
import model.World;

import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        //Define a simple MultiLayerNetwork:
        CreatureService service = new CreatureService();
        //service.printInitialWeights();
        Creature creature1 = service.createCreature(1);
        Creature creature2 = service.createCreature(2);
        //service.saveCreature(creature1);
        //service.saveCreature(creature2);
        //Creature c1 = service.restoreCreature(1);
        //Creature c2 = service.restoreCreature(2);
        List<Creature> creatureList = Arrays.asList(creature1, creature2);
        //System.out.println("Saved and loaded parameters are equal:      " + creature1.getBrain().getNet().params().equals(c1.getBrain().getNet().params()));
        //System.out.println("Saved and loaded configurations are equal:  " + creature1.getBrain().getNet().getLayerWiseConfigurations().equals(c1.getBrain().getNet().getLayerWiseConfigurations()));
        //System.out.println("Saved and loaded parameters are equal:      " + creature2.getBrain().getNet().params().equals(c2.getBrain().getNet().params()));
        //System.out.println("Saved and loaded configurations are equal:  " + creature2.getBrain().getNet().getLayerWiseConfigurations().equals(c2.getBrain().getNet().getLayerWiseConfigurations()));
        //EventQueue.invokeLater(() -> {
        //    SimulationWindow window = new SimulationWindow("prims - primordial soup simulator");
        //    window.setVisible(true);
        //});
        World world = new World(creatureList, null);
        service.calculateOutputs(creature1);
    }

}
