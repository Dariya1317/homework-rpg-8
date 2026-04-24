package com.narxoz.rpg.floor;

import com.narxoz.rpg.combatant.Hero;
import com.narxoz.rpg.state.NormalState;
import java.util.List;

public class RestFloor extends TowerFloor {

    private final String floorName;

    public RestFloor(String floorName) {
        this.floorName = floorName;
    }

    @Override
    protected String getFloorName() {
        return floorName;
    }

    @Override
    protected void setup(List<Hero> party) {
        System.out.println("  [SETUP] The party finds a quiet sanctuary to rest.");
    }

    @Override
    protected FloorResult resolveChallenge(List<Hero> party) {
        System.out.println("  [REST] The party rests and recovers...");
        for (Hero hero : party) {
            if (hero.isAlive()) {
                hero.heal(20);
                System.out.println("  [REST] " + hero.getName() + " restores 20 HP. (HP: "
                        + hero.getHp() + "/" + hero.getMaxHp() + ")");
            }
        }
        return new FloorResult(true, 0, "Party rested and recovered.");
    }

    @Override
    protected boolean shouldAwardLoot(FloorResult result) {
        System.out.println("  [HOOK] This is a rest floor — no loot awarded.");
        return false;
    }

    @Override
    protected void awardLoot(List<Hero> party, FloorResult result) {
    }

    @Override
    protected void cleanup(List<Hero> party) {
        for (Hero hero : party) {
            if (hero.isAlive() && !(hero.getState() instanceof NormalState)) {
                System.out.println("  [CLEANUP] " + hero.getName()
                        + "'s ailments fade after resting.");
                hero.setState(new NormalState());
            }
        }
    }
}
