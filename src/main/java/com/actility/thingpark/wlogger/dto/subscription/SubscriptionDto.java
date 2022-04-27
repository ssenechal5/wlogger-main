package com.actility.thingpark.wlogger.dto.subscription;

import com.actility.thingpark.wlogger.entity.Auditable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

@XmlRootElement(name = "subscription")
@XmlAccessorType(XmlAccessType.FIELD)
public class SubscriptionDto {

  private OccContext occContext;
  private Date creationTimestamp;
  //@XML
  private String alarmStateNotifications;
  private State state;
  private Subscriber subscriber;

  public SubscriptionDto() {
    // Default constructor
  }

  public SubscriptionDto occContext(Auditable a) {
    this.occContext = new OccContext(a);
    return this;
  }

  public SubscriptionDto alarmStateNotifications(String alarmStateNotifications) {
    this.alarmStateNotifications = alarmStateNotifications;
    return this;
  }

  public SubscriptionDto creationTimestamp(Date date) {
    this.creationTimestamp = date;
    return this;
  }

  public SubscriptionDto state(String value, Date timestamp) {
    this.state = new State();
    this.state.value = value;
    this.state.timestamp = timestamp;
    return this;
  }

  public SubscriptionDto subscriber(String id, String extID, String organization, String name) {
    this.subscriber = new Subscriber();
    this.subscriber.id = id;
    this.subscriber.extID = extID;
    this.subscriber.organization = organization;
    this.subscriber.name = name;
    return this;
  }

  public Date getCreationTimestamp() {
    return this.creationTimestamp;
  }

  public String getAlarmStateNotifications() {
    return this.alarmStateNotifications;
  }

  public State getState() {
    return this.state;
  }

  public String getStateValue() {
    return this.state.value;
  }

  public Date getStateTimestamp() {
    return this.state.timestamp;
  }

  public Subscriber getSubscriber() {
    return this.subscriber;
  }

  public String getSubscriberId() {
    return this.subscriber.id;
  }

  public String getSubscriberOrganization() {
    return this.subscriber.organization;
  }

  public String getSubscriberName() {
    return this.subscriber.name;
  }

  @XmlAccessorType(XmlAccessType.FIELD)
  static class State {
    private String value;
    private Date timestamp;
  }

  @XmlAccessorType(XmlAccessType.FIELD)
  static class Subscriber {

    @XmlElement(name = "ID")
    private String id;

    private String extID;
    private String organization;
    private String name;
  }
}
