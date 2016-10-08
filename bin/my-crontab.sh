#!/bin/sh

mkdir -p /share/CACHEDEV1_DATA/log
crontab -l > /tmp/crontab.list
grep "tvshow-manage.sh" /tmp/crontab.list
if [ ! $? -eq 0 ]
then
	echo "* * * * * /share/CACHEDEV1_DATA/bin/tvshow-manage.sh /share/CACHEDEV1_DATA/Multimedia/jdownloader >> /share/CACHEDEV1_DATA/log/crontab.log  2>> /share/CACHEDEV1_DATA/log/crontab.log" >> /tmp/crontab.list
	crontab /tmp/crontab.list
fi