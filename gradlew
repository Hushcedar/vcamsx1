#!/usr/bin/env sh
# Self-bootstrapping Gradle wrapper — downloads Gradle on first run, no jar needed
set -e

GRADLE_VERSION="8.0"
GRADLE_HOME="$HOME/.gradle/wrapper/dists/gradle-${GRADLE_VERSION}-bin"
GRADLE_BIN="$GRADLE_HOME/gradle-${GRADLE_VERSION}/bin/gradle"

if [ ! -f "$GRADLE_BIN" ]; then
  echo "[gradlew] Downloading Gradle ${GRADLE_VERSION}..."
  mkdir -p "$GRADLE_HOME"
  curl -L "https://services.gradle.org/distributions/gradle-${GRADLE_VERSION}-bin.zip" \
       -o /tmp/gradle.zip
  unzip -q /tmp/gradle.zip -d "$GRADLE_HOME"
  rm -f /tmp/gradle.zip
  echo "[gradlew] Gradle installed at $GRADLE_BIN"
fi

if [ -n "$JAVA_HOME" ]; then
  JAVACMD="$JAVA_HOME/bin/java"
elif command -v java >/dev/null 2>&1; then
  JAVACMD="java"
else
  echo "[gradlew] ERROR: Java not found. Run: pkg install openjdk-17"
  exit 1
fi

exec "$GRADLE_BIN" "$@"
