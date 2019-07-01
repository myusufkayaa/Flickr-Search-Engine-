
package Info;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Usage {

    @SerializedName("candownload")
    @Expose
    private Integer candownload;
    @SerializedName("canblog")
    @Expose
    private Integer canblog;
    @SerializedName("canprint")
    @Expose
    private Integer canprint;
    @SerializedName("canshare")
    @Expose
    private Integer canshare;

    public Integer getCandownload() {
        return candownload;
    }

    public void setCandownload(Integer candownload) {
        this.candownload = candownload;
    }

    public Integer getCanblog() {
        return canblog;
    }

    public void setCanblog(Integer canblog) {
        this.canblog = canblog;
    }

    public Integer getCanprint() {
        return canprint;
    }

    public void setCanprint(Integer canprint) {
        this.canprint = canprint;
    }

    public Integer getCanshare() {
        return canshare;
    }

    public void setCanshare(Integer canshare) {
        this.canshare = canshare;
    }

}
