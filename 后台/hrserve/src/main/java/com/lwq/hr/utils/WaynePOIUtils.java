package com.lwq.hr.utils;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * @Description:
 * @author: LinWeiQi
 */
public class WaynePOIUtils {

    private final static String excel2003L = ".xls";    //2003- 版本的excel
    private final static String excel2007U = ".xlsx";   //2007+ 版本的excel

    /**
     * @param [in, fileName]
     * @TODO 通用导入 读取数据
     * @date 2020/2/16
     */
    public static List<List<Object>> myImportExcel(InputStream in, String fileName) throws Exception {
        List<List<Object>> list = null;

        Workbook work = myGetWorkbook(in, fileName);
        Sheet sheet = null;
        Row row = null;
        Cell cell = null;

        list = new ArrayList<List<Object>>();
        //遍历Excel中所有的sheet
        for (int i = 0; i < work.getNumberOfSheets(); i++) {
            sheet = work.getSheetAt(i);
            if (sheet == null) {
                continue;
            }
            //遍历当前sheet中的所有行 2行开始
            for (int j = sheet.getFirstRowNum(); j <= sheet.getLastRowNum(); j++) {
                row = sheet.getRow(j);
                if (row == null || row.getFirstCellNum() == j) {
                    continue;
                }

                //遍历所有的列
                List<Object> li = new ArrayList<Object>();
                for (int k = row.getFirstCellNum(); k < row.getLastCellNum(); k++) {
                    cell = row.getCell(k);
                    if (cell == null) {
                        continue;
                    }

                    li.add(cell.getStringCellValue());
                }
                list.add(li);
            }
        }
        System.out.println(list);
        return list;

    }

    /**
     * @TODO 上传excel时判断使用的POI版本
     */
    public static Workbook myGetWorkbook(InputStream inStr, String fileName) throws Exception {
        Workbook workbook = null;
        String fileType = fileName.substring(fileName.lastIndexOf("."));
        if (excel2003L.equals(fileType) || excel2003L.toUpperCase().equals(fileType)) {
            workbook = new HSSFWorkbook(inStr);  //2003-
        } else if (excel2007U.equals(fileType) || excel2007U.toUpperCase().equals(fileType)) {
            workbook = new XSSFWorkbook(inStr);  //2007+
        } else {
            throw new Exception("解析的文件格式有误！");
        }
        return workbook;
    }

    /**
     * @TODO 统一格式化 表格中 某列 的数值小位数,时间格式  --未完善
     * @date 2020/2/16
     */
    public Object getCellValue(Cell cell) {
        Object value = null;
        DecimalFormat df = new DecimalFormat("0");  //格式化number String字符
        SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd");  //日期格式化
        DecimalFormat df2 = new DecimalFormat("0.00");  //格式化数字

        if (cell == null || cell.getCellType() == CellType.BLANK) {
            value = "";
        } else {
            //判断数据类型
            switch (cell.getCellType()) {
                case FORMULA:
                    value = "" + cell.getCellFormula();
                    break;
                case NUMERIC:
                    value = "" + cell.getNumericCellValue();
                    break;
                case STRING:
                    value = cell.getStringCellValue();
                    break;
                default:
                    break;
            }
        }

        return value;
    }

    /**
     * @param [lineStyleMap行样式, columnWidthMap列宽, headLists表头内容行集合, dataList数据内容行集合]
     * @TODO excel2003导出  废弃 作为代码参考 poi应该定制化util接口 参数只传入一个数据data
     * @date 2020/2/16
     */
    public static Workbook MyExportExcel2003(HashMap<Integer, CellStyle> lineStyleMap, HashMap<Integer, Integer> columnWidthMap, List<List<String>> headLists, List<List<?>> dataList) {
        // 表头数
        int headLineSize = headLists.size();
        // 数据列数
        int columnSize = dataList.size();
        // 1. 建立表格
        Workbook workbook = new HSSFWorkbook();
        // 2. 新建sheet页
        Sheet sheet = workbook.createSheet();

        // 3. 表头处理
        for (int i = 0; i < headLineSize; i++) {
            List<String> headStrList = headLists.get(i);
            // 新建行
            Row row = sheet.createRow(i);
            // 行样式
            CellStyle cellStyle = lineStyleMap.get(i);
            // 新建列
            for (int j = 0; j < headStrList.size(); j++) {

                Cell cell = row.createCell(j);
                cell.setCellValue(headStrList.get(j));
                cell.setCellStyle(cellStyle);
            }

        }
        // 列宽处理
        for (int i = 0; i < columnSize; i++) {
            sheet.setColumnWidth(i, columnWidthMap.get(i));
        }

        // 4. 遍历表格数据
        CellStyle dataStyle = lineStyleMap.get(headLineSize);
        for (int i = headLineSize; i < dataList.size(); i++) {
            Row row = sheet.createRow(i);
            List<?> rowData = dataList.get(i);
            for (int j = 0; j < rowData.size(); j++) {
                Cell cell = row.createCell(j);
                // 数据格式转换
                if (rowData.get(j) instanceof String) {
                    String value = (String) rowData.get(j);
                    cell.setCellValue(value);
                } else if (rowData.get(j) instanceof Integer) {
                    Integer value = ((Integer) rowData.get(j));
                    cell.setCellValue(value);
                } else if (rowData.get(j) instanceof Date) {
                    String value = (String) rowData.get(j);
                    cell.setCellValue(value);
                }
                cell.setCellStyle(dataStyle);
            }

        }

        return workbook;
    }

}
