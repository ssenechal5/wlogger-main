package com.actility.thingpark.wlogger.dto;

public class ElementDestinations implements Element {

  protected Integer idx;
  protected String url;
  protected String status;
  protected String errorMessage;

  public ElementDestinations() { super();}

  public ElementDestinations(Integer idx, String url, String status, String errorMessage) {
    super();
    this.idx = idx;
    this.url = url;
    this.status = status;
    this.errorMessage = errorMessage;
  }

  public Integer getIdx() {
    return idx;
  }

  public String getUrl() {
    return url;
  }

  public String getStatus() {
    return status;
  }

  public String getErrorMessage() {
    return errorMessage;
  }
}
