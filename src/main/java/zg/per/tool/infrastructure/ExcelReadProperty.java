package zg.per.tool.infrastructure;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Retention(RetentionPolicy.RUNTIME) // 保留到运行时，可通过注解获取
public @interface ExcelReadProperty {
    public String fileName() default "";

    public int sheetNum() default 0;

    public String cellName() default "";
}


