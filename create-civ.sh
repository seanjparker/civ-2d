#!/bin/bash
mkdir bin
javac -d bin $(find src -name *.java)
mkdir bin/buttons && cp -r gfx/buttons bin/buttons

if [ $? ]; then
  echo "Successfully compiled. Creating JAR executable."
  jar cvmf META-INF/MANIFEST.MF civ-2d.jar -C bin .
  if [ $? ]; then
    echo "Done"
  else
    echo "Error while creating JAR file, try again."
  fi
else
  echo "Error during compiling! Try re-downloading and try again."
fi
