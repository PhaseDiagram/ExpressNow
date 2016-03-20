package pw.arcticwind.expressnow.model.bmob;

import cn.bmob.v3.BmobObject;

//主题
public class Topic extends BmobObject {

    private String title;
    private Boolean good;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Boolean getGood() {
        return good;
    }

    public void setGood(Boolean good) {
        this.good = good;
    }

    public void clear() {
        title = null;
        good = null;
    }
}
