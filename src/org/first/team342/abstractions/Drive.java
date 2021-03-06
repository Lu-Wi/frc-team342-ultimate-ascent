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

import edu.wpi.first.wpilibj.command.Subsystem;
import org.first.team342.Controller;

/**
 *
 * @author Charlie
 */
public interface Drive {
    public void driveWithJoystick(Controller joystick);
    public void rammingSpeed(Controller joystick);
    public void turn(double speed);
    public void turn(double speed, double distance);
    public void forward(double speed, double distance);
    public void reverse(double speed, double distance);
    public void forward(double speed);
    public void reverse(double speed);
}
