#!/bin/sh
echo "hello.world" | java Parse
echo GET / HTTP/1.9 | java Parse
echo get /hui_/huiweishn HTTP/333.999 | java Parse
echo GET /hui./__./__ HTTP/.9 | java Parse
echo GET /hui./__./__ HTTP/11.9 | java Parse
echo GET /hui./__./__ HTTP/0.9 | java Parse
echo GET /hui./__./__ HTTP/0.9. | java Parse
echo GET /hui./__./__ HTTP/9.9 hd  | java Parse

