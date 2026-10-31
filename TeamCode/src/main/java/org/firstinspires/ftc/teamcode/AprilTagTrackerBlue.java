// Test Code 3: Finds the largest visible AprilTag, turns to center it, then drives
// toward it until its area reaches STOP_AREA.
//
// TODO (planned, not implemented yet):
//  - detect the four-tag cluster instead of a single tag
//  - only track tags that belong to the blue alliance (right now ANY tag ID is tracked,
//    including red-alliance tags)
//  - decide whether to shoot based on whether the cluster is upside down (tag angle)

package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;

@Autonomous
public class AprilTagTrackerBlue extends LinearOpMode {
    private Limelight3A limelight3A;
    private DcMotor frontLeft, frontRight;
    private DcMotor backLeft, backRight;

    private static final double TURN_KP = 0.018;
    private static final double MAX_TURN = 0.25;
    private static final double FORWARD_POWER = 0.18;
    private static final double DEAD_BAND = 1.5;
    private static final double STOP_AREA = 4.0;

    @Override
    public void runOpMode() throws InterruptedException {
        frontLeft = hardwareMap.get(DcMotor.class, "left_front");
        frontRight = hardwareMap.get(DcMotor.class, "right_front");
        backLeft = hardwareMap.get(DcMotor.class, "left_back");
        backRight = hardwareMap.get(DcMotor.class, "right_back");

        limelight3A = hardwareMap.get(Limelight3A.class, "limelight");

        // Right side is reversed (confirmed on the robot; matches pedro/Constants.java)
        frontRight.setDirection(DcMotor.Direction.REVERSE);
        backRight.setDirection(DcMotor.Direction.REVERSE);

        stopDrive();

        // Start the camera during init so it's already streaming results at START
        limelight3A.pipelineSwitch(2);
        limelight3A.start();

        telemetry.addLine("Blue Scoring April Tag Test");
        telemetry.update();

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

            LLResultTypes.FiducialResult target = null;
            double largestArea = -1.0;

            for (LLResultTypes.FiducialResult tag : result.getFiducialResults()) {
                if (tag.getTargetArea() > largestArea) {
                    largestArea = tag.getTargetArea();
                    target = tag;
                }
            }

            if (target == null) {
                stopDrive();

                telemetry.addLine("No AprilTag Detected");
                telemetry.update();

                idle();
                continue;
            }
            double tx = target.getTargetXDegrees();
            double area = target.getTargetArea();
            double turn = 0.0;

            if (Math.abs(tx) > DEAD_BAND) {
                turn = tx * TURN_KP;

                turn = Math.max(-MAX_TURN,
                        Math.min(MAX_TURN, turn));
            }

            double forward = 0.0;
            if (Math.abs(tx) <= DEAD_BAND
                    && area < STOP_AREA) {
                forward = FORWARD_POWER;
            }

            if (area >= STOP_AREA) {
                forward = 0.0;
                turn = 0.0;
            }

            // tx > 0 means the tag is to the right -> speed up left side to turn right
            double leftPower = forward + turn;
            double rightPower = forward - turn;

            double maxPower = Math.max(1.0, Math.max(Math.abs(leftPower), Math.abs(rightPower)));

            leftPower /= maxPower;
            rightPower /= maxPower;

            frontLeft.setPower(leftPower);
            backLeft.setPower(leftPower);
            frontRight.setPower(rightPower);
            backRight.setPower(rightPower);

            telemetry.addData("Target ID",
                    target.getFiducialId());
            telemetry.addData("Horizontal angle",
                    "%.2f degrees", tx);
            telemetry.addData("Target area",
                    "%.2f", area);
            telemetry.addData("Turn power",
                    "%.3f", turn);
            telemetry.addData("Forward power",
                    "%.3f", forward);
            telemetry.addData("Status",
                    area >= STOP_AREA
                            ? "Target reached"
                            : "Tracking");

            telemetry.update();

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