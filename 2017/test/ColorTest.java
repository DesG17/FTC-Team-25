package test;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DeviceInterfaceModule;
import com.qualcomm.robotcore.hardware.DigitalChannelController;
import com.qualcomm.robotcore.hardware.Servo;

import java.util.ServiceConfigurationError;

import team25core.ColorSensorTask;
import team25core.Robot;
import team25core.RobotEvent;

/**
 * Created by Izzie on 2/10/2016.
 */
@Autonomous(name = "Color Test 2", group="AutoTeam25")
@Disabled

public class ColorTest extends Robot {

    private DeviceInterfaceModule cdim;
    private ColorSensor color;

    @Override
    public void handleEvent(RobotEvent e) {

    }

    @Override
    public void init() {
        cdim = hardwareMap.deviceInterfaceModule.get("interface");
        color = hardwareMap.colorSensor.get("color");

        cdim.setDigitalChannelMode(0, DigitalChannelController.Mode.OUTPUT);
        cdim.setDigitalChannelState(0, false);
    }

    @Override
    public void loop() {
        int red = color.red();
        int blue = color.blue();

        if (red > blue) {
            telemetry.addData("STATE: ", "Red");
        } else if (red < blue) {
            telemetry.addData("STATE: ", "Blue");
        } else {
            telemetry.addData("STATE: ", "Unknown");
        }

        telemetry.addData("RED: ", red);
        telemetry.addData("BLUE: ", blue);
    }
}
