#!/usr/bin/env bash

# This script runs a specified JUnit test class given on the command line.
# If no class name is given, then it runs all tests.
# The maven output is cleaned-up considerably. If you want it even shorter,
# redirect stderr to /dev/null like
#   ./runtest.sh 2>/dev/null

spldir=$(dirname "$(realpath "$0")")
set -euo pipefail
tests=''
if (( $# > 0 )); then
  tests="-Dtest=$1"
  shift
  while (( $# > 0 )); do
    tests+=",$1"
    shift
  done
fi
mvn compile test-compile -Dorg.slf4j.simpleLogger.defaultLogLevel=warn
mvn test \
  -Dorg.slf4j.simpleLogger.defaultLogLevel=error \
  $tests \
  3>&1 4>&2 1>&4 2>&3
