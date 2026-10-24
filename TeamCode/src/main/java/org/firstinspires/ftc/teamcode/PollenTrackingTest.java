package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

@Autonomous
public class PollenTrackingTest extends LinearOpMode {
    private Limelight3A limelight3A;
    private DcMotor frontLeft, frontRight;
    private DcMotor backLeft, backRight;

    private static final double TURN_KP = 0.018;
    private static final double MAX_TURN = 0.25;
    private static final double FORWARD_POWER = 0.18;
    private static final double DEAD_BAND = 1.5;
    private static final double STOP_AREA = 4.0;


    @Override
    public void runOpMode() {
        frontLeft = hardwareMap.get(DcMotor.class, "left_front");
        frontRight = hardwareMap.get(DcMotor.class, "right_front");
        backLeft = hardwareMap.get(DcMotor.class, "left_back");
        backRight = hardwareMap.get(DcMotor.class, "right_back"); // remember deviceNames

        limelight3A = hardwareMap.get(Limelight3A.class, "limelight");

        frontLeft.setDirection(DcMotor.Direction.REVERSE);
        backLeft.setDirection(DcMotor.Direction.REVERSE);

        stopDrive();

        telemetry.addLine("BioBuzz Pollen Tracker");
        telemetry.update();

        waitForStart();

        limelight3A.pipelineSwitch(1);
        limelight3A.start();

        waitForStart();

        if (isStopRequested()) {
            limelight3A.stop();
            return;
        }

        while (opModeIsActive()) {

            LLResult result = limelight3A.getLatestResult();

            boolean found = result != null && result.isValid();

            if (!found) {
                stopDrive();

                telemetry.addLine("No Target Detected");
                telemetry.update();

                idle();
                continue;
            }

            double tx = result.getTx();
            double ta = result.getTa();

            double turn = 0.0;
            double forward = 0.0;

            if (ta >= STOP_AREA) {
                stopDrive();
                telemetry.addLine("Target reached");
            } else {
                if (Math.abs(tx) > DEAD_BAND) {
                    turn = TURN_KP * tx;
                    turn = Math.max(-MAX_TURN, Math.min(MAX_TURN, turn));
                    }

                forward = FORWARD_POWER;

                double leftPower = forward - turn;
                double rightPower = forward - turn;

                double maxPower = Math.max(1.0, Math.max(Math.abs(leftPower), Math.abs(rightPower)));

                leftPower /= maxPower;
                rightPower /= maxPower;

                frontLeft.setPower(leftPower);
                backLeft.setPower(leftPower);
                frontRight.setPower(rightPower);
                backRight.setPower(rightPower);

                }

            telemetry.addData("Target found: ", found);
            telemetry.addData("Horizontal offset (tx): ", "%.2f", tx);
            telemetry.addData("Target Area (ta): ", "%.2f%%", ta);
            telemetry.addData("Forward Power: ", "%.2f", forward);
            telemetry.addData("Turn correction: ", "%.3f", turn);

            idle();
            }

        stopDrive();
        limelight3A.stop();
        }
        private void stopDrive() {
        frontLeft.setPower(0);
        backLeft.setPower(0);
        frontRight.setPower(0);
        backRight.setPower(0);
    }
}