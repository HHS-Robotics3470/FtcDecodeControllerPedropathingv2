package org.firstinspires.ftc.teamcode.pedroPathing;

import com.pedropathing.control.FilteredPIDFCoefficients;
import com.pedropathing.control.PIDFCoefficients;
import com.pedropathing.follower.Follower;
import com.pedropathing.follower.FollowerConstants;
import com.pedropathing.ftc.FollowerBuilder;
import com.pedropathing.ftc.drivetrains.MecanumConstants;
import com.pedropathing.ftc.localization.constants.PinpointConstants;
import com.pedropathing.paths.PathConstraints;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class Constants {

    // ================= FOLLOWER =================
    public static FollowerConstants followerConstants = new FollowerConstants()
            .mass(9.7)

            // CRITICAL: friction model (must be small negative)
            .forwardZeroPowerAcceleration(-5.0)
            .lateralZeroPowerAcceleration(-7.0)

            // MAIN translation controller (this moves the robot)
            .translationalPIDFCoefficients(
                    new PIDFCoefficients(1.2, 0, 0, 0)
            )

            // Heading hold
            .headingPIDFCoefficients(
                    new PIDFCoefficients(1.0, 0, 0, 0.05)
            )

            // Wheel velocity correction
            .drivePIDFCoefficients(
                    new FilteredPIDFCoefficients(0.2, 0, 0, 0, 0.04)
            );

    // ================= DRIVETRAIN =================
    public static MecanumConstants driveConstants = new MecanumConstants()
            .maxPower(1.0)

            .rightFrontMotorName("fRight")
            .rightRearMotorName("bRight")
            .leftRearMotorName("bLeft")
            .leftFrontMotorName("fLeft")

            .leftFrontMotorDirection(DcMotorSimple.Direction.FORWARD)
            .leftRearMotorDirection(DcMotorSimple.Direction.REVERSE)
            .rightFrontMotorDirection(DcMotorSimple.Direction.REVERSE)
            .rightRearMotorDirection(DcMotorSimple.Direction.FORWARD)

            // USE REALISTIC VELOCITIES
            .xVelocity(40)   // inches/sec
            .yVelocity(40);  // inches/sec

    // ================= LOCALIZER =================
    public static PinpointConstants localizerConstants = new PinpointConstants()
            .forwardPodY(-6.25)
            .strafePodX(-6.5)
            .distanceUnit(DistanceUnit.INCH)
            .hardwareMapName("pinpoint")
            .encoderResolution(
                    GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD
            )
            .forwardEncoderDirection(
                    GoBildaPinpointDriver.EncoderDirection.REVERSED
            )
            .strafeEncoderDirection(
                    GoBildaPinpointDriver.EncoderDirection.REVERSED
            );

    // ================= PATH CONSTRAINTS =================
    public static PathConstraints pathConstraints =
            new PathConstraints(
                    0.8,   // max power
                    50,    // max velocity (in/s)
                    30,    // max accel
                    30     // max decel
            );

    // ================= FOLLOWER CREATION =================
    public static Follower createFollower(HardwareMap hardwareMap) {
        return new FollowerBuilder(followerConstants, hardwareMap)
                .pathConstraints(pathConstraints)
                .mecanumDrivetrain(driveConstants)
                .pinpointLocalizer(localizerConstants)
                .build();
    }
}
