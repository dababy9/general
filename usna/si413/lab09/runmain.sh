#!/usr/bin/env bash

# This script compiles and runs whatever Main class you give.
# For example, './runmain.sh FancyInterpreter' is the same as doing './spli.sh'

spldir=$(dirname "$(realpath "$0")")
main=$1
shift
mvn -f"$spldir" compile exec:java \
  -Dorg.slf4j.simpleLogger.defaultLogLevel=warn \
  -Dexec.mainClass="si413.spl.$main" \
  -Dexec.args="$@"
