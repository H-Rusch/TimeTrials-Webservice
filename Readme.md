# Summary

This application is a web-service that provides a REST API and uses JPA to connect to a database.

The service's purpose is to save times driven in Mario Kart.

# API - Endpoints

## Short Reference

| Endpoint | HTTP Method  | description                           | 
|----------|--------------|---------------------------------------|
| `/users` | `POST`       | Create a new user	                    |   
| `/times` | `POST`	      | Save a new time on a track for a user |   

## Detailed overview

### /users POST

Create a user in the database. The username has to be unique to the database and the passwords have to match. The new user has to be given as a `UserRequest` in `JSON` format.

If everything went well, a response detailing the username and userId of the created user will be returned.

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

Save a new time on a track for a user. The time has to be submitted as `TimeRequest` in JSON format.

- The time has to be given matching the following regex: `\d+:\d{2}.\d{3}`.
- The track has to be given as either the enum-constant or the enum-value's pretty-name. So for the track 'Baby Park' it has to be given as either `BABY_PARK_GCN` or `[GCN] Baby Park`.

Once the time was saved successfully, a response containing details about the created time will be returned.

#### Request

```JSON
{
  "userId": "ce590211-3d51-4047-b154-3e0f9d581287",
  "track": "BABY_PARK_GCN",
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
