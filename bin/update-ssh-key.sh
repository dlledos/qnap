#!/bin/sh
SSHKEY="/root/.ssh/authorized_keys"
grep "update-ssh-key.sh" $SSHKEY
if [ ! $? -eq 0 ]
then
	echo "# ADDDED by update-ssh-key.sh" >> $SSHKEY 
	echo "ssh-rsa AAAAB3NzaC1yc2EAAAABJQAAAQEApLEPS/nptP7oCHM43i5tswCbG4gG18Ie11E5a57z+UwHKJTXflxXCPKUwdUcL2fmJDNV3WNbfyeOi0b4nLXOsk9i3N18AQMYiPv69HEVLh87/rtPqyUgjbml8c2PNDpqCAVoR5cOLFXgM8U1eMm5/2VOFPBzmYQNAHtJnC85evcNE3ERI5qHPDCuWhx/GMZ8ze2FKA1fyk4fEiWTr/7Dhy1cl3DjUbn4lfML5dk+qJ+Rg2NBi5kPtxUtDXvB4o3irynYLEnCSnkQvcnMJ6nYtTYf64/pTCxKNVypbslfM4JrF8JgviTrpHX0k77ZLyYuFjqnl2GfzkPCU7ZW8ZzqTw== rsa-key-20160425"  >> $SSHKEY 
fi