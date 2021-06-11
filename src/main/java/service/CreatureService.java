package service;

import model.*;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.api.preprocessor.NormalizerMinMaxScaler;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.lossfunctions.LossFunctions;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class CreatureService {
    private static final int RIGHT_ARM = 0;
    private static final int LEFT_ARM = 1;
    private static final int BACK_ARM = 2;
    private static final int layers = 2;
    private static final INDArray ind0_W= Nd4j.create(new double[][]{
        new double[]{0.9136550426483154, 0.2254105508327484, 0.05268868803977966},
        new double[]{-0.4886256158351898, -0.17270053923130035, 0.5277238488197327},
        new double[]{-1.2825582027435303, -0.17129941284656525, -0.07840892672538757}});
    private static final INDArray ind0_b= Nd4j.create(new double[][]{
        new double[]{0.0, 0.0, 0.0}});
    private static final INDArray ind1_W= Nd4j.create(new double[][]{
        new double[]{-0.2528708279132843, -0.3493185043334961, 1.0037107467651367},
        new double[]{-0.025605902075767517, 0.3954494595527649, -0.07494216412305832},
        new double[]{0.02212219499051571, -1.0447677373886108, 0.3801596760749817}});
    private static final INDArray ind1_b= Nd4j.create(new double[][]{
        new double[]{0.0, 0.0, 0.0}});

    private static final Map<String, INDArray> keys = new HashMap<>();
    static {
        keys.put("0_W", ind0_W);
        keys.put("0_b", ind0_b);
        keys.put("1_W", ind1_W);
        keys.put("1_b", ind1_b);
    }
    //private static final double[][] w_0 = new double[][]{
    //    new double[]{0.1659,   -0.6131, -0.3444},
    //    new double[]{ -0.6771,    0.3848,   -0.0784},
    //new double[]{-0.1544,   -1.1473,   -0.6404}};
    //private static final String w_0_key = "0_W";
    //private static final double[][] w_1 = new double[][]{
    //    new double[]{ -0.7643,   -0.0471,    0.8813},
    //    new double[]{ 0.2855,   -0.1222,   -0.6253},
    //    new double[]{0.0746,   -0.6142,   -0.1725}};
    //private static final String w_1_key = "1_W";
    //private static final INDArray b_0 = Nd4j.create(new double[][]{
    //    new double[]{ 0,         0,         0}});
    //private static final String b_0_key = "0_b";
    //private static final INDArray b_1 = Nd4j.create(new double[][]{
    //    new double[]{ 0,         0,         0}});
    //private static final String b_1_key = "1_b";
    //private static final Map<String, INDArray> keys = new HashMap<>();
    //static {
    //   keys.put(w_0_key, Nd4j.create(w_0));
    //    keys.put(b_0_key, b_0);
    //    keys.put(w_0_key, Nd4j.create(w_0));
    //    keys.put(w_0_key, Nd4j.create(w_0));
    //}

    private MultiLayerConfiguration getConf() {
        return new NeuralNetConfiguration.Builder()
            .weightInit(WeightInit.XAVIER)
            .list()
            .layer(new DenseLayer.Builder().nIn(3).nOut(3).activation(Activation.TANH).build())
            .layer(new OutputLayer.Builder(LossFunctions.LossFunction.NEGATIVELOGLIKELIHOOD).activation(Activation.SOFTMAX).nIn(3).nOut(3).build())
            .build();
    }
    // call this to create the new vectors!
    public void printInitialWeights() {
        MultiLayerConfiguration conf = getConf();
        MultiLayerNetwork net = new MultiLayerNetwork(conf);
        net.init();
        Iterator paramap_iterator = net.paramTable().entrySet().iterator();

        StringBuilder keyBuilder = new StringBuilder();
        while(paramap_iterator.hasNext()) {
            Map.Entry<String, INDArray> me = (Map.Entry<String, INDArray>) paramap_iterator.next();
            keyBuilder.append("private static final INDArray ");
            keyBuilder.append("ind");
            keyBuilder.append(me.getKey());
            keyBuilder.append("= Nd4j.create(new double[][]{\n");
            double[][] value = me.getValue().toDoubleMatrix();
            for (int i = 0; i < value.length; i++) {
                double[] doubles = value[i];
                keyBuilder.append("new double[]{");
                for (int j = 0; j < doubles.length; j++) {
                    keyBuilder.append(doubles[j]);
                    if (j + 1  != doubles.length) {
                        keyBuilder.append(", ");
                    }
                }
                keyBuilder.append("}");
                if (i + 1 != value.length) {
                    keyBuilder.append(",\n");
                }
            }
            keyBuilder.append("});\n");
        }
        keyBuilder.append("\n");
        keyBuilder.append("private static final Map<String, INDArray> keys = new HashMap<>();\n");
        keyBuilder.append("static {\n");
        for (String key : net.paramTable().keySet()) {
            keyBuilder.append("       keys.put(");
            keyBuilder.append("\"");
            keyBuilder.append(key);
            keyBuilder.append("\"");
            keyBuilder.append(", ind");
            keyBuilder.append(key);
            keyBuilder.append(");\n");
        }
        keyBuilder.append("}\n");
        System.out.println(keyBuilder);

    }
    private int selectRandomNeuron(double rnd) {
        if (rnd > 0.33 && rnd < 0.66) {
            return 0;
        } else if ( rnd > 0.66){
            return 1;
        } else {
            return 2;
        }
    }

    private Brain createBrain() {
        MultiLayerConfiguration conf = getConf();
        MultiLayerNetwork net = new MultiLayerNetwork(conf);
        net.init();
        mutate(net, keys);
        return new Brain(net);
    }

    public void mutate(MultiLayerNetwork net) {
        mutate(net, net.paramTable());
    }

    public void mutate(MultiLayerNetwork net, Map<String, INDArray> paramTable) {
        Random rand = new Random();
        if (Math.random() > 0.5) {
            System.out.println("mutation!");
            ArrayList<String> keys = new ArrayList<>(paramTable.keySet());
            String key = keys.get(rand.nextInt(paramTable.keySet().size()));
            while (key.contains("b_")) {
                key = keys.get(rand.nextInt(net.paramTable().keySet().size()));
            }
            INDArray array = net.paramTable().get(key);
            double mutation = Math.random() * 10;
            array.getRow(rand.nextInt(array.rows())).muli(mutation);
            net.setParam(key, array);
        }
    }

    private Eyes createEyes() {
        return new Eyes();
    }

    private Antenna createAntenna() {
        return new Antenna();
    }

    private RectilinearMuscle createRectilinearMuscle() {
        return new RectilinearMuscle();
    }

    private RotationalMuscle createRotationalMuscle() {
        return new RotationalMuscle();
    }

    public Creature createCreature(int id) {
        return new Creature(createBrain(), createEyes(), createAntenna(), createAntenna(),createRectilinearMuscle(), createRotationalMuscle(), createRotationalMuscle(), id);
    }
    public Creature createCreature(int id, MultiLayerNetwork net) {
        return new Creature(createBrain(), createEyes(), createAntenna(), createAntenna(),createRectilinearMuscle(), createRotationalMuscle(), createRotationalMuscle(), id);
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

    private INDArray getSensoryData(Creature creature) {
        return Nd4j.create(new double[][]{new double[]{creature.getLeftAntenna().getInput(), creature.getRightAntenna().getInput(), creature.getEyes().getInput()}});
    }

    private double calculateRectilinearMuscleData(INDArray indArray) {
        INDArray back = indArray.getColumn(BACK_ARM);
        return back.getDouble(0);
    }

    public void calculateOutputs(Creature creature) {
        MultiLayerNetwork net = creature.getBrain().getNet();
        INDArray indArray = getSensoryData(creature);
        INDArray output = net.output(indArray);

        creature.getLeftMuscle().setOutput(output.getColumn(LEFT_ARM).getDouble());
        creature.getRightMuscle().setOutput(output.getColumn(RIGHT_ARM).getDouble());

        System.out.println("service diff: " +(creature.getRightMuscle().getOutput() - creature.getLeftMuscle().getOutput()));
        System.out.println("left " + creature.getLeftMuscle().getOutput());
        System.out.println("right"+ creature.getRightMuscle().getOutput());
        RectilinearMuscle forwardMuscle = creature.getRectilinearMuscle();
        forwardMuscle.setOutput(calculateRectilinearMuscleData(output));
    }
}
