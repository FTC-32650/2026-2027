package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;

@TeleOp(name="LimelightTest", group="Sensor")
public class LimelightTest extends LinearOpMode {
   private Limelight3A limelight;

   @Override
    public void runOpMode() {
       // make sure to put this deviceName into the tablet driver hub thingy
       limelight = hardwareMap.get(Limelight3A.class, "limelight");

       limelight.setPollRateHz(100);

       telemetry.setMsTransmissionInterval(11);

       limelight.pipelineSwitch(0);

       limelight.start();

       telemetry.addData("Status", "Limelight 3A Initialized");
       telemetry.update();

       waitForStart();

       while (opModeIsActive()) {
           LLResult result = limelight.getLatestResult();

           if (result != null && result.isValid()) {
               telemetry.addData("Target Found:", "YES");
               telemetry.addData("TX (Horizontal)", result.getTx());
               telemetry.addData("TY (Vertical)", result.getTy());
               telemetry.addData("TA (Target Area)", result.getTa());
           } else {
               telemetry.addData("Target Found:", "NO");
           }
           telemetry.update();
       }
   }
}
