package Sayajin;
import robocode.*;
import robocode.util.*;

public class Coordinate {

    private double x, y;

    /**
     * Default values.
     */
    public Coordinate() {
      this(0, 0);
    }

    /**
     * Defines the coordinate.
     * 
     * @param x double
     * @param y double
     */
    public Coordinate(double x, double y) {
      this.x = x;
      this.y = y;
    }

    /**
     * Gets this x-coordinate.
     * 
     * @return double
     */
    public double getX() {
      return x;
    }

    /**
     * Gets this y-coordinate.
     * 
     * @return double
     */
    public double getY() {
      return y;
    }
}