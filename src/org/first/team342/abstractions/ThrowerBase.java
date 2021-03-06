/*
 * Copyright 2013 FRC Team 342
 * 
 * This file is part of "FRC Team 342 Ultimate Ascent Robot".
 * 
 * "FRC Team 342 Ultimate Ascent Robot" is free software: you can redistribute 
 * it and/or modify it under the terms of the GNU General Public License as 
 * published by the Free Software Foundation, either version 3 of the License, 
 * or (at your option) any later version.
 *
 * "FRC Team 342 Ultimate Ascent Robot" is distributed in the hope that it
 * will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty 
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General
 * Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with "FRC Team 342 Ultimate Ascent Robot".  If not, see 
 * <http://www.gnu.org/licenses/>.
 */
package org.first.team342.abstractions;

import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.first.team342.RobotMap;

/**
 *
 * @author Team 342
 */
public abstract class ThrowerBase extends Subsystem implements Thrower {

    private double targetAngle;
    private SpeedController aim;
//    private Gyro gyro;
    private DigitalInput top;
    private DigitalInput bottom;
    protected Victor pushMotor;
    protected AnalogChannel potentiometer;
    protected DigitalInput pushLimitSwitchFront;
    protected DigitalInput pushLimitSwitchBack;
    protected Timer timer;
    protected DriverStation driver;
    public boolean pushDirectionIsForward = true;
 
    protected ThrowerBase() {
        super();
        this.targetAngle = 0.0;
        this.aim = new Victor(RobotMap.PWM_CHANNEL_AIM);
//        this.gyro = new Gyro(RobotMap.ANALOG_CHANNEL_GYRO);
        this.top = new DigitalInput(RobotMap.DIO_CHANNEL_THROWER_TOP);
        this.bottom = new DigitalInput(RobotMap.DIO_CHANNEL_THROWER_BOTTOM);
        //this.pushMotor = new Victor(RobotMap.PWM_CHANNEL_FIRE);
//        this.pushLimitSwitchFront = new DigitalInput(RobotMap.DIO_CHANNEL_FIRE);
//        this.pushLimitSwitchBack = new DigitalInput(RobotMap.DIO_CHANNEL_FIRE_BACK);
        this.potentiometer = new AnalogChannel(RobotMap.ANALOG_CHANNEL_POTENTIOMETER);
        this.driver = DriverStation.getInstance();
    }

    public void simpleRaise(double speed) {
        System.out.println("ThrowerAim Top switch is: " + this.top.get());
        if (!this.top.get()) {
            this.aim.set(0.0);
        } else {
            this.aim.set(speed);
        }
    }

    public void simpleLower(double speed) {
        System.out.println("ThrowerAim Bottom switch is: " + this.bottom.get());
        if (!this.bottom.get()) {
            this.aim.set(0.0);
//            this.gyro.reset();
        } else {
            this.aim.set(-speed);
        }
    }

    public void moveToAngle(double speed, double angle) {
        System.out.println("[DEBUG] moveToAngle method executed");
        double uncertainty = 1.0;
        if (!this.top.get()) {
            System.out.println("[DEBUG] moveToAngle method Top Limit Switch Tripped");
            this.aim.set(0.0);
        } else if (!this.bottom.get()) {
            System.out.println("[DEBUG] moveToAngle method Bottom Limit Switch Tripped");
            this.aim.set(0.0);
        } else if (this.convertAngle() + uncertainty
                < angle) {
            System.out.println("[DEBUG] moveToAngle method executed up");
            this.aim.set(speed);
        } else if (this.convertAngle() - uncertainty
                > angle) {
            System.out.println("[DEBUG] moveToAngle method executed down");
            this.aim.set(-speed);
        } else {
            System.out.println("[DEBUG] moveToAngle method in uncertainty");
            this.aim.set(0.0);
        }
    }

    public void moveToAngleCamera(double speed, int targetPixel, int currentPixel) {
        System.out.println("[DEBUG] moveToAngle method executed");
        int uncertainty = 2;
        if (!this.top.get()) {
            System.out.println("[DEBUG] moveToAngle method Top Limit Switch Tripped");
            this.aim.set(0.0);
        } else if (!this.bottom.get()) {
            System.out.println("[DEBUG] moveToAngle method Bottom Limit Switch Tripped");
            this.aim.set(0.0);
        } else if ( currentPixel + uncertainty 
                < targetPixel) {
            System.out.println("[DEBUG] moveToAngle method executed up");
            this.aim.set(speed);
        } else if (currentPixel - uncertainty
                > targetPixel) {
            System.out.println("[DEBUG] moveToAngle method executed down");
            this.aim.set(-speed);
        } else {
            System.out.println("[DEBUG] moveToAngle method in uncertainty");
            this.aim.set(0.0);
        }
    }

    public void moveToAngleSupporting(double angle) {
        double aimOut = (this.driver.getAnalogIn(3) / 10) * (angle - this.convertAngle());
        if (aimOut > 1) {
            aimOut = 1;
        }
        if (aimOut < .3) {
            aimOut = .3;
        }
        this.aim.set(-aimOut);
        System.out.println("Aim Out :" + aimOut);
    }

    //TODO: fix target angle settings
    public void increseAngle(double angle) {
        if (!this.top.get()) {
            this.aim.set(0.0);
        } else if (this.convertAngle(this.potentiometer.getVoltage()) < this.targetAngle) {
            this.aim.set(.5);
        } else if (this.convertAngle(this.potentiometer.getVoltage()) > this.targetAngle) {
            this.aim.set(-.5);
        } else {
            this.aim.set(0.0);
        }
    }

    //TODO: fix target angle settings
    public void decreaseAngle(double angle) {
        this.targetAngle = (this.targetAngle - angle);
        if (!this.bottom.get()) {
            this.aim.set(0.0);
        } else if (this.convertAngle(this.potentiometer.getVoltage()) - RobotMap.GYRO_DEAD_ZONE < this.targetAngle) {
            this.aim.set(.5);
//            this.targetAngle = this.gyro.getAngle();
        } else if (this.convertAngle(this.potentiometer.getVoltage()) + RobotMap.GYRO_DEAD_ZONE < this.targetAngle) {
            this.aim.set(-.5);
//            this.targetAngle = this.gyro.getAngle();
        } else {
            this.aim.set(0.0);
        }
    }

    public void potentiometerTest() {
        double rawValue = this.potentiometer.getVoltage();
        System.out.println("Raw Value: " + rawValue);
//        SmartDashboard.putNumber("Raw Value: ", rawValue);
        double angleValue = this.convertAngle(rawValue);
//        SmartDashboard.putNumber("Angle Value: ", angleValue);
        System.out.println("Angle Value: " + angleValue);
    }

    public void aimMotorStop() {
        this.aim.set(0.0);
    }

    public void pushMotorSet(double value) {
        pushMotor.set(value);
        System.out.println("vex motor running");
    }

    public void push(double speed) {
        if (!pushLimitSwitchFront.get()) {
            pushMotor.set(-speed);
            pushDirectionIsForward = false;
            System.out.println("[PUSH] Held, front switch " + pushLimitSwitchFront.get() + ", speed " + -speed + ", direction " + pushDirectionIsForward);
        } else if (!pushLimitSwitchBack.get()) {
            pushMotor.set(speed);
            pushDirectionIsForward = true;
            System.out.println("[PUSH] Held, back switch " + pushLimitSwitchBack.get() + ", speed " + speed + ", direction " + pushDirectionIsForward);
        } else {
            if (pushDirectionIsForward) {
                pushMotor.set(speed);
                System.out.println("[PUSH] Held, no switch, speed " + speed + ", direction " + pushDirectionIsForward);
            } else {
                pushMotor.set(-speed);
                System.out.println("[PUSH] Held, no switch, speed " + -speed + ", direction " + pushDirectionIsForward);
            }
        }
    }

    public void pushUntilEnd(double speed) {
        if (!pushLimitSwitchFront.get()) {
            pushMotor.set(-speed);
            pushDirectionIsForward = false;
            System.out.println("[PUSH] Released, front switch " + pushLimitSwitchFront.get() + ", speed " + -speed + ", direction " + pushDirectionIsForward);
        } else if (!pushLimitSwitchBack.get()) {
            pushMotor.set(0.0);
            pushDirectionIsForward = true;
            System.out.println("[PUSH] Released, back switch " + pushLimitSwitchBack.get() + ", speed " + 0.0 + ", direction " + pushDirectionIsForward);
        } else {
            if (pushDirectionIsForward) {
                pushMotor.set(speed);
                System.out.println("[PUSH] Released, no switch, speed " + speed + ", direction " + pushDirectionIsForward);
            } else {
                pushMotor.set(-speed);
                System.out.println("[PUSH] Released, no switch, speed " + -speed + ", direction " + pushDirectionIsForward);
            }
        }
    }

    public boolean getSwitchPushFront() {
        return pushLimitSwitchFront.get();
    }

    public boolean getSwitchPushBack() {
        return pushLimitSwitchBack.get();
    }

    public boolean getSwitchTop() {
        return this.top.get();
    }

    public boolean getSwitchBottom() {
        return this.bottom.get();
    }

    public double convertAngle(double input) {
        double angleConstant = 60.0;
        double angle = input * angleConstant;
        return angle;
    }

    //JH - Added method that gets pot volts on its own
    public double convertAngle() {
        double angleConstant = 60.;//JH - Move with the other constants
        double angle = angleConstant * this.potentiometer.getVoltage();
        return angle;
    }
//    public void resetGyro() {
//        this.gyro.reset();
//    }
}
