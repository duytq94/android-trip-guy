package com.dfa.vinatrip.domains.chat;

import com.dfa.vinatrip.models.TypeMessage;
import com.google.gson.annotations.SerializedName;

/**
 * Created by duytq on 09/27/2017.
 */

public class BaseMessage {
    @SerializedName("content")
    private String content;
    @SerializedName("time_arrive")
    private long timestamp;
    @SerializedName("from_user")
    private String from;
    @SerializedName("to_group")
    private String to;
    @SerializedName("type_message")
    private TypeMessage typeMessage;

    public BaseMessage(String content, long timestamp, String from, String to, TypeMessage typeMessage) {
        this.content = content;
        this.timestamp = timestamp;
        this.from = from;
        this.to = to;
        this.typeMessage = typeMessage;
    }

    public TypeMessage getTypeMessage() {
        return typeMessage;
    }

    public void setTypeMessage(TypeMessage typeMessage) {
        this.typeMessage = typeMessage;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }
}
