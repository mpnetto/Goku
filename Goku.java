package Sayajin;
import robocode.*;
import robocode.util.*;
import java.awt.Color;
import Sayajin.EnemyBot;
import java.awt.geom.Point2D;
import java.awt.geom.*;
import Sayajin.Vector;
import java.util.ArrayList;
//
public class Goku extends AdvancedRobot
{
	private boolean movingForward = true;

	private byte radarDirection = 1;
    final double PI = Math.PI;
    public static double fieldWidth;
    public static double fieldHeight;
    public static double nearWall = 18;
    public static double goAngle;

	
	private EnemyBot enemy = new EnemyBot();

    public static int BINS = 47;
    public static double _surfStats[] = new double[BINS];
    public Point2D.Double location;     // our bot's location
    public Point2D.Double _enemyLocation;  // enemy bot's location
 
    public static Rectangle2D.Double _fieldRect;
    public static double WALL_STICK = 160;

	public void run() {

        fieldWidth = 800.0;
        fieldHeight = 600.0;

        _fieldRect= new java.awt.geom.Rectangle2D.Double(nearWall, nearWall, fieldWidth - (2 * nearWall), fieldHeight - (2 * nearWall));
 
        setAdjustGunForRobotTurn(true);
        setAdjustRadarForGunTurn(true);
	
		int trigger = 10;
		
		setRoboColors();
		setAdjustRadarForRobotTurn(true);
		setAdjustGunForRobotTurn(true);
		setAdjustRadarForGunTurn(true);
		
		enemy.reset();
		setAhead(40000);

		while(true) {
			
            turnRadarRightRadians(Double.POSITIVE_INFINITY);

            execute();
		}
	}

	void shoot() {

		if (enemy.none())
			return;

		double firePower = Math.min(500 / enemy.getDistance(), 3);
		double bulletSpeed = 20 - firePower * 3;
		long time = (long)(enemy.getDistance() / bulletSpeed);

		double nextX = enemy.getFutureX(time);
		double nextY = enemy.getFutureY(time);
		double absDeg = absoluteBearing(getX(), getY(), nextX, nextY);
		
		setTurnGunRight(normalizeBearing(absDeg - getGunHeading()));

		if(getGunHeat() == 0)
			setFire(firePower);
	}

	public void onScannedRobot(ScannedRobotEvent e) {

        enemy.update(e, this);
        
        location = new Point2D.Double(getX(), getY());
 
        double lateralVelocity = getVelocity()*Math.sin(e.getBearingRadians());
        double absBearing = e.getBearingRadians() + getHeadingRadians();
 
        setTurnRadarRightRadians(Utils.normalRelativeAngle(absBearing - getRadarHeadingRadians()) * 2);

        int bulletDirection = 1;
        try {

            Point2D.Double bulletLocation =  enemy.getBulletLocation();
            bulletDirection = enemy.getBulletDirectionn();

            goAngle = Math.atan2(location.x - bulletLocation.x, location.y - bulletLocation.y) + 1.25 *  sign(checkBullet(-1) - checkBullet(1));
        } catch (Exception ex) { }
        System.out.println(goAngle);
        double angle;
        setTurnRightRadians(Math.tan(angle =  wallSmoothing(location, goAngle, bulletDirection) - getHeadingRadians()));
        setAhead(Math.cos(angle) * Double.POSITIVE_INFINITY);
 
 
        shoot();

        goAngle = absBearing + Math.PI;
    }

	public void onHitByBullet(HitByBulletEvent e) {

       // reverseDirection();
    }

    public double checkBullet( int direction) {
       
        //implementar logica fuzzy aqui;
        return 0.1;
    }

    public static void reverseDirection(AdvancedRobot robot, double goAngle) {
        double angle = Utils.normalRelativeAngle(goAngle - robot.getHeadingRadians());
        if (Math.abs(angle) > (Math.PI/2)) {
            if (angle < 0) {
                robot.setTurnRightRadians(Math.PI + angle);
            } else {
                robot.setTurnLeftRadians(Math.PI - angle);
            }
            robot.setBack(100);
        } else {
            if (angle < 0) {
                robot.setTurnLeftRadians(-1*angle);
           } else {
                robot.setTurnRightRadians(angle);
           }
            robot.setAhead(100);
        }
    }

    private static double wallSmoothing(Point2D.Double botLocation, double angle, int orientation) {
        while (!_fieldRect.contains(project(botLocation, angle, WALL_STICK))) {
            angle += orientation*0.05;
        }
        return angle;
    }

    public static Point2D.Double project(Point2D.Double sourceLocation,
        double angle, double length) {
        return new Point2D.Double(sourceLocation.x + Math.sin(angle) * length,
            sourceLocation.y + Math.cos(angle) * length);
    }
	
	double absoluteBearing(double x1, double y1, double x2, double y2) {
		double xo = x2-x1;
		double yo = y2-y1;
		double hyp = Point2D.distance(x1, y1, x2, y2);
		double arcSin = Math.toDegrees(Math.asin(xo / hyp));
		double bearing = 0;

		if (xo > 0 && yo > 0) {
			bearing = arcSin;
		} else if (xo < 0 && yo > 0) { 
			bearing = 360 + arcSin; 
		} else if (xo > 0 && yo < 0) { 
			bearing = 180 - arcSin;
		} else if (xo < 0 && yo < 0) {
			bearing = 180 - arcSin; 
		}

		return bearing;
	}
	
	double normalizeBearing(double angle) {
		while (angle >  180) angle -= 360;
		while (angle < -180) angle += 360;
		return angle;
	}

	public void onHitWall(HitWallEvent e) {

	}

	public void onWin(WinEvent e) {
		// Victory dance
        turnRight(36000);
        setRadarColor(Color.YELLOW);
	}

	private void setRoboColors() {
	
		setBodyColor(Color.ORANGE);
		setGunColor(Color.BLUE);
		setRadarColor(Color.BLACK);
		setBulletColor(Color.GREEN);
		setScanColor(Color.WHITE);
	}
 
    public static double absoluteBearing(Point2D.Double source, Point2D.Double target) {
        return Math.atan2(target.x - source.x, target.y - source.y);
    }

    private static int sign(double d) {
        if (d < 0) { return -1; }
        return 1;
    }
 
 
    public static double bulletVelocity(double power) {
        return (20.0 - (3.0*power));
    }	

   

}

