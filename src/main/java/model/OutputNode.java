package model;

abstract class OutputNode {
    private double output = 0.0;

    public double getOutput() {
        return output;
    }

    public void setOutput(double output) {
        this.output = output;
    }

    @Override
    public String toString() {
        return "OutputNode{" +
            "output=" + output +
            '}';
    }
}
