package com.narxoz.rpg.tower;

import com.narxoz.rpg.combatant.Hero;
import com.narxoz.rpg.floor.FloorResult;
import com.narxoz.rpg.floor.TowerFloor;
import java.util.List;

public class TowerRunner {

    public TowerRunResult run(List<Hero> party, List<TowerFloor> floors) {
        int floorsCleared = 0;

        for (TowerFloor floor : floors) {
            FloorResult result = floor.explore(party);

            if (result.isCleared()) {
                floorsCleared++;
            }
            if (party.stream().noneMatch(Hero::isAlive)) {
                System.out.println("\n  [TOWER] All heroes have fallen. The tower stands unconquered.");
                break;
            }
        }
        int heroesSurviving = (int) party.stream().filter(Hero::isAlive).count();
        boolean reachedTop = floorsCleared == floors.size();
        return new TowerRunResult(floorsCleared, heroesSurviving, reachedTop);
    }
}
