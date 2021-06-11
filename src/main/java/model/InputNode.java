package model;

abstract class InputNode {
    private double input = 0.0;

    public double getInput() {
        return input;
    }

    public void setInput(double input) {
        this.input = input;
    }

    @Override
    public String toString() {
        return "InputNode{" +
            "input=" + input +
            '}';
    }
}
