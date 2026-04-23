package com.narxoz.rpg.state;

import com.narxoz.rpg.combatant.Hero;

public class BerserkState implements HeroState {
    @Override
    public String getName() {
        return "Berserk";
    }
    @Override
    public int modifyOutgoingDamage(int basePower) {
        return (int) (basePower * 1.5);
    }
    @Override
    public int modifyIncomingDamage(int rawDamage) {
        return (int) (rawDamage * 1.3);
    }
    @Override
    public boolean canAct() {
        return true;
    }
    @Override
    public void onTurnStart(Hero hero) {
        if (hero.getHp() > hero.getMaxHp() * 0.7) {
            System.out.println("  [BERSERK] " + hero.getName() + " calms down and returns to Normal.");
            hero.setState(new NormalState());
        } else {
            System.out.println("  [BERSERK] " + hero.getName() + " rages! (HP: " + hero.getHp() + "/" + hero.getMaxHp() + ")");
        }
    }
    @Override
    public void onTurnEnd(Hero hero) {}
}
