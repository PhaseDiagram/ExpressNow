package pw.arcticwind.expressnow.model.bmob;

import android.graphics.drawable.Drawable;

import cn.bmob.v3.BmobObject;

//用户
public class User extends BmobObject {
    private String email;
    private String pass;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }
}
