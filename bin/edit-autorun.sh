#!/bin/sh

mkdir -p /tmp/config
mount -t ext2 $(/sbin/hal_app --get_boot_pd port_id=0)6 /tmp/config
nano /tmp/config/autorun.sh
chmod +x /tmp/config/autorun.sh
echo .
echo "unmounting /tmp/config..."
umount /tmp/config