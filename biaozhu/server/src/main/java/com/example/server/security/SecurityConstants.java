package com.example.server.security;

public class SecurityConstants {
    //Determines a public endpoint to register the user
    public static final String SIGN_UP_URL = "/users/register";
    // Contains the key to sign the token
    public static final String KEY = "D8CFB2A7A337464F97BF6B60A5D3057C";
    public static final String SECRET = "SECRET_KEY";
    // Contains the name of the header you are going to add the JWT to when doing a request.
    public static final String HEADER_NAME = "Authorization";
    // Contains the time (in milliseconds) during which the token is valid before expiring.
    public static final Long EXPIRATION_TIME = 10000L*600*30; //10MIN
    public static final String TOKEN_PREFIX = "Bearer ";
}
