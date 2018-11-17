#!/bin/bash -x

MOTIF=$1
REMPLACE=$2

if [ $# -gt 2 ]
then
	blank="true"
fi

IFS=$'\n'
for I in $(find . -type f -regex ".*$MOTIF.*" | grep -v ".@__thumb")
do
	DEST=$(echo "$I" | sed -e "s/\(.*\)$MOTIF\(.*\)/\1$REMPLACE\2/")
	CMD="mv '$I' '$DEST'"
	echo $CMD
	if [ ! "$blank" == "true" ]
	then 
		eval $CMD
	else
		echo "nothing done"
	fi
done

