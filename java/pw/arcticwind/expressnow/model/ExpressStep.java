package pw.arcticwind.expressnow.model;

//快递步骤, 对应快递的每一步, RecyclerView 的单个 item
public class ExpressStep {
    private String stepTime;
    private String stepContext;

    public ExpressStep(String stepTime, String stepContext) {
        this.stepContext = stepContext;
        this.stepTime = stepTime;
    }

    public String getStepContext() {
        return stepContext;
    }

    public void setStepContext(String stepContext) {
        this.stepContext = stepContext;
    }

    public String getStepTime() {
        return stepTime;
    }

    public void setStepTime(String stepTime) {
        this.stepTime = stepTime;
    }
}
