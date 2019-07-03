
package com.example.flickrsearchengine.info;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Publiceditability {

    @SerializedName("cancomment")
    @Expose
    private Integer cancomment;
    @SerializedName("canaddmeta")
    @Expose
    private Integer canaddmeta;

    public Integer getCancomment() {
        return cancomment;
    }

    public void setCancomment(Integer cancomment) {
        this.cancomment = cancomment;
    }

    public Integer getCanaddmeta() {
        return canaddmeta;
    }

    public void setCanaddmeta(Integer canaddmeta) {
        this.canaddmeta = canaddmeta;
    }

}
