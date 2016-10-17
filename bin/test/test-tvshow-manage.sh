#!/bin/sh


BIN="/home/dlledos/src/qnap/bin/tvshow-manage.sh"
TEST_DIR=$(cd $(dirname $0) ; pwd)
RESOURCES_DIR="$TEST_DIR/resources/$(basename $0)"


function myTest() {
    TEST=$1
    FILE=$2
    EXPECTED=$3
    rm -rf "$RESOURCES_DIR/$TEST"
    
    echo "# Testing $TEST"
    FILENAME="$RESOURCES_DIR/$FILE"
    touch $FILENAME
    
    $BIN "$RESOURCES_DIR" "$RESOURCES_DIR/$TEST"
    
    echo $(checkFileExist "$RESOURCES_DIR/$TEST/$EXPECTED")
}

function checkFileExist(){
    FILE="$1"
    if [ -f "$FILE" ]
    then
        echo "OK"
    else
        echo "KO"    
    fi
}

echo "### Testing $BIN with resources $RESOURCES_DIR ###"
myTest "nominal" "machin.S01E01.truc.avi" "machin/machin.S01E01.truc.avi"
#myTest "StartWithS00E00" "S01E01.truc.avi"
