import model.*;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.learning.config.Nesterovs;
import org.nd4j.linalg.lossfunctions.LossFunctions;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class CreateService {
    private MultiLayerConfiguration getConf() {
        return new NeuralNetConfiguration.Builder()
            .weightInit(WeightInit.XAVIER)
            .updater(new Nesterovs(0.1, 0.9))
            .list()
            .layer(new DenseLayer.Builder().nIn(4).nOut(3).activation(Activation.TANH).build())
            .layer(new OutputLayer.Builder(LossFunctions.LossFunction.NEGATIVELOGLIKELIHOOD).activation(Activation.SOFTMAX).nIn(3).nOut(3).build())
            .build();
    }

    private Brain createBrain() {
        MultiLayerConfiguration conf = getConf();
        MultiLayerNetwork net = new MultiLayerNetwork(conf);
        net.init();
        return new Brain(net);
    }

    private Eyes createEyes() {
        return new Eyes();
    }

    private List<Nose> createNoses() {
        return Arrays.asList(new Nose(), new Nose());
    }

    private RectilinearMuscle createRectilinearMuscle() {
        return new RectilinearMuscle();
    }

    private List<RotationalMuscle> createRotationalMuscle() {
        return Arrays.asList(new RotationalMuscle(), new RotationalMuscle());
    }

    public Creature createCreature(int id) {
        return new Creature(createBrain(), createEyes(), createNoses(), createRectilinearMuscle(), createRotationalMuscle(), id);
    }
    public Creature createCreature(int id, MultiLayerNetwork net) {
        return new Creature(new Brain(net), createEyes(), createNoses(), createRectilinearMuscle(), createRotationalMuscle(), id);
    }

    public void saveCreature(Creature creature) {
        File locationToSave = new File(creature.getSaveLocation()+creature.getId() +".zip");      //Where to save the network. Note: the file is in .zip format - can be opened externally
        MultiLayerNetwork net = creature.getBrain().getNet();
        boolean saveUpdater = true;                                             //Updater: i.e., the state for Momentum, RMSProp, Adagrad etc. Save this if you want to train your network more in the future
        try {
            net.save(locationToSave, saveUpdater);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Creature restoreCreature(int id) {
        File locationToSave = new File(Creature.saveLocation+id +".zip");
        try {
            MultiLayerNetwork restored = MultiLayerNetwork.load(locationToSave, true);
            return createCreature(id, restored);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
