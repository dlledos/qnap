#!/bin/sh

# Start JDOWNLOADER2
nohup /share/CACHEDEV1_DATA/.qpkg/jd2/opt/JDownloader2 &

/share/CACHEDEV1_DATA/bin/update-ssh-key.sh
/share/CACHEDEV1_DATA/bin/update-bashrc.sh
/share/CACHEDEV1_DATA/bin/update-crontab.sh