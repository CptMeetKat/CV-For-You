# CV 'For You'

Automatically create the most effective version of your resume, ensuring your skills are tailored to match every role you apply for.

### Table of contents
- [How does it work?](#how-does-it-work)
- [How to use?](#how-to-use)
- [Basic Usage](#basic-usage)
   * [Pre-requisites](#pre-requisites)
   * [Use Textfile as input](#use-textfile-as-input)
   * [Use Seek URL as input](#use-seek-url-as-input)
- [Using SEEK integration as input](#using-seek-integration-as-input)
   * [How to configure Seek auth token?](#how-to-configure-seek-auth-token)
- [Using Docker](#using-docker)   
- [Demo](#demo)
   * [Demo 1 - .NET](#demo-1---net)
   * [Demo 2 - Javascript](#demo-2---javascript)

## How does it work?
1. You define dynamic sections in your CV template
2. You provide the program a job description (via Seek or Textfile)
3. The algorithm will insert the most relevant information and generate a CV pdf

## How to use?
```
usage: ./CVForYou --seek-stats -a
 -cv,--cv-generator   Generate a dynamic CV
 -h,--help            print this message
 -sn,--seek-notes     Write notes on saved SEEK roles
 -sr,--seek-resumes   Upload CV directly to SEEK
 -ss,--seek-stats     Aggregate stats from Seek
```

```
usage: ./CVForYou --cv_generator -d <document_path> -c <compare_path> -s
                  <section_paths>
 -c,--compare <arg>              file to compare keywords to
 -ca,--compare-seek-all          compare from your seek saved job
 -cc,--compare-cache <arg>       compare from a previous cached seek saved
                                 job
 -cs,--compare-seek <arg>        pull JD from seek to compare
 -d,--document <arg>             path to the dynamic document
 -h,--help                       print this message
 -o,--output <arg>               output directory
 -s,--section <arg>              path to section definition files
 -sd,--section directory <arg>   directory of section definition files
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
./CVForYou -d assets/sample_components/document.html \
           -c assets/sample_components/description1.txt \
           -s assets/sample_components/sections/projects.json assets/sample_components/sections/tags.json
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
./CVForYou -d assets/sample_components/document.html \
           -cs <seek_job_url> \
           -s assets/sample_components/sections/projects.json assets/sample_components/sections/tags.json
```


# Using SEEK integration as input
The command line argument `-ca, --compare-seek-all` pulls data from your 'Saved Jobs' list in Seek to generate CVs. As such it requires an extra step to configure the auth token. 

### How to configure Seek auth token?
1. Login to Seek
2. Open browser console
3. Run Script in console
``` js
let auth = {};

const cookies = document.cookie.split('; ');
cookies.forEach(cookie => {
  const [key, value] = cookie.split('=');
  if(key == "JobseekerSessionId") { 
      auth[key] = value;
  }
});



for (let i = 0; i < localStorage.length; i++) {
  const key = localStorage.key(i);

  // Check if the key contains a specific pattern or substring
  if (key.includes("auth0spajs")) {
    const value = localStorage.getItem(key);
    let obj = JSON.parse(value);
    auth["access_token"] = obj.body.access_token;
    auth["refresh_token"] =  obj.body.refresh_token;
    auth["client_id"] =  obj.body.client_id;
  }
}

console.log(JSON.stringify(auth));

```

4. Copy-paste result in file named `auth` in repo directory


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
           -s assets/sample_components/sections/projects.json assets/sample_components/sections/tags.json \
           -o ./assets/sample_components/
```

As you add more dynamic sections you may prefer to run the following to shrink the command size

``` bash
docker run \
-v ./assets:/app/assets \
-v ./cache:/app/cache \
cv-for-you \
	-d /app/assets/sample_components/document.html \
	-ca \
	-sd /app/assets/sample_components/sections/ \
	-o ./assets/sample_components/
```

Note: Be sure to output to a mounted location

# Demo
Define generic sections in your CV, so they be programmatically populated

![image](https://github.com/user-attachments/assets/5b89d723-d546-43b3-af23-e0d68a8bf846)


### Demo 1 - .NET
Given a .NET job description, generate a CV tailored for .NET roles

![image](https://github.com/user-attachments/assets/dfc8fea7-4a8f-445b-987e-07526c2c4fa8)


### Demo 2 - Javascript
Given a Javascript job description, generate a CV tailored for Javascript roles

![image](https://github.com/user-attachments/assets/fec42896-58c6-4996-950c-b94741bd3dd3)

