#!/bin/sh

find . -type f -regex ".*[mkv|mp4|avi]$" | grep -v ".@__thumb" | xargs -I src mv src .