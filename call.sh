#!/bin/bash
echo "parallelStreams" | netcat 192.168.122.97 6666 -q 10000
