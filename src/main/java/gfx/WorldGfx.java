package gfx;

import model.Creature;
import model.Food;
import model.World;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import service.CreatureService;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class WorldGfx extends JPanel {
    private final World world;
    private final ArrayList<CreatureGfx> creatureGfxs;
    private final ArrayList<FoodGfx> foodGfxs = new ArrayList<>();
    private final CreatureService service;
    private static final long birthAge = 500;
    private static int counter = 2;
    public WorldGfx(World world, CreatureService service) {
        this.world = world;
        this.service = service;
        ArrayList<CreatureGfx> creatureGfxes = new ArrayList<>();
        for (Creature c: world.getCreatures()) {
           creatureGfxes.add(new CreatureGfx(c, service.calculateColor(c)));
        }
        this.creatureGfxs = creatureGfxes;

        for (Food f : world.getFood()) {
            foodGfxs.add(new FoodGfx(f));
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        for (FoodGfx foodGfx : foodGfxs) {
            foodGfx.draw(g2d);
        }

        for (CreatureGfx creatureGfx : creatureGfxs) {
            creatureGfx.draw(g2d);
        }
    }

    public void tick() {
        ArrayList<CreatureGfx> deadCreatures = new ArrayList<>();
        ArrayList<CreatureGfx> newCreatues = new ArrayList<>();
        for (CreatureGfx creatureGfx : creatureGfxs) {
            ArrayList<FoodGfx> eatenFood = new ArrayList<>();
            for (FoodGfx food : foodGfxs) {
                if (canEat(creatureGfx,food)) {
                    eatFood(creatureGfx, food.getFood());
                    eatenFood.add(food);
                }
            }
            removeFood(eatenFood);
            if (creatureGfx.getCreature().getAge() == birthAge) {
                newCreatues.add(giveBirth(creatureGfx.getCreature()));
            }
            if (creatureGfx.getCreature().getStarvationIndex() > 500*10) {
               deadCreatures.add(creatureGfx);
            }

            service.calculateOutputs(creatureGfx.getCreature());
            double diff = creatureGfx.getCreature().getLeftMuscle().getOutput() - creatureGfx.getCreature().getRightMuscle().getOutput();
            creatureGfx.tick(diff*5, creatureGfx.getCreature().getRectilinearMuscle().getOutput());
            creatureGfx.getCreature().getLeftAntenna().setInput(Math.random());
            creatureGfx.getCreature().getRightAntenna().setInput(Math.random());
            creatureGfx.getCreature().getEyes().setInput(Math.random());
            int newStarvationIndex = creatureGfx.getCreature().getStarvationIndex() + 5;
            creatureGfx.getCreature().setStarvationIndex(newStarvationIndex);
            creatureGfx.getCreature().incrementAge();
            if (Math.random() > 0.98) {
               addNewFood();
            }
        }
        removeCreature(deadCreatures);
        creatureGfxs.addAll(newCreatues);
        //repaint(creatureGfx.getBounds());
        repaint();
    }

    private CreatureGfx giveBirth(Creature creature) {
        MultiLayerNetwork net = creature.getBrain().getNet().clone();
        service.mutate(net);
        Creature newCreature = service.createCreature(counter++);
        System.out.println("Creature " + creature.getId() + " gave birth!");
        return new CreatureGfx(newCreature, service.calculateColor(newCreature));
    }

    private void eatFood(CreatureGfx creatureGfx, Food food) {
        System.out.println(creatureGfx.getCreature().getId() + " ate food!");
        int starvationIndatex = creatureGfx.getCreature().getStarvationIndex() - food.getFoodValue();
        creatureGfx.getCreature().setStarvationIndex(starvationIndatex);
    }

    private boolean canEat(CreatureGfx creatureGfx, FoodGfx food) {
        return creatureGfx.intersects(food);
    }
    private void removeCreature(ArrayList<CreatureGfx> creatureGfxes) {
        creatureGfxes.forEach(d -> {
            System.out.println("Create " + d.getCreature().getId() + " is dead");
            world.removeCreature(d.getCreature());
            Creature creature = service.createCreature(counter++);
            world.addCreature(creature);
            creatureGfxs.add(new CreatureGfx(creature, service.calculateColor(creature)));
        });

        creatureGfxs.removeAll(creatureGfxes);
    }
    private void removeFood(ArrayList<FoodGfx> food) {
        food.forEach(f -> {
            world.removeFood(f.getFood());
            foodGfxs.remove(f);
        });
    }

    private void addNewFood() {
        Food f = new Food(60);
        world.addFood(f);
        foodGfxs.add(new FoodGfx(f));
    }
}
