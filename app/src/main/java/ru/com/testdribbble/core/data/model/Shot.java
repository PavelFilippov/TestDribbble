package ru.com.testdribbble.core.data.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;
import lombok.Getter;
import lombok.Setter;
import ru.com.testdribbble.core.data.response.Error;

@Getter
@Setter
public class Shot extends RealmObject implements Serializable {

    private boolean animated;
    private String description;
    private int height;
    @SerializedName("html_url")
    private String htmlUrl;
    @PrimaryKey
    private long id;
    private Images images;
    private boolean low_profile;
    private RealmList<String> tags;
    private String title;
    private int width;
    @SerializedName("published_at")
    private Date publishedAt;
    @SerializedName("updated_at")
    private Date updatedAt;

    @Ignore
    public String message;
    @Ignore
    public List<Error> errors;

}
