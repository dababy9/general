#!/usr/bin/env bash

##################################################
# Program to help you submit stuff for this lab. #
# Just run ./submitme.sh                         #
# If you need to specify files, use club instead #
##################################################

set -euo pipefail
shopt -s nullglob extglob globstar

srcdir=$(dirname "$(readlink -f "$0")")
labname=$(basename "$srcdir")
cd "$srcdir"
subargs=( *.md src/main/**/*.g4 src/main/**/*.java )

if [[ ${#subargs[@]} -eq 0 ]]; then
  echo "ERROR: you have nothing here to submit!"
  exit 1
fi

function find_cmd {
  if command -v "$1" 2>/dev/null; then
    return 0
  fi
  shift
  for f; do
    if [[ -x $f ]]; then
      readlink -f "$f"
      return 0
    fi
  done
  return 1
}

function get_club {
  declare -g club
  if club=$(find_cmd club ~/bin/club ~/.club/club); then
    return 0
  fi
  echo "club program not found; attempting to download it now"
  git clone "https://gitlab.usna.edu/roche/club.git" ~/.club
  mkdir -p ~/bin
  ln -s ../.club/club ~/bin/club
  club=$(find_cmd ~/bin/club)
  return 0
}

function update_club {
  echo "Attempting to update your installation of club..."
  clubdir=$(dirname "$(readlink -f "$club")")
  pushd "$clubdir"
  if ! git pull; then
    echo "ERROR: could not update club using git. This might not work!"
  fi
  popd
  return 0
}

function submit_with_club {
  echo "using $club to submit..."
  if [[ -s .clubinfo ]]; then
    classproj=''
  else
    classproj="-cSI413 -p$labname"
  fi
  "$club" --full $classproj "${subargs[@]}"
  exit 0
}

if get_club; then
  #update_club
  submit_with_club
elif submit=$(find_cmd submit ~/bin/submit); then
  echo "using $submit to submit..."
  submit -c=SI413 -p=$labname "${subargs[@]}"
else
  echo <<EOF
ERROR: no way to submit.
Go download club at https://gitlab.usna.edu/roche/club#club
EOF
  exit 1
fi

:
