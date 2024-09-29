#!/usr/bin/env bash
cd "${0%/*}"
main=$1
shift
mvn compile exec:java \
  -Dorg.slf4j.simpleLogger.defaultLogLevel=warn \
  -Dexec.mainClass="si413.pat.$main" \
  -Dexec.args="$@"
