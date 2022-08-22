#!/bin/bash
cp ../target/fast-protein-1.0-jar-with-dependencies.jar fast-protein-1.0.jar
echo 'Copying new jar file'
cp -r ../target/classes/biolib/* .
echo 'Copying biolib files'
cp ../target/classes/biolib/.biolib/config.yml .biolib/config.yml
echo 'Copying biolib config.yml'
#biolib push simoesrenato/FastProtein
