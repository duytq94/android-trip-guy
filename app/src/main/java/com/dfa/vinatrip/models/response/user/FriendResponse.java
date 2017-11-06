package com.dfa.vinatrip.models.response.user;

import com.google.gson.annotations.SerializedName;

/**
 * Created by duytq on 11/06/2017.
 */

public class FriendResponse {
    @SerializedName("id")
    private long id;
    @SerializedName("user_request")
    private long idUserRequest;
    @SerializedName("user_receive")
    private long idUserReceive;
    @SerializedName("status")
    private int status;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getIdUserRequest() {
        return idUserRequest;
    }

    public void setIdUserRequest(long idUserRequest) {
        this.idUserRequest = idUserRequest;
    }

    public long getIdUserReceive() {
        return idUserReceive;
    }

    public void setIdUserReceive(long idUserReceive) {
        this.idUserReceive = idUserReceive;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
