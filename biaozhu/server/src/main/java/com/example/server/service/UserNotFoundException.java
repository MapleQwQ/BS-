package com.example.server.service;

public class UserNotFoundException extends RuntimeException{
    UserNotFoundException(Long id){super("User #" + id + "cannot be found.");}
    UserNotFoundException(String userName){super("User："+userName+" cannot be found");}
}
