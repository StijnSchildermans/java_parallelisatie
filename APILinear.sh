#!/bin/bash

read MESSAGE

cd ~/java/linear
java Linear "$MESSAGE" 1000 1000 1
echo "Done"
