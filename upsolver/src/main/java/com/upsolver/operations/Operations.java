package com.upsolver.operations;

import com.upsolver.components.Matrix;
import com.upsolver.components.OperationContent;
import com.upsolver.components.Row;

import java.util.Collections;
import java.util.List;
import java.util.OptionalDouble;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Operations {
    private static final int SEARCH_BY_INDEX_PARAMETER = 0; // the first field of the parameters list is for the INDEX of the field to process upon.
    private static final int SEARCH_BY_VALUE_PARAMETER = 1; // the second field of the parameters is for the value to filter in the filter operation.

    /**
     * The max aggregation function takes OperationContent = matrix and parameters and retrieves a matrix with one row.
     * The row contains one cell that holds the maximum value of the first field (SEARCH_BY_INDEX_PARAMETER)
     * from the input matrix.
     * @param OperationContent
     * @returns Matrix
     *
     */
    public static Function<OperationContent, Matrix> max = input -> {
        try {
            OptionalDouble min = input.getMatrix().getRows()
                    .stream()
                    .filter(o -> o.getRowField(SEARCH_BY_INDEX_PARAMETER) != null)
                    .mapToDouble(r -> Double.parseDouble(r.getRowField(SEARCH_BY_INDEX_PARAMETER))).max();
            if(min.isPresent()) {
                Row row = Row.createRow(String.valueOf(min.getAsDouble()));
                return new Matrix(Collections.singletonList(row));
            } else {
                return null;
            }
        } catch (Exception e) {
            System.out.println("Invalid MAX operation on field 0");
            return null;
        }
    };

    /**
     * The min aggregation function takes OperationContent = matrix and parameters and retrieves a matrix with one row.
     * The row contains one cell that holds the minimum value of the first field (SEARCH_BY_INDEX_PARAMETER)
     * from the input matrix.
     * @param OperationContent
     * @returns Matrix
     *
     */
    public static Function<OperationContent, Matrix> min = input -> {
        try {
            OptionalDouble min = input.getMatrix().getRows()
                    .stream()
                    .filter(o -> o.getRowField(SEARCH_BY_INDEX_PARAMETER) != null)
                    .mapToDouble(r -> Double.parseDouble(r.getRowField(SEARCH_BY_INDEX_PARAMETER))).min();
            if(min.isPresent()) {
                Row row = Row.createRow(String.valueOf(min.getAsDouble()));
                return new Matrix(Collections.singletonList(row));
            } else {
                return null;
            }
        } catch (Exception e) {
            System.out.println("Invalid MIN operation on field 0");
            return null;
        }
    };

    /**
     * The avg aggregation function takes OperationContent = matrix and parameters and retrieves a matrix with one row.
     * The row contains one cell that holds the average value of the first field (SEARCH_BY_INDEX_PARAMETER)
     * from the input matrix.
     * @param OperationContent
     * @returns Matrix
     *
     */
    public static Function<OperationContent, Matrix> avg = input -> {
        try {
            OptionalDouble min = input.getMatrix().getRows()
                    .stream()
                    .filter(o -> o.getRowField(SEARCH_BY_INDEX_PARAMETER) != null)
                    .mapToDouble(r -> Double.parseDouble(r.getRowField(SEARCH_BY_INDEX_PARAMETER))).average();
            if(min.isPresent()) {
                Row row = Row.createRow(String.valueOf(min.getAsDouble()));
                return new Matrix(Collections.singletonList(row));
            } else {
                return null;
            }
        } catch (Exception e) {
            System.out.println("Invalid AVG operation on field 0");
            return null;
        }
    };

    /**
     * The sum aggregation function takes OperationContent = matrix and parameters and retrieves a matrix with one row.
     * The row contains one cell that holds the sum value of the first field (SEARCH_BY_INDEX_PARAMETER)
     * from the input matrix.
     * @param OperationContent
     * @returns Matrix
     *
     */
    public static Function<OperationContent, Matrix> sum = input -> {
        try {
            Double min = input.getMatrix().getRows()
                    .stream()
                    .filter(o -> o.getRowField(SEARCH_BY_INDEX_PARAMETER) != null)
                    .mapToDouble(r -> Double.parseDouble(r.getRowField(SEARCH_BY_INDEX_PARAMETER))).sum();
            Row row = Row.createRow(String.valueOf(min));
            return new Matrix(Collections.singletonList(row));
        } catch (Exception e) {
            System.out.println("Invalid SUM operation on field 0");
            return null;
        }
    };

    /**
     * The ceil aggregation function takes OperationContent = matrix and parameters and retrieves a matrix with multiple rows.
     * Each row contains one cell that holds the rounded up value of the first field (SEARCH_BY_INDEX_PARAMETER)
     * from the input matrix.
     * @param OperationContent
     * @returns Matrix
     *
     */
    public static Function<OperationContent, Matrix> ceil = input -> {
        try {
            List<Row> rows = input.getMatrix()
                    .getRows()
                    .stream()
                    .filter(o -> o.getRowField(SEARCH_BY_INDEX_PARAMETER) != null)
                    .map(o -> Row.createRow(String.valueOf(
                            Math.ceil(Double.parseDouble(o.getRowField(SEARCH_BY_INDEX_PARAMETER))))))
                    .collect(Collectors.toList());
            return new Matrix(rows);
        } catch (Exception e) {
            System.out.println("Invalid CEIL operation on field 0");
            return null;
        }
    };

    /**
     * The pluck transformation function takes OperationContent = matrix and parameters and retrieves a matrix
     * with the SEARCH_BY_INDEX_PARAMETER’th field (zero indexed) as row from every row
     * @param OperationContent
     * @returns Matrix
     *
     */
    public static Function<OperationContent, Matrix> pluck = input -> {
        int fieldIndex = Integer.parseInt(input.getParameters().get(SEARCH_BY_INDEX_PARAMETER));
        List<Row> collect = input.getMatrix()
                .getRows()
                .stream()
                .filter(o -> o.getRowField(fieldIndex) != null)
                .map(o -> Row.createRow(o.getRowField(fieldIndex)))
                .collect(Collectors.toList());
        return new Matrix(collect);
    };

    /**
     * The filter transformation function takes OperationContent = matrix and parameters and retrieves a matrix
     * of all the rows where SEARCH_BY_INDEX_PARAMETER’th field is equal to SEARCH_BY_VALUE_PARAMETER
     * @param OperationContent
     * @returns Matrix
     *
     */
    public static Function<OperationContent, Matrix> filter = input -> {
        int fieldIndexParameter = Integer.parseInt(input.getParameters().get(SEARCH_BY_INDEX_PARAMETER));
        String searchValueParameter = input.getParameters().get(SEARCH_BY_VALUE_PARAMETER)
                .toLowerCase()
                .trim()
                .replaceAll("'", "");

        List<Row> collect = input.getMatrix()
                .getRows()
                .stream()
                .filter(o -> o.getRowField(fieldIndexParameter).toLowerCase().trim()
                        .equals(searchValueParameter))
                .collect(Collectors.toList());
        return new Matrix(collect);
    };

}
