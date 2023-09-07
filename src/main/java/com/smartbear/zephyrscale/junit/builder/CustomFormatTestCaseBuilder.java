package com.smartbear.zephyrscale.junit.builder;

import com.smartbear.zephyrscale.junit.annotation.TestCase;
import com.smartbear.zephyrscale.junit.customformat.CustomFormatTestCase;
import org.junit.runner.Description;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;

public class CustomFormatTestCaseBuilder {
    private CustomFormatTestCase testCase;
    private static Integer i = 0;

    public CustomFormatTestCaseBuilder build(Description description) {
        TestCase testCaseAnnotation = description.getAnnotation(TestCase.class);
        String testCaseKey = testCaseAnnotation != null ? testCaseAnnotation.key() : null;
        String testCaseName = testCaseAnnotation != null ? testCaseAnnotation.name(): null;

        List<Map<String, String>> data = getDataFromFile(description);
        for(Map.Entry<String, String> entry : data.get(i).entrySet()){
            String key = entry.getKey();
            testCaseName = testCaseName.replace("#"+ key,entry.getValue());
        }


        if(testCaseKey != null || testCaseName != null) {
            testCase = new CustomFormatTestCase();
            testCase.setKey(isNotEmpty(testCaseKey) ? testCaseKey : null);
            testCase.setName(isNotEmpty(testCaseName) ? testCaseName : null);
        }
        i++;
        return this;
    }

    private List<Map<String, String>> getDataFromFile(Description description){
        List<Map<String, String>> data = new ArrayList<>();
        Annotation[] annotations = description.getTestClass().getAnnotations();
        for (Annotation annotation : annotations) {
            Class<? extends Annotation> type = annotation.annotationType();
            if (type.getSimpleName().equals("UseTestDataFrom")){
                String fileName = null;
                String separate = ",";
                try {
                    fileName = "src/test/resources/" + type.getDeclaredMethod("value").invoke(annotation).toString();
                    separate = type.getDeclaredMethod("separator").invoke(annotation).toString();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (fileName != null){
                    data = getDataInCSV(fileName, separate);
                }
            }
        }
        return data;
    }

    private List<Map<String, String>> getDataInCSV(String fileName, String separator){
        List<Map<String, String>> records = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String[] header = br.readLine().split(separator);
            String line;
            while ((line = br.readLine()) != null) {
                Map<String, String> dataMap = new HashMap<>();
                if (line.endsWith(",")){
                    line = line.concat(",");
                }
                String[] data = line.replaceAll(",,",", ,").split(separator);
                for (int i = 0; i < header.length; i++){
                    dataMap.put(header[i], data[i]);
                }
                records.add(dataMap);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return records;
    }

    public CustomFormatTestCase getTestCase() {
        return testCase;
    }
}
