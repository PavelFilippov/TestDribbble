package ru.com.testdribbble.core.data.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import ru.com.testdribbble.core.data.response.BaseRESTModel;

@Getter
@Setter
public class Shot extends BaseRESTModel implements Serializable {

    private boolean animated;
    private String description;
    private int height;
    @SerializedName("html_url")
    private String htmlUrl;
    private long id;
    private Images images;
    private boolean low_profile;
    private List<String> tags;
    private String title;
    private int width;
    @SerializedName("published_at")
    private Date publishedAt;
    @SerializedName("updated_at")
    private Date updatedAt;

}
