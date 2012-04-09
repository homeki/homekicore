#!/bin/bash

DBUSER=root
DBPASS=homeki
HDBUSER=homeki
HDBPASS=homeki

echo -n "connecting to mysql: "
echo "exit" | mysql --user=$DBUSER --password=$DBPASS > /dev/null 2>&1

if [ $? -ne 0 ]; then
	echo "connection failed, manual database setup needed"
	START=no
else
	echo "done"

	echo -n "creating mysql homeki user: "
	echo "SELECT User FROM mysql.user WHERE User = 'homeki'" | mysql --user=$DBUSER --password=$DBPASS | grep homeki > /dev/null 2>&1
	if [ $? -ne 0 ]; then
		echo "CREATE USER '$HDBUSER'@'localhost' IDENTIFIED BY '$HDBPASS'" | mysql --user=$DBUSER --password=$DBPASS > /dev/null 2>&1
		echo "done"
	else
		echo "not needed, already exists"
	fi

	echo -n "creating mysql homeki database: "
	echo "SHOW DATABASES" | mysql --user=$DBUSER --password=$DBPASS | grep homeki > /dev/null 2>&1
        if [ $? -ne 0 ]; then
                echo "CREATE DATABASE homeki" | mysql --user=$DBUSER --password=$DBPASS > /dev/null 2>&1
		echo "done"
	else
		echo "not needed, already exists"
        fi

	echo "GRANT ALL ON homeki.* TO 'homeki'@'localhost'" | mysql --user=$DBUSER --password=$DBPASS > /dev/null 2>&1
fi