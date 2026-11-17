package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Simple servo test.
 * Press TRIANGLE (Y on Logitech/Xbox) on gamepad 1 to toggle the servo
 * between POSITION_A and POSITION_B.
 *
 * In the Robot Configuration, name the servo "servo"
 * (or change SERVO_NAME below to match your config).
 */
@TeleOp(name = "Servo Test", group = "Test")
public class ServoTest extends LinearOpMode {

    private static final String SERVO_NAME = "servo";
    private static final double POSITION_A = 0.0;   // starting position
    private static final double POSITION_B = 1.0;   // toggled position

    @Override
    public void runOpMode() {
        Servo servo = hardwareMap.get(Servo.class, SERVO_NAME);

        boolean atPositionB = false;
        boolean lastTriangle = false;

        telemetry.addLine("Ready. Press TRIANGLE to toggle the servo.");
        telemetry.update();

        waitForStart();

        servo.setPosition(POSITION_A);

        while (opModeIsActive()) {
            boolean triangle = gamepad1.triangle;

            // Only toggle on the moment the button is pressed (not while held)
            if (triangle && !lastTriangle) {
                atPositionB = !atPositionB;
                servo.setPosition(atPositionB ? POSITION_B : POSITION_A);
            }
            lastTriangle = triangle;

            telemetry.addData("Triangle", triangle ? "PRESSED" : "released");
            telemetry.addData("Servo target", atPositionB ? POSITION_B : POSITION_A);
            telemetry.update();
        }
    }
}
