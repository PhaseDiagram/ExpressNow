package pw.arcticwind.expressnow.model;

//快递最新进展
//用于收藏页, 历史页等
public class FavorCell {
    private String com;
    private String num;
    private String time;
    private String info;
    private int status;

    private String comPinyin;

    public FavorCell(String com, String num, int status, String time, String info, String comPinyin) {
        this.com = com;
        this.info = info;
        this.num = num;
        this.status = status;
        this.time = time;
        this.comPinyin = comPinyin;
    }


    public String getCom() {
        return com;
    }

    public void setCom(String com) {
        this.com = com;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getComPinyin() {
        return comPinyin;
    }

    public void setComPinyin(String comPinyin) {
        this.comPinyin = comPinyin;
    }
}
