/**
 * This program is for FRC team 2197 at the 2020 St. Joe. Competition
 * 
 * All commented out code was subsystems that were removed during 
 * the development process, but have remained in the code
 * in case the team decided to re-implement them.
 */

package frc.robot;

import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.DigitalInput;
// import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.Joystick;
// import edu.wpi.first.wpilibj.drive.MecanumDrive;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;

import com.revrobotics.CANSparkMax;
// import com.revrobotics.ColorMatch;
// import com.revrobotics.ColorSensorV3;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;


public class Robot extends TimedRobot {

  private static final int kFrontLeftChannel = 11;
  private static final int kRearLeftChannel = 9;
  private static final int kFrontRightChannel = 10;
  private static final int kRearRightChannel = 8;

  private static final int kIntakeChannel = 3;

  private static final int kConveyerChannel = 2;
  private static final int kElevatorChannel = 6;
  private static final int kElevatorHelperChannel = 4;

  private static final int kClimberChannel = 7;
  private static final int kTransverseChannel = 5;

  private static final int kShooterChannel = 1;
  // private static final int kShooterChannel = 12;

  // private static final int kWheelOfFortuneChannel = 5;
  // private static final int kWheelOfFortune_Up_Down_Channel = 6;


  private static final int kDriverJoystickChannel = 0;
  private static final int kOperatorJoystickChannel = 1;

  WPI_TalonFX frontLeft;
  WPI_TalonFX rearLeft;
  WPI_TalonFX frontRight;
  WPI_TalonFX rearRight;

  CANSparkMax intakeMotor;

  CANSparkMax conveyerMotor;
  CANSparkMax elevatorMotor;
  CANSparkMax elevatorHelperMotor;

  CANSparkMax climberMotor;
  CANSparkMax transverseMotor;

  CANSparkMax shooterMotor;
  // WPI_TalonFX shooterMotor;

  // CANSparkMax wheelOfFortuneMotor;
  // CANSparkMax wheelOfFortuneUpDownMotor;

  NetworkTableEntry mode;
  NetworkTableEntry shooterShuffle;
  // NetworkTableEntry WoFReadyShuffle;
  NetworkTableEntry conveyerShuffle;
  NetworkTableEntry driveShuffle;
  NetworkTableEntry elevatorShuffle;
  NetworkTableEntry transverseShuffle;
  NetworkTableEntry intakeShuffle;
  NetworkTableEntry climberShuffle;
  NetworkTableEntry elevatorHelperShuffle;

  // private MecanumDrive m_robotDrive;
  private Joystick m_driverStick;
  private Joystick m_operatorStick;

  DigitalInput intakeLimit;
  DigitalInput shooterLimit;

  Elevator elevator;
  Shooter shooter;
  WeightedMecanumDrive weightedDrive;

  CameraServer camera;

  // WheelOfFortune WoF;

  //define i2c port for color sensor

  // private final I2C.Port i2cPort = I2C.Port.kOnboard;

  /**
   * A Rev Color Sensor V3 object is constructed with an I2C port as a 
   * parameter. The device will be automatically initialized with default 
   * parameters.
   */
  // private final ColorSensorV3 m_colorSensor = new ColorSensorV3(i2cPort);

  /**
   * A Rev Color Match object is used to register and detect known colors. This can 
   * be calibrated ahead of time or during operation.
   * 
   * This object uses a simple euclidian distance to estimate the closest match
   * with given confidence range.
   */
  // private final ColorMatch m_colorMatcher = new ColorMatch();


  private static final int kPDPId = 15;

  private final PowerDistributionPanel m_pdp = new PowerDistributionPanel(kPDPId);

  // method to initialize the robot
  @Override
  public void robotInit() {

    // initalize 11 robot motors
    frontLeft = new WPI_TalonFX(kFrontLeftChannel);
    rearLeft = new WPI_TalonFX(kRearLeftChannel);
    frontRight = new WPI_TalonFX(kFrontRightChannel);
    rearRight = new WPI_TalonFX(kRearRightChannel);

    intakeMotor = new CANSparkMax(kIntakeChannel, MotorType.kBrushed);

    conveyerMotor = new CANSparkMax(kConveyerChannel, MotorType.kBrushed);
    elevatorMotor = new CANSparkMax(kElevatorChannel, MotorType.kBrushed);
    elevatorHelperMotor = new CANSparkMax(kElevatorHelperChannel, MotorType.kBrushed);

    climberMotor = new CANSparkMax(kClimberChannel, MotorType.kBrushless);
    transverseMotor = new CANSparkMax(kTransverseChannel, MotorType.kBrushed);

    // shooterMotor = new WPI_TalonFX(kShooterChannel);

    shooterMotor = new CANSparkMax(kShooterChannel, MotorType.kBrushed);

    // wheelOfFortuneMotor = new CANSparkMax(kWheelOfFortuneChannel, MotorType.kBrushless);
    // wheelOfFortuneUpDownMotor = new CANSparkMax(kWheelOfFortune_Up_Down_Channel, MotorType.kBrushless);

    // init shuffleboard tabs
    // WoFReadyShuffle = Shuffleboard.getTab("Driving").add("WoF Ready?", false).getEntry();
    mode = Shuffleboard.getTab("Driving").add("Manual Mode?", true).getEntry();
    shooterShuffle = Shuffleboard.getTab("Driving").add("Shooter Speed", 1).getEntry();
    conveyerShuffle = Shuffleboard.getTab("Driving").add("Conveyer Speed", -1).getEntry();
    driveShuffle = Shuffleboard.getTab("Driving").add("Drive Speed", 1).getEntry();
    elevatorShuffle = Shuffleboard.getTab("Driving").add("Elevator Speed", 1).getEntry();
    intakeShuffle = Shuffleboard.getTab("Driving").add("Intake Speed", 1).getEntry();
    transverseShuffle = Shuffleboard.getTab("Driving").add("Transverse Speed", 1).getEntry();
    climberShuffle = Shuffleboard.getTab("Driving").add("Climber Speed", 1).getEntry();
    elevatorHelperShuffle = Shuffleboard.getTab("Driving").add("Elevator Helper Speed", 1).getEntry();


    // Invert drive motors
    frontLeft.setInverted(true);
    rearLeft.setInverted(true);
    frontRight.setInverted(true);
    rearRight.setInverted(true);

    // set up drive train object
    // m_robotDrive = new MecanumDrive(frontLeft, rearLeft, frontRight, rearRight);

    // set up joystick
    m_driverStick = new Joystick(kDriverJoystickChannel);
    m_operatorStick = new Joystick(kOperatorJoystickChannel);

    // WoF = new WheelOfFortune(wheelOfFortuneMotor, m_colorMatcher, m_colorSensor, 0.4, m_stick);

    // initalize ports for limit switches
    intakeLimit = new DigitalInput(0);
    shooterLimit = new DigitalInput(1);

    elevator = new Elevator(intakeLimit, shooterLimit, conveyerMotor,
                       elevatorMotor, elevatorHelperMotor, m_operatorStick, shooterMotor);
    shooter = new Shooter(shooterMotor, elevator);
    weightedDrive = new WeightedMecanumDrive(frontLeft, rearLeft, frontRight, rearRight);

    // camera.startAutomaticCapture();
  }

  // method that runs doing autonomous mode and teleop
  @Override
  public void robotPeriodic() {
    /*
     * Get the current going through channel 7, in Amperes. The PDP returns the
     * current in increments of 0.125A. At low currents
     * the current readings tend to be less accurate.
     */
    SmartDashboard.putNumber("Current Channel 0", m_pdp.getCurrent(0));

    /*
     * Get the voltage going into the PDP, in Volts.
     * The PDP returns the voltage in increments of 0.05 Volts.
     */
    SmartDashboard.putNumber("PDP Voltage", m_pdp.getVoltage());

    /*
     * Retrieves the temperature of the PDP, in degrees Celsius.
     */
    SmartDashboard.putNumber("PDP Temperature", m_pdp.getTemperature());

    SmartDashboard.putNumber("Front Left Temperature", frontLeft.getTemperature());
    SmartDashboard.putNumber("Rear Left Temperature", rearLeft.getTemperature());
    SmartDashboard.putNumber("Front Right Temperature", frontRight.getTemperature());
    SmartDashboard.putNumber("Rear Right Temperature", rearRight.getTemperature());

    SmartDashboard.putBoolean("Intake Limit Switch", intakeLimit.get());
    SmartDashboard.putBoolean("Shooter Limit Switch", shooterLimit.get());
    // boolean WoFReady = WoFReadyShuffle.getBoolean(false);
    /* 
    if(WoFReady == true) {
      if (m_stick.getRawButton(2)) {
        WoF.rotateThreeTimes();
      }

      if (m_stick.getRawButton(3)) {
        WoF.autoWoF();
      }
    }
    


    if (m_stick.getRawButton(4)){
      wheelOfFortuneUpDownMotor.set(1);
    } else {
      wheelOfFortuneUpDownMotor.set(0);
    }

    
    if (m_stick.getRawButton(5)){
      wheelOfFortuneUpDownMotor.set(-1);
    } else {
      wheelOfFortuneUpDownMotor.set(0);
    }

    */

    boolean manualMode = mode.getBoolean(true);

    double conveyerSpeed = conveyerShuffle.getDouble(-1);
    double shooterSpeed = shooterShuffle.getDouble(1);
    double elevatorSpeed = elevatorShuffle.getDouble(1);
    double intakeSpeed = intakeShuffle.getDouble(1);
    double transverseSpeed = transverseShuffle.getDouble(1);
    double climberSpeed = climberShuffle.getDouble(1);
    double elevatorHelperSpeed = elevatorHelperShuffle.getDouble(0.6);


    if (manualMode) {
      if (m_operatorStick.getRawButton(1)) {
        shooterMotor.set(shooterSpeed);
      } else {
        shooterMotor.set(0);
      }
  
      if(m_operatorStick.getRawButton(3)) {
        conveyerMotor.set(conveyerSpeed);
      } else {
        conveyerMotor.set(0);
      }

      if(m_operatorStick.getRawButton(4)) {
        elevatorMotor.set(elevatorSpeed);
      } else {
        elevatorMotor.set(0);
      }

      if(m_operatorStick.getRawButton(5)) {
        elevatorHelperMotor.set(elevatorHelperSpeed);
      } else {
        elevatorHelperMotor.set(0);
      }
    } else {
      if(m_operatorStick.getRawButton(1)) {
        elevator.startShooter(elevatorSpeed, elevatorHelperSpeed, conveyerSpeed, shooterSpeed);
      }

      // if(m_operatorStick.getRawButton(10)) {
        // elevator.runConveyer(conveyerSpeed, elevatorHelperSpeed);
      // }

      // if(m_operatorStick.getRawButton(11)) {
        //elevator.runElevatorUp(elevatorSpeed, elevatorHelperSpeed, conveyerSpeed);
      // }

      elevator.runElevator(conveyerSpeed, elevatorSpeed, elevatorHelperSpeed);
    }
    

    if(m_operatorStick.getRawButton(2)) {
      intakeMotor.set(intakeSpeed);
    } else {
      intakeMotor.set(0);
    }

    if(m_operatorStick.getRawButton(8)) {
      transverseMotor.set(transverseSpeed);
    } else {
      transverseMotor.set(0);
    }

    if(m_operatorStick.getRawButton(9)) {
      transverseMotor.set(-1 * transverseSpeed);
    } else {
      transverseMotor.set(0);
    }

    if(m_operatorStick.getRawButton(6)) {
      climberMotor.set(climberSpeed);
    } else {
      climberMotor.set(0);
    }

    if(m_operatorStick.getRawButton(7)) {
      climberMotor.set(-1 * climberSpeed);
    } else {
      climberMotor.set(0);
    }
  }

  // method that only runs during teleop
  @Override
  public void teleopPeriodic() {

    double driveSpeed = driveShuffle.getDouble(1);

    // this is for testing each drive motor individually
    // do NOT uncomment this unless you comment out the driveCartestian method

    /**
    if (m_stick.getRawButton(12)) {
      frontLeft.set(.5);
    } else {
      frontLeft.set(0);
    }
    */


    // Use the joystick X axis for lateral movement, Y axis for forward
    // movement, and Z axis for rotation.
    //m_robotDrive.driveCartesian(m_driverStick.getX() * driveSpeed, m_driverStick.getY() * driveSpeed, m_driverStick.getZ() *driveSpeed, 0.0);

    // weighted strafing method to offset weight inbalance
    weightedDrive.driveXY(-1 * m_driverStick.getX() * driveSpeed, m_driverStick.getY() * driveSpeed, 
                          m_driverStick.getRawButton(4), m_driverStick.getRawButton(3));
  }
}
