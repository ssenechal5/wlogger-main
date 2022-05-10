package com.actility.thingpark.wlogger.model;

import com.actility.thingpark.wlogger.dto.ElementLfd;
import com.actility.thingpark.wlogger.entity.history.Utils;
import io.vertx.core.json.JsonObject;

public class Lfd {
  public final String dfc;
  public final Integer cnt;

  public Lfd(Builder builder) {
    this.dfc = builder.dfc;
    this.cnt = builder.cnt;
  }

  public static Builder builder() {
    return new Builder();
  }

  public ElementLfd getAsElement() {
    return new ElementLfd(Utils.getToString(dfc), Utils.getToString(cnt));
  }

  public JsonObject getAsJson() {
    return new JsonObject().put("dfc", dfc).put("cnt", cnt);
  }

  public String getAsCsv() {
    return dfc + "=" + cnt;
  }

  public static final class Builder {
    protected String dfc;
    protected Integer cnt;

    private Builder() {
    }

    public Builder withDfc(String dfc) {
      this.dfc = dfc;
      return this;
    }

    public Builder withCnt(Integer cnt) {
      this.cnt = cnt;
      return this;
    }

    public Lfd build() {
      return new Lfd(this);
    }
  }
}
