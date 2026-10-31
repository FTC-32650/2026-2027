package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous
public class PollenTrackingTest extends LinearOpMode {
    private Limelight3A limelight3A;
    private DcMotor frontLeft, frontRight;
    private DcMotor backLeft, backRight;
    private DcMotor intake;

    private static final double TURN_KP = 0.018;
    private static final double MAX_TURN = 0.25;
    private static final double FORWARD_POWER = 0.18;
    private static final double DEAD_BAND = 1.5;
    private static final double STOP_AREA = 4.0;

    // Once the piece is close, keep the intake running this long even if the camera
    // loses sight of it (it usually drops out of view right as it gets pulled in)
    private static final double INTAKE_HOLD_SECONDS = 1.0;

    private final ElapsedTime intakeTimer = new ElapsedTime();
    private boolean intaking = false;


    @Override
    public void runOpMode() {
        frontLeft = hardwareMap.get(DcMotor.class, "left_front");
        frontRight = hardwareMap.get(DcMotor.class, "right_front");
        backLeft = hardwareMap.get(DcMotor.class, "left_back");
        backRight = hardwareMap.get(DcMotor.class, "right_back");
        intake = hardwareMap.get(DcMotor.class, "intake"); // remember deviceNames

        limelight3A = hardwareMap.get(Limelight3A.class, "limelight");

        // Right side is reversed (confirmed on the robot; matches pedro/Constants.java)
        frontRight.setDirection(DcMotor.Direction.REVERSE);
        backRight.setDirection(DcMotor.Direction.REVERSE);

        stopDrive();

        // Start the camera during init so it's already streaming results at START
        limelight3A.pipelineSwitch(1);
        limelight3A.start();

        telemetry.addLine("BioBuzz Pollen Tracker");
        telemetry.update();

        waitForStart();

        if (isStopRequested()) {
            limelight3A.stop();
            return;
        }

        while (opModeIsActive()) {

            // Finish an intake cycle before looking for the next target
            if (intaking) {
                if (intakeTimer.seconds() < INTAKE_HOLD_SECONDS) {
                    stopDrive();
                    intake.setPower(-1.0);

                    telemetry.addLine("Intaking...");
                    telemetry.addData("Time left: ", "%.2f s",
                            INTAKE_HOLD_SECONDS - intakeTimer.seconds());
                    telemetry.update();

                    idle();
                    continue;
                }
                intaking = false;
                intake.setPower(0);
            }

            LLResult result = limelight3A.getLatestResult();

            boolean found = result != null && result.isValid();

            if (!found) {
                stopDrive();
                intake.setPower(0);

                telemetry.addLine("No Target Detected");
                telemetry.update();

                idle();
                continue;
            }

            double tx = result.getTx();
            double ta = result.getTa();

            double turn = 0.0;
            double forward = 0.0;

            if (ta >= STOP_AREA && Math.abs(tx) <= 3.0) {
                stopDrive();
                telemetry.addLine("Target reached");
                intake.setPower(-1.0);
                intaking = true;
                intakeTimer.reset();
            } else {
                intake.setPower(0);

                if (Math.abs(tx) > DEAD_BAND) {
                    turn = TURN_KP * tx;
                    turn = Math.max(-MAX_TURN, Math.min(MAX_TURN, turn));
                }

                forward = FORWARD_POWER;

                // tx > 0 means the ball is to the right -> speed up left side to turn right
                double leftPower = forward + turn;
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
            telemetry.update();

            idle();
        }

        stopDrive();
        intake.setPower(0);
        limelight3A.stop();
    }

    private void stopDrive() {
        frontLeft.setPower(0);
        backLeft.setPower(0);
        frontRight.setPower(0);
        backRight.setPower(0);
    }
}
