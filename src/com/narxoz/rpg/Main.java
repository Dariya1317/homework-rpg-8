package com.narxoz.rpg;

import com.narxoz.rpg.combatant.Hero;
import com.narxoz.rpg.floor.BossFloor;
import com.narxoz.rpg.floor.CombatFloor;
import com.narxoz.rpg.floor.RestFloor;
import com.narxoz.rpg.floor.TowerFloor;
import com.narxoz.rpg.floor.TrapFloor;
import com.narxoz.rpg.state.BerserkState;
import com.narxoz.rpg.state.NormalState;
import com.narxoz.rpg.tower.TowerRunResult;
import com.narxoz.rpg.tower.TowerRunner;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        Hero warrior = new Hero("Warrior", 100, 20, 5);
        warrior.setState(new NormalState());

        Hero mage = new Hero("Mage", 70, 30, 2);
        mage.setState(new BerserkState());

        List<Hero> party = List.of(warrior, mage);

        List<TowerFloor> floors = List.of(
            new CombatFloor("Skeleton Crypt"),
            new TrapFloor("Poison Corridor"),
            new RestFloor("Ancient Shrine"),
            new BossFloor("Dragon's Lair")
        );

        System.out.println("========================================");
        System.out.println("   THE HAUNTED TOWER - CLIMB BEGINS");
        System.out.println("========================================");
        System.out.println("Party: " + warrior.getName() + " [" + warrior.getState().getName() + "]"
                + ", " + mage.getName() + " [" + mage.getState().getName() + "]");

        TowerRunner runner = new TowerRunner();
        TowerRunResult result = runner.run(party, floors);

        System.out.println("\n========================================");
        System.out.println("         TOWER RUN COMPLETE");
        System.out.println("========================================");
        System.out.println("Floors cleared:    " + result.getFloorsCleared() + " / " + floors.size());
        System.out.println("Heroes surviving:  " + result.getHeroesSurviving());
        System.out.println("Tower conquered:   " + (result.isReachedTop() ? "YES" : "NO"));
        System.out.println("========================================");
    }
}
