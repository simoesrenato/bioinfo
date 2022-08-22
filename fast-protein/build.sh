#!/bin/bash

mvn clean install
strval1="deploy"
strval2=$1

echo "Copying to biolib"
cp target/fast-protein-1.0-jar-with-dependencies.jar fast-protein-1.0.jar


if [ $strval1 == $strval2 ]; then
  echo "Strings are equal"
  echo "Deploying in biolib"
  cd biolib
  biolib push simoesrenato/FastProtein
  echo "Building and deploying completed"
else
  echo "Building completed"
fi

