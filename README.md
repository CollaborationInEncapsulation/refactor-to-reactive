# Refactor to Reactive

## Prerequisites and Essential guideline for running the project

To run the project is recommended to use following configurations for the environment:

* JDK 1.8 or higher (project compatible with JDK 9 and JDK 10)
* Docker or Installed locally MongoDB 3.4
* Registered account at [https://developer.gitter.im](https://developer.gitter.im) and available token at [https://developer.gitter.im/apps](https://developer.gitter.im/apps)
* Created Community at [https://gitter.im/#createcommunity](https://gitter.im/#createcommunity)


### Compiling the Project 

The Project uses [Gradle](https://gradle.org/) as the primary build tool to grab all required dependencies and compile the Code then.

To compile the Project, please open a terminal and execute the following command in the Project's root folder:

``` SH
./gradlew clean build
```

### Preparing MongoDB 3.4

The Project uses [MongoDB](https://www.mongodb.com/) as the primary database for all data's querying and storing. In turn, because of particular limitations of Spring Data 4.x, we cannot use MongoDB with a version higher than 3.4.

There are two available options in order to install MongoDB: 

#### (Option 1) Dockerized MongoDB

> Note, that option requires the essential understanding of the [Docker's CLI] (https://docs.docker.com/engine/reference/commandline/cli/)

> Before starting that option, please ensure that [Docker](https://docs.docker.com/install/) (has already been installed on the local machine).

It is necessary to execute the following command in the terminal to run MongoDB image in the Docker container:  

``` SH
docker run --name test-mongo -p 27017:27017 -d mongo:3.4
```

#### (Option 2) Local Community MongoDB Server

There is an option to install MongoDB locally. All required information related to the local installation is available by the following [link](https://www.mongodb.com/download-center?jmp=nav#community).

> Remember, to reproduce Refactoring process from the beginning, it is necessary to use MongoDB version not higher than 3.4.

### Preparing the Environment

To properly run the Project the proper environment variables / YAML properties are required. The following is the list of available *Spring Framework* properties/environment variables: 

| Spring property | Environment variable | Description |
| --------------- | -------------------- | ----------- |
| `gitter.auth.token` | `GITTER_TOKEN` | Personal Access Token which can be used to access the Gitter API. | 
| `gitter.api.endpoint` | - | The address of public Gitter REST API endpoint. The default value is [https://api.gitter.im/](https://api.gitter.im/). To learn more, [see following API docs](https://developer.gitter.im/docs/rest-api) |
| `gitter.api.version` | - | The version of the Gitter REST API. The default value is `v1`. |
| `gitter.api.messages-resource` | `GITTER_ROOM` | Path to the Messages Resource. **Note**, in environment variable case, it is unnecessary defining the whole path since it has already been defined as the following: `rooms/${GITTER_ROOM}/chatMessages`. To get created Gitter room id, please [see API docs](https://developer.gitter.im/docs/rooms-resource#list-rooms). |
| `gitter.stream.endpoint` | - | The address of public Gitter Streaming API endpoint. The default value is [https://stream.gitter.im/](https://stream.gitter.im/). To learn more, [see following API docs](https://developer.gitter.im/docs/streaming-api)  |
| `gitter.stream.version` | - | The version of the Gitter Streaming API. The default value is `v1`. |
| `gitter.stream.messages-resource` | `GITTER_ROOM` | Path to the Messages Resource. **Note**, in environment variable case, it is unnecessary defining the whole path since it has already been defined as the following: `rooms/${GITTER_ROOM}/chatMessages`. To get created gitter room id, please [see API docs](https://developer.gitter.im/docs/rooms-resource#list-rooms). |

### Following the Story

All refactoring steps of the Project are recorded in the commit history of the Project.  Thus, to switch between the commits, please execute the following command in the terminal: 

``` SH
git checkout master~5
```

> Note by running the above command, the project will be checked out to the initial state. Then, by increasing the `~number` from 5 to 0, it will be possible to cross all refactoring steps during the five development's iterations.
