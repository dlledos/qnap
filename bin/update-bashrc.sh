#!/bin/sh
export PATH=/opt/bin:$PATH

BASHRC="/root/.bashrc"

grep "update-bashrc.sh" $BASHRC
if [ ! $? -eq 0 ]
then
	echo "# ADDDED by update-bashrc.sh " >> $BASHRC 
	echo "export JAVA_HOME=/usr/local/jre" >> $BASHRC
	echo "export PATH=/opt/bin:$PATH:/share/CACHEDEV1_DATA/bin/:\$JAVA_HOME/bin" >> $BASHRC 
	echo "git config --global user.email didier.lledos@gmail.com" >> $BASHRC 
	echo "git config --global user.name \"Didier Lledos\"" >> $BASHRC 
	echo "cd /share/CACHEDEV1_DATA/Multimedia/jdownloader" >> $BASHRC 
	source $BASHRC
fi
