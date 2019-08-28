package com.upsolver.components;

import java.util.ArrayList;
import java.util.List;

public class Row {
    private List<String> data;

    public Row(List<String> data) {
        this.data = data;
    }

    public Row(String[] values) {
        List<String> tmpData = new ArrayList<String>();
        for (String value : values) {
            tmpData.add(value);
        }
        this.data = tmpData;
    }

    public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }

    public String getRowField(int index) {
        return data.get(index);
    }

    public static Row createRow(String[] data) {
        List<Row> rows = new ArrayList<>();
        return new Row(data);
    }

    public static Row createRow(String data) {
        List<Row> rows = new ArrayList<>();
        String[] dataArr = new String[1];
        dataArr[0] = data;
        return new Row(dataArr);
    }

    public String getRowDataWithDelimiter(String delimiter)  {
        StringBuilder sb = new StringBuilder();
        for(String currData : this.data) {
            sb.append(currData);
            sb.append(delimiter);
        }
        return sb.toString();
    }
}
