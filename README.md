# httpserver
Learning how to works a server with com.sun.net.httpserver

## Requires
* Java (1.8 o higher)
* Gradle
* Git

## Installation and execution

**1.** Clone, compile, and execute the application
```
$ git clone https://github.com/lsalamo/httpserver.git
$ cd httpserver
$ gradle buildJar
$ java -jar build/libs/httpserver-1.0.jar
```
If the application httpserver is executed correctly must to show the message showed below
```
Server is running on http://localhost:8000/app/index
```
**2.** Go to browser and view the next url to check that the server is running
If you want to see this in action, check out [localhost](http://localhost:8000/app/index)

## How does it work?

By default, he user repository will create three new users with their roles when you start the application.

| Page | Username | Password | Role |
| --- | --- | --- | --- |
| user 1 | user1 | 1234 | `PAGE_1` |
| user 2 | user2 | 1234 | `ADMIN` |
| user 3 | user3 | 1234 | `PAGE_1` `PAGE_2` |

**1.** Web pages

The server application has three pages available. You need permissions to access them

| Page | Role | Url |
| --- | --- | --- |
| page 1 | `PAGE_1` | http://localhost:8000/app/page1 |
| page 2 | `PAGE_2` | http://localhost:8000/app/page2 |
| page 3 | `PAGE_3` | http://localhost:8000/app/page3 |

**2.** API Rest

Exists an API Rest which has the methods GET and POST available. If you are logged with a role disctint to ADMIN to access a PUT or POST methods, the application will returned a forbidden exception. Below there are some examples using CURL called 

#### Get all users

```
curl -i -X GET --user user2:1234 http://localhost:8000/api/users
```

would result in JSON like:

```json
[{"id":1,"userName":"user1","roleList":["PAGE_1"],"admin":false},{"id":2,"userName":"user2","roleList":["ADMIN"],"admin":true},{"id":3,"userName":"user3","roleList":["PAGE_1","PAGE_2"],"admin":false}
```

#### Get specific user, in this case the user 1

```
curl -i -X GET --user user2:1234 http://localhost:8000/api/users/1
```

would result in JSON like:

```json
{"id":1,"userName":"user1","roleList":["PAGE_1"],"admin":false}
```

#### Add new user

```
curl -i -X POST --data "user=user&pwd=1234&role=PAGE_1,PAGE_2" --user user2:1234 http://localhost:8000/api/users
```

would result in JSON like:

```json
{"id":4,"userName":"user","roleList":["PAGE_1","PAGE_2"],"admin":false}
```
#### Update specific user, in this case the user 3

```
curl -i -X POST --data "user=user3&pwd=1234&role=PAGE_1,PAGE_2,PAGE_3" --user user2:1234 http://localhost:8000/api/users/3
```

would result in JSON like:

```json
{"id":3,"userName":"user3","roleList":["PAGE_1","PAGE_2","PAGE_3"],"admin":false}
```

