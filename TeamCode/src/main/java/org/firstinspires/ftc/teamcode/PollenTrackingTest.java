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

        boolean tracking = false;

        while (opModeIsActive()) {
            if (gamepad1.a) tracking = true;
            if (gamepad1.b) tracking = false;

            LLResult result = limelight3A.getLatestResult();

            boolean found = (result != null && result.isValid());

            double tx = found ? result.getTx() : 0.0;
            double turn = 0.0;

            if (tracking && found) {
                double deadband = 1.5;

                if (Math.abs(tx) > deadband) {
                    double kP = 0.018;
                    turn = kP * tx;

                    turn = Math.max(-0.25, Math.min(0.25, turn));
                }
            }
            if (tracking && found) {
                setDrive(-turn, turn, -turn, turn);
            }   else {
                    stopDrive();
                }

            telemetry.addData("Tracking enabled: ", tracking);
            telemetry.addData("Target found: ", found);
            telemetry.addData("Horizontal Offset:", "%.2f", tx);
            telemetry.addData("Turn command: ", "%.3f", turn);
            telemetry.update();

            sleep(20);
            }
        stopDrive();
        }

        private void setDrive(double fl, double fr, double bl, double br) {
        frontLeft.setPower(fl);
        frontRight.setPower(fr);
        backLeft.setPower(bl);
        backRight.setPower(br);
        }

        private void stopDrive() {
        setDrive(0, 0,0, 0);
        }
    }


