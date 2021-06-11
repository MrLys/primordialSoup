package model;

import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;

public class Brain {
    private final MultiLayerNetwork net;

    public Brain(MultiLayerNetwork net) {
        this.net = net;
    }

    public MultiLayerNetwork getNet() {
        return net;
    }
}
