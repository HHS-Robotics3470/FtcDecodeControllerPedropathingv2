package org.firstinspires.ftc.teamcode;

import com.bylazar.configurables.annotations.Configurable;
import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.telemetry.TelemetryManager;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.subSystems.*;

@Autonomous(name = "Shoot 3 + Park Back", group = "Autonomous")
@Configurable
public class BackRightAuto extends OpMode {

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

            // ===== SPIN UP FLYWHEEL =====
            case 0:
                shooter.shooterOn();
                shooter.Hoodup();
                timer.reset();
                state = 1;
                break;

            // ===== WAIT TO SPIN UP =====
            case 1:
                if (timer.seconds() > 7.0) {
                    state = 2;
                }
                break;

            // ===== SHOT 1 =====
            case 2:
                spindexer.moveHoldToOuttake(1);
                timer.reset();
                state = 3;
                break;

            case 3:
                if (timer.seconds() > 1) {
                    shooter.shooterArmUp();
                    timer.reset();
                    state = 4;
                }
                break;

            case 4:
                if (timer.seconds() > 0.5) {
                    shooter.shooterArmDown();
                    timer.reset();
                    state = 5;
                }
                break;

            // ===== SHOT 2 =====
            case 5:
                spindexer.moveHoldToOuttake(2);
                timer.reset();
                state = 6;
                break;

            case 6:
                if (timer.seconds() > 1) {
                    shooter.shooterArmUp();
                    timer.reset();
                    state = 7;
                }
                break;

            case 7:
                if (timer.seconds() > 0.5) {
                    shooter.shooterArmDown();
                    timer.reset();
                    state = 8;
                }
                break;

            // ===== SHOT 3 =====
            case 8:
                spindexer.moveHoldToOuttake(3);
                timer.reset();
                state = 9;
                break;

            case 9:
                if (timer.seconds() > 0.8) {
                    shooter.shooterArmUp();
                    timer.reset();
                    state = 10;
                }
                break;

            case 10:
                if (timer.seconds() > 3) {
                    shooter.shooterArmDown();
                    shooter.stop();
                    spindexer.moveToHold(1);
                    timer.reset();
                    state = 11;  // ready to move forward
                }
                break;

            // ===== MOVE FORWARD (PARK) =====
            case 11:
                if (timer.seconds() < 0.5) {  // move forward for 1 second
                    drive.setDrivePower(.6, .6, .6, .6);
                } else {
                    drive.stopAllMotors();
                    state = 12;  // done
                }
                break;

            case 12:
                // done, stop everything
                drive.stopAllMotors();
                break;
        }

        panelsTelemetry.debug("State", state);
        panelsTelemetry.update(telemetry);
    }
}
