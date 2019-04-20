package com.dstz.form.api.model;

public enum FormType {
    PC("pc"),
<<<<<<< HEAD
    VUE("vue"),
    MOBILE("mobile"),
    PC_IVIEW("pc_iview");
=======
    MOBILE("mobile");
>>>>>>> branch 'master' of https://gitee.com/agile-bpm/agile-bpm-basic.git

    private final String value;

    FormType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static FormType fromValue(String v) {
        for (FormType c : FormType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
