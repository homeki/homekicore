#!/bin/bash

DBUSER=root
DBPASS=homeki

echo "DROP DATABASE homeki" | mysql --user=$DBUSER --password=$DBPASS > /dev/null 2>&1
