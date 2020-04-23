/**
* This is a class to control the mecanum drive
* but will balance the load to make strafing
* as close to linear as possible
* Currently untested: 3/4/2020
*/

package frc.robot;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.drive.MecanumDrive;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;

 public class WeightedMecanumDrive {

    WPI_TalonFX frontLeft;
    WPI_TalonFX rearLeft;
    WPI_TalonFX frontRight;
    WPI_TalonFX rearRight;
    MecanumDrive m_drive;

    NetworkTableEntry frontShuffle;
    NetworkTableEntry rearShuffle;
    
    public WeightedMecanumDrive(WPI_TalonFX frontLeft, WPI_TalonFX rearLeft, WPI_TalonFX frontRight, WPI_TalonFX rearRight) {
        m_drive = new MecanumDrive(frontLeft, rearLeft, frontRight, rearRight);
        this.frontLeft = frontLeft;
        this.rearLeft = rearLeft;
        this.frontRight = frontRight;
        this.rearRight = rearRight;

        frontShuffle = Shuffleboard.getTab("Driving").add("Front Weighted", 0.4).getEntry();
        rearShuffle = Shuffleboard.getTab("Driving").add("Rear Weighted", 0.7).getEntry();
    }

    // this method will drive the robot normally when moving in the Y
    // direction or when rotating, but when strafing
    // a weight will be applied so that the front motors move slower
    // than the back motors. This is to help offset weight inbalance
    public void driveXY(double x, double y, boolean rightButton, boolean leftButton) {

        // receive weights from shuffleboard so drivers can edit between matches quickly
        double frontWeighted = frontShuffle.getDouble(0.7);
        double rearWeighted = rearShuffle.getDouble(1);
        
        // drive normally when strafe buttons are not pressed
        if(!rightButton && !leftButton) {
            m_drive.driveCartesian(0, y, x);
        } 
        // when right button is pressed, strafe right.
        else if(rightButton && !leftButton) {
            frontLeft.set(-1 * frontWeighted);
            rearLeft.set(rearWeighted);
            frontRight.set(-1 * frontWeighted);
            rearRight.set(rearWeighted);
        }
        // when left button is pressed, strafe left.
        else if(!rightButton && leftButton) {
            frontLeft.set(frontWeighted);
            rearLeft.set(-1 * rearWeighted);
            frontRight.set(frontWeighted);
            rearRight.set(-1 * rearWeighted);
        }
    }
 }