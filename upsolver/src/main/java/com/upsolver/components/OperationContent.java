package com.upsolver.components;

import java.util.List;

public class OperationContent {
    private Matrix matrix;
    private List<String> parameters;

    public OperationContent(Matrix matrix, List<String> parameters) {
        this.matrix = matrix;
        this.parameters = parameters;
    }

    public Matrix getMatrix() {
        return matrix;
    }

    public List<String> getParameters() {
        return parameters;
    }
}
