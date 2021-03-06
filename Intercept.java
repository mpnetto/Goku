package Sayajin;
import robocode.*;
import robocode.util.*;

public class Intercept { 
    public Coordinate impactPoint = new Coordinate(0,0); 
    public double bulletHeading_deg; 

    protected Coordinate bulletStartingPoint ; 
    protected Coordinate targetStartingPoint = new Coordinate(); 
    public double targetHeading; 
    public double targetVelocity; 
    public double bulletPower; 
    public double angleThreshold; 
    public double distance; 

    protected double impactTime; 
    protected double angularVelocity_rad_per_sec; 

    public void calculate (double xb, double yb, double xt,  double yt, double tHeading, double vt, double bPower, double angularVelocity_deg_per_sec){
        angularVelocity_rad_per_sec = Math.toRadians(angularVelocity_deg_per_sec);

        bulletStartingPoint = new Coordinate(xb, yb);
        targetStartingPoint = new Coordinate(xt, yt);

        targetHeading = tHeading;
        targetVelocity = vt;bulletPower = bPower;
        double vb = 20-3*bulletPower;

        double dX,dY;// Start with initial guesses at 10 and 20 ticks
        impactTime = getImpactTime(10, 20, 0.01);
        impactPoint = getEstimatedPosition(impactTime);
        dX = (impactPoint.getX() - bulletStartingPoint.getX());
        dY = (impactPoint.getY() - bulletStartingPoint.getY());
        distance = Math.sqrt(dX*dX+dY*dY);
        bulletHeading_deg = Math.toDegrees(Math.atan2(dX,dY));
        angleThreshold = Math.toDegrees (Math.atan(10/distance));
    }
                
    protected Coordinate getEstimatedPosition(double time) {
        double x = targetStartingPoint.getX() +   targetVelocity * time * Math.sin(Math.toRadians(targetHeading));
        double y = targetStartingPoint.getY() +   targetVelocity * time * Math.cos(Math.toRadians(targetHeading));
        return new Coordinate(x,y);
    }
    
    private double f(double time) {
        double vb = 20-3*bulletPower;
        Coordinate targetPosition = getEstimatedPosition(time);
        double dX = (targetPosition.getX() - bulletStartingPoint.getX());
        double dY = (targetPosition.getY() - bulletStartingPoint.getY());
        return Math.sqrt(dX*dX + dY*dY) - vb * time;
    }
    
    private double getImpactTime(double t0,  double t1, double accuracy) {
        double X = t1;
        double lastX = t0;
        int iterationCount = 0;
        double lastfX = f(lastX);
        while ((Math.abs(X - lastX) >= accuracy) &&  (iterationCount < 15)) {
            iterationCount++;
            double fX = f(X);
            if ((fX-lastfX) == 0.0) break;
            double nextX = X - fX*(X-lastX)/(fX-lastfX);
            lastX = X;X = nextX;lastfX = fX;
        }
        return X;
    }
}

