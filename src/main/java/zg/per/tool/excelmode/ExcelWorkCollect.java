package zg.per.tool.excelmode;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.Data;

/**
 * 最终汇总信息
 */
@Data
public class ExcelWorkCollect {
    @ExcelProperty("工号")
    private String employeeId;

    @ExcelProperty("姓名")
    private String employeeName;

    @ColumnWidth(12)
    @ExcelProperty("行业标签")
    private String tag;

    @ColumnWidth(12)
    @ExcelProperty("职位")
    private String position;

    @ColumnWidth(20)
    @ExcelProperty("项目编码")
    private String projectId;

    @ColumnWidth(40)
    @ExcelProperty("项目名称")
    private String projectName;

    @ColumnWidth(15)
    @ExcelProperty("入项时间")
    private String inoutOfTime;

    @ColumnWidth(15)
    @ExcelProperty("出项时间")
    private String outOfTime;

    @ExcelProperty("人月")
    private String manMonth;

    @ExcelProperty("工时")
    private String manHour;

    @ExcelProperty("备注")
    private String remake;

    @ExcelProperty("总报工")
    private String totalManHour;

    @ExcelProperty("总人月")
    private String totalManMonth;
}
