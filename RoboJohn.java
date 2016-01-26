package sample;

import java.awt.Color;

import robocode.AdvancedRobot;
import robocode.HitByBulletEvent;
import robocode.HitWallEvent;
import robocode.ScannedRobotEvent;
import robocode.WinEvent;
import static robocode.util.Utils.normalRelativeAngleDegrees;

/**
 * RoboJohn - a robot by John Tunisi
 */
public class RoboJohn extends AdvancedRobot {
	
	private String lastRotation = "";
	private int moveDirection = 1;
	private Double lastAbsoluteBearing = -1.0;
	
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
			turnGunRight(10);
		}
	}
	
	private void strafe(Double bearing) {
		ahead(100);
		setTurnRight(bearing + 90);
		
//		if (getTime() % 20 == 0) {
			moveDirection *= -1;
			setAhead(150 * moveDirection);
//		}
		execute();
	}

	/**
	 * onScannedRobot:  We have a target.  Go get it.
	 */
	public void onScannedRobot(ScannedRobotEvent e) {
		// Calculate exact location of the robot
		Double absoluteBearing = getHeading() + e.getBearing();
		Double bearingFromGun = normalRelativeAngleDegrees(absoluteBearing - getGunHeading());

		strafe(absoluteBearing);
		lastAbsoluteBearing = absoluteBearing;
		
		// If it's close enough, fire!
		if (Math.abs(bearingFromGun) <= 3) {
			turnGunRight(bearingFromGun);
			// We check gun heat here, because calling fire()
			// uses a turn, which could cause us to lose track
			// of the other robot.
			if (getGunHeat() == 0) {
				fire(Math.min(3 - Math.abs(bearingFromGun), getEnergy() - .1));
			}
		} // otherwise just set the gun to turn.
		// Note:  This will have no effect until we call scan()
		else {
			turnGunRight(bearingFromGun);
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
		if (e.getBearing() < -90) {
			ahead(20);
		} else if (e.getBearing() >= -90 && e.getBearing() <= 0) {
			back(20);
			if (lastRotation.equals("Left")) {
				turnLeft((Math.random() % 90) + 90);
			} else {
				turnRight((Math.random() % 90) + 90);
			}
		} else if (e.getBearing() > 0 && e.getBearing() <= 90) { 
			back(20);
			if (lastRotation.equals("Left")) {
				turnLeft((Math.random() % 90) + 90);
			} else {
				turnRight((Math.random() % 90) + 90);
			}
		} else if (e.getBearing() > 90) {
			ahead(20);
		}
	}	
	
}

