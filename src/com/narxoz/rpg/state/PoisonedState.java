package com.narxoz.rpg.state;

import com.narxoz.rpg.combatant.Hero;

public class PoisonedState implements HeroState {

    private static final int POISON_DAMAGE_PER_TURN = 5;

    private int turnsRemaining;

    public PoisonedState(int duration) {
        this.turnsRemaining = duration;
    }

    @Override
    public String getName() {
        return "Poisoned";
    }

    @Override
    public int modifyOutgoingDamage(int basePower) {
        return (int) (basePower * 0.7);
    }

    @Override
    public int modifyIncomingDamage(int rawDamage) {
        return (int) (rawDamage * 1.2);
    }

    @Override
    public boolean canAct() {
        return true;
    }

    @Override
    public void onTurnStart(Hero hero) {
        hero.takeDamage(POISON_DAMAGE_PER_TURN);
        System.out.println("  [POISON] " + hero.getName() + " takes " + POISON_DAMAGE_PER_TURN
                + " poison damage! (HP: " + hero.getHp() + ")");
        turnsRemaining--;
    }

    @Override
    public void onTurnEnd(Hero hero) {
        if (turnsRemaining <= 0) {
            System.out.println("  [POISON] " + hero.getName() + "'s poison wears off.");
            hero.setState(new NormalState());
        }
    }
}
