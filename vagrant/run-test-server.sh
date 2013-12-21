#!/usr/bin/env bash

if [[ $# > 0 ]]; then
	echo "Starting with debug"
	EXTRA="-agentlib:jdwp=transport=dt_socket,server=y,suspend=y,address=5050"
fi

vagrant ssh -- -t "java $EXTRA -jar -Dtest=true -Djava.util.logging.config.file=/host/logging-dev.properties -Djna.library.path=/opt/telldus/lib /host/build/libs/homekicore-*.jar"
