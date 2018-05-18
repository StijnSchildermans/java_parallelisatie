#!/bin/bash

read MESSAGE

cd ~/java/triangles
java Triangles "$MESSAGE" rowsCols 30 10
echo "Done"
