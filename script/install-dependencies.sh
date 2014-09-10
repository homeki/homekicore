#!/bin/bash

# This script installs dependencies used by CircleCI.

DEPS="$BUILD_META_HOME/installed-dependencies"

if [ ! -e $DEPS ]; then
  mkdir $BUILD_META_HOME &&
  ./gradlew tasks &&
  pushd script > /dev/null && bundle install --path="$BUILD_META_HOME/bundle" && popd > /dev/null &&
  touch $DEPS
fi
