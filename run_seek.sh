#!/bin/bash

echo "WARNING: you may be required to change the seek URL in this script for future versions"

mvn clean compile exec:java -Dexec.args="-d prod_components/CV_Pretty.html -cs https://www.seek.com.au/job/76113399 -s prod_components/tags1.json prod_components/tags2.json prod_components/projects.json -o prod_components/"


