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

package opmodes;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import team25core.GamepadTask;
import team25core.MechanumGearedDrivetrain;
import team25core.Robot;
import team25core.RobotEvent;
import team25core.RunToEncoderValueTask;
import team25core.TankMechanumControlSchemeBackwards;
import team25core.TankMechanumControlSchemeReverse;
import team25core.TeleopDriveTask;

@TeleOp(name = "LM1 CODE")
//@Disabled
public class RollingStoneTeleop extends Robot {

    private DcMotor frontLeft;
    private DcMotor frontRight;
    private DcMotor rearLeft;
    private DcMotor rearRight;

    //amory's


    private Servo foundationHookLeft;
    private Servo foundationHookRight;

    //emily's code

    private Servo leftServo;
    private Servo rightServo;
    private Servo monsterRetentionServo;
    private DcMotor liftMotor;
    private final double OPEN_LEFT_SERVO = (float)10 / (float)256;
    private final double OPEN_RIGHT_SERVO = (float)250 / (float)256;
    private final double CLOSE_LEFT_SERVO = (float)95 / (float)256;
    private final double CLOSE_RIGHT_SERVO = (float)199 / (float)256;
    private final double OPEN_MONSTER_RETENTION_SERVO = 220 / 256; //FIXME
    private final double CLOSE_MONSTER_RETENTION_SERVO = 117 / 256; //FIXME
    private final int DELTA_HEIGHT = 180;
    private final int LINEAR_INITIAL_POS = 100;
    private final DcMotorSimple.Direction LIFT_DIRECTION_UP = DcMotorSimple.Direction.FORWARD;
    private final DcMotorSimple.Direction LIFT_DIRECTION_DOWN = DcMotorSimple.Direction.REVERSE;
    private int currentHeight =  LINEAR_INITIAL_POS;

    private Telemetry.Item linearPos;

    private Telemetry.Item linearEncoderVal;
    //emily's code

    private TeleopDriveTask drivetask;

    //private FourWheelDirectDrivetrain drivetrain;
    private MechanumGearedDrivetrain drivetrain;

    private static final int TICKS_PER_INCH = 79;


    @Override
    public void handleEvent(RobotEvent e) {
        // Nothing to do here...
    }

    @Override
    public void init() {

        foundationHookLeft = hardwareMap.servo.get("foundationHookLeftServo");
        foundationHookRight = hardwareMap.servo.get("foundationHookRightServo");

        foundationHookLeft.setPosition(0.0390625);
        foundationHookRight.setPosition(0.34765625);

        frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
        frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        rearLeft = hardwareMap.get(DcMotor.class, "rearLeft");
        rearRight = hardwareMap.get(DcMotor.class, "rearRight");

        //added following 4 lines
        rearLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rearRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        //emily's
        liftMotor = hardwareMap.get(DcMotor.class, "liftMotor");
        leftServo = hardwareMap.servo.get("leftServo");
        rightServo = hardwareMap.servo.get("rightServo");
        monsterRetentionServo = hardwareMap.servo.get("monsterRetentionServo");
        monsterRetentionServo.setPosition(CLOSE_MONSTER_RETENTION_SERVO);

        liftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        liftMotor.setPower(0.0);

        liftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        liftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        linearPos = telemetry.addData("linearpos", 0);

        //linearEncoderVal.addData("linearpos", "unknown");
        //emily's

        TankMechanumControlSchemeReverse scheme = new TankMechanumControlSchemeReverse(gamepad1);

        drivetrain = new MechanumGearedDrivetrain(360, frontRight, rearRight, frontLeft, rearLeft);
        drivetrain.setNoncanonicalMotorDirection();
    }


    public void liftMotorOneStep(DcMotorSimple.Direction direction)
    {
        if (direction == DcMotorSimple.Direction.REVERSE) {

            currentHeight -=  DELTA_HEIGHT;
            if(currentHeight < 50) {
                currentHeight = 50;
            }

        } else {
            currentHeight += DELTA_HEIGHT;
            if (currentHeight > 500) {
                currentHeight = 500;
            }

        }
        linearPos.setValue(currentHeight);

        liftMotor.setDirection(direction);
        //linearEncoderVal.setValue(currentHeight);
        this.addTask(new RunToEncoderValueTask(this,  liftMotor, currentHeight, .75));
    }

    @Override
    public void start() {

       //switch (gamepadEvent.kind)


        TankMechanumControlSchemeBackwards scheme = new TankMechanumControlSchemeBackwards(gamepad1);
        // added lines 146 and 148
        drivetask = new TeleopDriveTask(this, scheme, frontLeft, frontRight, rearLeft, rearRight);

        this.addTask(drivetask);
        //this.addTask(new TankDriveTask(this, drivetrain));

        monsterRetentionServo.setPosition(OPEN_MONSTER_RETENTION_SERVO);

        leftServo.setPosition(OPEN_LEFT_SERVO);
        rightServo.setPosition(OPEN_RIGHT_SERVO);

        this.addTask(new GamepadTask(this, GamepadTask.GamepadNumber.GAMEPAD_1) {
            //@Override
            public void handleEvent(RobotEvent e) {
                GamepadEvent gamepadEvent = (GamepadEvent) e;

                switch (gamepadEvent.kind) {
                    case RIGHT_BUMPER_DOWN:
                        foundationHookLeft.setPosition(0.0390625); //open
                        foundationHookRight.setPosition(0.34765625);
                        break;
                    case RIGHT_TRIGGER_DOWN:
                        foundationHookLeft.setPosition(0.54296875);
                        foundationHookRight.setPosition(0.83984375);
                        break;
                    default:
                        break;
                }
            }
        });

        

        //emily's
        this.addTask(new GamepadTask(this, GamepadTask.GamepadNumber.GAMEPAD_2) {
            //@Override
            public void handleEvent(RobotEvent e) {
                GamepadEvent gamepadEvent = (GamepadEvent) e;

                switch (gamepadEvent.kind) {
                    case BUTTON_B_DOWN:
                        leftServo.setPosition(OPEN_LEFT_SERVO);
                        rightServo.setPosition(OPEN_RIGHT_SERVO);
                        break;
                    case BUTTON_X_DOWN:
                        leftServo.setPosition(CLOSE_LEFT_SERVO);
                        rightServo.setPosition(CLOSE_RIGHT_SERVO);
                        break;
                    case BUTTON_A_DOWN:
                        //liftMotor.setPower(0.5);
                        liftMotorOneStep(LIFT_DIRECTION_DOWN);
                        break;
                    case BUTTON_Y_DOWN:
                        //liftMotor.setPower(0.0);
                        liftMotorOneStep(LIFT_DIRECTION_UP);
                        break;
                    case DPAD_RIGHT_DOWN:
                        monsterRetentionServo.setPosition(OPEN_MONSTER_RETENTION_SERVO);
                        break;
                    case DPAD_LEFT_DOWN:
                        monsterRetentionServo.setPosition(CLOSE_MONSTER_RETENTION_SERVO);
                        break;
                }
            }
        });
    }
}





