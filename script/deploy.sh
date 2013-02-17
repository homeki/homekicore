#!/bin/bash

set -e

pushd `dirname "$0"`> /dev/null

pushd ../ant > /dev/null
ant all
popd > /dev/null
pushd ../build/dist > /dev/null
scp * apt@server.homeki.com:~/deb/unstable

popd > /dev/null
