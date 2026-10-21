set positional-arguments

default:
    @just --list

# Fetch app and vendored-plugin dependencies, then regenerate code.
setup:
    flutter pub get --enforce-lockfile --no-example
    (cd packages/wakelock_plus && flutter pub get --enforce-lockfile --no-example)
    scripts/codegen.sh

# Run the full suite, or pass test paths and Flutter test options.
test *args:
    scripts/run_tests.sh "$@"

# Build a debug-signed APK for sideloading (build/app/outputs/flutter-apk/app-debug.apk).
apk:
    flutter build apk --debug
