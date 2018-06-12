package com.fwms.basedevss.base.util;

import java.util.Locale;
import java.util.ResourceBundle;

public class I18nUtils {
    public static String getBundleStringByLang(String lang, String key)
    {
        ResourceBundle bundle = I18nHelper.getBundle("com.fwms.basedevss.i18n.service", new Locale(lang));
        return bundle.getString(key);
    }
}