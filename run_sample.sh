#!/bin/bash


sections=$(echo assets/sample_components/sections/*.json)
mvn clean compile exec:java -Dexec.args="-d assets/sample_components/document.html -c assets/sample_components/description1.txt -s $sections"


