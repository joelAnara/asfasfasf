package com.example.demo.dto;

public class VerificationResponse {
    private boolean success;
    private boolean exists;
    private String message;

    public VerificationResponse() {}

    public VerificationResponse(boolean success, boolean exists, String message) {
        this.success = success;
        this.exists = exists;
        this.message = message;
    }

    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }
    public boolean isExists() { return exists; }
    public void setExists(boolean exists) { this.exists = exists; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}
