package pw.arcticwind.expressnow.model;

import java.util.ArrayList;
import java.util.List;

//快递信息, 对应返回的整个 JSON
public class ExpressInfo {
    private String status;
    private String com;
    private String comSpell;
    private String num;
    private String tel;
    private List<ExpressStep> expressStepList;

    public ExpressInfo(String status, String com, String comSpell, String num, String tel, List<ExpressStep> expressStepList) {
        this.comSpell = comSpell;
        this.com = com;
        this.expressStepList = expressStepList;
        this.num = num;
        this.status = status;
        this.tel = tel;
    }

    public String getCom() {
        return com;
    }

    public void setCom(String com) {
        this.com = com;
    }

    public List<ExpressStep> getExpressStepList() {
        return expressStepList;
    }

    public void setExpressStepList(List<ExpressStep> expressStepList) {
        this.expressStepList = expressStepList;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getComSpell() {
        return comSpell;
    }

    public void setComSpell(String comSpell) {
        this.comSpell = comSpell;
    }
}