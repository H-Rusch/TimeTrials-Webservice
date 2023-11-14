## API - Endpoints

## Short Reference

| Endpoint | HTTP Method | description                                                     | 
|----------|-------------|-----------------------------------------------------------------|
| `/users` | `POST`      | Create a new user by submitting a `UserRequest`in JSON format.	 |   
|          | 	           | 	                                                               |   

## Example Requests

### /users POST

#### Request

```JSON
{
    "username": "Example Name",
    "password": "abc123456789",
    "repeatedPassword": "abc123456789"
}
```

#### Response

```JSON
{
    "userId": "ce590211-3d51-4047-b154-3e0f9d581287",
    "username": "Example Name"
}
```
