#!/usr/bin/env bash
set -euo pipefail
if (( $# > 0 )); then
  tests="-Dtest=$1"
else
  tests=''
fi
mvn compile test-compile -Dorg.slf4j.simpleLogger.defaultLogLevel=warn
mvn test \
  -Dorg.slf4j.simpleLogger.defaultLogLevel=error \
  $tests \
  3>&1 4>&2 1>&4 2>&3
