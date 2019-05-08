    package Sayajin;
    import robocode.*;
    import robocode.util.*;
    import java.awt.Color;
    import Sayajin.EnemyBot;
    import java.awt.geom.Point2D;
    import java.awt.geom.*;
    import Sayajin.Vector;
    import java.util.ArrayList;
    import Sayajin.FuzzySystem;
    //
    public class Goku extends AdvancedRobot
    {

        final static double PI = Math.PI;
        public static double fieldWidth;
        public static double fieldHeight;
        public static double nearWall = 18;
        public static double goAngle;
        public static double lastLatVel;


        static final int BINS = 46;
        
        private EnemyBot enemy = new EnemyBot();
        public Point2D.Double location; 
        FuzzySystem fs;    
    
    
        public static Rectangle2D.Double field;
        public static double WALL_STICK = 140;

        // Movement
        private static double guessStats[][][] = new double[4][4][BINS+1];

        public void run() {

            fieldWidth = 800.0;
            fieldHeight = 600.0;

            field = new java.awt.geom.Rectangle2D.Double(nearWall, nearWall, fieldWidth - (2 * nearWall), fieldHeight - (2 * nearWall));
            fs = new FuzzySystem(fieldWidth, fieldHeight);
    
            setRoboColors();
            setAdjustRadarForRobotTurn(true);
            setAdjustGunForRobotTurn(true);
            setAdjustRadarForGunTurn(true);
            
            enemy.reset();
            setAhead(40000);

            while(true) {
                
                turnRadarRightRadians(Double.POSITIVE_INFINITY);
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
                
            double fire = fs.setFire(enemy.getDistance(), enemy.getVelocity());
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
                bulletDirection = enemy.getBulletDirection();

                goAngle = Math.atan2(location.x - bulletLocation.x, location.y - bulletLocation.y) + 1.25 *  sign(checkBullet(-1) - checkBullet(1));
            } catch (Exception ex) { }
            double angle;
            setTurnRightRadians(Math.tan(angle =  wallSmoothing(location, goAngle, bulletDirection) - getHeadingRadians()));
            setAhead(Math.cos(angle) * Double.POSITIVE_INFINITY);
    
    
            shoot();

            lastLatVel = lateralVelocity;

            goAngle = absBearing + PI;
        }

        public double checkBullet( int direction ) {
       
            // int index = getFactorIndex(surfWave,
            //     predictColision(direction));

            Point2D.Double pos = predictColision(direction);

            System.out.println("X e Y: " + pos.x + " - " + pos.y);
     
            // return _surfStats[index];

            return 0.1;
        }

        public Point2D.Double predictColision( int direction) {
            Point2D.Double predictedPosition = (Point2D.Double)location.clone();
            double predictedVelocity = getVelocity();
            double predictedHeading = getHeadingRadians();
            double maxTurning, moveAngle, moveDir;
     
            int tick = 0; // number of ticks in the future
            boolean intercepted = false;

            while(!intercepted && tick < 500) {

                moveAngle = wallSmoothing(predictedPosition, absoluteBearing(enemy.getBulletLocation(), predictedPosition) + (direction * (Math.PI/2)), direction) - predictedHeading;
                moveDir = 1;

                if(Math.cos(moveAngle) < 0) {
                    moveAngle += Math.PI;
                    moveDir = -1;
                }

                moveAngle = Utils.normalRelativeAngle(moveAngle);

                maxTurning = Math.PI/720d*(40d - 3d*Math.abs(predictedVelocity));
                predictedHeading = Utils.normalRelativeAngle(predictedHeading + limit(-maxTurning, moveAngle, maxTurning));

                predictedVelocity += (predictedVelocity * moveDir < 0 ? 2*moveDir : moveDir);
                predictedVelocity = limit(-8, predictedVelocity, 8);
     
                predictedPosition = project(predictedPosition, predictedHeading, predictedVelocity);
     
                tick++;
     
                if (predictedPosition.distance(enemy.getBulletLocation()) < enemy.getBulletDistanceTraveled() + (tick * enemy.getBulletVelocity()) + enemy.getBulletVelocity()) {
                    intercepted = true;
                }

            }
     
            return predictedPosition;
        }

        public void onHitByBullet(HitByBulletEvent e) {

        // reverseDirection();
        }

        

        public static void reverseDirection(AdvancedRobot robot, double goAngle) {
            double angle = Utils.normalRelativeAngle(goAngle - robot.getHeadingRadians());
            if (Math.abs(angle) > (PI/2)) {
                if (angle < 0) {
                    robot.setTurnRightRadians(PI + angle);
                } else {
                    robot.setTurnLeftRadians(PI - angle);
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
            while (!field.contains(project(botLocation, angle, WALL_STICK))) {
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

        public static double limit(double min, double value, double max) {
            return Math.max(min, Math.min(value, max));
        }

    

    }


