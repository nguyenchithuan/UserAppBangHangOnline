package edu.wkd.userappbanghangonline.model.request;

import java.util.Map;

public class FCMRequest {
   private String to;
   private Map<String,String> notification;

    public FCMRequest(String to, Map<String, String> notification) {
        this.to = to;
        this.notification = notification;
    }

    public FCMRequest() {
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public Map<String, String> getNotification() {
        return notification;
    }

    public void setNotification(Map<String, String> notification) {
        this.notification = notification;
    }
}
