package org.usfirst.frc.team78.robot.subsystems;
import org.usfirst.frc.team78.robot.Robot;
import org.usfirst.frc.team78.robot.RobotMap;
import org.usfirst.frc.team78.robot.commands.DriveWithJoysticks;

import edu.wpi.first.wpilibj.BuiltInAccelerometer;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.command.Subsystem;


/**
 *
 */
public class Chassis extends Subsystem {
    
	//MOTORS
	Victor leftDrive1 = new Victor(RobotMap.LEFT_DRIVE_1);
	Victor rightDrive1 = new Victor(RobotMap.RIGHT_DRIVE_1);
	Victor hDrive = new Victor(RobotMap.H_DRIVE);
	
	//SENSORS
	
	Gyro gyro = new Gyro(RobotMap.GYRO);
	Encoder rightEnc = new Encoder(RobotMap.RIGHT_ENC_A, RobotMap.RIGHT_ENC_B);
	Encoder leftEnc = new Encoder(RobotMap.LEFT_ENC_A, RobotMap.LEFT_ENC_B);
	BuiltInAccelerometer accelerometer = new BuiltInAccelerometer();
	
	
	//VARIABLES
	
		//DISTANCE CALCULATION
	double distanceError;
	final double distanceP = 0.0003;
	final double DISTANCE_ERROR_THRESHOLD = 175; //in clicks
	public int errorNeutralizedCount = 0;
	final double STRAIGHT_ERROR_CONST = (0.006);
	final double STRAIGHT_STRAFE_ERROR_CONST = (.032);
	public double targetHeading = 0;
	double strafeRampCount= 0.25;
	double strafeRampAddition = .017;
	
	
	double turnP = (.0045);
	final double TURN_ERROR_THRESHOLD = 2.5;
	

    public void initDefaultCommand() {
    	setDefaultCommand(new DriveWithJoysticks());
    }
    
 //_____________________________________________________________________________________________
 //drive methods
    public void driveWithJoysticks(){
        double left = Robot.oi.getDriverLeftStick();
       	double right = Robot.oi.getDriverRightStick();
        	
       	if(Robot.oi.driverStick.getRawButton(7) && Robot.oi.driverStick.getRawButton(8)){
       		setSpeed(left, right);
       	}
       	else{
       	setSpeed(left*.45, right*.45);
       	}
       	
       	if(strafeRampCount > 0.7){
       		strafeRampCount = 0.7 - strafeRampAddition;
       	}
       	
       	if(Robot.oi.driverStick.getRawButton(5) && (Robot.oi.getDriverRightStick() == 0 && Robot.oi.getDriverLeftStick() == 0)){//left strafe
       		strafeRampCount += strafeRampAddition;
       		setStrafeSpeed(-strafeRampCount);
       		straightStrafeCorrection(targetHeading);
       	}
       	else if(Robot.oi.driverStick.getRawButton(5)){
       		strafeRampCount += strafeRampAddition;
       		setStrafeSpeed(-strafeRampCount);
       		targetHeading = getGyro();
       	}
       	else if(Robot.oi.driverStick.getRawButton(6) && (Robot.oi.getDriverRightStick() == 0 && Robot.oi.getDriverLeftStick() == 0)){
       		strafeRampCount += strafeRampAddition;
       		setStrafeSpeed(strafeRampCount);
       		straightStrafeCorrection(targetHeading);
       	}
       	else if(Robot.oi.driverStick.getRawButton(6)){
       		strafeRampCount += strafeRampAddition;
       		setStrafeSpeed(strafeRampCount);
       		targetHeading = getGyro();
       	}
       	else{
       		setStrafeSpeed(0);
       		strafeRampCount = .25;
       	}

       	
    }//end drive with joysticks
    

    
    public void setSpeed(double left, double right){

    		leftDrive1.set(-left);
    		rightDrive1.set(right);
    }//end set speed
    
    public void setStrafeSpeed(double speed){
    	hDrive.set(speed);
    }
   

    public void straightStrafeCorrection(double heading){
    	double driftError = heading - getGyro();
    	setSpeed(-((STRAIGHT_STRAFE_ERROR_CONST)*driftError), ((STRAIGHT_STRAFE_ERROR_CONST)*driftError));
    }
    
    public void stopAllDrive(){
    	setSpeed(0,0);
    	setStrafeSpeed(0);
    }
 
//_____________________________________________________________________________________________________________
//AUTO METHODS
    public void driveStraightDistance(double distance){
    	distanceError = -(distance - ((getLeftEnc()+getRightEnc())/2));//TODO why negated?
    	double speed = distanceP*(distanceError);

    	if (speed < .25 && speed > 0){
    		speed = .25;
    	}
    	if(speed > .5)
    		speed = .5;
    	if(speed <-.5)
    		speed = -.5;
    	
    	if(Math.abs(distanceError) < DISTANCE_ERROR_THRESHOLD){
    		errorNeutralizedCount ++;
    	}
    	else{
    		errorNeutralizedCount = 0;
    	}
    	
    	double driftError = getGyro();
    	setSpeed(speed+((STRAIGHT_ERROR_CONST)*driftError), speed-((STRAIGHT_ERROR_CONST)*driftError));
    }//end drive straight distance
    
 public void controlTurnSpeed(double target){
    	double speed;
    	double error = target- getGyro();
    	speed = (-1)*turnP*(error);
    	//IComponent += (-1)*I*(error);
    	//speed += IComponent;
    	//DComponent = (-1)*(D*((error-lastError)));
    	//speed += DComponent;
    	
    	if (speed > .7){
    		speed = .7;
    	}
    	if(speed < -.7){ 
    		speed = -.7;
    	}
    	
    	if (speed < .30 && speed > 0){
    		speed = .30;
    	}
    	if(speed > -.30 && speed < 0){ 
    		speed = -.30;
    	}
    	
    	setTurnSpeed(-speed);
    	//lastError = error;
    	
    	if(Math.abs(error) < TURN_ERROR_THRESHOLD){
    		errorNeutralizedCount ++;
    	}
    	else{
    		errorNeutralizedCount = 0;
    	}
    	
    }//end control turn speed
    
    public void setTurnSpeed(double speed){
    	setSpeed(-speed, speed);
    }
    
//_____________________________________________________________________________________________________________
//SENSOR METHODS
    
    public double getGyro(){
    	return gyro.getAngle();
    }
    
    public double getLeftEnc(){
    	return -leftEnc.getRaw();
    }
    
    public double getRightEnc(){
    	return rightEnc.getRaw();
    }
    
    public double getAccelX(){
    	return accelerometer.getX();
    }
    
    public double getAccelY(){
    	return accelerometer.getY();
    }
    
    public double getAccelZ(){
    	return accelerometer.getZ();
    }
    
    public void resetSensorData(){
    	gyro.reset();
    	leftEnc.reset();
    	rightEnc.reset();
    	errorNeutralizedCount = 0;
    }//end reset sensor data
    
} // end Chassis class

