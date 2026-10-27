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

# Build a profile APK by default; modes: profile, debug, release (requires signing).
apk mode="profile":
    flutter build apk --"$1"
