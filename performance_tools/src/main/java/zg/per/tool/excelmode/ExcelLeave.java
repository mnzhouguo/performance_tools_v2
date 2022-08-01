package zg.per.tool.excelmode;

import lombok.Data;
import zg.per.tool.infrastructure.ExcelReadProperty;

/**
 * 请假信息
 */
@Data
@ExcelReadProperty(fileName = "请假信息.xls", sheetNum = 1)
public class ExcelLeave {
    @ExcelReadProperty(cellName = "工号")
    public String employeeId;

    @ExcelReadProperty(cellName = "工作日")
    public String workday;

    public double getDoubleWorkday() {
        if (this.workday != null && this.workday.length() > 0) {
            return Double.parseDouble(this.workday);
        }
        return 0;
    }
}
