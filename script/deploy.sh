#!/bin/bash

set -e

read -p "Did you remember to change Configuration.java for release? (y/N)"
if [[ $REPLY != "y" && $REPLY != "Y" ]]; then
	exit
fi

pushd `dirname "$0"`> /dev/null

pushd ../ant > /dev/null
ant all
popd > /dev/null
pushd ../build/dist > /dev/null
scp * apt@server.homeki.com:~/deb/unstable

popd > /dev/null
