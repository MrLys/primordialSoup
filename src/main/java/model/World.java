package model;

import java.util.List;

public class World {
    private final List<Creature> creatures;
    private final List<Food> food;

    public World(List<Creature> creatures, List<Food> food) {
        this.creatures = creatures;
        this.food = food;
    }

    public List<Creature> getCreatures() {
        return creatures;
    }

    public List<Food> getFood() {
        return food;
    }
}
