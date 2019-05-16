package ru.com.testdribbble.core.data.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import ru.com.testdribbble.core.data.response.BaseRESTModel;

@Getter
@Setter
public class Token extends BaseRESTModel implements Serializable {

    @SerializedName("access_token")
    private String accessToken;

    @SerializedName("token_type")
    private String tokenType;

    private String scope;

}
