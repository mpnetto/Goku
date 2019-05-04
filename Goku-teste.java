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
    public  double fieldWidth ;
    public  double fieldHeight;
    public  double nearWall;
	
	private EnemyBot enemy = new EnemyBot();

	public static int BINS = 47;
	public static double _surfStats[] = new double[BINS];
	public Point2D.Double location;     // our bot's location
	public Point2D.Double _enemyLocation;  // enemy bot's location

	public ArrayList _enemyWaves;
	public ArrayList _surfDirections;
	public ArrayList _surfAbsBearings;

	public static double _oppEnergy = 100.0;
	public  Rectangle2D.Double _fieldRect ;
    public static double WALL_STICK = 160;

	public void run() {

        fieldWidth = getWidth();
        fieldHeight = getHeight();
        nearWall = 18;

        _fieldRect = new java.awt.geom.Rectangle2D.Double(nearWall, nearWall, fieldWidth - (nearWall * 2), fieldHeight - (nearWall * 2));

		_enemyWaves = new ArrayList();
        _surfDirections = new ArrayList();
        _surfAbsBearings = new ArrayList();
 
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
			//checkWall();
			
            turnRadarRightRadians(Double.POSITIVE_INFINITY);
            // scan();
			// shoot();
			execute();
			// ++turn;
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
	
	public 	void scan() {
		if (enemy.none()) {
			setTurnRadarRight(36000);
		} else {
			double t = getHeading() - getRadarHeading() + enemy.getBearing();
			//t += 30 * radarDirection;
			setTurnRadarRight(t);
			radarDirection *= -1;
		}
	}

	/**
	 * onScannedRobot: What to do when you see another robot
	 */
	public void onScannedRobot(ScannedRobotEvent e) {
		// Replace the next line with any behavior you would like
        enemy.update(e, this);
        
        double absBearing = e.getBearingRadians() + getHeadingRadians();

		double absoluteBearing = getHeading() + enemy.getBearing();
		double bearingFromGun = Utils.normalRelativeAngleDegrees(absoluteBearing - getGunHeading());
        double bearingFromRadar = Utils.normalRelativeAngleDegrees(absoluteBearing - getRadarHeading());
        double lateralVelocity = getVelocity()*Math.sin(Math.toRadians(enemy.getBearing()));
		
		if (this.movingForward) {
			setTurnRight(Utils.normalRelativeAngleDegrees(e.getBearing() + 80));
			
		} else {
			setTurnRight(Utils.normalRelativeAngleDegrees(e.getBearing() + 100));
		}

		if (bearingFromGun == 0) {
			scan();
		}

		location = new Point2D.Double(getX(), getY());
       
        //setTurnRadarRightRadians(Utils.normalRelativeAngle(absBearing - getRadarHeadingRadians()) * 2);
        setTurnRadarRight((getHeading() - getRadarHeading() + enemy.getBearing()) * 2);
   
 
        _surfDirections.add(0, new Integer((lateralVelocity >= 0) ? 1 : -1));
        _surfAbsBearings.add(0, new Double(absBearing + Math.PI));
 
 
        double bulletPower = _oppEnergy - e.getEnergy();
        if (bulletPower < 3.01 && bulletPower > 0.09
            && _surfDirections.size() > 2) {
            EnemyWave ew = new EnemyWave();
            ew.fireTime = getTime() - 1;
            ew.bulletVelocity = bulletVelocity(bulletPower);
            ew.distanceTraveled = bulletVelocity(bulletPower);
            ew.direction = ((Integer)_surfDirections.get(2)).intValue();
            ew.directAngle = ((Double)_surfAbsBearings.get(2)).doubleValue();
            ew.fireLocation = (Point2D.Double)_enemyLocation.clone(); // last tick
 
            _enemyWaves.add(ew);
        }
 
        _oppEnergy = e.getEnergy();
 
        // update after EnemyWave detection, because that needs the previous
        // enemy location as the source of the wave
        _enemyLocation = project(location, absBearing, e.getDistance());
 
        updateWaves();
        doSurfing();

        shoot();

	}

	public void updateWaves() {
        for (int x = 0; x < _enemyWaves.size(); x++) {
            EnemyWave ew = (EnemyWave)_enemyWaves.get(x);
 
            ew.distanceTraveled = (getTime() - ew.fireTime) * ew.bulletVelocity;
            if (ew.distanceTraveled >
                location.distance(ew.fireLocation) + 50) {
                _enemyWaves.remove(x);
                x--;
            }
        }
    }

    public EnemyWave getClosestSurfableWave() {
        double closestDistance = Double.POSITIVE_INFINITY; // I juse use some very big number here
        EnemyWave surfWave = null;
 
        for (int x = 0; x < _enemyWaves.size(); x++) {
            EnemyWave ew = (EnemyWave)_enemyWaves.get(x);
            double distance = location.distance(ew.fireLocation)
                - ew.distanceTraveled;
 
            if (distance > ew.bulletVelocity && distance < closestDistance) {
                surfWave = ew;
                closestDistance = distance;
            }
        }
 
        return surfWave;
    }

		public static int getFactorIndex(EnemyWave ew, Point2D.Double targetLocation) {
        double offsetAngle = (absoluteBearing(ew.fireLocation, targetLocation)
            - ew.directAngle);
        double factor = Utils.normalRelativeAngle(offsetAngle)
            / maxEscapeAngle(ew.bulletVelocity) * ew.direction;
 
        return (int)limit(0,
            (factor * ((BINS - 1) / 2)) + ((BINS - 1) / 2),
            BINS - 1);
    }

		public void logHit(EnemyWave ew, Point2D.Double targetLocation) {
        int index = getFactorIndex(ew, targetLocation);
 
        for (int x = 0; x < BINS; x++) {
            // for the spot bin that we were hit on, add 1;
            // for the bins next to it, add 1 / 2;
            // the next one, add 1 / 5; and so on...
            _surfStats[x] += 1.0 / (Math.pow(index - x, 2) + 1);
        }
    }

	/**
	 * onHitByBullet: What to do when you're hit by a bullet
	 */
	public void onHitByBullet(HitByBulletEvent e) {
		// Replace the next line with any behavior you would like
		if (!_enemyWaves.isEmpty()) {
            Point2D.Double hitBulletLocation = new Point2D.Double(
                e.getBullet().getX(), e.getBullet().getY());
            EnemyWave hitWave = null;
 
            // look through the EnemyWaves, and find one that could've hit us.
            for (int x = 0; x < _enemyWaves.size(); x++) {
                EnemyWave ew = (EnemyWave)_enemyWaves.get(x);
 
                if (Math.abs(ew.distanceTraveled -
                    location.distance(ew.fireLocation)) < 50
                    && Math.abs(bulletVelocity(e.getBullet().getPower()) 
                        - ew.bulletVelocity) < 0.001) {
                    hitWave = ew;
                    break;
                }
            }
 
            if (hitWave != null) {
                logHit(hitWave, hitBulletLocation);
 
                // We can remove this wave now, of course.
                _enemyWaves.remove(_enemyWaves.lastIndexOf(hitWave));
            }
        }
	}

	public Point2D.Double predictPosition(EnemyWave surfWave, int direction) {
        Point2D.Double predictedPosition = (Point2D.Double)location.clone();
        double predictedVelocity = getVelocity();
        double predictedHeading = getHeadingRadians();
        double maxTurning, moveAngle, moveDir;
 
        int counter = 0; // number of ticks in the future
        boolean intercepted = false;
 
        do {    // the rest of these code comments are rozu's
            moveAngle =
                wallSmoothing(predictedPosition, absoluteBearing(surfWave.fireLocation,
                predictedPosition) + (direction * (Math.PI/2)), direction)
                - predictedHeading;
            moveDir = 1;
 
            if(Math.cos(moveAngle) < 0) {
                moveAngle += Math.PI;
                moveDir = -1;
            }
 
            moveAngle = Utils.normalRelativeAngle(moveAngle);
 
            // maxTurning is built in like this, you can't turn more then this in one tick
            maxTurning = Math.PI/720d*(40d - 3d*Math.abs(predictedVelocity));
            predictedHeading = Utils.normalRelativeAngle(predictedHeading
                + limit(-maxTurning, moveAngle, maxTurning));
 
            // this one is nice ;). if predictedVelocity and moveDir have
            // different signs you want to breack down
            // otherwise you want to accelerate (look at the factor "2")
            predictedVelocity +=
                (predictedVelocity * moveDir < 0 ? 2*moveDir : moveDir);
            predictedVelocity = limit(-8, predictedVelocity, 8);
 
            // calculate the new predicted position
            predictedPosition = project(predictedPosition, predictedHeading,
                predictedVelocity);
 
            counter++;
 
            if (predictedPosition.distance(surfWave.fireLocation) <
                surfWave.distanceTraveled + (counter * surfWave.bulletVelocity)
                + surfWave.bulletVelocity) {
                intercepted = true;
            }
        } while(!intercepted && counter < 500);
 
        return predictedPosition;
    }

		public double checkDanger(EnemyWave surfWave, int direction) {
        int index = getFactorIndex(surfWave,
            predictPosition(surfWave, direction));
 
        return _surfStats[index];
    }
 
    public void doSurfing() {
        EnemyWave surfWave = getClosestSurfableWave();
 
        if (surfWave == null) { return; }
 
        double dangerLeft = checkDanger(surfWave, -1);
        double dangerRight = checkDanger(surfWave, 1);
 
        double goAngle = absoluteBearing(surfWave.fireLocation, location);
        if (dangerLeft < dangerRight) {
            goAngle = wallSmoothing(location, goAngle - (Math.PI/2), -1);
        } else {
            goAngle = wallSmoothing(location, goAngle + (Math.PI/2), 1);
        }
 
        setBackAsFront(this, goAngle);
    }
	
	/**
	 * onHitWall: What to do when you hit a wall
	 */


	public void reverseDirection() {
		if (this.movingForward) {
			setBack(40000);
			this.movingForward = false;
		} else {
			setAhead(40000);
			this.movingForward = true;
		}
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
	
		//reverseDirection();
	}

	public void onWin(WinEvent e) {
		// Victory dance
		turnRight(36000);
	}

	private void setRoboColors() {
	
		setBodyColor(Color.ORANGE);
		setGunColor(Color.BLUE);
		setRadarColor(Color.BLACK);
		setBulletColor(Color.GREEN);
		setScanColor(Color.WHITE);
	}

	class EnemyWave {
        Point2D.Double fireLocation;
        long fireTime;
        double bulletVelocity, directAngle, distanceTraveled;
        int direction;
 
        public EnemyWave() { }
    }
 
    public double wallSmoothing(Point2D.Double botLocation, double angle, int orientation) {
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
 
    public static double absoluteBearing(Point2D.Double source, Point2D.Double target) {
        return Math.atan2(target.x - source.x, target.y - source.y);
    }
 
    public static double limit(double min, double value, double max) {
        return Math.max(min, Math.min(value, max));
    }
 
    public static double bulletVelocity(double power) {
        return (20.0 - (3.0*power));
    }
 
    public static double maxEscapeAngle(double velocity) {
        return Math.asin(8.0/velocity);
    }
 
    public static void setBackAsFront(AdvancedRobot robot, double goAngle) {
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
	
}


