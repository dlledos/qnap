#!/bin/sh

PATH=/opt/bin:$PATH

SRC=${1:-.}
DEST=${2:-"/share/CACHEDEV1_DATA/Multimedia/Série"}
dryrun=${3:-"no"}

IFS=$'\n'
for I in $(find $SRC -type f -regex ".*[s|S][0-9]+[e|E][0-9]+.*[mkv|mp4|avi|MKV|AVI|MP4]$" | grep -v ".@__thumb")
do
	FILE=$(echo $I | tr -s "\!" "." | tr -s " " "." | tr -s "\[" "." | tr -s "\]" "." | tr -s "_" "." | tr -s "-" "." | sed -e "s/.*\/\(.*\)/\1/" | sed -e "s/^\.\(.*\)/\1/")
	#DIR=$(echo $FILE | sed -e "s/\(^.*\)\.\([S|s][0-9]\+[E|e][0-9]\+.*\)/\1/")
	RES=$(echo $FILE | perl -ne 'chomp;print scalar reverse . "\n";' | sed -e "s/\(.*\.\)\([0-9]\+[E|e][0-9]\+[S|s]\)\.\(.*\)/\1 \2 \3/")
	#RES=$(echo $FILE | sed -e "s/\(.*\)[S|s]\([0-9]\+\)[E|e]\([0-9]\+\)\(.*\)/\1 \2 \3 \4/")
	DEBUT=$(echo "$RES" | cut -d " " -f3 | perl -ne 'chomp;print scalar reverse . "\n";')
	SE=$(echo "$RES" | cut -d " " -f2 | perl -ne 'chomp;print scalar reverse . "\n";')
	SAISON=$(printf "%02d" $(echo $((10#$(echo "$SE" | sed -e "s/[S|s]\([0-9]\+\)[E|e]\([0-9]\+\)/\1/")))))
	EPISODE=$(printf "%02d" $(echo $((10#$(echo "$SE" | sed -e "s/[S|s]\([0-9]\+\)[E|e]\([0-9]\+\)/\2/")))))
	FIN=$(echo "$RES" | cut -d " " -f1 | perl -ne 'chomp;print scalar reverse . "\n";')
	DIR=$(echo $DEBUT | sed -r 's/(^.|\..)/\U&/g')
	FILE="${DIR}.S${SAISON}E$EPISODE$FIN"
	mkdir -p "$DEST/$DIR"
	
        if [ -e "$DEST/$DIR/$FILE" ]
	then 
        	echo "$I => $new_file already exist"
	else
		if [ "$dryrun" == "no" ]
	        then
			echo mv "$I" "$DEST/$DIR/$FILE" >&2
			mv "$I" "$DEST/$DIR/$FILE"
		fi
		echo mv "$I" "$DEST/$DIR/$FILE" >&2
	fi
done
