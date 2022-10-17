package com.mytechnodev.redisdemo.pojos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ResponseDataPOJO {
    @JsonProperty("status_code")
    @SerializedName("status_code")
    String statusCode;
    String message;
    @JsonProperty("response_date")
    @SerializedName("response_date")
    String responseDate;
    List<Data> result = new ArrayList<>();

    @lombok.Data
    public static class Data{
        String id;
        String name;
        Integer status;
        @JsonProperty("created_at")
        @SerializedName("created_at")
        String createdAt;
    }
}
