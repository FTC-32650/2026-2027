# FTC Team #32650 (BioBuzz) — 2026–2027 robot code

FTC SDK 12.0, Java, mecanum drivetrain, Limelight 3A vision, Pedro Pathing
(Pinpoint odometry + Foresight follower). Team code lives in
`TeamCode/src/main/java/org/firstinspires/ftc/teamcode/`. Don't edit
`FtcRobotController/` or the top-level Gradle files unless asked — those come
from the FTC SDK.

## Building

The Android SDK must be installed and `ANDROID_HOME` set (the cloud environment's
setup script does this). If `local.properties` is missing, create it:

```bash
echo "sdk.dir=$ANDROID_HOME" > local.properties
```

Fast compile check (use this after every change):

```bash
./gradlew --no-daemon -q --max-workers=1 :TeamCode:compileDebugJavaWithJavac
```

Full Robot Controller APK:

```bash
./gradlew --no-daemon -q --max-workers=1 assembleDebug
```

- `--max-workers=1` avoids HTTP 429 "Too Many Requests" from Maven Central on the
  first (uncached) build. If a build fails with 429, just re-run it.
- The "source/target value 8 is obsolete" warnings are expected.
- Code can only be compiled here, not run. Anything involving motors, sensors or
  the Limelight has to be tested on the robot — say so when reporting changes.

## Dependencies

Declared in `build.dependencies.gradle`. Pedro Pathing is
`com.pedropathing:revhub` + `com.pedropathing:tuning` (Maven Central). The
`teamcode/pedro/procedures/` tuners are copied from the official Pedro
Quickstart (github.com/Pedro-Pathing/Quickstart); prefer re-syncing from there
over hand-editing them.

`gradle/wrapper/gradle-wrapper.jar` must stay committed (`.gitignore` has an
exception for it), otherwise `./gradlew` breaks on fresh clones.

## Hardware config names

Must match the Robot Controller configuration exactly:

| Device | Name |
|---|---|
| Front left / back left motor | `left_front` / `left_back` |
| Front right / back right motor | `right_front` / `right_back` |
| Intake motor | `intake` |
| Limelight 3A | `limelight` |
| goBILDA Pinpoint | `pinpoint` |

Limelight pipelines: `1` = pollen / game piece, `2` = AprilTags.

## Known issues

- Motor directions: the **right** motors are reversed everywhere (`MecanumDrive`,
  the vision OpModes and `pedro/Constants.java`). Confirmed on the robot —
  reversing the left side instead made `PollenTrackingTest` drive backwards.

## Conventions

- New OpModes: `LinearOpMode`, hardware names from the table above, start the
  Limelight during init (before `waitForStart()`), and stop motors/camera at the end.
- Don't use Java `assert` — assertions are disabled on Android. Use explicit null checks.
- Keep the README's status table and OpMode list in sync when adding or removing OpModes.
- Work on a branch and open a PR; don't push directly to `main`.
