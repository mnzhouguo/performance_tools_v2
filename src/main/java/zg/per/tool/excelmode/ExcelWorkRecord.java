package zg.per.tool.excelmode;

import lombok.Data;
import zg.per.tool.infrastructure.ExcelReadProperty;

@Data
@ExcelReadProperty(fileName = "报工明细 (6).xls", sheetNum = 0)
public class ExcelWorkRecord {

    @ExcelReadProperty(cellName = "工号")
    public String employeeId;

    @ExcelReadProperty(cellName = "姓名")
    public String employeeName;

    @ExcelReadProperty(cellName = "项目编码")
    public String projectId;

    @ExcelReadProperty(cellName = "项目名称")
    public String projectName;

    @ExcelReadProperty(cellName = "提报工时")
    public String workingHours;

    @ExcelReadProperty(cellName = "驳回工时")
    public String rejectHours;

    public double getDoubleWorkingHours() {
        return Double.parseDouble(workingHours);
    }
}
