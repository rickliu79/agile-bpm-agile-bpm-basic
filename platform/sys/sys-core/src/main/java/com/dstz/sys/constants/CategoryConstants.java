package com.dstz.sys.constants;

/**
 * <pre>
 * 描述：分类类别
 * </pre>
 */
public enum CategoryConstants {
    CAT_FLOW("FLOW_TYPE", "流程分类"),
    CAT_FORM("FORM_TYPE", "表单类型"),
    CAT_FILE("FILE_TYPE", "文件类型"),
    CAT_ATTACH("ATTACH_TYPE", "附件类型"),
    CAT_DIC("DIC", "数据字典"),
    CAT_FILE_FORMAT("FILEFORMAT", "分类类型--附件文件格式"),
    CAT_REPORT("REPORT_TYPE", "分类类型--报表"),
    CAT_DESKTOP("DESKTOP_TYPE", "桌面类型--报表");

    private String key;
    private String label;

    CategoryConstants(String key, String label) {
        this.key = key;
        this.label = label;
    }

    public String key() {
        return key;
    }

    public String label() {
        return label;
    }
}
