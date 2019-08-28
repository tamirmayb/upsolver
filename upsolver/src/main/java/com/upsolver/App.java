package com.upsolver;

import com.upsolver.operations.OperationsManager;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;

import java.io.File;
import java.io.IOException;

public class App {
    public static void main(String[] args) {
        File dataFile = new File("c:/tmp/data.csv");
        LineIterator it = null;
        OperationsManager operationsManager = new OperationsManager("filter(3, 'Iowa') -> pluck(11) -> max");
        try {
            it = FileUtils.lineIterator(dataFile, "UTF-8");
            while (it.hasNext()) {
                String line = it.nextLine();
                String[] rowData = line.split(",");
                if(!operationsManager.processDataRow(rowData)) {
                    break;
                }
            }
            operationsManager.finalizeProcess();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                it.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
