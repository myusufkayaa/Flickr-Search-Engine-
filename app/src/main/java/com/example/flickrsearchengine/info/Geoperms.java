
package com.example.flickrsearchengine.info;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Geoperms {

    @SerializedName("ispublic")
    @Expose
    private Integer ispublic;
    @SerializedName("iscontact")
    @Expose
    private Integer iscontact;
    @SerializedName("isfriend")
    @Expose
    private Integer isfriend;
    @SerializedName("isfamily")
    @Expose
    private Integer isfamily;

    public Integer getIspublic() {
        return ispublic;
    }

    public void setIspublic(Integer ispublic) {
        this.ispublic = ispublic;
    }

    public Integer getIscontact() {
        return iscontact;
    }

    public void setIscontact(Integer iscontact) {
        this.iscontact = iscontact;
    }

    public Integer getIsfriend() {
        return isfriend;
    }

    public void setIsfriend(Integer isfriend) {
        this.isfriend = isfriend;
    }

    public Integer getIsfamily() {
        return isfamily;
    }

    public void setIsfamily(Integer isfamily) {
        this.isfamily = isfamily;
    }

}
