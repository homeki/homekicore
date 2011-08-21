#! /bin/sh

DAEMON="/usr/bin/java"
DAEMON_ARGS="-jar /opt/homekey/homekeycore.jar"
STDOUT_FILE="/opt/homekey/stdout.log"
STDERR_FILE="/opt/homekey/stderr.log"

exec $DAEMON $DAEMON_ARGS 1>>$STDOUT_FILE 2>>$STDERR_FILE
