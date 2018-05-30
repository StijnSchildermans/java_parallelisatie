#!/bin/bash

read MESSAGE

cd ./cache
java Cache "$MESSAGE" 
echo "Done"
