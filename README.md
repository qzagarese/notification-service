A simple notification center back-end service
=====================

A simple API to search for last notifications, mark them as read or unread and possibly delete them.

## Running the API
- Create a fat jar running `mvn package from` the directory containing the pom.xml file.
- Populate the sample database running `java -jar target/notification-service-0.0.1-SNAPSHOT.jar db migrate config.yml`
- Run the API: `java -jar target/notification-service-0.0.1-SNAPSHOT.jar server config.yml`   


## API Doc

### Get latest notifications
Gets the latest notifications for the requesting user from "since" on.
If "since" is not specified, it returns the latest notifications from last 24 hours.  
Items are grouped by eventType and each group is ordered by eventTimestamp desc.
 
`GET /notifications/latest?since={since}&until={until}`

#####Parameters
* since: unix timestamp. The instant to start from (included). Defaults to `NOW - 24hours`
* until: unix timestamp. The instant to stop (excluded). Defaults to `NOW`


#####Request Example
This request returns all the notifications for the provided credentials

`GET /notifications/latest?since=0 HTTP/1.1`

`Host: localhost:8443`

`Authorization: Basic U2FtOmJheg==`

`Cache-Control: no-cache`

#####Returns
* A list of notifications grouped by type. Each group is ordered by eventTimestamp from latest to oldest 
* type : json
* Status : 200
    
#####Example output:

```javascript

	{
    "content": {
        "CAP_TRESHOLD_REACHED": {
            "content": [
                {
                    "guid": "ba54a7e4-6211-4921-8c4d-7fb522f9046d",
                    "deviceGuid": "dcd7f334-6625-4552-b32f-78afaf545949",
                    "eventType": "CAP_TRESHOLD_REACHED",
                    "title": "Data cap threshold",
                    "content": "8MB Remaining until 10/2/2014",
                    "eventTimestamp": 1390439640000,
                    "sentTimestamp": 1390439670000,
                    "read": true
                }
            ],
            "items": 1,
            "total": 1,
            "unread": 0
        },
        "SITE_BLOCKED": {
            "content": [
                {
                    "guid": "fb97d0fd-e043-4d78-9093-165e2e36d3f4",
                    "deviceGuid": "dcd7f334-6625-4552-b32f-78afaf545949",
                    "eventType": "SITE_BLOCKED",
                    "eventSubtype": "FACEBOOK_APP",
                    "title": "Site/App block",
                    "content": "Facebook App",
                    "eventTimestamp": 1390476120000,
                    "sentTimestamp": 1390476180000,
                    "read": true
                },
                {
                    "guid": "0ec61cee-23c3-402d-aac3-d98cc18701f3",
                    "deviceGuid": "879c4f18-4c6e-462d-afa4-1866c0e38b18",
                    "eventType": "SITE_BLOCKED",
                    "eventSubtype": "CNN_COM",
                    "title": "Site/App block",
                    "content": "www.cnn.co.uk",
                    "eventTimestamp": 1390458840000,
                    "sentTimestamp": 1390458870000,
                    "read": false
                },
                {
                    "guid": "aa926c4c-b120-4178-b105-7c25b94cc916",
                    "deviceGuid": "879c4f18-4c6e-462d-afa4-1866c0e38b18",
                    "eventType": "SITE_BLOCKED",
                    "title": "Security block",
                    "content": "www.badsite.com",
                    "eventTimestamp": 1390448040000,
                    "sentTimestamp": 1390448070000,
                    "read": false
                }
            ],
            "items": 3,
            "total": 3,
            "unread": 2
        }
	    "items": 4,
	    "total": 4,
	    "unread": 2	
	}
```   

### Read notification
Returns a specific notification and sets it as read
 
`PATCH /notifications/{guid}`

#####Parameters
* guid: the notification guid


#####Request Example

`PATCH /notifications/cbe01d4d-43fb-4bee-a43e-9421f5b34f41 HTTP/1.1`

`Host: localhost:8443`

`Authorization: Basic U2FtOmJheg==`

`Cache-Control: no-cache`

#####Returns
* The requested notification 
* type : json
* Status : 200 (404 if the notification does not exist)
    
#####Example output:

```javascript

	{
	    "guid": "cbe01d4d-43fb-4bee-a43e-9421f5b34f41",
	    "deviceGuid": "879c4f18-4c6e-462d-afa4-1866c0e38b18",
	    "eventType": "CAP_EXTENDED",
	    "title": "Data cap threshold",
	    "content": "100MB added.  108MB remaining until 10/2/2014",
	    "eventTimestamp": 1390444440000,
	    "sentTimestamp": 1390444490000,
	    "read": true
	}
```   

### Mark notification as read
Sets the specified notification as read
 
`PATCH /notifications/{guid}/read`

#####Parameters
* guid: the notification guid


#####Request Example

`PATCH /notifications/cbe01d4d-43fb-4bee-a43e-9421f5b34f41/read HTTP/1.1`

`Host: localhost:8443`

`Authorization: Basic U2FtOmJheg==`

`Cache-Control: no-cache`


#####Returns
* type : void
* Status : 200 (404 if the notification does not exist)
    

### Mark notification as unread
Sets the specified notification as read
 
`PATCH /notifications/{guid}/unread`

#####Parameters
* guid: the notification guid


#####Request Example

`PATCH /notifications/cbe01d4d-43fb-4bee-a43e-9421f5b34f41/unread HTTP/1.1`

`Host: localhost:8443`

`Authorization: Basic U2FtOmJheg==`

`Cache-Control: no-cache`


#####Returns
* type : void
* Status : 200 (404 if the notification does not exist)
    
### Delete a notification
Deletes the specified notification
 
`DELETE /notifications/{guid}`

#####Parameters
* guid: the notification guid


#####Request Example

`DELETE /notifications/cbe01d4d-43fb-4bee-a43e-9421f5b34f41 HTTP/1.1`

`Host: localhost:8443`

`Authorization: Basic U2FtOmJheg==`

`Cache-Control: no-cache`

#####Body
empty

#####Returns
* type : void
* Status : 204 (404 if the notification does not exist)
    


    
