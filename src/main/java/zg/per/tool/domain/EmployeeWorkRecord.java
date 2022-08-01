package zg.per.tool.domain;

import lombok.NoArgsConstructor;

/**
 * 员工项目投入
 */
@NoArgsConstructor
public class EmployeeWorkRecord {

    public EmployeeWorkRecord(Employee employee, Project project, double manHour, double manMonth) {
        this.employee = employee;
        this.project = project;
        this.manHour = manHour;
        this.manMonth = manMonth;
    }

    private Employee employee;

    private Project project;

    /**
     * 投入人月
     */
    private double manMonth;

    /**
     * 项目投入工时
     */
    private double manHour;

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public double getManMonth() {
        return manMonth;
    }

    public void setManMonth(double manMonth) {
        this.manMonth = manMonth;
    }

    public double getManHour() {
        return manHour;
    }

    public void setManHour(double manHour) {
        this.manHour = manHour;
    }
}
