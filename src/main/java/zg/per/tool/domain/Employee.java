package zg.per.tool.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import zg.per.tool.excelmode.ExcelWorkCollect;

import java.util.ArrayList;
import java.util.List;

/**
 * 员工
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Employee {
    public Employee(String employeeId) {
        this.id = employeeId;
    }

    /**
     * 员工ID
     */
    private String id;

    /**
     * 员工姓名
     */
    private String name;

    /**
     * 员工业务标签
     */
    private String businessTag;

    /**
     * 员工职位
     */
    private String position;


    /**
     * 总报工小时数
     */
    private double totalManHour;

    /**
     * 总人月数
     */
    private double totalManMonth;

    /**
     * 报工记录
     */
    private List<EmployeeWorkRecord> employeeWorkRecordList;

    /**
     * 报工记录
     */
    private List<EmployeeLeaveRecord> employeeLeaveRecordList;

    public List<ExcelWorkCollect> toExcelWorkCollect() {
        List<ExcelWorkCollect> list = new ArrayList();

        if (this.employeeWorkRecordList != null && this.employeeWorkRecordList.size() > 0) {
            for (EmployeeWorkRecord record : this.employeeWorkRecordList) {
                ExcelWorkCollect excelWorkCollect = new ExcelWorkCollect();
                excelWorkCollect.setEmployeeId(this.id);
                excelWorkCollect.setEmployeeName(this.name);

                excelWorkCollect.setTag(this.businessTag);
                excelWorkCollect.setPosition(this.position);

                excelWorkCollect.setProjectId(record.getProject().getId());
                excelWorkCollect.setProjectName(record.getProject().getName());

                excelWorkCollect.setManHour(String.valueOf(record.getManHour()));
                excelWorkCollect.setManMonth(String.valueOf(record.getManMonth()));

                excelWorkCollect.setTotalManHour(String.valueOf(this.totalManHour));
                excelWorkCollect.setTotalManMonth(String.valueOf(this.totalManMonth));

                list.add(excelWorkCollect);
            }
        }
        return list;
    }
}
