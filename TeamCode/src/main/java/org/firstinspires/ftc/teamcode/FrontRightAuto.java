package org.firstinspires.ftc.teamcode;

import com.bylazar.configurables.annotations.Configurable;
import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.telemetry.TelemetryManager;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.subSystems.*;

@Autonomous(name = "Shoot 3 Front Move First", group = "Autonomous")
@Configurable
public class FrontRightAuto extends OpMode {

    private TelemetryManager panelsTelemetry;

    private Outtake shooter;
    private Spindexer spindexer;
    private Mecnum drive;

    private int state = 0;      // state machine
    private ElapsedTime timer = new ElapsedTime();

    @Override
    public void init() {

        shooter = new Outtake();
        shooter.init(hardwareMap);

        spindexer = new Spindexer();
        spindexer.init(hardwareMap);

        drive = new Mecnum();
        drive.init(hardwareMap);

        panelsTelemetry = PanelsTelemetry.INSTANCE.getTelemetry();
        panelsTelemetry.debug("Status", "Initialized");
        panelsTelemetry.update(telemetry);
    }

    @Override
    public void loop() {

        switch (state) {

            // ===== MOVE FORWARD FIRST =====
            case 0:
                timer.reset();
                state = 1;
                break;

            case 1:
                // move forward at 0.5 power for 0.5 seconds
                if (timer.seconds() < 0.5) {
                    drive.setDrivePower(-0.5, -0.5, -0.5, -0.5);
                } else {
                    drive.stopAllMotors();
                    timer.reset();
                    state = 2; // next: spin up flywheel
                }
                break;

            // ===== SPIN UP FLYWHEEL =====
            case 2:
                shooter.shooterOnshort();
                shooter.Hooddown();
                timer.reset();
                state = 3;
                break;

            // ===== WAIT TO SPIN UP =====
            case 3:
                if (timer.seconds() > 2.0) { // wait 2 seconds for flywheel to reach speed
                    state = 4;
                }
                break;

            // ===== SHOT 1 =====
            case 4:
                spindexer.moveHoldToOuttake(1);
                timer.reset();
                state = 5;
                break;

            case 5:
                if (timer.seconds() > 0.5) {
                    shooter.shooterArmUp();
                    timer.reset();
                    state = 6;
                }
                break;

            case 6:
                if (timer.seconds() > 0.5) {
                    shooter.shooterArmDown();
                    timer.reset();
                    state = 7;
                }
                break;

            // ===== SHOT 2 =====
            case 7:
                spindexer.moveHoldToOuttake(2);
                timer.reset();
                state = 8;
                break;

            case 8:
                if (timer.seconds() > 0.5) {
                    shooter.shooterArmUp();
                    timer.reset();
                    state = 9;
                }
                break;

            case 9:
                if (timer.seconds() > 0.5) {
                    shooter.shooterArmDown();
                    timer.reset();
                    state = 10;
                }
                break;

            // ===== SHOT 3 =====
            case 10:
                spindexer.moveHoldToOuttake(3);
                timer.reset();
                state = 11;
                break;

            case 11:
                if (timer.seconds() > 0.5) {
                    shooter.shooterArmUp();
                    timer.reset();
                    state = 12;
                }
                break;

            case 12:
                if (timer.seconds() > 0.5) {
                    shooter.shooterArmDown();
                    shooter.stop();
                    spindexer.moveToHold(1);
                    state = 13;  // done
                }
                break;

            case 13:
                // finished, stop all motors just in case
                drive.stopAllMotors();
                break;
        }

        panelsTelemetry.debug("State", state);
        panelsTelemetry.update(telemetry);
    }
}
