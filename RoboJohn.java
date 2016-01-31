package sample;

import static robocode.util.Utils.normalRelativeAngleDegrees;

import java.awt.Color;

import robocode.*;
import robocode.AdvancedRobot;
import robocode.HitByBulletEvent;
import robocode.HitWallEvent;
import robocode.ScannedRobotEvent;
import robocode.WinEvent;

/**
 * RoboJohn - a robot by John Tunisi
 */
public class RoboJohn extends AdvancedRobot {
	
	private int moveDirection = 1;
	private int radarDirection = 1;
	private Double enemyBearing = 0.0;
	
	/**
	 * TrackFire's run method
	 */
	public void run() {
		setBodyColor(Color.pink);
		setGunColor(Color.pink);
		setRadarColor(Color.pink);
		setScanColor(Color.pink);
		setBulletColor(Color.pink);

		while (true) {
			radar();
			strafe();
			execute();
		}
	}
	
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
	
	private void strafe() {
		// always square off against our enemy
		setTurnRight(normalizeBearing(enemyBearing + 90));

		// strafe by changing direction every 20 ticks
		if (getTime() % 20 == 0) {
			moveDirection *= -1;
			setAhead(150 * moveDirection);
		}
	}

	/**
	 * onScannedRobot:  We have a target.  Go get it.
	 */
	public void onScannedRobot(ScannedRobotEvent e) {
		// Calculate exact location of the robot
		Double absoluteBearing = getHeading() + e.getBearing();
		Double bearingFromGun = normalRelativeAngleDegrees(absoluteBearing - getGunHeading());
		enemyBearing = e.getBearing();
		
		
		setTurnRight(e.getBearing() + 90);
		
		// If it's close enough, fire!
		if (Math.abs(bearingFromGun) <= 3) {
			setTurnGunRight(bearingFromGun);
			// We check gun heat here, because calling fire()
			// uses a turn, which could cause us to lose track
			// of the other robot.
			if (getGunHeat() == 0) {
				setFire(Math.min(3 - Math.abs(bearingFromGun), getEnergy() - .1));
			}
		} // otherwise just set the gun to turn.
		// Note:  This will have no effect until we call scan()
		else {
			setTurnGunRight(bearingFromGun);
		}
		// Generates another scan event if we see a robot.
		// We only need to call this if the gun (and therefore radar)
		// are not turning.  Otherwise, scan is called automatically.
		if (bearingFromGun == 0) {
			scan();
		}
	}

	public void onWin(WinEvent e) {
		// Victory dance
		turnRight(36000);
	}
	

	/**
	 * onHitByBullet: What to do when you're hit by a bullet
	 */
	public void onHitByBullet(HitByBulletEvent e) {
		
	}
	
	/**
	 * onHitWall: What to do when you hit a wall
	 */
	public void onHitWall(HitWallEvent e) {
		if (getVelocity() == 0) {
			moveDirection *= -1;
		}
		
		strafe();
//		if (e.getBearing() < -90) {
//			ahead(20);
//		} else if (e.getBearing() >= -90 && e.getBearing() <= 0) {
//			back(20);
//			if (lastRotation.equals("Left")) {
//				turnLeft((Math.random() % 90) + 90);
//			} else {
//				turnRight((Math.random() % 90) + 90);
//			}
//		} else if (e.getBearing() > 0 && e.getBearing() <= 90) { 
//			back(20);
//			if (lastRotation.equals("Left")) {
//				turnLeft((Math.random() % 90) + 90);
//			} else {
//				turnRight((Math.random() % 90) + 90);
//			}
//		} else if (e.getBearing() > 90) {
//			ahead(20);
//		}
	}
	
	double normalizeBearing(double angle) {
		while (angle > 180) angle -= 360;
		while (angle < -180) angle += 360;
		return angle;
	}
	
}

