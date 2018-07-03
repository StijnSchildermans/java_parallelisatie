#!/bin/bash
echo "threadPool 100" | netcat 192.168.122.180 6666 -q 10000
