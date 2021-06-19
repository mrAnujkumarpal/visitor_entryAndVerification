package vms.vevs.i18;

import org.springframework.lang.Nullable;

public interface MessageByLocaleService {

     String getMessage(String code);

    //tring getMessage(String code, @Nullable Object[] args, @Nullable String defaultMessage);

     String getMessage(String code, Object[] args);


}
