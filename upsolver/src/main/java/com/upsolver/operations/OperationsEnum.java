package com.upsolver.operations;

import com.upsolver.components.Matrix;
import com.upsolver.components.OperationContent;

import java.util.function.Function;

public enum OperationsEnum {
    SUM ("sum", Operations.sum, false),
    AVG("avg", Operations.avg, false),
    MIN("min", Operations.min, false),
    MAX("max", Operations.max, false),
    CEIL("ceil", Operations.ceil, false),
    PLUCK("pluck", Operations.pluck, true),
    FILTER("filter", Operations.filter, true);

    private final String name;
    private final Function<OperationContent, Matrix> function;
    private final boolean isTransformation;

    OperationsEnum(String name, Function<OperationContent, Matrix> function, boolean isTransformation) {
        this.name = name;
        this.function = function;
        this.isTransformation = isTransformation;
    }

    public String getName() {
        return name;
    }

    public boolean isTransformation() {
        return isTransformation;
    }

    public Function<OperationContent, Matrix> getFunction() {
        return function;
    }
}
