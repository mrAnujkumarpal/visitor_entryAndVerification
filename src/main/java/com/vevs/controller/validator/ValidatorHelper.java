package com.vevs.controller.validator;


import com.opencsv.CSVReader;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import com.vevs.entity.bulk.BulkLocationRecords;
import com.vevs.entity.bulk.BulkUserRecords;
import com.vevs.entity.common.VMSEnum;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@Configurable
public class ValidatorHelper {
    private static final String[] EXTRA_FIELDS = {"id", "uploadedFileId"};

    public static boolean validateMinMaxLengthOfStr(String str, int minLength, int maxLength) {
        Boolean bool = false;
        int len = str.length();
        if (len >= minLength && len <= maxLength) {
            bool = true;
        }
        return bool;
    }

    public boolean validateValueByRegex(String value, String regex) {
        Boolean bool = false;
        if (regex != null && regex.trim().length() != 0) {

            boolean matchesRegex = matchesRegex(value, regex);
            if (matchesRegex) {
                bool = true;
            }
        }
        return bool;
    }


    private boolean matchesRegex(String value, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(value);
        return matcher.matches();
    }
    public List<String[]> fetchFileDataRowWise(MultipartFile file) throws IOException {
        InputStream is = file.getInputStream();

        BufferedReader reader = new BufferedReader(new InputStreamReader(is));

        CSVReader csvReader = new CSVReader(reader);
        List<String[]> rawList = new ArrayList<>();
        rawList = csvReader.readAll();
        reader.close();
        csvReader.close();
        is.close();
        return rawList;
    }


    public List<String> getAllClassMembers(String module) {
        List<String> nameList = new ArrayList<String>();
        List<Field> fields;
        if (StringUtils.equals(module, VMSEnum.MODULE_NAME.LOCATION.name())) {
            fields = getClassFields(BulkLocationRecords.class);
        } else {
            fields = getClassFields(BulkUserRecords.class);
        }

        List<String> excludedFieldList = excludeFieldList();

        for (Field field : fields) {
            String name = field.getName();
            if (excludedFieldList.contains(name)) {

                continue;
            }
            nameList.add(name);

        }
        return nameList;

    }

    private List<String> excludeFieldList() {
        return Arrays.asList(EXTRA_FIELDS);
    }


    private List<Field> getClassFields(Class<?> type) {
        List<Field> fields = new ArrayList<Field>();
        fields = getClassFields(fields, type);

        return fields;
    }
    private List<Field> getClassFields(List<Field> fields, Class<?> type) {
        fields.addAll(Arrays.asList(type.getDeclaredFields()));
        if (type.getSuperclass() != null) {
            fields = getClassFields(fields, type.getSuperclass());
        } else {
            return fields;
        }
        return fields;
    }
}
