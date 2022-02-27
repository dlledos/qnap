#!/bin/sh

MOTIF=$1
REMPLACE=$2
S=1
E=1


IFS=$'\n'
for I in $(find . -type f -regex ".*$MOTIF.*" | grep -v ".@__thumb")
do
	echo $I
	EP=$(printf "%02d" $E)
	DEST=$(echo "$I" | sed -e "s/\(.*\)$MOTIF\(.*\)/\1$REMPLACE.S${S}E${EP}.\2/")
	E=$[ $E + 1 ]
	echo mv "$I" "$DEST"
	mv "$I" "$DEST"
done
