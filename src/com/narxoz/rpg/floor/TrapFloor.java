package com.narxoz.rpg.floor;

import com.narxoz.rpg.combatant.Hero;
import com.narxoz.rpg.state.NormalState;
import com.narxoz.rpg.state.PoisonedState;
import java.util.List;

public class TrapFloor extends TowerFloor {

    private final String floorName;

    public TrapFloor(String floorName) {
        this.floorName = floorName;
    }

    @Override
    protected String getFloorName() {
        return floorName;
    }

    @Override
    protected void setup(List<Hero> party) {
        System.out.println("  [SETUP] The corridor is filled with dart traps and poison mist!");
    }

    @Override
    protected FloorResult resolveChallenge(List<Hero> party) {
        System.out.println("  [TRAP] The party moves through the trapped corridor...");
        int totalDamageTaken = 0;
        boolean anyoneAlive = false;

        for (Hero hero : party) {
            if (!hero.isAlive()) continue;

            hero.onTurnStart();

            int received = hero.receiveAttack(15);
            totalDamageTaken += received;
            System.out.println("  [TRAP] A dart hits " + hero.getName()
                    + " for " + received + " dmg! (HP: " + hero.getHp() + ")");

            if (hero.isAlive()) {
                if (hero.getState() instanceof NormalState) {
                    System.out.println("  [TRAP] Poison mist envelops " + hero.getName() + "!");
                    hero.setState(new PoisonedState(2));
                }
                anyoneAlive = true;
            }

            hero.onTurnEnd();
        }

        boolean cleared = anyoneAlive;
        String summary = cleared
                ? "Party pushed through the traps, but took heavy damage."
                : "The party was overwhelmed by the traps.";
        System.out.println("  [RESULT] " + summary);
        return new FloorResult(cleared, totalDamageTaken, summary);
    }

    @Override
    protected boolean shouldAwardLoot(FloorResult result) {
        if (result.getDamageTaken() >= 30) {
            System.out.println("  [HOOK] Too much damage taken — no loot awarded.");
            return false;
        }
        return result.isCleared();
    }

    @Override
    protected void awardLoot(List<Hero> party, FloorResult result) {
        System.out.println("  [LOOT] Skilled trap navigation! Each survivor gains 15 HP.");
        for (Hero hero : party) {
            if (hero.isAlive()) hero.heal(15);
        }
    }
}
