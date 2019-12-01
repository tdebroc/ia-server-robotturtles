
package com.grooptown.snorkunking.service.engine.player;

import java.util.Objects;

public class Position {
    private int line;
    private int column;

    public Position() {
    }

    public Position(int line, int column) {
        this.column = column;
        this.line = line;
    }

    public void setLine(int line) {
        this.line = line;
    }

    public void setColumn(int column) {
        this.column = column;
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
