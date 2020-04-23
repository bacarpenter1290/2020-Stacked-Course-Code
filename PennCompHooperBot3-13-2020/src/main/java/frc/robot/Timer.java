/**
 * This class contains a timer for use through the project
 */

 package frc.robot;

 public class Timer {

    static public void waitMilli(long milli) {
        long startTime = System.currentTimeMillis();
        long elapsedTime = System.currentTimeMillis();
        do {

            elapsedTime = System.currentTimeMillis() - startTime;

        } while(elapsedTime < milli);
    }
 }