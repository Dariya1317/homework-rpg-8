package com.narxoz.rpg.floor;

import com.narxoz.rpg.combatant.Hero;
import com.narxoz.rpg.combatant.Monster;
import com.narxoz.rpg.state.BerserkState;
import com.narxoz.rpg.state.NormalState;
import java.util.List;

public class BossFloor extends TowerFloor {

    private final String floorName;
    private Monster boss;

    public BossFloor(String floorName) {
        this.floorName = floorName;
    }
    @Override
    protected String getFloorName() {
        return floorName;
    }
    @Override
    protected void announce() {
        System.out.println("\n!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        System.out.println("!!! BOSS BATTLE: " + floorName + " !!!");
        System.out.println("!!! Prepare for the fight of your lives !!!");
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n");
    }
    @Override
    protected void setup(List<Hero> party) {
        boss = new Monster("Ancient Dragon", 80, 18);
        System.out.println("  [SETUP] The Ancient Dragon awakens from its slumber!");
    }
    @Override
    protected FloorResult resolveChallenge(List<Hero> party) {
        System.out.println("  [COMBAT] Boss battle begins!");
        int totalDamageTaken = 0;
        while (boss.isAlive() && hasAliveHeroes(party)) {
            for (Hero hero : party) {
                if (!hero.isAlive() || !boss.isAlive()) continue;

                hero.onTurnStart();

                if (hero.canAct()) {
                    int dmg = hero.getEffectiveDamage();
                    boss.takeDamage(dmg);
                    System.out.println("  [COMBAT] " + hero.getName() + " attacks " + boss.getName()
                            + " for " + dmg + " dmg! (Boss HP: " + boss.getHp() + ")");
                } else {
                    System.out.println("  [COMBAT] " + hero.getName() + " skips turn.");
                }

                if (boss.isAlive()) {
                    int received = hero.receiveAttack(boss.getAttackPower());
                    totalDamageTaken += received;
                    System.out.println("  [COMBAT] " + boss.getName() + " strikes " + hero.getName()
                            + " for " + received + " dmg! (Hero HP: " + hero.getHp() + ")");

                    if (hero.isAlive() && hero.getHp() < hero.getMaxHp() * 0.5
                            && hero.getState() instanceof NormalState) {
                        System.out.println("  [BERSERK TRIGGER] " + hero.getName()
                                + " is near death and enters a rage!");
                        hero.setState(new BerserkState());
                    }
                }

                hero.onTurnEnd();
            }
        }
        boolean cleared = !boss.isAlive() && hasAliveHeroes(party);
        String summary = cleared
                ? "The Ancient Dragon has been slain!"
                : "The party fell before the Ancient Dragon.";
        System.out.println("  [RESULT] " + summary);
        return new FloorResult(cleared, totalDamageTaken, summary);
    }
    @Override
    protected void awardLoot(List<Hero> party, FloorResult result) {
        if (result.isCleared()) {
            System.out.println("  [LOOT] Victory! The party is fully restored!");
            for (Hero hero : party) {
                if (hero.isAlive()) hero.heal(hero.getMaxHp());
            }
        }
    }
    private boolean hasAliveHeroes(List<Hero> party) {
        return party.stream().anyMatch(Hero::isAlive);
    }
}
