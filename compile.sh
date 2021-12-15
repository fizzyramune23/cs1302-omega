#!/bin/bash

OPTS="-q -e" # quiet and produce execution error messages
JFX="-Dprism.order=sw -Dexec.mainClass=cs1302.omega.OmegaDriver" # specify the software renderer

set -ex
mvn $OPTS clean
mvn $OPTS compile
mvn $OPTS exec:java $JFX $@
