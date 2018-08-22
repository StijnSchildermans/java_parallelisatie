#!/bin/bash

read MESSAGE

cd dedup/sequential
./dedup -c -v -p -t 14 -i ../../../dedup_data/Leesvoer_Multiplied.tar -o ../../../dedup_data/Leesvoer_Multiplied.dedup
echo "Done"
