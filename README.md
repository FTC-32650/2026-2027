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
| 🧲 Intake | ✅ Working | Runs while **Square** is held |
| 👁️ Limelight vision | 🧪 Testing | Pollen tracking (pipeline 1) and AprilTags (pipeline 2) |
| 🗺️ Pedro Pathing | 🔧 Tuning | Pinpoint odometry + Foresight follower; tuned values saved |
| 🎯 Turret | ⏳ Not started | |
| 🤖 Autonomous routines | ⏳ Not started | Only vision test OpModes so far |

---

## 🎮 OpModes

These show up on the Driver Station.

### TeleOp
| Name | File | What it does |
|---|---|---|
| **Mecanum Drive Robot Centric** | `MecanumDrive.java` | Main driving code. Left stick drives/strafes, right stick turns, **Square** runs the intake. |

### Autonomous (tests)
| Name | File | What it does |
|---|---|---|
| **PollenTrackingTest** | `PollenTrackingTest.java` | Finds a game piece with the Limelight, drives toward it, and runs the intake once it's close. |
| **AprilTagTrackerBlue** | `AprilTagTrackerBlue.java` | Finds the largest AprilTag and steers toward it — start of blue-alliance scoring logic. |
| **LimelightTest** | `LimelightTest.java` | Just prints what the Limelight sees (TX, TY, area) to telemetry. Good for checking the camera. |

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
| Limelight 3A | `limelight` | — |
| goBILDA Pinpoint | `pinpoint` | — |

**Limelight pipelines:** `1` = pollen / game piece · `2` = AprilTags

---

## 📁 Where the code lives

```
TeamCode/src/main/java/org/firstinspires/ftc/teamcode/
├── MecanumDrive.java          ← main TeleOp
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

---

## ⚠️ Known issues

- **Pedro Pathing isn't listed in the Gradle dependencies yet**, so the `pedro/` code won't compile until it's added to `build.dependencies.gradle`.
- **AprilTagTrackerBlue doesn't turn correctly** — both sides get the same power, and forward power is always on, so it never stops at the tag.

---

## ✅ To do

- [ ] Add Pedro Pathing to Gradle
- [ ] Fix AprilTag tracking
- [ ] Build the turret
- [ ] Write real autonomous routines
- [ ] Field-centric drive option

---

<sub>Built on the [FTC Robot Controller SDK](https://github.com/FIRST-Tech-Challenge/FtcRobotController) v12.0.</sub>
