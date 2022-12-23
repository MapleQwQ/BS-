package com.example.server.service;

public class ArticleNotFoundException extends RuntimeException {

  ArticleNotFoundException(Long id) {
    super("Article #" + id + " cannot be found.");
  }
}
