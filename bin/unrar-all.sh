#!/bin/sh

IFS=$'\n'
for I in $(ls *.rar)
do
	unrar x -o- "$I"
done
