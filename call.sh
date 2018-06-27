#!/bin/bash
echo "threadPool 10000" | netcat 192.168.122.180 6666 -q 10000
