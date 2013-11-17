#!/usr/bin/env bash

vagrant ssh -- -t 'java -jar -Djava.util.logging.config.file=/host/logging-dev.properties -Djna.library.path=/opt/telldus/lib /host/build/jar/homekicore.jar'
