package vms.vevs.controller.validator;


import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@Configurable
public class ValidatorHelper {


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

}
