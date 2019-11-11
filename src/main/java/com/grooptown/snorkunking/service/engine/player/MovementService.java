package com.grooptown.snorkunking.service.engine.player;

import java.util.Arrays;

import static com.grooptown.snorkunking.service.engine.player.DirectionEnum.*;

public class MovementService {
    public final static DirectionEnum[] directions = new DirectionEnum[] {
            WEST, NORTH, EAST, SOUTH
    };
    private final static int[] directionsX = {-1, 0, 1, 0};
    private final static int[] directionsY = {0, -1, 0, 1};
    public static Position getNextPosition(Position previousPosition, DirectionEnum direction) {
        int currentIndex = Arrays.asList(directions).indexOf(direction);
        return new Position(
                previousPosition.getLine() + directionsY[currentIndex],
                previousPosition.getColumn() + directionsX[currentIndex]
        );
    }

    public static DirectionEnum getNextPosition(DirectionEnum direction, boolean clockWise) {
        int currentIndex = Arrays.asList(directions).indexOf(direction);
        int nextPosition = currentIndex + (clockWise ? 1 : -1);
        nextPosition = (nextPosition + 4) % 4;
        return directions[nextPosition];
    }

    static DirectionEnum getOppositeDirection(DirectionEnum direction) {
        int currentIndex = Arrays.asList(directions).indexOf(direction);
        int nextPosition = (currentIndex + 2) % 4;
        return directions[nextPosition];
    }
}
