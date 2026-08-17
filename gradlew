#!/bin/sh
GRADLE_OPTS="${GRADLE_OPTS:-}"
APP_BASE_NAME="gradlew"
APP_HOME="$(cd "$(dirname "$0")" && pwd)"
CLASSPATH="$APP_HOME/gradle/wrapper/gradle-wrapper.jar"
exec "$JAVACMD" "${JVM_OPTS[@]}" -classpath "$CLASSPATH" \
    org.gradle.wrapper.GradleWrapperMain "$@"
