package ru.com.testdribbble.core.data.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import ru.com.testdribbble.core.data.response.BaseRESTModel;

@Getter
@Setter
public class User extends BaseRESTModel implements Serializable {

    @SerializedName("avatar_url")
    private String avatarUrl;
    private String bio;
    @SerializedName("can_upload_shot")
    private boolean canUploadShot;
    @SerializedName("created_at")
    private Date createdAt;
    @SerializedName("followers_count")
    private int followersCount;
    @SerializedName("html_url")
    private String htmlUrl;
    private long id;
    private String location;
    private String login;
    private String name;
    private boolean pro;
    private String type;

}
