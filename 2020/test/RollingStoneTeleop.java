/*
 * Copyright (c) September 2017 FTC Teams 25/5218
 *
 *  All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without modification,
 *  are permitted (subject to the limitations in the disclaimer below) provided that
 *  the following conditions are met:
 *
 *  Redistributions of source code must retain the above copyright notice, this list
 *  of conditions and the following disclaimer.
 *
 *  Redistributions in binary form must reproduce the above copyright notice, this
 *  list of conditions and the following disclaimer in the documentation and/or
 *  other materials provided with the distribution.
 *
 *  Neither the name of FTC Teams 25/5218 nor the names of their contributors may be used to
 *  endorse or promote products derived from this software without specific prior
 *  written permission.
 *
 *  NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 *  LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 *  AS IS AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 *  THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESSFOR A PARTICULAR PURPOSE
 *  ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 *  FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 *  DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 *  SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 *  CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR
 *  TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
 *  THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package test;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import team25core.FourWheelDirectDrivetrain;
import team25core.GamepadTask;
import team25core.Robot;
import team25core.RobotEvent;
import team25core.TankDriveTask;
import team25core.TankMechanumControlSchemeReverse;
import team25core.TeleopDriveTask;

@TeleOp(name = "LeagueMeet0")
//@Disabled
public class RollingStoneTeleop extends Robot {

    private DcMotor frontLeft;
    private DcMotor frontRight;
    private DcMotor rearLeft;
    private DcMotor rearRight;

    //emily's code

    private Servo leftServo;
    private Servo rightServo;
    private Servo monsterRetentionServo;
    private DcMotor liftMotor;
    private final double OPEN_LEFT_SERVO = 85 / 256; //FIXME
    private final double OPEN_RIGHT_SERVO = 171 / 256; //FIXME
    private final double CLOSE_LEFT_SERVO = 52 / 256; //FIXME
    private final double CLOSE_RIGHT_SERVO = 196 / 256; //FIXME
    private final double OPEN_MONSTER_RETENTION_SERVO = 256 / 256;
    private final double CLOSE_MONSTER_RETENTION_SERVO = 128 / 256;
    //emily's code

    private TeleopDriveTask drivetask;

    private FourWheelDirectDrivetrain drivetrain;

    private static final int TICKS_PER_INCH = 79;


    @Override
    public void handleEvent(RobotEvent e) {
        // Nothing to do here...
    }

    @Override
    public void init() {
        frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
        frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        rearLeft = hardwareMap.get(DcMotor.class, "rearLeft");
        rearRight = hardwareMap.get(DcMotor.class, "rearRight");

        //emily's
        liftMotor = hardwareMap.get(DcMotor.class, "liftMotor");
        leftServo = hardwareMap.servo.get("leftServo");
        rightServo = hardwareMap.servo.get("rightServo");
        monsterRetentionServo = hardwareMap.servo.get("monsterRetentionServo");
        monsterRetentionServo.setPosition(CLOSE_MONSTER_RETENTION_SERVO);

        liftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        liftMotor.setPower(0.0);
        //emily's

        TankMechanumControlSchemeReverse scheme = new TankMechanumControlSchemeReverse(gamepad1);

        drivetrain = new FourWheelDirectDrivetrain(frontRight, rearRight, frontLeft, rearLeft);
        drivetrain.setNoncanonicalMotorDirection();
    }

    @Override
    public void start() {
        this.addTask(new TankDriveTask(this, drivetrain));

        monsterRetentionServo.setPosition(OPEN_MONSTER_RETENTION_SERVO);
        //emily's
        this.addTask(new GamepadTask(this, GamepadTask.GamepadNumber.GAMEPAD_1) {
            //@Override
            public void handleEvent(RobotEvent e) {
                GamepadEvent gamepadEvent = (GamepadEvent) e;

                switch (gamepadEvent.kind) {
                    case LEFT_BUMPER_DOWN:
                        leftServo.setPosition(OPEN_LEFT_SERVO);
                        rightServo.setPosition(OPEN_RIGHT_SERVO);
                        break;
                    case RIGHT_BUMPER_DOWN:
                        leftServo.setPosition(CLOSE_LEFT_SERVO);
                        rightServo.setPosition(CLOSE_RIGHT_SERVO);
                        break;
                    case BUTTON_A_DOWN:
                        liftMotor.setPower(-0.5);
                        break;
                    case BUTTON_Y_DOWN:
                        liftMotor.setPower(0.5);
                        break;
                    case BUTTON_B_DOWN:
                        liftMotor.setPower(0.0);
                        break;
                    default:
                        break;
                }
            }
        });
    }
}





