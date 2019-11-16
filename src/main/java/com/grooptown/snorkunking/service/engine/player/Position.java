
package com.grooptown.snorkunking.service.engine.player;

import java.util.Objects;

public class Position {
    private final int line;
    private final int column;

    public Position(int line, int column) {
        this.column = column;
        this.line = line;
    }

    public int getColumn() {
        return column;
    }

    public int getLine() {
        return line;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Position position = (Position) o;
        return line == position.line &&
            column == position.column;
    }

    @Override
    public int hashCode() {
        return Objects.hash(line, column);
    }

    @Override
    public String toString() {
        return "Position{" + "" + line + ", " + column + '}';
    }
}
