#!/bin/ksh

for filename in /afs/cs.unc.edu/project/courses/comp520-s12/Submit/duozhao/pa2/tests/1/*
do
	echo 
	echo "<==============================Starting the Next File: ==============================>"
	echo $filename
	cat $filename 
	echo 
	java -classpath /afs/cs.unc.edu/project/courses/comp520-s12/Submit/duozhao/pa2 miniJava.Compiler $filename
done;

