# News RESTful web-service
Tool providing functionality for working with news management system


### Overall information

* Implemented CRUD operations for User, News, Comments and full-text search by various parameters
* DTO generation from .proto [files](src/main/proto)
* Added authorization structure with roles `ADMIN`, `JOURNALIST`, `SUBSCRIBER` and
  restricting access based on role
* Done database migration (via Flyway)
* Added [Dockerfile](Dockerfile) to build container for application
* Added [docker-compose](docker-compose.yml) to run applications with postgres database
* Added unit-tests and integration tests (using H2 database for testing)

## Requirements

| parameter       | version           |
| ------------- |:-------------:| 
| Java|8 | 
| Gradle|7+ | 
| Tomcat|7+ | 

## End points description

#### Authentication end points

| end point       | method           |     consumes       | description | available for|
| ------------- |:-------------:|:-------------:|:-------------:|:-------------:|
| api/auth/register| POST | [RegistrationRequestDto](src/main/java/com/example/demo/dto/RegistrationRequestProto.java) |provides registration service for subscribers|ALL|

#### User end points

| end point       | method           |     consumes       | description | available for |
| ------------- |:-------------:|:-------------:|:-------------:| :-------------:|
| api/user| GET | - | returns list of user | ADMIN|
| api/user/{id}| GET | - | returns user with provided `id`| ADMIN|
| api/user| POST | [UserDto](src/main/java/com/example/demo/dto/UserProto.java)| service for user creation | ADMIN|
| api/user/{id}| PUT | [UserDto](src/main/java/com/example/demo/dto/UserProto.java) | service for user update | ADMIN|
| api/user/{id}| DELETE | - | delete user with provided `id` | ADMIN|

#### News end points

| end point       | method           |     consumes   | description           | available for|
| ------------- |:-------------:|:-------------:|:-------------:|:-------------:|
| api/news| GET | - | returns list of news| ALL
| api/news?page=a&size=b| GET | - | returns list of news with page number `a` and page size `b`| ALL
| api/news/{id}| GET | - | returns news with provided `id`|ALL|
| api/news/{id}?page=a&size=b| GET | - | returns news with provided `id` and with comment page number `a` and size `b`|ALL|
| api/news/?title=str| GET | - |returns list of news with title equals to `str`|ALL|
| api/news/?titleLike=str| GET | - |returns list of news with title contains `str`|ALL|
| api/news/?text=str| GET | - |returns list of news with text equals to `str`|ALL|
| api/news/?textLike=str| GET | - |returns list of news with text contains `str`|ALL|
| api/news| POST | [NewsDto](src/main/java/com/example/demo/dto/NewsProto.java)  | service for news creation|ADMIN, JOURNALIST|
| api/news/{id}| PUT | [NewsDto](src/main/java/com/example/demo/dto/NewsProto.java)  | service for news update|ADMIN, JOURNALIST (owner)|
| api/news/{id}| DELETE | - | delete news with provided `id`| ADMIN, JOURNALIST (owner)|

#### Comment end points

| end point       | method           |    consumes   |  description          | available for|
| ------------- |:-------------:|:-------------:|:-------------:|:-------------:|
| api/comment| GET | - | returns list of comments| ALL|
| api/comment?page=a&size=b| GET | - | returns list of comments with page number `a` and page size `b`|ALL|
| api/comment/{id}| GET | - |returns comment with provided `id`|ALL|
| api/comment/?text=str| GET | - |returns list of news with text equals to `str`|ALL|
| api/comment/?textLike=str| GET | - |returns list of news with text contains `str`|ALL|
| api/comment| POST | [CommentDto](src/main/java/com/example/demo/dto/CommentProto.java)  | service for comment creation|ADMIN, JOURNALIST, SUBSCRIBER|
| api/comment/{id}| PUT |[CommentDto](src/main/java/com/example/demo/dto/CommentProto.java)  | service for comment update|ADMIN, JOURNALIST (owner), SUBSCRIBER (owner)|
| api/comment/{id}| DELETE | - | delete comment with provided `id`| ADMIN, JOURNALIST (owner), SUBSCRIBER (owner)|

How to run app in Docker
==============

- Download Docker Desktop for Windows (if not installed):
  > https://docs.docker.com/docker-for-windows/install/

- Run Docker

- Build docker image based on Dockerfile (run cmd command where Dockerfile is stored)
  > docker build

- Build services described in docker-compose.yml
  > docker-compose build

- Create and start app containers
  > docker-compose up
