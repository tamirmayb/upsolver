package com.upsolver.operations;

import com.upsolver.components.Matrix;
import com.upsolver.components.OperationContent;
import com.upsolver.components.Row;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class OperationsManager {
    private Map<OperationsEnum, List<String>> operations = new LinkedHashMap<>();
    private List<String> fieldList = new ArrayList<>();
    private Matrix matrix = new Matrix();

    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm");

    /**
     * Constructor for OperationsManager, creates the operations list for the process
     * @param operationsStr
     */
    public OperationsManager(String operationsStr) {
        System.out.println("Processing operations for: " + operationsStr);
        if(operationsStr != null && !operationsStr.isEmpty()) {
            convertOperationsStrToList(operationsStr);
            if(operations.isEmpty()) {
                System.out.println("No operations found");
            }
        } else {
            System.out.println("No operations found");
        }
    }

    /**
     * Each row of the file is converted to a matrix, transformation operations need to be performed at this point
     * since they produce a matrix with a different layout, so each row is processed and added to the final matrix result.
     * @param rowData
     * @return
     */
    public boolean processDataRow(String[] rowData) {
        if(this.fieldList.isEmpty()) {
            this.fieldList.addAll(Arrays.asList(rowData));
            //validate input parameters
            return checkNumberOfFieldsInParams();
        } else if(this.operations.entrySet().stream().findFirst().isPresent()) {
            Matrix matrix = new Matrix();
            matrix.addRow(Row.createRow(rowData));
            this.matrix.join(runTransformationOperations(matrix));
        }
        return true;
    }

    /**
     * After processing each row the final matrix result is processed by the aggregation operations.
     * Finally, the final aggregated matrix is saved to csv file
     */
    public void finalizeProcess() {
        Matrix matrix = runAggregationOperations(this.matrix);
        if(matrix != null && matrix.getRows() != null && !matrix.getRows().isEmpty()) {
            writeMatrixToCSV(matrix);
        } else {
            System.out.println("No results found for this query");
        }
    }

    //iterate and run each transformation operation recursively
    private Matrix runTransformationOperations(Matrix matrix) {
        Matrix resultMatrix = matrix;
        for(Map.Entry<OperationsEnum, List<String>> operation : operations.entrySet()) {
            if (operation.getKey().isTransformation()) {
                if(resultMatrix != null) {
                    resultMatrix = doRunOperation(resultMatrix, operation);
                }
            }
        }
        return resultMatrix;
    }

    //iterate and run each aggregation operation recursively
    private Matrix runAggregationOperations(Matrix matrix) {
        Matrix resultMatrix = matrix;
        for (Map.Entry<OperationsEnum, List<String>> operation : operations.entrySet()) {
            if (!operation.getKey().isTransformation()) {
                if(resultMatrix != null) {
                    resultMatrix = doRunOperation(resultMatrix, operation);
                }
            }
        }
        return resultMatrix;
    }

    private Matrix doRunOperation(Matrix matrix, Map.Entry<OperationsEnum, List<String>> operation) {
        return operation.getKey().getFunction().apply(new OperationContent(matrix, operation.getValue()));
    }

    private void convertOperationsStrToList(String operationsStr) {
        String[] operationsArr = operationsStr.trim().toLowerCase().split("->");
        for(String operationStrTmp : operationsArr) {
            String operationStr = operationStrTmp.trim();
            if(OperationsEnum.SUM.getName().equals(operationStr)) {
                operations.put(OperationsEnum.SUM, null);
            }

            if(OperationsEnum.AVG.getName().equals(operationStr)) {
                operations.put(OperationsEnum.AVG, null);
            }

            if(OperationsEnum.MIN.getName().equals(operationStr)) {
                operations.put(OperationsEnum.MIN, null);
            }

            if(OperationsEnum.MAX.getName().equals(operationStr)) {
                operations.put(OperationsEnum.MAX, null);
            }

            if(OperationsEnum.CEIL.getName().equals(operationStr)) {
                operations.put(OperationsEnum.CEIL, null);
            }

            if(operationStr.startsWith(OperationsEnum.PLUCK.getName())) {
                operations.put(OperationsEnum.PLUCK, getParamList(operationStr));
            }

            if(operationStr.startsWith(OperationsEnum.FILTER.getName())) {
                operations.put(OperationsEnum.FILTER, getParamList(operationStr));
            }
        }
    }

    private List<String> getParamList(String paramStr) {
        return Arrays.asList(paramStr.substring(paramStr.indexOf("(") + 1, paramStr.lastIndexOf(")")).split(","));
    }

    private boolean checkNumberOfFieldsInParams() {
        if(operations.entrySet().isEmpty()) {
            return false;
        }

        Optional<Map.Entry<OperationsEnum, List<String>>> firstParam = operations.entrySet()
                .stream()
                .filter(o -> o.getValue() != null && !o.getValue().isEmpty())
                .findFirst();
        if(firstParam.isPresent()) {
            int maxFieldIndexInParameters = Integer.parseInt(Collections.max(firstParam.get().getValue()));
            if(maxFieldIndexInParameters >= fieldList.size()) {
                System.out.println("Invalid value in INDEX parameter, there are not enough fields in the file to process the request");
                return false;
            }
        }

        return true;
    }

    private void writeMatrixToCSV(Matrix matrix) {
        LocalDateTime now = LocalDateTime.now();
        String fileName = "c:/tmp/matrix_" + DATETIME_FORMATTER.format(ZonedDateTime.of(now, ZoneId.of("UTC-4"))) +".csv";
        try {
            FileWriter csvWriter = new FileWriter(fileName);
            List<OperationsEnum> operationsEnums = new ArrayList<>(operations.keySet());
            OperationsEnum lastOperation = operationsEnums.get(operationsEnums.size() - 1);

            //add field(s) name(s) to result matrix file
            csvWriter.append(String.join(",", getMatrixFields(lastOperation, operations.get(lastOperation))));
            csvWriter.append("\n");

            for (Row rowData : matrix.getRows()) {
                csvWriter.append(String.join(",", rowData.getRowDataWithDelimiter(",")));
                csvWriter.append("\n");
            }
            csvWriter.flush();
            csvWriter.close();
            System.out.println("Matrix file " + fileName + " created");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //gets the field names for the csv file
    private String getMatrixFields(OperationsEnum lastOperation, List<String> operationParams) {
        switch (lastOperation) {
            case PLUCK:
                return fieldList.get(Integer.parseInt(operationParams.get(0)));
            case FILTER:
                return String.join(",", this.fieldList);
            default:
                return fieldList.get(0);
        }
    }
}
