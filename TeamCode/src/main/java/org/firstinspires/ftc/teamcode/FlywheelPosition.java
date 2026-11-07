package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DigitalChannel;

@TeleOp(name = "Flywheel Position Test")
public class FlywheelPosition extends LinearOpMode {

    @Override
    public void runOpMode() {
        DigitalChannel magSwitch = hardwareMap.get(DigitalChannel.class, "mag_switch");
        magSwitch.setMode(DigitalChannel.Mode.INPUT);

        telemetry.addLine("Ready");
        telemetry.update();
        waitForStart();

        while (opModeIsActive()) {
            // Hall-effect switches are usually active-low: false = magnet detected
            boolean magnetDetected = !magSwitch.getState();
            telemetry.addData("Magnet", magnetDetected);
            telemetry.update();
        }
    }
}