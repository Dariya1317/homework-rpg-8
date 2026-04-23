package com.narxoz.rpg.state;

import com.narxoz.rpg.combatant.Hero;

public class StunnedState implements HeroState {

    @Override
    public String getName() {
        return "Stunned";
    }
    @Override
    public int modifyOutgoingDamage(int basePower) {
        return basePower;
    }
    @Override
    public int modifyIncomingDamage(int rawDamage) {
        return (int) (rawDamage * 1.5);
    }
    @Override
    public boolean canAct() {
        return false;
    }
    @Override
    public void onTurnStart(Hero hero) {
        System.out.println("  [STUNNED] " + hero.getName() + " is stunned and cannot act!");
    }
    @Override
    public void onTurnEnd(Hero hero) {
        System.out.println("  [STUNNED] " + hero.getName() + " recovers from stun.");
        hero.setState(new NormalState());
    }
}
