# CV 'For You'

Automatically create the most effective version of your resume, ensuring your skills are tailored to match every role you apply for.

## How does it work?
1. You define dynamic sections in your CV template
2. You provide the program a job description (via Seek or Textfile)
3. The algorithm will insert the most relevant information and generate a CV pdf

## How to use?
```
usage: ./CVForYou -d <document_path> -c <compare_path> -s <section_paths>
 -c,--compare <arg>         file to compare keywords to
 -ca,--compare-seek-all     compare from your seek saved job
 -cs,--compare-seek <arg>   pull JD from seek to compare
 -d,--document <arg>        path to the dynamic document
 -h,--help                  print this message
 -o,--output <arg>          path of output
 -s,--section <arg>         path to section definition files
```

# Basic Usage
## Pre-requisites
Either:
- Maven, Chrome
- Docker

## Use Textfile as input
1. Build the project
``` bash
./build
```
3. Build sample document via textfile job description
``` bash
./CvForYou -d assets/sample_components/document.html \
           -c assets/sample_components/description1.txt \
           -s assets/sample_components/projects.json assets/sample_components/tags.json
```

## Use Seek URL as input
1. Build the project
``` bash
./build
```
2. Obtain a Seek Job URL
- e.g. `https://www.seek.com.au/job/12345678`
3. Build the sample document via seek integration
``` bash
./CvForYou -d assets/sample_components/document.html \
           -cs <seek_job_url> \
           -s assets/sample_components/projects.json assets/sample_components/tags.json
```


# Using SEEK integration as input
The command line argument `-ca, --compare-seek-all` pulls data from your 'Saved Jobs' list in Seek to generate CVs. As such it requires an extra step to configure the auth token. 

# Using Docker
1. Build the docker image
``` bash
docker build -t cv-for-you .
```

Note: You will need to rebuild if you manually update the auth key

2. Generate the sample document
``` bash
docker run -v ./assets:/app/assets \
           -v ./cache:/app/cache cv-for-you \
           -d assets/sample_components/document.html \
           -c assets/sample_components/description1.txt \
           -s assets/sample_components/projects.json assets/sample_components/tags.json
```

### How to configure Seek auth token?
1. Login to Seek
2. Open browser console
3. Run Script in console
``` js
for (let i = 0; i < localStorage.length; i++) {
  const key = localStorage.key(i);
  if (key.includes("auth0spajs")) {
    const value = localStorage.getItem(key);
    let obj = JSON.parse(value);
    let access_token = "Bearer " + obj.body.access_token;
    console.log(access_token);
  }
}
```
4. Copy-paste result in file named `auth` in repo directory

## Demo
Define generic sections in your CV, so they be programmatically populated

![image](https://github.com/user-attachments/assets/5b89d723-d546-43b3-af23-e0d68a8bf846)


### Demo 1 - .NET
Given a .NET job description, generate a CV tailored for .NET roles

![image](https://github.com/user-attachments/assets/dfc8fea7-4a8f-445b-987e-07526c2c4fa8)


### Demo 2 - Javascript
Given a Javascript job description, generate a CV tailored for Javascript roles

![image](https://github.com/user-attachments/assets/fec42896-58c6-4996-950c-b94741bd3dd3)

