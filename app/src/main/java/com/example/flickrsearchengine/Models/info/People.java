
package com.example.flickrsearchengine.Models.info;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class People {

    @SerializedName("haspeople")
    @Expose
    private Integer haspeople;

    public Integer getHaspeople() {
        return haspeople;
    }

    public void setHaspeople(Integer haspeople) {
        this.haspeople = haspeople;
    }

}
