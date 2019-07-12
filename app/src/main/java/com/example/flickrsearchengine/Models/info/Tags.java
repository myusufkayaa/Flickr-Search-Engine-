
package com.example.flickrsearchengine.Models.info;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Tags {

    @SerializedName("tag")
    @Expose
    private List<Object> tag = null;

    public List<Object> getTag() {
        return tag;
    }

    public void setTag(List<Object> tag) {
        this.tag = tag;
    }

}
