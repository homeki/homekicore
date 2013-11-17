#!/usr/bin/env bash

vagrant ssh -- -t 'java -agentlib:jdwp=transport=dt_socket,server=y,suspend=y,address=5050 -jar -Djava.util.logging.config.file=/host/logging-dev.properties -Djna.library.path=/opt/telldus/lib /host/build/libs/homekicore-*.jar'
