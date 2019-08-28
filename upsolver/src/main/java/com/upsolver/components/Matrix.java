package com.upsolver.components;

import java.util.ArrayList;
import java.util.List;

public class Matrix {
    private List<Row> rows;

    public Matrix(List<Row> rows) {
        this.rows = rows;
    }

    public Matrix() {
        rows = new ArrayList<>();
    }

    public List<Row> getRows() {
        return rows;
    }

    public void setRows(List<Row> rows) {
        this.rows = rows;
    }

    public void addRow(Row row) {
        rows.add(row);
    }

    public void join(Matrix matrix) {
        if(matrix.getRows() != null && !matrix.getRows().isEmpty()) {
            for(Row row : matrix.getRows()) {
                this.addRow(row);
            }
        }
    }
}
