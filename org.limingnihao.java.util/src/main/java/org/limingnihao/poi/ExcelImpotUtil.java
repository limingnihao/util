package org.limingnihao.poi;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.limingnihao.util.ExcelUtil;
import org.limingnihao.util.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by lishiming on 2017/2/3.
 */
public class ExcelImpotUtil {

    private static final Logger logger = LoggerFactory.getLogger(ExcelImpotUtil.class);

    public static ArrayList<ArrayList<String>> importBySheetIndex2003(String filePath, int sheetIndex) {
        logger.info("import2003 - filePath=" + filePath + ", sheetIndex=" + sheetIndex);
        File file = new File(filePath);
        if (!file.exists()) {
            logger.info("import2003 - 文件不存在");
            return null;
        }
        //logger.info("import2003 - 1.文件存在 - filePath=" + filePath);
        ArrayList<ArrayList<String>> rowList = new ArrayList<ArrayList<String>>();
        try {
            POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream(filePath));
            // 得到Excel工作簿对象
            HSSFWorkbook workbook = new HSSFWorkbook(fs);
            // 得到Excel工作表对象
            int max = workbook.getNumberOfSheets();
            sheetIndex = sheetIndex <= 0 ? max : sheetIndex;
            // 得到Excel工作表对象
            HSSFSheet sheet = workbook.getSheetAt(sheetIndex - 1);
            int rowNumLast = sheet.getLastRowNum();
            //logger.info("import2003 - filePath=" + filePath + ", sheet=" + k + ", rowNumLast=" + rowNumLast);
            // i循环行
            for (int i = 0; i <= rowNumLast; i++) {
                ArrayList<String> columnList = new ArrayList<String>();
                // 得到Excel工作表的行
                HSSFRow row = sheet.getRow(i);
                if (row == null) {
                    continue;
                }
                // j循环列
                for (int j = 0; j < row.getLastCellNum(); j++) {
                    // 得到Excel工作表指定行的单元格
                    HSSFCell cell = row.getCell(j);
                    //logger.info("~~~~~~[" + i + "," + j + "] - cell=" + cell );
                    Object value = null;
                    if (cell != null) {
                        if (cell.getCellTypeEnum() == CellType.NUMERIC) {
                            Double d = cell.getNumericCellValue();
                            value = d.doubleValue();
                        } else if (cell.getCellTypeEnum() == CellType.STRING) {
                            value = cell.getStringCellValue();
                        }
                        columnList.add(value != null ? value.toString().trim() : "");
                    } else {
                        columnList.add("");
                    }
                    //logger.info("~~~~~~[" + i + "," + j + "] - cell=" + cell + ", value=" + value);
                }
                //logger.info("i=" + i + "" + Arrays.toString(columnList.toArray()));
                rowList.add(columnList);
            }
        } catch (Exception e) {
            logger.info("import2003 - filePath=" + filePath + ", " + e);
            e.printStackTrace();
        }
        return rowList;
    }

    public static ArrayList<ArrayList<String>> importBySheetIndex2007(String filePath, int sheetIndex) {
        logger.info("import2007 - filePath=" + filePath + ", sheetIndex=" + sheetIndex);
        File file = new File(filePath);
        if (!file.exists()) {
            logger.info("import2007 - 文件不存在");
            return null;
        }
        //logger.info("import2007 - 1.文件存在 - filePath=" + filePath);
        ArrayList<ArrayList<String>> rowList = new ArrayList<ArrayList<String>>();
        try {
            //NPOIFSFileSystem fs = new NPOIFSFileSystem();
            // 得到Excel工作簿对象
            XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(filePath));
            // 得到Excel工作表对象
            int max = workbook.getNumberOfSheets();
            sheetIndex = sheetIndex <= 0 ? max : sheetIndex;
            XSSFSheet sheet = workbook.getSheetAt(sheetIndex - 1);
            int rowNumLast = sheet.getLastRowNum();
            logger.info("import2007 - filePath=" + filePath + ", sheet=" + sheetIndex + ", rowNumLast=" + rowNumLast);
            // i循环行
            for (int i = 0; i <= rowNumLast; i++) {
                ArrayList<String> columnList = new ArrayList<String>();
                // 得到Excel工作表的行
                XSSFRow row = sheet.getRow(i);
                if (row == null) {
                    continue;
                }
                // j循环列
                for (int j = 0; j < row.getLastCellNum(); j++) {
                    // 得到Excel工作表指定行的单元格
                    XSSFCell cell = row.getCell(j);
                    //logger.info("~~~~~~[" + i + "," + j + "] - cell=" + cell );
                    Object value = null;
                    if (cell != null) {
                        if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                            if (HSSFDateUtil.isCellDateFormatted(cell)) {
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                value = sdf.format(HSSFDateUtil.getJavaDate(cell.getNumericCellValue())).toString();
                            } else {
                                Double d = cell.getNumericCellValue();
                                value = d.intValue();
                            }
                        } else if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
                            value = cell.getStringCellValue();
                        }
                        columnList.add(value != null ? value.toString().trim() : "");
                    } else {
                        columnList.add("");
                    }
//						logger.info("~~~~~~[" + i + "," + j + "] - " + cell.getCellType() + ", cell=" + cell + ", value=" + value);
                }
                //logger.info("i=" + i + "" + Arrays.toString(columnList.toArray()));
                rowList.add(columnList);
            }
        } catch (Exception e) {
            logger.info("import2007 - filePath=" + filePath + ", " + e);
            e.printStackTrace();
        }
        return rowList;
    }

}
