package model;

import util.SimConstants;

public class Vehicle {
    private double x, y;
    private double speed;
    private Direction direction;
    private CarModel model;
    private boolean active = true;
    private final double width;
    private final double height;
    public Vehicle(Direction direction, double speed, double x, double y, CarModel model) {
        this.direction = direction;
        this.speed = speed;
        this.x = x;
        this.y = y;
        this.model = model;
        this.height = model.getHeight();
        this.width = model.getWidth();

    }

    public void updatePosition(boolean canMove, long now) {
        if (!active || !canMove) return;

        switch (direction) {
            case EAST:  x += speed; break;
            case WEST:  x -= speed; break;
            case NORTH: y -= speed; break;
            case SOUTH: y += speed; break;
        }

        // Sahneden çıkarsa pasif yapılır
        if (x < -150 || x > 950 || y < -150 || y > 750)
        {
            active = false;
        }
    }

    public boolean isActive() {
        return active;
    }

    public double getX() { return x; }
    public double getY() { return y; }
    public double getWidth() { return width; }
    public double getHeight() { return height; }
    public Direction getDirection() { return direction; }
    public CarModel getModel() { return model; }

    public double distanceTo(Vehicle other) {
        if (this.direction != other.direction) return Double.MAX_VALUE;

        switch (direction) {
            case EAST:  return other.x - (this.x + width);
            case WEST:  return this.x - (other.x + width);
            case NORTH: return this.y - (other.y + height);
            case SOUTH: return other.y - (this.y + height);
        }
        return Double.MAX_VALUE;
    }

    public boolean approachingIntersection() {
        Double boundary = SimConstants.LIGHT_BOUNDARIES.get(direction);
        switch (direction) {
            case EAST:  return x + width >= boundary && x < boundary + 10;
            case WEST:  return x <= boundary && x > boundary - 10;
            case NORTH: return y <= boundary && y > boundary - 10;
            case SOUTH: return y + height >= boundary && y < boundary + 10;
        }
        return false;
    }
}
