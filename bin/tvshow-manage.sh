#!/bin/sh

date
SRC=${1:-.}
DEST="/share/CACHEDEV1_DATA/Multimedia/Série"
IFS=$'\n'
for I in $(/opt/bin/find $SRC -type f -regex ".*[s|S][0-9]+[e|E][0-9]+.*[mkv|mp4|avi]$" | grep -v ".@__thumb")
do
	FILE=$(echo $I | tr -s " " "." | tr -s "_" "." | tr -s "-" "." | sed -e "s/.*\/\(.*\)/\1/" | sed -e "s/^\.\(.*\)/\1/")
	DIR=$(echo $FILE | sed -e "s/\(^.*\)\.[S|s].*/\1/")
	mkdir -p "$DEST/$DIR"
	echo mv "$I" "$DEST/$DIR/$FILE"
	mv "$I" "$DEST/$DIR/$FILE"
done