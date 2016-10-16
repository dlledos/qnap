#!/bin/sh

PATH=/opt/bin:$PATH
#PHOTOS_DIR="/share/CACHEDEV1_DATA/Multimedia/Camera-à-trier"
PHOTOS_DIR=$(pwd)
DOUBLON_DIR="$PHOTOS_DIR/../doublons-$(basename $(cd $PHOTOS_DIR; pwd))"

function getType(){
	EXT=$1
	if [ "$EXT" = "jpg" ]
	then
		TYPE=IMG
	fi
	if [ "$EXT" = "mp4" ]
	then
		TYPE=VID
	fi
	echo $TYPE
}


function create_filename(){
	DATE=$1
	EXT=$2
	ORIG=$3
	NB=1
	TYPE=$(getType "$EXT")
	FILE="$DATE.$TYPE$NB.$EXT"
	if [ ! "$PHOTOS_DIR/$FILE" = "$ORIG" ]
	then
		if [ -f $PHOTOS_DIR/$FILE ]
		then
			SIZE_NEW=$(wc -c $PHOTOS_DIR/$FILE | cut -d " " -f1)
			SIZE_ORIG=$(wc -c $ORIG| cut -d " " -f1)
			if [  $SIZE_NEW -eq $SIZE_ORIG ]
			then
				mkdir -p "$DOUBLON_DIR"
				NB=$[ $(ls $DOUBLON_DIR/$DATE.${TYPE}*.$EXT 2>/dev/null| wc -w) + 1 ]
				FILE="$DATE.$TYPE$NB.$EXT"				
				echo $DOUBLON_DIR/$FILE
			else
				NB=$[ $(ls $DATE.${TYPE}*.$EXT 2>/dev/null| wc -w) + 1 ]
				FILE="$DATE.$TYPE$NB.$EXT"
				echo $PHOTOS_DIR/$FILE
			fi
		else
			echo $PHOTOS_DIR/$FILE
		fi
	fi
}

function rename () {
	FILTER="$1"
	TAG="$2"
	EXT="$3"
	for FILE in $(find $PHOTOS_DIR -name "$FILTER" | grep -v .@__thumb)
	do
		DATE=$(exiftool -b -${TAG} $FILE | tr -s ":" "-" |tr -s " " "_")
		if [ -n "$DATE" ]
		then
			NEW=$(create_filename $DATE "$EXT" $FILE)
			if [ -n "$NEW" ]
			then
				echo mv $FILE $NEW
				mv "$FILE" "$NEW"
			fi
		fi
	done
}

rename "PANO*" "ModifyDate" "jpg"
rename "*.jpg" "DateTimeOriginal" "jpg"
rename "*.mp4" "CreateDate" "mp4"
