package com.steelteam.openrhynn.logic;

import com.steelteam.openrhynn.models.Cell;

/*
    This code belongs to marlowe & macrolutions Ltd,
    license: https://github.com/marlowe-fw/Rhynn/blob/master/LICENSE
*/
public class WorldTools {
    //TO-DO
    public static boolean withinRange(int attacker_x, int attacker_y, int target_x, int target_y, int range) {
        if(attacker_x + 20 + range > target_x && attacker_x - range < target_x && attacker_y + 20 + range > target_y && attacker_y - range < target_y)
            return true;
        return false;
    }

    public static boolean isBlocked(World world, int xPosPx, int yPosPx, int dim) {
        /* cords to cell cords*/


        int x1 = (xPosPx + World.blockTolerance) / Cell.cellSize;
        int x2 = (xPosPx + dim - 1 - World.blockTolerance) / Cell.cellSize;
        int y1 = (yPosPx + World.blockTolerance) / Cell.cellSize;
        int y2 = (yPosPx + dim - 1 - World.blockTolerance) / Cell.cellSize;

        int widthPx = world.width * Cell.cellSize;
        int heightPx = world.height * Cell.cellSize;

        boolean result = xPosPx + dim <= widthPx &&
                        yPosPx + dim <= heightPx &&
                                xPosPx >= 0 && yPosPx >= 0 &&
                                !(world.cells[x1][y1].blocked) &&
                                !(world.cells[x1][y2].blocked) &&
                                !(world.cells[x2][y1].blocked) &&
                                !(world.cells[x2][y2].blocked);

        return !result;
    }

    public static boolean isPeaceful(World world, int xPosPx, int yPosPx, int dim) {
        /* cords to cell cords*/


        int x1 = (xPosPx + World.blockTolerance) / Cell.cellSize;
        int x2 = (xPosPx + dim - 1 - World.blockTolerance) / Cell.cellSize;
        int y1 = (yPosPx + World.blockTolerance) / Cell.cellSize;
        int y2 = (yPosPx + dim - 1 - World.blockTolerance) / Cell.cellSize;

        int widthPx = world.width * Cell.cellSize;
        int heightPx = world.height * Cell.cellSize;

        boolean result = xPosPx + dim <= widthPx &&
                yPosPx + dim <= heightPx &&
                xPosPx >= 0 && yPosPx >= 0 &&
                !(world.cells[x1][y1].peaceful) &&
                !(world.cells[x1][y2].peaceful) &&
                !(world.cells[x2][y1].peaceful) &&
                !(world.cells[x2][y2].peaceful);

        return !result;
    }
}
