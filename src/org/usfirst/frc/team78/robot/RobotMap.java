package org.usfirst.frc.team78.robot;
/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class RobotMap {


	
	//MOTOR COLORS
	final static int DOUBLE_YELLOW = 0;
	final static int BLUE = 1;
	final static int ORANGE = 2;
	final static int GREY = 3;
	final static int DOUBLE_GREEN = 4;

	
	//MOTORS(PWM)
	final static public int LEFT_DRIVE_1 = BLUE;
	final static public int RIGHT_DRIVE_1 = ORANGE;
	final static public int H_DRIVE = DOUBLE_GREEN;
	final static public int ELEVATOR = DOUBLE_YELLOW;
	final static public int CLAW = GREY;
	
	//ANALOG IN
	final static public int GYRO = 0;
	final static public int CLAW_POT = 1;
	
	//DIGITAL IO
	final static public int LEFT_ENC_A = 0;
	final static public int LEFT_ENC_B = 1;
	final static public int RIGHT_ENC_A = 2;
	final static public int RIGHT_ENC_B = 3;
	final static public int LIFT_ENC_A = 4;
	final static public int LIFT_ENC_B = 5;
	final static public int ZERO_LIMIT = 6;
	
	//RELAYS
	public final static int INDICATOR_LIGHT = 0;
	
	
	//MANIPULATOR BUTTONS
	final static public int floorBtn = 16;
	final static public int scoringPlatformBtn = 8;
	final static public int stepBtn = 11;
	final static public int onToteBtn = 2;
	final static public int aboveToteBtn = 10;
	final static public int slowLiftBtn = 17;
	final static public int openClawBtn = 9;
	final static public int closeClawBtn = 5;
	final static public int upLiftStick = 14;
	final static public int downLiftStick = 15;
	final static public int overrideSwitch = 3;
	
	//MANIPULATOR  BACKUP BUTTONS
	final static public int overrideBackupBtn = 5;
	final static public int openClawBackupBtn = 9;
	final static public int closeClawBackupBtn = 10;
	
	
}
