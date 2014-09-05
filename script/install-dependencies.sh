#!/bin/bash

# This script installs dependencies used by CircleCI.

DEPS="$ANDROID_HOME/installed-dependencies"

if [ ! -e $DEPS ]; then
  ./gradlew tasks && # trigger gradle download
  touch $DEPS
fi
