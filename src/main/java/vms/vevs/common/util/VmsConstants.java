package vms.vevs.common.util;

public class VmsConstants {

    public static final String MAIL_SENDER="mail4uanuj@gmail.com";
    public static final String OTP_SUBJECT="One Time Password [OTP] : VMS";
    public static final String ORG_CODE="MRV";

    public static final long JWT_TOKEN_VALIDITY=5 * 60 * 60;

    public static final String ZERO_TO_NINE = "[0-9]";
    public static final String NUMBER_PATTERN = "^[0-9,.]+$";
    public static final String MOBILE_PATTERN = "\\d{10}";
    public static final String STRING_PATTERN = "(?i)[a-z]+([,.\\s]+[a-z]+)*";
    public static final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)$";
    public static final String PASSWORD_PATTERN ="^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{6,10}$";


}
