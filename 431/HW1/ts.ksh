#!/bin/ksh
file=$1
# while loop
while read line
do
	echo "$line" | java Parse
	echo
done <"$file"
