#!/bin/sh

MOTIF=$1
REMPLACE=$2

IFS=$'\n'
for I in $(find . -type f -regex ".*$MOTIF.*" | grep -v ".@__thumb")
do
	echo $I
	DEST=$(echo "$I" | sed -e "s/\(.*\)$MOTIF\(.*\)/\1$REMPLACE\2/")
	echo mv "$I" "$DEST"
	mv "$I" "$DEST"
done

