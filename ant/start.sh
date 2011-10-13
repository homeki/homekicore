#! /bin/sh

DAEMON="/usr/bin/java"
DAEMON_ARGS="-jar /opt/homeki/homekicore.jar"
STDOUT_FILE="/opt/homeki/stdout.log"
STDERR_FILE="/opt/homeki/stderr.log"

exec $DAEMON $DAEMON_ARGS 1>>$STDOUT_FILE 2>>$STDERR_FILE
