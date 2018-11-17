#!/bin/sh -e
DATE=$(date +"%Y-%m-%d.%H:%M:%S")
DIR="/share/CACHEDEV1_DATA/Multimedia/Cloud-Drive-Sync/Dropbox/Applications/SMSBackupRestore/Applications/SMSBackupRestore/"
FILE="$DIR/sms.xml"
DEST="$DIR/sms.$DATE.xml"

sed -e "s/&#[0-9]*;&#[0-9]*;/:-)/g" $FILE > $DEST
