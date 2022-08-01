package zg.per.tool.excelmode;

import lombok.Data;
import zg.per.tool.infrastructure.ExcelReadProperty;

@Data
@ExcelReadProperty(fileName = "员工信息.xlsx", sheetNum = 0)
public class ExcelEmployee {

    @ExcelReadProperty(cellName = "工号")
    public String employeeId;

    @ExcelReadProperty(cellName = "姓名")
    public String employeeName;

    @ExcelReadProperty(cellName = "标签")
    public String tag;

    @ExcelReadProperty(cellName = "标准职位名称")
    private String position;
}
