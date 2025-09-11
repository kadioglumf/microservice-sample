package com.kadioglumf.kafkaeventrouter;

class Router {
  static final String NO_ROUTE = "no-route-found";

  public static RouterBuilder builder() {
    return new RouterBuilder();
  }

  private final String routeTopic;
  private final String value;

  private Router(String routeTopic, String value) {
    this.routeTopic = routeTopic;
    this.value = value;
  }

  public String getRouteTopic() {
    return routeTopic;
  }

  public String getValue() {
    return value;
  }

  static class RouterBuilder {
    private String routeTopic;
    private String value;

    public RouterBuilder routeTopic(String routeTopic) {
      this.routeTopic = routeTopic;
      return this;
    }

    public RouterBuilder value(String value) {
      this.value = value;
      return this;
    }

    public Router build() {
      return new Router(routeTopic, value);
    }
  }
}
