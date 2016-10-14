#!/bin/sh

PHOTOS_DIR="/share/CACHEDEV1_DATA/Multimedia/Camera-à-trier"

for PHOTO in $(find . -regex "[mp4|jpg])
  echo "$PHOTO"
do
#exiftool
