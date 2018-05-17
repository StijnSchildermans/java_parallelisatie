javac -classpath . *.java

echo 'Sequential:'
time java Triangles sequential 30
echo ''
echo 'Parallel:'
time java Triangles parallel 30
echo ''
echo 'Parallel with streams:' 
time java Triangles parallelStreams 30
echo ''
echo 'Parallel with streams better:'
time java Triangles parallelStreamsBeter 30

