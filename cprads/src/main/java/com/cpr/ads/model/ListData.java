package com.cpr.ads.model;

import java.util.List;

/**
 * @author Created by Pear on 8/2/2019.
 * lấy thông tin từ API, gồm 2 trường success và data(chứa list item)
 */
public class ListData {
    private List<ItemData> data;
    private int success;

    public List<ItemData> getData() {
        return data;
    }

    public void setData(List<ItemData> data) {
        this.data = data;
    }

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    @Override
    public String toString() {
        return
                "Response{" +
                        "data = '" + data + '\'' +
                        ",success = '" + success + '\'' +
                        "}";
    }
}
