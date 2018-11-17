#!/bin/bash

REMPLACE=$1

if [ $# -gt 1 ]
then
	blank="true"
fi

IFS=$'\n'
for I in $(find . -type f | grep -v ".@__thumb")
do
	dir=$(dirname $I)
	DEST=$(echo "$I" | sed -e "s/$dir\/\(.*\)/$dir\/$REMPLACE\1/")
	CMD="mv \"$I\" \"$DEST\""
	if [ ! "$blank" == "true" ]
	then 
		echo
		eval $CMD
	else
		echo -n "nothing done to "
	fi
	echo $CMD
done

