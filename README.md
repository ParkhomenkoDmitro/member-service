# Member Service Application
## Epic/Story As a technical lead I want to have a “Member” service so that I can easily:
* Create a new member
* Read an existing member
* Update an existing member
* Delete members which are no longer used
* List existing members
* No frontend needed, only REST services are needed

## Build & Run:
1) cd project_folder_here
2) mvn clean install
3) java -jar ./target/member-service-0.1.0.jar

## API documentation
URL: http://localhost:8080/app/swagger-ui.html

## Usage
1) You should sign up a new admin or use default admin (login: "hachiko", pwd: "hachiko")

API for sign up: /admins/sign-up (see API documentation section)

2) Login with your new admin or use default admin, in response you will receive response Headers "authorization",
for example: "authorization": "Bearer eyJhbGciOiJIUzUxMiJ9.eyJqdGkiOiI2NDkzMzIxODk5MjU2Iiwic3ViIjoiaGFjaGlrbyJ9.2s4pc1uqSoKk0P0Hpd01nSEqR_k13xkYDaXb5AnGiVNIe_51kiC2QYaTBSn5qdRJuRzcWZ5AuYMRHu4wk8jj-A", 
Use it as Authorization parameter in all Member API requests.

API for login: /admins/login (see API documentation section)
