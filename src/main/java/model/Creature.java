package model;


public class Creature {
    private final Brain brain;
    private final Eyes eyes;
    private final Antenna leftAntenna;
    private final Antenna rightAntenna;
    private final RectilinearMuscle rectilinearMuscle;
    private final RotationalMuscle rightMuscle;
    private final RotationalMuscle leftMuscle;
    private final int id;
    public static final String saveLocation = "src/main/resources/creature";

    public Creature(Brain brain, Eyes eyes, Antenna leftAntenna, Antenna rightAntenna, RectilinearMuscle rectilinearMuscle, RotationalMuscle rightMuscle, RotationalMuscle leftMuscle, int id) {
        this.brain = brain;
        this.eyes = eyes;
        this.leftAntenna = leftAntenna;
        this.rightAntenna = rightAntenna;
        this.rectilinearMuscle = rectilinearMuscle;
        this.rightMuscle = rightMuscle;
        this.leftMuscle = leftMuscle;
        this.id = id;
    }


    public RectilinearMuscle getRectilinearMuscle() {
        return rectilinearMuscle;
    }

    public Eyes getEyes() {
        return eyes;
    }

    public Brain getBrain() {
        return brain;
    }

    public int getId() {
        return id;
    }

    public String getSaveLocation() {
        return saveLocation;
    }

    public RotationalMuscle getRightMuscle() {
        return rightMuscle;
    }

    public RotationalMuscle getLeftMuscle() {
        return leftMuscle;
    }

    public Antenna getRightAntenna() {
        return rightAntenna;
    }

    public Antenna getLeftAntenna() {
        return leftAntenna;
    }

    @Override
    public String toString() {
        return "Creature{" +
            "brain=" + brain +
            ", eyes=" + eyes +
            ", leftAntenna=" + leftAntenna +
            ", rightAntenna=" + rightAntenna +
            ", rectilinearMuscle=" + rectilinearMuscle +
            ", rightMuscle=" + rightMuscle +
            ", leftMuscle=" + leftMuscle +
            ", id=" + id +
            '}';
    }
}
