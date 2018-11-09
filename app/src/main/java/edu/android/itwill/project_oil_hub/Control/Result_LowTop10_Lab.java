package edu.android.itwill.project_oil_hub.Control;

import edu.android.itwill.project_oil_hub.Model.Result_LowTop10;

public class Result_LowTop10_Lab {

    public static Result_LowTop10_Lab instance = null;

    private Result_LowTop10 resultLowTop10;

    private Result_LowTop10_Lab() {
        resultLowTop10 = new Result_LowTop10();
    }

    public static Result_LowTop10_Lab getInstance() {
        if(instance == null)
            instance = new Result_LowTop10_Lab();

        return instance;
    }

    public Result_LowTop10 getResultLowTop10() {
        return resultLowTop10;
    }

    public void setResultLowTop10(Result_LowTop10 resultLowTop10) {
        this.resultLowTop10 = resultLowTop10;
    }
}
