#!/bin/sh

find . -maxdepth 1 -empty -type d -not -path . -exec rm -r "{}" \;
