# 🐝 BioBuzz — FTC Team #32650

**Robot code for the 2026–2027 FIRST Tech Challenge season.**

![FTC SDK](https://img.shields.io/badge/FTC%20SDK-12.0-orange)
![Language](https://img.shields.io/badge/language-Java-blue)
![Drivetrain](https://img.shields.io/badge/drivetrain-mecanum-green)
![Vision](https://img.shields.io/badge/vision-Limelight%203A-purple)

---

## 📍 Where we are

| Subsystem | Status | Notes |
|---|---|---|
| 🚗 Mecanum drive | ✅ Working | Robot-centric TeleOp on gamepad 1 |
| 🧲 Intake | ✅ Working | **Square** toggles it on and off |
| 🚀 Launcher | 🔧 In progress | Motor is set up, but no button runs it in TeleOp right now |
| 🧭 Flywheel position | 🧪 Testing | Magnet sensor test OpMode (`Flywheel Position Test`) |
| 🎯 Auto-aim | 🔧 In progress | **D-pad down** calls the Limelight aligner; it reads the target but doesn't move anything yet |
| 👁️ Limelight vision | 🧪 Testing | Pollen tracking (pipeline 1) and AprilTags (pipeline 2) |
| 🗺️ Pedro Pathing | 🔧 Tuning | Pinpoint odometry + Foresight follower; in Gradle and compiling, tuned values saved |
| 🎯 Turret | ⏳ Not started | |
| 🤖 Autonomous routines | ⏳ Not started | Only vision test OpModes so far |

---

## 🎮 OpModes

These show up on the Driver Station.

### TeleOp
| Name | File | What it does |
|---|---|---|
| **Mecanum Drive Robot Centric** | `MecanumDrive.java` | Main driving code. See the controls below. |
| **Flywheel Position Test** | `FlywheelPosition.java` | Shows on telemetry whether the magnet (hall-effect) sensor `mag_switch` sees the magnet. |

**Controls (gamepad 1)**

| Input | Does |
|---|---|
| Left stick | Drive forward/back and strafe |
| Right stick (X) | Turn |
| **Square** | Toggle the intake on/off |
| **D-pad down** | Auto-aim with the Limelight (work in progress, see below) |

### Autonomous (tests)
| Name | File | What it does |
|---|---|---|
| **PollenTrackingTest** | `PollenTrackingTest.java` | Finds a game piece with the Limelight, drives toward it, and runs the intake once it's close. |
| **AprilTagTrackerBlue** | `AprilTagTrackerBlue.java` | Turns to center the largest AprilTag, then drives toward it and stops once it's close. Start of blue-alliance scoring logic. |
| **LimelightTest** | `LimelightTest.java` | Just prints what the Limelight sees (TX, TY, area) to telemetry. Good for checking the camera. |

### Helper classes (not OpModes)
| Class | File | What it does |
|---|---|---|
| **AprilTagAlignerBlue** | `AprilTagAlignerBlue.java` | Limelight auto-aim helper used by the TeleOp. Starts the Limelight on the AprilTag pipeline; `align()` is still a stub. Not the same thing as `AprilTagTrackerBlue`. |

### Pedro Pathing tuners
Found in `pedro/Tuning.java`: **Mecanum Tuner**, **Pinpoint Tuner**, **Foresight Tuner**, and **Tests** (hold, line, and curve tests).

---

## 🔌 Hardware config names

The names in the Robot Controller config **must match these exactly**.

| Device | Config name | Port |
|---|---|---|
| Front left motor | `left_front` | 0 |
| Back left motor | `left_back` | 1 |
| Front right motor | `right_front` | 2 |
| Back right motor | `right_back` | 3 |
| Intake motor | `intake` | — |
| Launcher motor | `launcher` | — |
| Limelight 3A | `limelight` | — |
| goBILDA Pinpoint | `pinpoint` | — |
| Flywheel magnet sensor (digital) | `mag_switch` | — |

**Limelight pipelines:** `1` = pollen / game piece · `2` = AprilTags

---

## 📁 Where the code lives

```
TeamCode/src/main/java/org/firstinspires/ftc/teamcode/
├── MecanumDrive.java          ← main TeleOp
├── AprilTagAlignerBlue.java   ← auto-aim helper used by the TeleOp
├── FlywheelPosition.java      ← flywheel magnet sensor test
├── PollenTrackingTest.java    ← vision: drive to game piece
├── AprilTagTrackerBlue.java   ← vision: drive to AprilTag
├── LimelightTest.java         ← vision: camera check
└── pedro/
    ├── Constants.java         ← drive, odometry & follower settings
    ├── Tuning.java            ← list of tuner OpModes
    └── procedures/            ← tuner code
```

Everything outside `TeamCode/` is the official FTC SDK — don't edit it.

---

## 🚀 Getting started

1. Install [Android Studio](https://developer.android.com/studio).
2. Clone this repo and open the folder in Android Studio.
3. Wait for Gradle to sync.
4. Plug in the Control Hub (or connect over Wi-Fi) and press **Run ▶**.

Building from the command line (needs the Android SDK and `ANDROID_HOME` set):

```bash
echo "sdk.dir=$ANDROID_HOME" > local.properties     # only if local.properties is missing
./gradlew --no-daemon -q --max-workers=1 :TeamCode:compileDebugJavaWithJavac   # quick compile check
./gradlew --no-daemon -q --max-workers=1 assembleDebug                         # full Robot Controller APK
```

If the first build fails with HTTP 429 from Maven Central, just run it again.

---

## 🤖 Working with Claude Code

`CLAUDE.md` has the build steps, config names and coding rules for the repo. `.claude/agents/` adds four helpers: **ftc-reviewer** (reviews code changes), **repo-explorer** (finds where things are), **doc-writer** (docs) and **test-writer** (tests for logic that doesn't need hardware).

---

## ⚠️ Known issues

- **The launcher has no button in TeleOp.** The **Cross** binding was removed when the intake became a toggle, so the `launcher` motor is set up but never runs.
- **Auto-aim doesn't move yet.** `AprilTagAlignerBlue.align()` reads the Limelight's TX and stops there.
- **AprilTagAlignerBlue gets its hardware from the Blocks `hardwareMap`** (`BlocksOpModeCompanion`) instead of the TeleOp's own. That's probably empty in a Java OpMode, which would crash the TeleOp during init. Needs testing on the robot.
- **AprilTagTrackerBlue follows any AprilTag**, red-alliance ones included, not just blue.

---

## ✅ To do

- [x] Add Pedro Pathing to Gradle
- [x] Fix AprilTag tracking (turning and stopping)
- [ ] Give the launcher a button again
- [ ] Finish aiming: Limelight auto-align (D-pad down) or manual aim with L1/R1
- [ ] Only track blue-alliance AprilTags
- [ ] Build the turret
- [ ] Write real autonomous routines
- [ ] Field-centric drive option

---

<sub>Built on the [FTC Robot Controller SDK](https://github.com/FIRST-Tech-Challenge/FtcRobotController) v12.0.</sub>
