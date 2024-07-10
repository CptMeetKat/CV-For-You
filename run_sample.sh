#!/bin/bash


mvn clean compile exec:java -Dexec.args="-d sample_components/document.html -c sample_components/description2.txt -s sample_components/projects.json sample_components/tags.json"


