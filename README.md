# Melody Dash (Java)

A four-lane rhythm game written in Java using AWT/Swing and **hsa2.GraphicsConsole**. Choose **Easy / Normal / Hard**, hit the falling tiles as they cross the judgment line, and chase high combos and accuracy.

> Files in this repo: `Intro.java`, `Easy.java`, `Normal.java`, `Hard.java`, `Tile.java`.

---

## Features
- 🎵 Three difficulty modes with distinct speeds/spawn rates
- ⌨️ Responsive keyboard input mapped to lanes
- ✅ Hit window near a grading line with score/combo logic
- 🧱 Simple `Tile` entity with update/draw loop
- 🖼️ Separate Intro screen with clickable buttons to launch each mode
- 🔊 (Optional) `javax.sound.sampled` hooks for SFX/music

---

## Game Modes (overview)
| Mode   | Speed / Density | Audience |
|--------|------------------|----------|
| Easy   | Slow / Sparse    | Beginners |
| Normal | Medium           | Intermediates |
| Hard   | Fast / Dense     | Experts |

> Exact constants (tile speed, spawn interval, grading line `YLINE`) are set inside each mode class.

---

## Controls
- **Lanes:** `A S D F` (left → right)
- **Quit window:** standard window close
- **From Intro:** click a mode button to start

(If your key mapping differs in code, update this section to match your `keyPressed` handlers.)

---

## How to Run

### 1) Quick run with `javac` (recommended)
Requirements:
- **JDK 17+**
- **hsa2.GraphicsConsole** JAR (place it at `lib/hsa2.jar` or adjust the path below)

Compile:
```bash
# macOS/Linux
javac -cp lib/hsa2.jar *.java

# Windows (PowerShell / CMD)
javac -cp lib\hsa2.jar *.java
