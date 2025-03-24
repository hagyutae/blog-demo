package com.sb02.blogdemo.exception;

public class LoadDataFileFailure extends RuntimeException {
    public LoadDataFileFailure(String message) {
        super(message);
    }
}
