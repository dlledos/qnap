#!/bin/sh

BASHRC="/root/.bashrc"

grep "update-bashrc.sh" $BASHRC
if [ ! $? -eq 0 ]
then
	echo "# ADDDED by update-bashrc.sh " >> $BASHRC 
	echo "export PATH=/opt/bin:$PATH:/share/CACHEDEV1_DATA/bin/" >> $BASHRC 
	echo "cd /share/CACHEDEV1_DATA/Multimedia/jdownloader" >> $BASHRC 
	source $BASHRC
fi
