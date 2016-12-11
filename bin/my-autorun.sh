#!/bin/sh

# Start JDOWNLOADER2
export PATH=/opt/bin:$PATH
/opt/bin/nohup /share/CACHEDEV1_DATA/.qpkg/jd2/opt/JDownloader2 &

/share/CACHEDEV1_DATA/bin/update-ssh-key.sh
/share/CACHEDEV1_DATA/bin/update-bashrc.sh
/share/CACHEDEV1_DATA/bin/update-crontab.sh
/share/CACHEDEV1_DATA/bin/increase-tmp-size.sh
