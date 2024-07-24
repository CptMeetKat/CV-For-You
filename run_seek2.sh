#!/bin/bash

mvn -q clean compile exec:java -Dexec.args="-d prod_components/CV_Pretty.html -ca -s prod_components/tags1.json prod_components/tags2.json prod_components/projects.json -o prod_components/"


