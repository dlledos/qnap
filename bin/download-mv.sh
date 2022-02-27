#!/bin/sh

find . -type f -iregex ".*[mkv|mp4|avi]$" -exec mv "{}" . \;
#find . -type f -exec mv "{}" . \;



