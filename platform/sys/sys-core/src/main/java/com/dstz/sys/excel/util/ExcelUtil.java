package com.dstz.sys.excel.util;

import com.dstz.base.api.exception.BusinessException;
import com.dstz.sys.excel.Excel;
import com.dstz.sys.excel.editor.IFontEditor;
import com.dstz.sys.excel.style.Align;
import com.dstz.sys.excel.style.BorderStyle;
import com.dstz.sys.excel.style.Color;
import com.dstz.sys.excel.style.font.BoldWeight;
import com.dstz.sys.excel.style.font.Font;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 一些工具方法
 *
 * @author zxh
 */
public class ExcelUtil {

    /**
     * 获取工作表的行数
     *
     * @param sheet HSSFSheet表对象
     * @return 表行数
     */
    public static int getLastRowNum(HSSFSheet sheet) {
        int lastRowNum = sheet.getLastRowNum();
        if (lastRowNum == 0) {
            lastRowNum = sheet.getPhysicalNumberOfRows() - 1;
        }
        return lastRowNum;
    }

    /**
     * 获取该行第一个单元格的下标
     *
     * @param row 行对象
     * @return 第一个单元格下标，从0开始
     */
    public static int getFirstCellNum(HSSFRow row) {
        return row.getFirstCellNum();
    }

    /**
     * 获取该行最后一个单元格的下标
     *
     * @param row 行对象
     * @return 最后一个单元格下标，从0开始
     */
    public static int getLastCellNum(HSSFRow row) {
        return row.getLastCellNum();
    }

    /**
     * 获取POI的行对象
     *
     * @param sheet 表对象
     * @param row   行号，从0开始
     * @return
     */
    public static HSSFRow getHSSFRow(HSSFSheet sheet, int row) {
        if (row < 0) {
            row = 0;
        }
        HSSFRow r = sheet.getRow(row);
        if (r == null) {
            r = sheet.createRow(row);
        }
        return r;
    }

    /**
     * 获取单元格对象
     *
     * @param sheet 表对象
     * @param row   行，从0开始
     * @param col   列，从0开始
     * @return row行col列的单元格对象
     */
    public static HSSFCell getHSSFCell(HSSFSheet sheet, int row, int col) {
        HSSFRow r = getHSSFRow(sheet, row);
        return getHSSFCell(r, col);
    }

    /**
     * 获取单元格对象
     *
     * @param row 行，从0开始
     * @param col 列，从0开始
     * @return 指定行对象上第col行的单元格
     */
    public static HSSFCell getHSSFCell(HSSFRow row, int col) {
        if (col < 0) {
            col = 0;
        }
        HSSFCell c = row.getCell(col);
        c = c == null ? row.createCell(col) : c;
        return c;
    }

    /**
     * 获取工作表对象
     *
     * @param workbook 工作簿对象
     * @param index    表下标，从0开始
     * @return
     */
    public static HSSFSheet getHSSFSheet(HSSFWorkbook workbook, int index) {
        if (index < 0) {
            index = 0;
        }
        if (index > workbook.getNumberOfSheets() - 1) {
            workbook.createSheet();
            return workbook.getSheetAt(workbook.getNumberOfSheets() - 1);
        } else {
            return workbook.getSheetAt(index);
        }
    }

    /**
     * 下载文件
     *
     * @param workBook
     * @param fileName
     * @param response
     * @throws IOException
     */

    public static void downloadExcel(HSSFWorkbook workBook, String fileName, HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.ms-excel");
        if (System.getProperty("file.encoding").equals("GBK")) {
            response.setHeader("Content-Disposition", "attachment;filename=\"" + new String(fileName.getBytes(), "ISO-8859-1") + ".xls" + "\"");
        } else {
            response.setHeader("Content-Disposition", "attachment;filename=\"" + URLEncoder.encode(fileName, "utf-8") + ".xls" + "\"");
        }
        OutputStream os = null;
        try {
            os = response.getOutputStream();
            workBook.write(os);
            os.flush();
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (os != null)
                os.close();
        }
    }

    /**
     * 导出excel文件。
     *
     * @param title     excel表格名称
     * @param rowHeight 行高
     * @param fieldMap  字段名映射 为一个LinkedHashMap
     * @param data      行数据
     * @return
     * @throws Exception
     */
    public static HSSFWorkbook exportExcel(String title, int rowHeight, Map<String, String> fieldMap, List data) throws Exception {

        int size = fieldMap.size();
        Excel excel = new Excel();

        int titleCols = size; // 列数

        if (titleCols == 0) {
            throw new Exception("请设置列！");
        }

        // 设置页名
        excel.sheet().sheetName(title);// 重命名当前处于工作状态的表的名称

        int i = 0;
        // 设置表头，第二行开始
        for (String name : fieldMap.values()) { // 表头已经排序过了

            excel.cell(0, i).value(name).align(Align.CENTER) // 设置水平对齐方式
                    .bgColor(Color.GREY_25_PERCENT) // 设置背景色
                    .fontHeightInPoint(14).width(256 * 50)// 增加宽度
                    .border(BorderStyle.THIN, Color.BLACK) // 设置外边框样式
                    .font(new IFontEditor() { // 设置字体
                        @Override
                        public void updateFont(Font font) {
                            font.boldweight(BoldWeight.BOLD);// 粗体
                            font.color(Color.BLACK);// 字体颜色
                        }
                    });
            i++;
        }

        // 插入数据，第三行开始
        int rows = 1;
        for (Object obj : data) {
            Map rowObj = (Map) obj;
            int col = 0;
            for (String key : fieldMap.keySet()) {
                String val = rowObj.get(key) == null ? "" : rowObj.get(key).toString();
                excel.cell(rows, col).value(val).border(BorderStyle.MEDIUM, Color.BLACK) // 设置外边框样式
                        .fontHeightInPoint(14).warpText(true).align(Align.LEFT); // 设置水平对齐方式
                col++;
            }
            rows++;
        }

        return excel.getWorkBook();
    }

    /**
     * <pre>
     * 读取某个xls
     * </pre>
     *
     * @param xlsfilePath xls的地址
     * @return Map<工作表号   ,       Map   <   行号   ,       Map   <   列号   ,       字符串数据>>>
     */
    public static Map<Integer, Map<Integer, Map<Integer, String>>> readExcel(String xlsfilePath) {
        Map<Integer, Map<Integer, Map<Integer, String>>> result = new HashMap<>();
        // 获得工作簿
        Workbook workbook;
        try {
            POIFSFileSystem xlsFile = new POIFSFileSystem(new FileInputStream(xlsfilePath));
            workbook = WorkbookFactory.create(xlsFile);
        } catch (Exception e) {
            throw new BusinessException(e);
        }
        // 获得工作表个数
        int sheetCount = workbook.getNumberOfSheets();
        // 遍历工作表
        for (int i = 0; i < sheetCount; i++) {
            Map<Integer, Map<Integer, String>> map = new HashMap<>();
            Sheet sheet = workbook.getSheetAt(i);
            // 获得行数
            int rows = sheet.getLastRowNum() + 1;
            // 获得列数，先获得一行，在得到改行列数
            Row tmp = sheet.getRow(0);
            if (tmp == null) {
                continue;
            }
            int cols = tmp.getPhysicalNumberOfCells();
            // 读取数据
            for (int row = 0; row < rows; row++) {
                Row r = sheet.getRow(row);
                Map<Integer, String> m = new HashMap<>();
                for (int col = 0; col < cols; col++) {
                    m.put(col, r.getCell(col).getStringCellValue());
                }
                map.put(row, m);
            }
            result.put(i, map);
        }
        return result;
    }
}
