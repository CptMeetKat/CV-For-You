#!/bin/bash

mvn -q clean compile exec:java -Dexec.args="-d assets/prod_components/CV_Pretty.html -ca -s assets/prod_components/tags1.json assets/prod_components/tags2.json assets/prod_components/projects.json -o assets/prod_components/"


