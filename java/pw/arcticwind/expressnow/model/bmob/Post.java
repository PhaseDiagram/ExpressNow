package pw.arcticwind.expressnow.model.bmob;

import cn.bmob.v3.BmobObject;

//帖子
public class Post extends BmobObject {
    private String time;
    private String user;
    private String body;
    private String parent;

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }
}
