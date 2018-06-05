package com.dstz.sys.excel.editor.font;

import com.dstz.sys.excel.editor.IFontEditor;
import com.dstz.sys.excel.style.font.BoldWeight;
import com.dstz.sys.excel.style.font.Font;

/**
 * 实现一些常用的字体<br/>
 * 该类用于把字体加粗
 *
 * @author zxh
 */
public class BoldFontEditor implements IFontEditor {

    public void updateFont(Font font) {
        font.boldweight(BoldWeight.BOLD);
    }

}
