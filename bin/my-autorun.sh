#!/bin/sh

# Start JDOWNLOADER2
export PATH=/opt/bin:$PATH

/share/CACHEDEV1_DATA/bin/update-ssh-key.sh
/share/CACHEDEV1_DATA/bin/update-bashrc.sh
/share/CACHEDEV1_DATA/bin/increase-tmp-size.sh
/share/CACHEDEV1_DATA/bin/start-jdownloader.sh
