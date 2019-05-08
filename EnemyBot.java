package Sayajin;

import robocode.*;
import robocode.util.*;
import robocode.ScannedRobotEvent;
import java.util.ArrayList;
import java.awt.geom.Point2D;
import java.awt.geom.*;
import java.io.Serializable;
import java.util.LinkedList;

public class EnemyBot extends Robot {

    private volatile double bearing;
    private volatile double distance;
    private volatile double energy;
    private volatile double relativeEnergy;
    private volatile double heading;
    private volatile String name = "";
    private volatile double velocity;
    private volatile double x;
    private volatile double y;

    public volatile ArrayList surfingDirections;
    public volatile ArrayList absoluteBearings;
    public volatile LinkedList bulletWave;

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getBearing() {
        return bearing;
    }

    public void setBearing(double bearing) {
        this.bearing = bearing;
    }

    public double getEnergy() {
        return energy;
    }

    public void setEnergy(double energy) {
        this.energy = energy;
    }

    public double getHeading() {
        return heading;
    }

    public void setHeading(double heading) {
        this.heading = heading;
    }

    public double getVelocity() {
        return velocity;
    }

    public void setVelocity(double velocity) {
        this.velocity = velocity;
    }

    public void reset() {
        bearing = 0.0;
        distance = 0.0;
        energy = 0.0;
        relativeEnergy = 100.0;
        heading = 0.0;
        name = "";
        velocity = 0.0;
        x = 0.0;
        y = 0.0;

        surfingDirections = new ArrayList();
        absoluteBearings = new ArrayList();
        bulletWave = new LinkedList();
    }

    public void update(ScannedRobotEvent e, AdvancedRobot robot) {
        bearing = e.getBearing();
        distance = e.getDistance();
        energy = e.getEnergy();
        heading = e.getHeading();
        name = e.getName();
        velocity = e.getVelocity();
        double bulletPower = this.relativeEnergy - this.energy;
        relativeEnergy = this.energy;


        double lateralVelocity = robot.getVelocity()*Math.sin(e.getBearingRadians());
        double absBearing = e.getBearingRadians() + robot.getHeadingRadians();

        surfingDirections.add(0, new Integer((lateralVelocity >= 0) ? 1 : -1));
        absoluteBearings.add(0, new Double(absBearing + Math.PI));

        if (bulletPower <= 3.0 && bulletPower >= 0.1 && surfingDirections.size() > 2) {
            Bullet bullet = new Bullet(
                new Point2D.Double(x, y),
                robot.getTime() - 1, 
                bulletVelocity(bulletPower), 
                ((Double) absoluteBearings.get(2)).doubleValue(),
                bulletVelocity(bulletPower), 
                ((Integer) surfingDirections.get(2)).intValue()
            );
            System.out.println(bullet);
            System.out.println("-------------------------------------------");

            bulletWave.addLast(bullet);
        }

        updateBullets(robot);

        x = robot.getX() + Math.sin(absBearing) * e.getDistance();
        y = robot.getY() + Math.cos(absBearing) * e.getDistance();
    }

    public boolean none() {
        return "".equals(name);
    }

    public double getFutureX(long when){
        return x + Math.sin(Math.toRadians(getHeading())) * getVelocity() * when;
    }

    public double getFutureY(long when){
        return y + Math.cos(Math.toRadians(getHeading())) * getVelocity() * when;
    }

    public double bulletVelocity(double power) {
        return (20.0 - (3.0*power));
    }

    public  Point2D.Double getBulletLocation() {
        Bullet bullet = (Bullet) bulletWave.getFirst();
        return bullet.location;
    }

    public int getBulletDirection() {
        Bullet bullet = (Bullet) bulletWave.getFirst();
        return bullet.direction;
    }

    public double getBulletVelocity() {
        Bullet bullet = (Bullet) bulletWave.getFirst();
        return bullet.velocity;
    }

    public double getBulletDistanceTraveled() {
        Bullet bullet = (Bullet) bulletWave.getFirst();
        return bullet.distanceTraveled;
    }
    public double getBulletDirectAngle() {
        Bullet bullet = (Bullet) bulletWave.getFirst();
        return bullet.directAngle;
    }


    class Bullet implements Serializable{
        Point2D.Double location;
        long time;
        double velocity;
        double directAngle;
        double distanceTraveled;
        int direction;
 
        public Bullet( Point2D.Double location, long time, double velocity, double directAngle, double distanceTraveled, int direction) {

            this.location = location;
            this.time = time;
            this.velocity = velocity;
            this.directAngle = directAngle;
            this.distanceTraveled = distanceTraveled;
            this.direction = direction;
         }

        @Override
        public String toString() {
            return new StringBuffer("").append(this.location.x).append("   ").append(this.location.y)
                    .append("   ").append(this.time).append("   ").append(this.velocity)
                    .append("   ").append(this.directAngle).append("   ").append(this.distanceTraveled)
                    .append("   ").append(this.direction).toString();
        }
    }

    public void updateBullets(AdvancedRobot robot) {
        Point2D.Double location = new Point2D.Double(robot.getX(), robot.getY()) ;

        for (int i = 0; i < bulletWave.size(); i++) {
            Bullet bullet = (Bullet)bulletWave.get(i);
 
            bullet.distanceTraveled = (robot.getTime() - bullet.time) * bullet.velocity;
            if (bullet.distanceTraveled > location.distance(bullet.location) + 50) {
                bulletWave.remove(i);
                i--;
            }
        }
    }
}

