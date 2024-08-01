#!/bin/bash

echo "WARNING: you may be required to change the seek URL in this script for future versions"

mvn clean compile exec:java -Dexec.args="-d assets/prod_components/CV_Pretty.html -cs https://www.seek.com.au/job/76113399 -s assets/prod_components/tags1.json assets/prod_components/tags2.json assets/prod_components/projects.json -o assets/prod_components/"


