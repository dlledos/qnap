#!/bin/sh

PATH=/opt/bin:$PATH
date
SRC=${1:-.}
DEST="/share/CACHEDEV1_DATA/Multimedia/Série"
IFS=$'\n'
for I in $(find $SRC -type f -regex ".*[s|S][0-9]+[e|E][0-9]+.*[mkv|mp4|avi]$" | grep -v ".@__thumb")
do
	FILE=$(echo $I | tr -s " " "." | tr -s "_" "." | tr -s "-" "." | sed -e "s/.*\/\(.*\)/\1/" | sed -e "s/^\.\(.*\)/\1/")
	DIR=$(echo $FILE | sed -e "s/\(^.*\)\.\([S|s][0-9]*[E|e][0-9].*\)/\1/")
	RES=$(echo $FILE | sed -e "s/\(.*)[S|s]\([0-9]*\)[E|e]\([0-9]*\)\(\.*\)/\1 \2 \3 \4/")
	DEBUT=$(echo "$RES" | cut -d " " -f1)
	SAISON=$(printf "%02d" $(echo $((10#$(echo "$RES" | cut -d " " -f2)))))
	EPISODE=$(printf "%02d" $(echo $((10#$(echo "$RES" | cut -d " " -f3)))))
	FIN=$(echo "$RES" | cut -d " " -f4)
	FILE="${DEBUT}S${SAISON}E$EPISODE$FIN"
	mkdir -p "$DEST/$DIR"
	echo mv "$I" "$DEST/$DIR/$FILE"
	mv "$I" "$DEST/$DIR/$FILE"
done