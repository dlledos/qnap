#!/bin/bash

MOTIF=$1
REMPLACE=$2
dryrun=${3:'no'}

if [ $# -gt 2 ]
then
	blank="true"
fi

IFS=$'\n'
for I in $(find . -type f -regex ".*$MOTIF.*" | grep -v ".@__thumb")
do
	DEST=$(dirname "$I")/$(basename "$I" | sed -e "s/$MOTIF/$REMPLACE/")
	CMD="mv \"$I\" \"$DEST\""
	if [ ! "$blank" == "true" ]
	then 
		if [ "$dryrun" != "no" ]
		then
			eval $CMD
		fi
	else
		echo -n "nothing done for "
	fi
	echo $CMD
done

