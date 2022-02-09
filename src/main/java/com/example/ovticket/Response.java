package com.example.ovticket;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.lang3.StringUtils;

public interface Response {

    String getCode();

    String getMessage();

    @JsonIgnore
    String getChannel();

    @JsonIgnore
    default boolean isSuccess() {
        return StringUtils.equals("200", getCode());
    }
}
