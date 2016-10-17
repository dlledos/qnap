#!/bin/sh


BIN="/home/dlledos/src/qnap/bin/tvshow-manage.sh"
TEST_DIR=$(cd $(dirname $0) ; pwd)
RESOURCES_DIR="$TEST_DIR/resources/$(basename $0)"

echo $BIN
echo $RESOURCES_DIR
