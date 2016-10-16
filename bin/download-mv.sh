#!/bin/sh

find . -type f -regex ".*[mkv|mp4|avi]$" -exec mv "{}" . \;