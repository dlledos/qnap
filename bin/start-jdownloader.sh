#!/bin/sh

function isJD2running(){
	CMD=$(ps l -C java | grep JDownloader2)
	RUNNING=$(echo -n $CMD | wc -l)
	PID=$(echo $CMD | tr -s " " " " | cut -d " " -f3)
}

isJD2running
if [ "$RUNNING" -ne 1 ]
then
	echo "Starting Jdownloader2 ..."
	cd /tmp
	/share/CACHEDEV1_DATA/.qpkg/JDownloader2/JDownloader2.sh start
	#/opt/bin/nohup /share/CACHEDEV1_DATA/.qpkg/jd2/opt/JDownloader2 &
else
	echo "Jdownloader2 already strated with PID = $PID ..."
	echo $CMD
fi
