#!/bin/sh

rm *.txt *.m4a *.srt *.jpg; IFS=$'\n' ; for I in `ls  Talking\ Tom\ and\ Friends\ -\ *.mp4`; do NEW=$(echo $I  | cut -d "-" -f2- | cut -d "(" -f1,2 | tr -s " " " " |sed 's/\(.*\) (Épisode \([0-9]*\).*/Talking.Tom.S01E\2.\1\.mp4/' | tr -s " " "." );  mv "$I" "$NEW"; done ; tvshow-manage.sh
