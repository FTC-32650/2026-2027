package org.firstinspires.ftc.teamcode.pedro;

import com.pedropathing.algorithm.Foresight;
import com.pedropathing.algorithm.ForesightConfig;
import com.pedropathing.controllers.Controller;
import com.pedropathing.follower.Follower;
import com.pedropathing.math.Matrix;
import com.pedropathing.math.Vector2D;
import com.pedropathing.revhub.drivetrains.Mecanum;
import com.pedropathing.revhub.drivetrains.MecanumConfig;
import com.pedropathing.revhub.localizers.PinpointConfig;
import com.pedropathing.revhub.localizers.PinpointLocalizer;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class Constants {
    public static Follower create(HardwareMap h) {
        return new Follower(
                new PinpointLocalizer(h, localizerConfig),
                new Mecanum(h, driveConfig),
                new Foresight(foresightConfig)
        );
    }


    public static MecanumConfig driveConfig = new MecanumConfig(
            c -> {
                c.frontLeftName.set("left_front");
                c.backLeftName.set("left_back");
                c.frontRightName.set("right_front");
                c.backRightName.set("right_back");

                c.frontLeftDirection.set(DcMotorSimple.Direction.FORWARD);
                c.backLeftDirection.set(DcMotorSimple.Direction.FORWARD);
                c.frontRightDirection.set(DcMotorSimple.Direction.REVERSE);
                c.backRightDirection.set(DcMotorSimple.Direction.REVERSE);
            }
    );

    public static PinpointConfig localizerConfig = new PinpointConfig(c -> {
        c.name.set("pinpoint");
        c.podType.set(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD);
        c.xPodOffset.set(-3.306000176377184);
        c.yPodOffset.set(-6.456005667138288);
        c.xPodDirection.set(GoBildaPinpointDriver.EncoderDirection.FORWARD);
        c.yPodDirection.set(GoBildaPinpointDriver.EncoderDirection.FORWARD);
        c.globalDistanceUnit.set(DistanceUnit.INCH);
        c.offsetUnits.set(DistanceUnit.INCH);
    });

    public static ForesightConfig foresightConfig = new ForesightConfig(
            c -> {
                Controller primaryTranslationalForward = Controller.proportional(0.14048633857331816);
                Controller secondaryTranslationalForward = Controller.proportional(0.05190594823873848);
                Controller primaryTranslationalLateral = Controller.proportional(0.19520751536552128);
                Controller secondaryTranslationalLateral = Controller.proportional(0.07212396088668432);

                c.forwardTranslational.set(Controller.piecewise(secondaryTranslationalForward).put(2.5, primaryTranslationalForward));
                c.strafeTranslational.set(Controller.piecewise(secondaryTranslationalLateral).put(2.5, primaryTranslationalLateral));

                c.coast.set(Controller.proportionalFeedforward(0.01524828271172081));
                c.brake.set(Controller.proportionalFeedforward(0.012961040304962687));

                c.headingFeedback.set(Controller.proportional(2.186438643013376));
                c.headingBrakeCoefficients.set(Vector2D.cartesian(0.04700667966587898, 0.004316159493590085));

                c.linearBrakeCoefficients.set(Matrix.diag(1.072415863005756, 0.05986328281655439));
                c.quadraticBrakeCoefficients.set(Matrix.diag(-0.017904022583162926, 0.0012725960786668647));

                c.maxAchievableForwardVelocity.set(69.2064077404729);
                c.maxAchievableStrafeVelocity.set(60.00268480703782);
                c.naturalForwardDeceleration.set(35.07562724637436);
                c.naturalStrafeDeceleration.set(55.075509001568165);
            }



    );




}
