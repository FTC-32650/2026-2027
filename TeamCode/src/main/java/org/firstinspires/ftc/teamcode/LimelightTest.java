package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

@Autonomous
public class LimelightTest extends OpMode {
    private Limelight3A limelight3A;


    @Override
    public void init() {
        limelight3A = hardwareMap.get(Limelight3A.class, "limelight");
        limelight3A.pipelineSwitch(1); // 0 is for apriltags and 1,2 for pollen and nectar respectively
    }

    @Override
    public void start() {
        limelight3A.start();
    }

    @Override
    public void loop() {
        LLResult llResult = limelight3A.getLatestResult();
        assert llResult != null;
        if (llResult.isValid()) {
            telemetry.addData("Target Found:", "YES");
            telemetry.addData("TX (Horizontal)", llResult.getTx());
            telemetry.addData("TY (Vertical)", llResult.getTy());
            telemetry.addData("TA (Target Area)", llResult.getTa());
        }
        telemetry.update();

    }
}



