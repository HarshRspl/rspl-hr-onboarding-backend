package com.rspl.onboarding.api;

public class ApiResponse<T> {
  private boolean success;
  private String message;
  private T data;
  private Object details;

  public ApiResponse() {}

  public static <T> ApiResponse<T> ok(T data, String message) {
    ApiResponse<T> r = new ApiResponse<>();
    r.success = true;
    r.message = message;
    r.data = data;
    return r;
  }

  public static <T> ApiResponse<T> ok(T data) { return ok(data, "OK"); }

  public static <T> ApiResponse<T> fail(String message, Object details) {
    ApiResponse<T> r = new ApiResponse<>();
    r.success = false;
    r.message = message;
    r.details = details;
    return r;
  }

  public boolean isSuccess() { return success; }
  public void setSuccess(boolean success) { this.success = success; }
  public String getMessage() { return message; }
  public void setMessage(String message) { this.message = message; }
  public T getData() { return data; }
  public void setData(T data) { this.data = data; }
  public Object getDetails() { return details; }
  public void setDetails(Object details) { this.details = details; }
}
