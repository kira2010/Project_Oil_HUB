package edu.android.itwill.project_oil_hub.Model;

import java.util.ArrayList;
import java.util.List;

public class Result_LowTop10 {
    private List<Oil_LowTop10> list;

    public Result_LowTop10() {
        list = new ArrayList<>();
    }

    public Result_LowTop10(List<Oil_LowTop10> list) {
        this.list = list;
    }

    public List<Oil_LowTop10> getList() {
        return list;
    }

    public void setList(List<Oil_LowTop10> list) {
        this.list = list;
    }
}
