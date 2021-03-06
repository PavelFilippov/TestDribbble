package ru.com.testdribbble.core.data.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import io.realm.RealmObject;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Images extends RealmObject implements Serializable {

    private String hidpi;
    private String normal;
    @SerializedName("one_x")
    private String oneX;
    @SerializedName("two_x")
    private String twoX;
    private String teaser;

}
