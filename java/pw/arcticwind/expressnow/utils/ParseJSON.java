package pw.arcticwind.expressnow.utils;

import android.content.Context;
import android.view.inputmethod.EditorInfo;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import pw.arcticwind.expressnow.model.ExpressInfo;
import pw.arcticwind.expressnow.model.ExpressStep;
import pw.arcticwind.expressnow.model.ExpressStepAdapter;
import pw.arcticwind.expressnow.model.FavorCell;
import pw.arcticwind.expressnow.model.bmob.Post;
import pw.arcticwind.expressnow.model.bmob.Topic;
import pw.arcticwind.expressnow.model.bmob.User;

//解析 JSON 的工具类
//都是供调用的静态方法
public class ParseJSON {

    public static String preParse(JSONObject jsonObject) {
        int errCode = -1;
        try {
            errCode = jsonObject.getInt("errCode");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        switch (errCode) {
            case 0:
                return "查询中...";
            case 1:
            case 6:
                return  "单号不存在!您是否选错了快递？";
            case 2:
                return  "验证码错误";
            case 3:
            case 4:
            case 5:
            case 7:
            case 10:
            case 20:
            case 21:
            case 22:
                return  "错误代码" + errCode;
            default:
                return  "未知错误代码" + errCode;
        }
    }


    public static ExpressInfo parseJSON(JSONObject jsonObject) {
        try {
            String com = jsonObject.getString("expTextName");
            String comSpell = jsonObject.getString("expSpellName");
            String num = jsonObject.getString("mailNo");
            String tel = jsonObject.getString("tel");
            int status = jsonObject.getInt("status");
            String statusStr = "";
            switch (status) {
                case 0:
                    statusStr = "查询失败";
                    break;
                case 1:
                    statusStr = "正常";
                    break;
                case 2:
                    statusStr = "派送中";
                    break;
                case 3:
                    statusStr = "已签收";
                    break;
                case 4:
                    statusStr = "退回";
                    break;
                case 5:
                    statusStr = "其他问题";
                    break;
                default:
                    statusStr = "未知状态";
                    break;
            }

            JSONArray jsonArray = jsonObject.getJSONArray("data");
            List<ExpressStep> expressStepList = new ArrayList<>();

            int length = jsonArray.length();
            for (int i = 0; i < length; i++) {
                JSONObject j = jsonArray.getJSONObject(i);
                String resultTime = j.getString("time");
                String resultContext = j.getString("context");
                ExpressStep expressStep = new ExpressStep(resultTime, resultContext);
                expressStepList.add(expressStep);
            }
            return new ExpressInfo(statusStr, com, comSpell, num, tel, expressStepList);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static FavorCell parseLatest(Context context, File file) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String jsonString = "", temp;
            while ((temp = reader.readLine()) != null) {
                jsonString += temp;
            }
            JSONObject jsonObject = new JSONObject(jsonString);
            return parseLatest(context, jsonObject);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Toast.makeText(context, "parseLatest error", Toast.LENGTH_SHORT).show();
        return null;
    }

    public static FavorCell parseLatest(Context context, JSONObject jsonObject) {
        try {
            String num = jsonObject.getString("mailNo");
            String com = jsonObject.getString("expTextName");
            int status = jsonObject.getInt("status");
            JSONObject jsonObject1 = jsonObject.getJSONArray("data").getJSONObject(0);
            String time = jsonObject1.getString("time");
            String info = jsonObject1.getString("context");
            String comPinyin = jsonObject.getString("expSpellName");
            FavorCell favorCell = new FavorCell(com, num, status, time, info, comPinyin);
            return favorCell;
        }catch (JSONException e) {
            e.printStackTrace();
        }

        Toast.makeText(context, "parseLatest error", Toast.LENGTH_SHORT).show();
        return null;
    }

    public static User parseUser(JSONArray jsonArray) {
        User user = new User();
        try {
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            user.setEmail(jsonObject.getString("email"));
            user.setPass(jsonObject.getString("pass"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return user;
    }

    public static List<Topic> parseTopics(JSONArray jsonArray) {
        int n = jsonArray.length();
        List<Topic> list = new ArrayList<>();
        try {
            for (int i = 0; i < n; i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Topic topic = new Topic();
                topic.setTitle(jsonObject.getString("title"));
                topic.setGood(jsonObject.getBoolean("good"));
                topic.setObjectId(jsonObject.getString("objectId"));
                list.add(topic);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }
    public static List<Post> parsePosts(JSONArray jsonArray) {
        int n = jsonArray.length();
        List<Post> list = new ArrayList<>();
        try {
            for (int i = 0; i < n; i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Post post = new Post();
                post.setBody(jsonObject.getString("body"));
                post.setTime(jsonObject.getString("updatedAt"));
                list.add(post);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }
}
