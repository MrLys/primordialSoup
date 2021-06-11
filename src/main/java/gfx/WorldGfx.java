package gfx;

import model.Creature;
import model.Food;
import model.World;
import service.CreatureService;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class WorldGfx extends JPanel {
    private final World world;
    private final List<CreatureGfx> creatureGfxs;
    private final List<FoodGfx> foodGfxs = new ArrayList<>();
    private final CreatureService service;

    public WorldGfx(World world, CreatureService service) {
        this.world = world;
        this.service = service;
        List<CreatureGfx> creatureGfxes = new ArrayList<>();
        for (Creature c: world.getCreatures()) {
           creatureGfxes.add(new CreatureGfx(c));
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
        for (CreatureGfx creatureGfx : creatureGfxs) {
            service.calculateOutputs(creatureGfx.getCreature());
            System.out.println(world.getCreatures().get(0).equals(creatureGfx.getCreature()));
            if (creatureGfx.getCreature().getLeftMuscle().getOutput() > 0) {
                creatureGfx.tick(creatureGfx.getCreature().getLeftMuscle().getOutput(), creatureGfx.getCreature().getRectilinearMuscle().getOutput());
            } else {
                creatureGfx.tick(creatureGfx.getCreature().getRightMuscle().getOutput(), creatureGfx.getCreature().getRectilinearMuscle().getOutput());
            }
            creatureGfx.getCreature().getLeftAntenna().setInput(Math.random());
            creatureGfx.getCreature().getRightAntenna().setInput(Math.random());
        }
        //repaint(creatureGfx.getBounds());
        repaint();
    }
}
