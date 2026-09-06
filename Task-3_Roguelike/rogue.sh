#!/bin/bash
cd Rogue
if gradle roguelike; then
    java -jar build/libs/rogue.jar
else
    echo "Build failed"
    exit 1
fi