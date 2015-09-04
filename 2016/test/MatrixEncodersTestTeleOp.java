/* Copyright (c) 2015 Craig MacFarlane

All rights reserved.

Redistribution and use in source and binary forms, with or without modification,
are permitted (subject to the limitations in the disclaimer below) provided that
the following conditions are met:

Redistributions of source code must retain the above copyright notice, this list
of conditions and the following disclaimer.

Redistributions in binary form must reproduce the above copyright notice, this
list of conditions and the following disclaimer in the documentation and/or
other materials provided with the distribution.

Neither the name of Craig MacFarlane nor the names of its contributors
may be used to endorse or promote products derived from this software without
specific prior written permission.

NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
"AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. */

package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.modernrobotics.ModernRoboticsMatrixDcMotorController;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;

/**
 * A simple example of run to position with a matrix controller.
 */
public class MatrixEncodersTestTeleOp extends OpMode {

    private DcMotor motor1;
    private ModernRoboticsMatrixDcMotorController mc;
    private boolean loopOnce = false;
    private boolean initOnce = false;
    private int battery;

    @Override
    public void init()
    {
        if (!initOnce) {
            motor1.setChannelMode(DcMotorController.RunMode.RESET_ENCODERS);
            motor1.setChannelMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
            initOnce = true;
        }
    }

    @Override
    public void start()
    {
        motor1 = hardwareMap.dcMotor.get("motor_1");
        mc = (ModernRoboticsMatrixDcMotorController)hardwareMap.dcMotorController.get("MatrixControllerMotor");
    }

    public void stop()
    {
        motor1.setPower(0.0);
        initOnce = false;
        loopOnce = false;
    }

    @Override
    public void loop()
    {
        int position;

        if (!loopOnce) {
            loopOnce = true;
            motor1.setPower(0.1);
        }

        /*
         * Note that due to overall system latency, this approach is
         * going to be somewhat inaccurate.  Inaccuracy increases with
         * speed.
         */
        position = motor1.getCurrentPosition();
        if (position > 1000) {
            motor1.setPower(0.0);
        }
        telemetry.addData("Position: ", position);

        battery = mc.getBattery();
        telemetry.addData("Battery: ", ((float)battery/1000));
    }
}