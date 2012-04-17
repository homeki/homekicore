#!/bin/bash
	
echo -n "creating postgresql homeki user: "
echo "SELECT usename FROM pg_user WHERE usename = 'homeki';" | sudo -u postgres psql | grep homeki > /dev/null 2>&1
if [ $? -ne 0 ]; then
	echo "CREATE USER homeki WITH PASSWORD 'homeki';" | sudo -u postgres psql > /dev/null 2>&1
	echo "done"
else
	echo "not needed, already exists"
	fi

	echo -n "creating postgresql homeki database: "
echo "SELECT datname FROM pg_database WHERE datname = 'homeki';" | sudo -u postgres psql | grep homeki > /dev/null 2>&1
    if [ $? -ne 0 ]; then
        echo "CREATE DATABASE homeki WITH owner = homeki;" | sudo -u postgres psql > /dev/null 2>&1
	echo "done"
else
	echo "not needed, already exists"
    fi
