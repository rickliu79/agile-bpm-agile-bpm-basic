package com.dstz.base.core.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.io.SAXReader;


/**
 * xml操作类。<br>
 * 包括xml的读取，xml的转换等。
 * @author dstz
 */
public class Dom4jUtil {
    private static final Log logger = LogFactory.getLog(Dom4jUtil.class);

    /**
     * 将符合格式的xml字符串 转化成 Document
     *
     * @param s
     * @return
     */
    public static Document loadXml(String s) {
        Document document = null;
        try {
            document = DocumentHelper.parseText(s);
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage(), ex.getCause());
        }
        return document;
    }

    /**
     * 加载一个XML文件转成Document对象
     *
     * @param filename
     * @return
     */
    public static Document load(String filename, String encode) {
        Document document = null;
        try {
            SAXReader saxReader = new SAXReader();
            saxReader.setEncoding(encode);
            document = saxReader.read(new File(filename));
        } catch (Exception ex) {
            // logger.error("load XML File error:"+ex.getMessage());
        }
        return document;
    }

    /**
     * 按指定编码转化字符串为Document
     *
     * @param xml
     * @param encode
     * @return
     * @throws UnsupportedEncodingException
     */
    public static Document loadXml(String xml, String encode)
            throws UnsupportedEncodingException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(
                xml.getBytes(encode));
        return loadXml(inputStream, encode);
    }

    /**
     * 根据输入流返回Document
     *
     * @param is
     * @return
     */
    public static Document loadXml(InputStream is) {
        return loadXml(is, "utf-8");
    }

    public static Document loadXml(InputStream is, String charset) {
        Document document = null;
        try {
            SAXReader reader = new SAXReader();
            reader.setEncoding(charset);
            document = reader.read(is);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return document;
    }

}
