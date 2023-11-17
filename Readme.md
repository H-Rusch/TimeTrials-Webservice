## API - Endpoints

## Short Reference

| Endpoint | HTTP Method | description                                                                            | 
|----------|-------------|----------------------------------------------------------------------------------------|
| `/users` | `POST`      | Create a new user by submitting a `UserRequest` in JSON format.	                       |   
| `/times` | `POST`	     | Save a new time on a track for a player by submitting a `TimeRequest` in JSON format.	 |   

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

### /times POST

#### Request

```JSON
{
  "userId": "ce590211-3d51-4047-b154-3e0f9d581287",
  "track": "BABY_PARK",
  "time": "1:08.482"
}
```

#### Response

```JSON
{
  "username": "Example Name",
  "track": "Baby Park",
  "time": "1:08.482",
  "createdAt": "2023-08-14@15:07:11"
}
```
