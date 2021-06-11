package model;

import java.util.ArrayList;

public class World {
    private final ArrayList<Creature> creatures;
    private final ArrayList<Food> food;

    public World(ArrayList<Creature> creatures, ArrayList<Food> food) {
        this.creatures = creatures;
        this.food = food;
    }

    public ArrayList<Creature> getCreatures() {
        return creatures;
    }

    public ArrayList<Food> getFood() {
        return food;
    }

    public void removeFood(Food eatedFood) {
        food.remove(eatedFood);
    }

    public void removeCreature(Creature deadCreature) {
        creatures.remove(deadCreature);
    }

    public void addFood(Food newFood) {
        this.food.add(newFood);
    }

    public void addCreature(Creature newCreature) {
        this.creatures.add(newCreature);
    }
}
