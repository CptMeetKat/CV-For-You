#!/bin/bash


mvn clean compile exec:java -Dexec.args="-d assets/sample_components/document.html -c assets/sample_components/description1.txt -s assets/sample_components/projects.json assets/sample_components/tags.json"


