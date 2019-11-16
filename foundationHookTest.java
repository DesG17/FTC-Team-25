package test;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name = "foundationHookTest", group = "")
public class foundationHookTest extends LinearOpMode {

  private Servo foundationHookLeft;
  private Servo foundationHookRight;


    @Override
    public void runOpMode() {
    foundationHookLeft = hardwareMap.servo.get("foundationHookLeft");
    foundationHookRight = hardwareMap.servo.get("foundationHookRight");

    foundationHookLeft.setPosition(0.0390625);
    foundationHookRight.setPosition(0.34765625);
    waitForStart();
    if (opModeIsActive()) {
      while (opModeIsActive()) {
        if (gamepad1.x) {
          foundationHookLeft.setPosition(0.0390625);
          foundationHookRight.setPosition(0.34765625);
          // Open position for left servo: 10/256
          // Open position for right servo: 89/256
        } else if (gamepad1.b) {
          foundationHookLeft.setPosition(0.54296875);
          foundationHookRight.setPosition(0.83984375);
          // Close position for left servo: 247/256
          // Close position for right servo: 215/256
        }
        telemetry.update();
      }
    }
  }
}
