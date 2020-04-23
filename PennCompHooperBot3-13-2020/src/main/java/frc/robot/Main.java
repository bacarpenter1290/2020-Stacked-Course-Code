/**
 * file name: PennCompHooperBot3-13-2020
 * written by Baylee Carpenter
 * Written: 3/4/2020
 *
 * this is code for Hooper Bot during the 2020 St. Joe Comp. at Penn High School
 * 
 * tested features:
 * mecanum drive, manual intake, shooter, and elevator
 * 
 * untested features:
 * weight compensated drive, auto shooter
 * 
 * commented out features:
 * automatic wheel of fortune (tested)
 */

package frc.robot;

import edu.wpi.first.wpilibj.RobotBase;

public final class Main {
  private Main() {
  }
  public static void main(String... args) {
    RobotBase.startRobot(Robot::new);
  }
}
