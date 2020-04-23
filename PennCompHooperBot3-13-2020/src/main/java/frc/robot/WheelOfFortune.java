/**
 * this is the class that is used to control the wheel of fortune and color sensor
 */

 
package frc.robot;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj.Joystick;

import com.revrobotics.ColorSensorV3;
import com.revrobotics.ColorMatchResult;

import com.revrobotics.CANSparkMax;

import com.revrobotics.ColorMatch;

public class WheelOfFortune {
    public CANSparkMax WoF;
    public ColorMatch m_colorMatcher;
    public double defaultWoF;
    public ColorSensorV3 m_colorSensor;
    public Joystick m_stick;

    // These colors match the colors found on the "Wheel of Fortune" (WoF)
    private final Color kBlueTarget = ColorMatch.makeColor(0.143, 0.427, 0.429);
    private final Color kGreenTarget = ColorMatch.makeColor(0.197, 0.561, 0.240);
    private final Color kRedTarget = ColorMatch.makeColor(0.561, 0.232, 0.114);
    private final Color kYellowTarget = ColorMatch.makeColor(0.361, 0.524, 0.113);


	public WheelOfFortune(CANSparkMax WoF, ColorMatch m_colorMatcher, ColorSensorV3 m_colorSensor, double defaultWoF, Joystick m_stick) {
        this.WoF = WoF;
        this.m_colorMatcher = m_colorMatcher;
        this.defaultWoF = defaultWoF;
        this.m_colorSensor = m_colorSensor;
        this.m_stick = m_stick;

        /** 
         * This uses the colors that are defined above and adds them to the colorMatcher class
         * This is then used in the matchClosestColor function to determine the color of the object
         */
        m_colorMatcher.addColorMatch(kBlueTarget);
        m_colorMatcher.addColorMatch(kGreenTarget);
        m_colorMatcher.addColorMatch(kRedTarget);
        m_colorMatcher.addColorMatch(kYellowTarget);
    }

	public void autoWoF() {

        double WoFSpeed = SmartDashboard.getNumber("WoF Speed", defaultWoF);
    
        String gameData;
    
        String colorWoF = getWoF();
        double WoFSpeedActual = WoFSpeed;
        gameData = DriverStation.getInstance().getGameSpecificMessage();
        if(gameData.length() > 0)
        {
          switch (gameData.charAt(0))
          {
            case 'B' :
              //Blue case code
              while(colorWoF != "Red" && !m_stick.getRawButton(10)){
                WoF.set(WoFSpeedActual);
                colorWoF = getWoF();
              }
              break;
            case 'G' :
              //Green case code
              if(colorWoF == "Blue" || colorWoF == "Green") {
                WoFSpeedActual = -1 * WoFSpeedActual;
              }
              while(colorWoF != "Yellow" && !m_stick.getRawButton(10)){
                WoF.set(WoFSpeedActual);
                colorWoF = getWoF();
              }
              break;
            case 'R' :
              //Red case code
              while(colorWoF != "Blue" && !m_stick.getRawButton(10)){
                WoF.set(WoFSpeedActual);
                colorWoF = getWoF();
              }
              break;
            case 'Y' :
              //Yellow case code
              if(colorWoF == "Yellow" || colorWoF == "Red") {
                WoFSpeedActual = -1 * WoFSpeedActual;
              }
              while(colorWoF != "Green" && !m_stick.getRawButton(10)){
                WoF.set(WoFSpeedActual);
                colorWoF = getWoF();
              }
              break;
            default :
              //This is corrupt data
              break;
          }
        } else {
          //Code for no data received yet
        }
        WoF.set(0);
      }

    public String getWoF() {
        /**
         * The method GetColor() returns a normalized color value from the sensor and can be
         * useful if outputting the color to an RGB LED or similar. To
         * read the raw color, use GetRawColor().
         * 
         * The color sensor works best when within a few inches from an object in
         * well lit conditions (the built in LED is a big help here!). The farther
         * an object is the more light from the surroundings will bleed into the 
         * measurements and make it difficult to accurately determine its color.
         */
        Color detectedColor = m_colorSensor.getColor();
    
        /**
         * Run the color match algorithm on our detected color
         */
        ColorMatchResult match = m_colorMatcher.matchClosestColor(detectedColor);
        String colorstring = "Default";

        if (match.confidence > .85) {
          if (match.color == kBlueTarget) {
              colorstring = "Blue";
          } else if (match.color == kRedTarget) {
              colorstring = "Red";
          } else if (match.color == kGreenTarget) {
              colorstring = "Green";
          } else if (match.color == kYellowTarget) {
              colorstring = "Yellow";
          } else {
              colorstring = "Unknown";
          }
        }
        /**
         * Open Smart Dashboard or Shuffleboard to see the color detected by the 
         * sensor.
         */
        SmartDashboard.putNumber("Red", detectedColor.red);
        SmartDashboard.putNumber("Green", detectedColor.green);
        SmartDashboard.putNumber("Blue", detectedColor.blue);
        SmartDashboard.putNumber("Confidence", match.confidence);
        SmartDashboard.putString("Detected Color", colorstring);
    
        return colorstring;
    }

    // this method will rotate the WoF three times
    // this is done by counting the number of times that the colors change
    // since there are 8 color slices on the wheel, 24 slices = 3 rotations
    public void rotateThreeTimes() {
        int slices = 0;

        Color detectedColor = m_colorSensor.getColor();
        ColorMatchResult match = m_colorMatcher.matchClosestColor(detectedColor);

        Color prevColor = match.color;
        Color currColor;

        // use 28 slices vs. 24 to compensate for over counting slices
        // due to errors in color sensor
        while(slices < 28 && !m_stick.getRawButton(10)) {

            detectedColor = m_colorSensor.getColor();
            match = m_colorMatcher.matchClosestColor(detectedColor);

            WoF.set(1);
            currColor = match.color;

            if(currColor != prevColor) {
                slices++;
            }

            prevColor = currColor;
        }
        WoF.set(0);
    }
}