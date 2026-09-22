# MagicSMP patched build

Upload **all files and folders in this project to the ROOT of the GitHub repository**.

The repository root must show:

- `.github/`
- `baseline/`
- `guard/`
- `patcher/`
- `build.sh`
- `README.md`

Do not upload only the `guard` folder.

GitHub Actions runs `build.sh`. It installs `baseline/MagicSMP-current.jar` as a local Maven dependency, compiles the sell GUI protection, and patches that class into a copy of the current MagicSMP JAR. The artifact is `build/MagicSMP.jar`.

The baseline JAR is preserved; the output is a separate file.
