Simple project that gets information about specific owner and his specific repository information usign github rest api v3.

Aplication serve one rest method::

GET /repositories/{owner}/{repository-name}

and it returns information:

{
"fullName": "...",
"description": "...",
"cloneUrl": "...",
"stars": 0,
"createdAt": "..."
}

---

## Compile project

To build project you need maven >= 3.0 and java 1.8

Run build:

mvn clean install

---

## Run project

Simply use:

java -jar github-repo-seeker-1.0-SNAPSHOT-jar-with-dependencies.jar

## Configuration
Configuration can be changed by placing along with the jar file named config.yaml

Default configuration:

```yaml
server:
  port: 8080
  threads: 1
  development: false

app:
  clientPoolSize: 20 #http client pool size
  clientTimeout: 10 #in seconds
  githubResourceV3: https://api.github.com/repos/
  githubUserAgent: exercise
  githubAccept: application/vnd.github.v3+json
  githubUserBasic: YWxsZWdyb0V4ZXJjaXNlOkNpSHVuQmFIaWR1andpNg==
```