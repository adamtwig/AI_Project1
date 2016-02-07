package sample;

import static robocode.util.Utils.normalRelativeAngleDegrees;

import java.awt.Color;
import java.awt.geom.Point2D;

import robocode.AdvancedRobot;
import robocode.HitByBulletEvent;
import robocode.HitWallEvent;
import robocode.ScannedRobotEvent;
import robocode.WinEvent;

/*
Developers: John Tunisi
            Adam Terwilliger

Version: February 9, 2016

Purpose: CIS 365 AI Project 1

Details: We looked to develop a Robocode bot
         that is the ultimate opponent to 
         the sample bot, Corners.

Credits: Grand Valley State University
*/

/* extending Advanced Robot provided additional functionality */
public class RoboJohn extends AdvancedRobot {
	
	private int moveDirection = 1;
	private int radarDirection = 1;
	private Double enemyBearing = 0.0;
	
	public void run() {
		setBodyColor(Color.pink);
		setGunColor(Color.pink);
		setRadarColor(Color.pink);
		setScanColor(Color.pink);
		setBulletColor(Color.pink);
		
        // first thing we do is move to the center of field
		moveToPoint(getBattleFieldCenter());
		execute();

        // only move to the center once
		boolean first = true;
		
		while (true) {
            // once we are in the center, call our radar and strafe methods
			if (!first || getLocation().distance(getBattleFieldCenter()) < 10.0) {
				radar();
				strafe();
				execute();	
			} else {
                // if we haven't got the center yet, go there
				moveToPoint(getBattleFieldCenter());
				execute();
				first = false;
			}
		}
	}
	
    // rotate radar until we find opponent
	private void radar() {
		if (enemyBearing == 0.0) {
			setTurnRadarRight(360);
		} else {
			double turn = getHeading() - getRadarHeading() + enemyBearing;
			turn += 30 * radarDirection;
			setTurnRadarRight(normalizeBearing(turn));
			radarDirection *= -1;
		}
	}
	
    // every 20 ticks strafe
	private void strafe() {
        // line up orthogonally to bot
		setTurnRight(normalizeBearing(enemyBearing + 90));

		if (getTime() % 20 == 0) {
			moveDirection *= -1;
			setAhead(500 * moveDirection);
		}
	}

    // code adapted from sample bots
	public void onScannedRobot(ScannedRobotEvent e) {
		// Calculate exact location of the robot
		Double absoluteBearing = getHeading() + e.getBearing();
		Double velocity = e.getVelocity();
		Double bearingFromGun = normalRelativeAngleDegrees(absoluteBearing - getGunHeading());
		enemyBearing = e.getBearing();
		
		
		setTurnRight(e.getBearing() + 90);
		
        // wait to fire until opponent has stopped moving
		if (velocity < .02 && velocity > -.02) {
			
			if (Math.abs(bearingFromGun) <= 3) {
				setTurnGunRight(bearingFromGun);
				
				if (getGunHeat() == 0) {
					setFire(Math.min(3 - Math.abs(bearingFromGun), getEnergy() - .1));
				}
			} else {
				setTurnGunRight(bearingFromGun);
			}
			if (bearingFromGun == 0) {
				scan();
			}
		}
	}

	public void onWin(WinEvent e) {
		turnRight(36000);
	}

    // don't continue to get hit
	public void onHitByBullet(HitByBulletEvent e) {
		changeDirection();
	}

    // don't continue to run into wall
	public void onHitWall(HitWallEvent e) {
		changeDirection();
		strafe();
	}
	
	private Point2D getLocation() {
		return new Point2D.Double(getX(), getY());
	}
	
	private Point2D getBattleFieldCenter() {
		return new Point2D.Double((getBattleFieldWidth() / 2), (getBattleFieldHeight() / 2));
	}
	
	private double absoluteBearingDegrees(Point2D source, Point2D target) {
        return Math.toDegrees(Math.atan2(target.getX() - source.getX(), target.getY() - source.getY()));
    }
    
    // helper method for moving to center
    private void moveToPoint(Point2D point) {
    	Double distanceFromPoint = getLocation().distance(point);
        Double angle = normalizeBearing(absoluteBearingDegrees(getLocation(), point) - getHeading());
        
        if (Math.abs(angle) > 90.0) {
        	distanceFromPoint *= -1;
        	
        	if (angle > 0.0) {
        		angle -= 180.0;
        	} else {
        		angle += 180.0;
        	}
        }
        setTurnRight(angle);
        setAhead(distanceFromPoint);
    }
	
	private void changeDirection() {
		if (getVelocity() == 0) {
			moveDirection *= -1;
		}
	}
	
	private double normalizeBearing(double angle) {
		while (angle > 180) angle -= 360;
		while (angle < -180) angle += 360;
		return angle;
	}
}
