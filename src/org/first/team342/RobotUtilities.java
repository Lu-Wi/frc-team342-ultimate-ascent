/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.first.team342;

import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.can.CANTimeoutException;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.tables.TableKeyNotDefinedException;

/**
 * Random utilities for robot operation.
 * 
 * @author FIRST Team 342
 */
public class RobotUtilities {
    /**
     * Initialize a CAN Jaguar device with the given device address.
     * 
     * @param deviceNumber the CAN device address.
     * 
     * @return the initialized CAN device.  If an error occurs then <code>null<code> is returned.
     */
    public static CANJaguar initializeCANJaguar(int deviceNumber) {
        CANJaguar jaguar = null;

        try {
            jaguar = new CANJaguar(deviceNumber);
        } catch (CANTimeoutException ex) {
            System.out.println(ex.getMessage() + " - Device Number: " + deviceNumber);
        }

        return jaguar;
    }
    public static int getIntSmartDashboard(String key){
        double value = 0.0;
        try{
        value = SmartDashboard.getNumber(key);
        }catch(TableKeyNotDefinedException ex){
            System.out.println("can not find key:" + key);
        }
        return (int)value;
    }
}
