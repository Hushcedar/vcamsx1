#!/bin/sh
APP_HOME="$(cd "$(dirname "$0")" && pwd)"
CLASSPATH="${APP_HOME}/gradle/wrapper/gradle-wrapper.jar"
JAVACMD="${JAVA_HOME:+$JAVA_HOME/bin/}java"
exec "${JAVACMD}" -classpath "${CLASSPATH}" org.gradle.wrapper.GradleWrapperMain "$@"
