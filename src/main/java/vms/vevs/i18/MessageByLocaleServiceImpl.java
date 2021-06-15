package vms.vevs.i18;
import org.springframework.context.MessageSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class MessageByLocaleServiceImpl implements MessageByLocaleService {

    @Autowired
    private MessageSource messageSource;


    @Override
    public String getMessage(String id) {
        Locale locale = LocaleContextHolder.getLocale();
        System.out.println( " locale : " +  locale);
        return messageSource.getMessage(id, null, locale);
    }


}
