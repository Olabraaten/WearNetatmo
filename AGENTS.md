# Repository Guidelines

## Project Structure & Module Organization
- Single Android app module in `app/`.
- Kotlin sources live in `app/src/main/java/solutions/silly/wearnetatmo/` with feature areas like `api/`, `model/`, `repository/`, `presentation/`, and `complication/`.
- UI themes and Compose setup are in `app/src/main/java/solutions/silly/wearnetatmo/presentation/theme/`.
- Android resources (drawables, strings, icons) are in `app/src/main/res/`.
- Build outputs land in `app/build/`.

## Build, Test, and Development Commands
- `./gradlew assembleDebug` builds a debug APK.
- `./gradlew installDebug` installs the debug build on a connected device/emulator.
- `./gradlew assembleRelease` builds a release APK (minify is currently off).
- `./gradlew clean` removes build outputs.

## Coding Style & Naming Conventions
- Kotlin with 4-space indentation; follow Android Studio/Kotlin defaults.
- Use PascalCase for classes and objects (e.g., `NetatmoRepository`), camelCase for functions/vars.
- Constants are upper snake case (e.g., `NETATMO_CLIENT_ID`) and typically live in `Constants.kt` or a dedicated object.
- Compose UI lives under `presentation/`; keep UI state in view models (`ConfigViewModel`) and UI in activities/composables.

## Testing Guidelines
- No tests are currently present. Add unit tests under `app/src/test/` and instrumented tests under `app/src/androidTest/`.
- Common commands:
  - `./gradlew testDebugUnitTest` for JVM unit tests.
  - `./gradlew connectedDebugAndroidTest` for device/emulator tests.

## Commit & Pull Request Guidelines
- Recent commits use short, sentence-style summaries (e.g., “Improve error handling.”). Keep messages concise and descriptive.
- PRs should include a clear description, testing performed, and screenshots or screen recordings for UI changes.

## Security & Configuration Notes
- Avoid committing secrets. If you add new credentials, prefer `local.properties` or Gradle properties and document expected keys.
- Netatmo configuration is referenced in `app/src/main/java/solutions/silly/wearnetatmo/SecretConstants.kt`; treat updates carefully.
