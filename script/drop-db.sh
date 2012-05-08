#!/bin/bash

echo -n "dropping homeki database: "
echo "DROP DATABASE homeki" | sudo -u postgres psql > /dev/null 2>&1
echo "done"
