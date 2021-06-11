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
import java.util.Iterator;
import java.util.Map;

public class CreatureService {
    private static final int RIGHT_ARM = 0;
    private static final int LEFT_ARM = 1;
    private static final int BACK_ARM = 2;
    private static final int layers = 2;
    private static final double[][] w_0 = new double[][]{
        new double[]{0.1659,   -0.6131, -0.3444},
        new double[]{ -0.6771,    0.3848,   -0.0784},
    new double[]{-0.1544,   -1.1473,   -0.6404}};
    private static final String w_0_key = "0_W";
    private static final double[][] w_1 = new double[][]{
        new double[]{ -0.7643,   -0.0471,    0.8813},
        new double[]{ 0.2855,   -0.1222,   -0.6253},
        new double[]{0.0746,   -0.6142,   -0.1725}};
    private static final String w_1_key = "1_W";
    private static final INDArray b_0 = Nd4j.create(new double[][]{
        new double[]{ 0,         0,         0}});
    private static final String b_0_key = "0_b";
    private static final INDArray b_1 = Nd4j.create(new double[][]{
        new double[]{ 0,         0,         0}});
    private static final String b_1_key = "1_b";

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

        while(paramap_iterator.hasNext()) {
            Map.Entry<String, INDArray> me = (Map.Entry<String, INDArray>) paramap_iterator.next();
            StringBuilder keyBuilder = new StringBuilder();
            String key = me.getKey();
            if (key.contains("b")) {
                keyBuilder.append("private static final INDArray ");
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
                keyBuilder.append("});");
                System.out.println(keyBuilder);
            } else {
                keyBuilder.append("private static final double[][] ");
                keyBuilder.append(me.getKey());
                keyBuilder.append("= new double[][]{\n");
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
                keyBuilder.append("};");
                System.out.println(keyBuilder);
            }

        }
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
        if (Math.random() > 0.9) {
            System.out.println("mutation!");
            NormalizerMinMaxScaler scaler = new NormalizerMinMaxScaler();
            int neuron = selectRandomNeuron(Math.random());
            double mutation = Math.random() * 10;
            INDArray w0 = Nd4j.create(w_0.clone());
            INDArray w1 = Nd4j.create(w_1.clone());
            if (Math.random() > 0.5) {

                // first layer
                INDArray array = w0;
                //System.out.println("mutating first layer from " + array);
                INDArray neurons = array.getRow(neuron);
                neurons = neurons.muli(mutation);
                //scaler.setFeatureStats(Nd4j.create(new double[]{neurons.minNumber().doubleValue()}), Nd4j.create(new double[]{neurons.maxNumber().doubleValue()}));
                //scaler.transform(array);
                System.out.println("mutating first layer to " + array);
            } else {
                // second layer

                INDArray array = w1;
                //System.out.println("mutating second layer from " + array);
                INDArray neurons = array.getRow(neuron);
                neurons = neurons.muli(mutation);
                //scaler.setFeatureStats(Nd4j.create(new double[]{neurons.minNumber().doubleValue()}), Nd4j.create(new double[]{neurons.maxNumber().doubleValue()}));
                //scaler.transform(array);
                //System.out.println("mutating second layer to " + array);

            }
            net.setParam(w_0_key, w0);
            net.setParam(b_0_key, b_0);
            net.setParam(w_1_key, w1);
            net.setParam(b_1_key, b_1);
        }
        return new Brain(net);
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
    private double calculateRotationalMuscleData(INDArray indArray) {
        INDArray right = indArray.getColumn(RIGHT_ARM);
        INDArray left = indArray.getColumn(LEFT_ARM);
        return right.getDouble(0) - left.getDouble(0);
    }

    private double calculateRectilinearMuscleData(INDArray indArray) {
        INDArray back = indArray.getColumn(BACK_ARM);
        return back.getDouble(0);
    }

    public void calculateOutputs(Creature creature) {
        MultiLayerNetwork net = creature.getBrain().getNet();
        INDArray indArray = getSensoryData(creature);
        INDArray output = net.output(indArray);

        double rotationAngle = calculateRotationalMuscleData(output);
        RotationalMuscle muscleToWork;
        if (rotationAngle > 0) {
            muscleToWork = creature.getRightMuscle();
        } else {
            muscleToWork = creature.getLeftMuscle();
        }
        muscleToWork.setOutput(rotationAngle);
        RectilinearMuscle forwardMuscle = creature.getRectilinearMuscle();
        forwardMuscle.setOutput(calculateRectilinearMuscleData(output));
        System.out.println(creature.getRightMuscle().getOutput());
        System.out.println(creature.getLeftMuscle().getOutput());
    }
}
