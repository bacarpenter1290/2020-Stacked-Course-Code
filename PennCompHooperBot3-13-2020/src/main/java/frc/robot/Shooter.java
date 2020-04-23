/**
 * This is the class that controls the shooter
 */

 package frc.robot;

// import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import com.revrobotics.CANSparkMax;


public class Shooter {
     CANSparkMax shooter;

     public Shooter(CANSparkMax shooter, Elevator elevator) {
         this.shooter = shooter;
     }

     public void shoot(double shooterSpeed, double conveyerSpeed, 
                        double elevatorSpeed, double elevatorHelperSpeed, Elevator elevator) {
        //if(elevator.getBallCount() > 0) {
           // shooter.set(shooterSpeed);

            //Timer.waitMilli(2000);

            // elevator.shoot(elevatorSpeed, elevatorHelperSpeed, conveyerSpeed); 
            
            // elevator.runElevatorOneBall(elevatorSpeed, elevatorHelperSpeed, conveyerSpeed);

            //shooter.set(0);
        //}
     }
 }