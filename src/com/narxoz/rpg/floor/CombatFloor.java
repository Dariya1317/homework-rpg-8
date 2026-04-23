package com.narxoz.rpg.floor;

import com.narxoz.rpg.combatant.Hero;
import com.narxoz.rpg.combatant.Monster;
import com.narxoz.rpg.state.NormalState;
import com.narxoz.rpg.state.PoisonedState;
import com.narxoz.rpg.state.StunnedState;

import java.util.ArrayList;
import java.util.List;

public class CombatFloor extends TowerFloor {

    private final String floorName;
    private List<Monster> monsters;

    public CombatFloor(String floorName) {
        this.floorName = floorName;
    }

    @Override
    protected String getFloorName() {
        return floorName;
    }

    @Override
    protected void setup(List<Hero> party) {
        monsters = new ArrayList<>();
        monsters.add(new Monster("Poison Viper", 30, 8));
        monsters.add(new Monster("Stone Guardian", 45, 12));
        System.out.println("  [SETUP] Two enemies appear: Poison Viper and Stone Guardian!");
    }

    @Override
    protected FloorResult resolveChallenge(List<Hero> party) {
        System.out.println("  [COMBAT] Battle begins!");
        int totalDamageTaken = 0;

        while (hasAliveMonsters() && hasAliveHeroes(party)) {
            for (Hero hero : party) {
                if (!hero.isAlive()) continue;

                hero.onTurnStart();

                Monster target = firstAliveMonster();
                if (target == null) break;

                if (hero.canAct()) {
                    int dmg = hero.getEffectiveDamage();
                    target.takeDamage(dmg);
                    System.out.println("  [COMBAT] " + hero.getName() + " attacks " + target.getName()
                            + " for " + dmg + " dmg. (Monster HP: " + target.getHp() + ")");

                    if (!target.isAlive()) {
                        System.out.println("  [COMBAT] " + target.getName() + " is defeated!");
                        monsters.remove(target);
                        target = firstAliveMonster();
                        if (target == null) {
                            hero.onTurnEnd();
                            break;
                        }
                    }
                } else {
                    System.out.println("  [COMBAT] " + hero.getName() + " skips turn.");
                }

                int received = hero.receiveAttack(target.getAttackPower());
                totalDamageTaken += received;
                System.out.println("  [COMBAT] " + target.getName() + " hits " + hero.getName()
                        + " for " + received + " dmg. (Hero HP: " + hero.getHp() + ")");

                applyMonsterEffect(target, hero);

                hero.onTurnEnd();
            }
        }

        boolean cleared = !hasAliveMonsters() && hasAliveHeroes(party);
        String summary = cleared
                ? "Party cleared " + floorName + "!"
                : "Party was defeated on " + floorName + ".";
        System.out.println("  [RESULT] " + summary);
        return new FloorResult(cleared, totalDamageTaken, summary);
    }

    @Override
    protected void awardLoot(List<Hero> party, FloorResult result) {
        if (result.isCleared()) {
            System.out.println("  [LOOT] Survivors recover 10 HP.");
            for (Hero hero : party) {
                if (hero.isAlive()) hero.heal(10);
            }
        }
    }

    private void applyMonsterEffect(Monster monster, Hero hero) {
        if (!hero.isAlive()) return;
        if (!(hero.getState() instanceof NormalState)) return;

        if (monster.getName().equals("Poison Viper")) {
            System.out.println("  [EFFECT] Poison Viper's bite poisons " + hero.getName() + "!");
            hero.setState(new PoisonedState(3));
        } else if (monster.getName().equals("Stone Guardian")) {
            System.out.println("  [EFFECT] Stone Guardian's slam stuns " + hero.getName() + "!");
            hero.setState(new StunnedState());
        }
    }

    private boolean hasAliveMonsters() {
        return monsters.stream().anyMatch(Monster::isAlive);
    }

    private boolean hasAliveHeroes(List<Hero> party) {
        return party.stream().anyMatch(Hero::isAlive);
    }

    private Monster firstAliveMonster() {
        return monsters.stream().filter(Monster::isAlive).findFirst().orElse(null);
    }
}
