package com.example.demo.exception;

import java.util.Calendar;

/**
 * This class contains information about exception
 */
public class ErrorInfo {
    public final Calendar timestamp;
    public final String url;
    public final String message;

    public ErrorInfo(String url, String message) {
        this.url = url;
        this.message = message;
        this.timestamp = Calendar.getInstance();
    }
}
