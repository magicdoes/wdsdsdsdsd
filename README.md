# MagicSMP rebuildable patch project

This repository uses the current working MagicSMP JAR as the baseline and builds **one MagicSMP.jar** with the sell-GUI protection integrated into the main plugin at build time.

## What the fix does
- Protects `/sell` display/category/multiplier slots 45-53.
- Blocks direct pickup, number-key/hotbar swaps, double-click collection and dragging involving the protected row.
- Leaves sell input slots 0-44 usable.
- Does not require a second plugin JAR.
- Keeps the existing KeyAll webhook and all features already present in the baseline JAR.

## Build on GitHub
Upload the whole project to a repository and push. Open **Actions > Build MagicSMP**, then download the `MagicSMP` artifact. The artifact contains `MagicSMP.jar`.

## Build locally
Requires Java 21 and Maven:

```bash
bash build.sh
```

Output: `build/MagicSMP.jar`

## Important
This is the safe first reconstruction step: the current production JAR remains the behavioral baseline, while new fixes live as source and are injected during the build. It avoids pretending that decompiled code is identical to the original source. More classes can be migrated into clean source incrementally later.
