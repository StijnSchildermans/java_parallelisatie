#!/bin/bash

read MESSAGE

cd ./triangles
java Triangles "$MESSAGE" rowsCols 30 10
echo "Done"
