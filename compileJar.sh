#!/bin/bash -e

# launches sbt task: assembly. It compiles the Jar file.
./sbt.sh assembly
ln -sf target/scala-2.11/Fractals.jar ./Fractals.jar

