#!/usr/bin/env bash
# This script compiles and runs the FancyInterpreter with whatever arguments you give
spldir=$(dirname "$(realpath "$0")")
mvn -f"$spldir" compile exec:java@splc \
  -Dorg.slf4j.simpleLogger.defaultLogLevel=warn \
  -Dexec.args="$*"
