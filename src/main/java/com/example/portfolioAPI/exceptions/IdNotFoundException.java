package com.example.portfolioAPI.exceptions;

public class IdNotFoundException extends RuntimeException{
    public IdNotFoundException(Long id){
        super("Transaction with ID "+ id +" not found!");
    }
}
