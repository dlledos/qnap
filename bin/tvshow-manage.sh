#!/bin/sh

date
SRC=${1:-.}
DEST="/share/CACHEDEV1_DATA/Multimedia/Série"
IFS=$'\n'
for I in $(/opt/bin/find $SRC -type f -regex ".*[s|S][0-9]+[e|E][0-9]+.*[mkv|mp4|avi]$" | grep -v ".@__thumb")
do
	DIR=$(echo $I | /opt/bin/sed -e "s/.*[\/]\(.*\)[.|][s|S][0-9][0-9][e|E][0-9][0-9].*[mkv|mp4|avi]$/\1/")
	mkdir -p "$DEST/$DIR"
	echo mv "$I" "$DEST/$DIR"
	mv "$I" "$DEST/$DIR"
done