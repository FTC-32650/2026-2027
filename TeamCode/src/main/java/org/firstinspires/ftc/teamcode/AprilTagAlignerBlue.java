package org.firstinspires.ftc.teamcode;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

public class AprilTagAlignerBlue {

    private LinearOpMode opMode;
    private Limelight3A limelight3A;

    private DcMotor launcherRotate = null;
    public AprilTagAlignerBlue(LinearOpMode opMode) {
        this.opMode = opMode;
        init();
    }

    public void init() {
        limelight3A = hardwareMap.get(Limelight3A.class, "limelight");
        limelight3A.pipelineSwitch(2);
        limelight3A.start();
    }

    public void align() {
        LLResult result = limelight3A.getLatestResult();
        double tx = result.getTx();
    }
}
