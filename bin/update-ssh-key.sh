#!/bin/sh
export PATH=/opt/bin:$PATH

SSHKEY="/root/.ssh/authorized_keys"
# La clé se trouve dans C:\Users\Did\.ssh\id_rsa.ppk à ouvrir avec PuTTYgen
SSHKEY_VALUE="/share/CACHEDEV1_DATA/homes/admin/id_rsa_windows10"

grep "update-ssh-key.sh" $SSHKEY
if [ ! $? -eq 0 ]
then
	echo "# ADDDED by update-ssh-key.sh" >> $SSHKEY 
	cat $SSHKEY_VALUE  >> $SSHKEY 
fi
