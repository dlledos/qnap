#!/bin/sh

PATH=/opt/bin:$PATH
PHOTOS_DIR=$(pwd)

function rename () {
	FILTER="$1"
	TAG="$2"
	IFS=$'\n'
#	for FILE in $(find $PHOTOS_DIR -name "$FILTER" | grep -v .@__thumb)
	for FILE in $(ls $FILTER | grep -v .@__thumb)
	do
		DATE=$(exiftool -b -${TAG} $FILE | tr -s ":" "-" |tr -s " " "_")
		if [ -n "$DATE" ]
		then
			DIR=$(echo $DATE | cut -d "_" -f1)
			mkdir -p "${DIR}"
			set -x 
			mv "${FILE}" "${DIR}/"
			set +x
			fi
	done
}

#rename "PANO*" "ModifyDate"
rename "*.jpg" "DateTimeOriginal"
rename "*.JPG" "DateTimeOriginal"
rename "*.jpeg" "DateTimeOriginal"
#rename "*.mp4" "CreateDate"
