#!/bin/bash


BIN="/share/CACHEDEV1_DATA/bin/tvshow-manage.sh"
ROOT_DIR=$(cd $(dirname $0) ; pwd)
RESOURCES_DIR="$ROOT_DIR/resources/$(basename $0)"


function myTest() {
    TEST="$1"
    FILE="$2"
    EXPECTED="$3"
    TEST_DIR="$RESOURCES_DIR/$TEST"
    rm -rf "$RESOURCES_DIR"
    mkdir -p "$TEST_DIR"
    FILENAME="$TEST_DIR/$FILE"
    touch "$FILENAME"

    "$BIN" "$RESOURCES_DIR" "$TEST_DIR" 2>/dev/null

	RESULT=$(checkFileExist "$TEST_DIR/$EXPECTED")
    echo "# Testing $TEST :" $RESULT
	if [ "$RESULT" = "KO" ]
	then
		echo "  -> expected : $TEST_DIR/$EXPECTED"
		echo "  -> but got  : $TEST_DIR/$(ls $TEST_DIR)/$(cd $TEST_DIR/$(ls $TEST_DIR); ls)"
	fi
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

myTest "nominal" "machin.S01E01.truc.avi" "machin/machin.S01E01.truc.avi"
myTest "espace-dans-fichier" "machin S01E01 truc avi" "machin/machin.S01E01.truc.avi"
myTest "commence-avec-un-point" ".machin S01E01 truc avi" "machin/machin.S01E01.truc.avi"
myTest "commence-avec-un-tiret" "-machin S01E01 truc avi" "machin/machin.S01E01.truc.avi"
myTest "nombre-sur-2-chiffres" "-machin S1E1 truc avi" "machin/machin.S01E01.truc.avi"
myTest "se-dans-le-texte-reste-se" "machin.S01E01.se.avi" "machin/machin.S01E01.se.avi"
myTest "deux-fois-SxxExx-prends-le-1er" "machin.S02E03.truc.S01E48.bidule.mp4" "machin/machin.S02E03.truc.S01E48.bidule.mp4"
myTest "parenthèse-après-SxxExx" "machin.S02E03(1).truc.mp4" "machin/machin.S02E03.(1).truc.mp4"
