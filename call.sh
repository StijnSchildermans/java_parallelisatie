#!/bin/bash
echo "blah" | netcat 192.168.122.180 6666 -q 10000
