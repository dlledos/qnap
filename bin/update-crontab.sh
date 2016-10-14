#!/bin/sh

DESTINATION="/tmp/crontab.list"
LOG_DIR="/share/CACHEDEV1_DATA/log"
LOG="$LOG_DIR/crontab.log"
mkdir -p $LOG_DIR
crontab -l > $DESTINATION
SCRIPT=$(basename $0)
grep "$SCRIPT"  $DESTINATION
if [ ! $? -eq 0 ]
then
	echo "# ADDED BY $SCRIPT" >>  $DESTINATION
	echo "1,11,21,31,41,51 * * * * /share/CACHEDEV1_DATA/bin/tvshow-manage.sh /share/CACHEDEV1_DATA/Multimedia/jdownloader >> $LOG 2>> $LOG" >> $DESTINATION
	crontab $DESTINATION
fi
