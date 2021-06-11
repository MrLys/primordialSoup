package model;

import java.util.List;

public class Creature {
    private final Brain brain;
    private final Eyes eyes;
    private final List<Nose> noses;
    private final RectilinearMuscle rectilinearMuscle;
    private final List<RotationalMuscle> rotationalMuscle;
    private final int id;
    public static final String saveLocation = "src/main/resources/creature";

    public Creature(Brain brain, Eyes eyes, List<Nose> noses, RectilinearMuscle rectilinearMuscle, List<RotationalMuscle> rotationalMuscle, int id) {
        this.brain = brain;
        this.eyes = eyes;
        this.noses = noses;
        this.rectilinearMuscle = rectilinearMuscle;
        this.rotationalMuscle = rotationalMuscle;
        this.id = id;
    }


    public List<RotationalMuscle> getRotationalMuscle() {
        return rotationalMuscle;
    }

    public RectilinearMuscle getRectilinearMuscle() {
        return rectilinearMuscle;
    }

    public List<Nose> getNoses() {
        return noses;
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

}
