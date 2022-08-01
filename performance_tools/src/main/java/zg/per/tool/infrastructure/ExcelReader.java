package zg.per.tool.infrastructure;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;

import java.io.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @param <T>
 */
public class ExcelReader<T> {

    /**
     * 字段为值、excel对应的列表、列号
     */
    private Map<String, Map<String, Integer>> map;
    private HSSFSheet sheet;
    private Class<?> tClazz;
    private List<T> tList;
    private ExcelReadProperty excelReadProperty;

    private HSSFSheet getSheet(String filePath, int sheetIndex) throws IOException {
        //用流的方式先读取到你想要的excel的文件
        FileInputStream fis = new FileInputStream(new File(filePath));

        //解析excel
        POIFSFileSystem pSystem = new POIFSFileSystem(fis);

        //获取整个excel
        HSSFWorkbook hb = new HSSFWorkbook(pSystem);

        //获取制定表单
        HSSFSheet sheet = hb.getSheetAt(sheetIndex);

        fis.close();
        return sheet;
    }

    public List<T> findAll(Class clazz) throws Exception {
        this.tClazz = clazz;
        this.map = new HashMap<>();
        this.tList = new ArrayList<T>();

        this.excelReadProperty = (ExcelReadProperty) clazz.getAnnotation(ExcelReadProperty.class);
        this.sheet = getSheet(excelReadProperty.fileName(), excelReadProperty.sheetNum());

        initMap();
        readData();

        return this.tList;
    }

    private void initMap() {
        //获取类的属性数组
        Field[] fieldList = this.tClazz.getDeclaredFields();
        for (Field field : fieldList) {
            if (field.isAnnotationPresent(ExcelReadProperty.class)) {

                ExcelReadProperty fieldAnnotation = field.getAnnotation(ExcelReadProperty.class);
                Map<String, Integer> excelCell = new HashMap<>();
                Row headRow = sheet.getRow(0);

                if (headRow != null) {
                    int lastCellNum = headRow.getLastCellNum();
                    for (int cellIndex = 0; cellIndex < lastCellNum; cellIndex++) {
                        Cell cell = headRow.getCell(cellIndex);
                        if (cell != null) {
                            if (cell.toString().equals(fieldAnnotation.cellName())) {
                                excelCell.put(fieldAnnotation.cellName(), cellIndex);
                            }
                        }
                    }
                }
                map.put(field.getName(), excelCell);
            }
        }
    }

    private void readData() throws InstantiationException, IllegalAccessException, NoSuchFieldException {
        int rowNumber = sheet.getLastRowNum();
        for (int rowIndex = 1; rowIndex < rowNumber; rowIndex++) {
            Row row = sheet.getRow(rowIndex);
            Object obj = this.tClazz.newInstance();

            for (String filedName : map.keySet()) {
                Field field = this.tClazz.getDeclaredField(filedName);
                field.setAccessible(true);

                Map<String, Integer> excelMap = map.get(filedName);
                Cell cell = row.getCell((Integer) excelMap.values().toArray()[0]);
                if (cell != null) {
                    cell.setCellType(CellType.STRING);
                    field.set(obj, cell.getStringCellValue());
                }
            }
            tList.add((T) obj);
        }
    }
}

