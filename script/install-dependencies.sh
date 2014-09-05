#!/bin/bash

# This script installs dependencies used by CircleCI.

DEPS="$BUILD_META_HOME/installed-dependencies"

if [ ! -e $DEPS ]; then
  mkdir $BUILD_META_HOME &&
  ./gradlew tasks && # trigger gradle download
  touch $DEPS
fi
