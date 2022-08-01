package zg.per.tool.servcies;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.merge.LoopMergeStrategy;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.WriteTable;
import zg.per.tool.domain.*;
import zg.per.tool.excelmode.ExcelEmployee;
import zg.per.tool.excelmode.ExcelLeave;
import zg.per.tool.excelmode.ExcelWorkCollect;
import zg.per.tool.excelmode.ExcelWorkRecord;
import zg.per.tool.infrastructure.ExcelReader;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class CalculateService {
    private List<Employee> employeeList = new ArrayList<>();

    List<ExcelEmployee> excelEmployeeList;

    public List<Employee> calculate(List<ExcelWorkRecord> recordList) throws Exception {
        Map<String, List<ExcelWorkRecord>> employeeWorkMap =
                recordList.stream().collect(Collectors.groupingBy(ExcelWorkRecord::getEmployeeId));

        ExcelReader<ExcelEmployee> excelEmployeeExcelReader = new ExcelReader<>();
        excelEmployeeList = excelEmployeeExcelReader.findAll(ExcelEmployee.class);

        for (String employeeId : employeeWorkMap.keySet()) {
            generateEmployee(employeeId, employeeWorkMap);
        }
        return employeeList;
    }

    private void generateEmployee(String employeeId, Map<String, List<ExcelWorkRecord>> employeeWorkMap) throws Exception {
        Employee employee = new Employee(employeeId);

        List<ExcelWorkRecord> emlRecordList = employeeWorkMap.get(employeeId);

        employee.setName(emlRecordList.get(0).employeeName);

        double employeeManHour = emlRecordList.stream().mapToDouble(ExcelWorkRecord::getDoubleWorkingHours).sum();
        employee.setTotalManHour(employeeManHour);
        employee.setTotalManMonth(manHourToMonth(employeeManHour));

        List<EmployeeWorkRecord> employeeWorkRecords = new ArrayList<>();
        addLeave(employeeId, employee, employeeWorkRecords);

        Optional<ExcelEmployee> currentEmployee = excelEmployeeList.stream().filter(s -> s.getEmployeeId().equals(employeeId)).findFirst();
        if (currentEmployee.isPresent()) {
            employee.setBusinessTag(currentEmployee.get().getTag());
            employee.setPosition(currentEmployee.get().getPosition());
        }
        Map<String, List<ExcelWorkRecord>> projectRecordMap =
                emlRecordList.stream().collect(Collectors.groupingBy(ExcelWorkRecord::getProjectId));

        for (String projectId : projectRecordMap.keySet()) {
            List<ExcelWorkRecord> projectRecordList = projectRecordMap.get(projectId);
            double projectManHour = projectRecordList.stream().mapToDouble(ExcelWorkRecord::getDoubleWorkingHours).sum();
            employeeWorkRecords.add(new EmployeeWorkRecord(employee, new Project(projectId, projectRecordList.get(0).projectName), projectManHour, manHourToMonth(projectManHour)));
        }

        employee.setEmployeeWorkRecordList(employeeWorkRecords);
        employeeList.add(employee);
    }

    private void addLeave(String employeeId, Employee employee, List<EmployeeWorkRecord> employeeWorkRecords) throws Exception {
        ExcelReader<ExcelLeave> excelLeaveList = new ExcelReader<>();
        List<ExcelLeave> excelLeaves = excelLeaveList.findAll(ExcelLeave.class);

        Map<String, List<ExcelLeave>> excelLeaveListMap =
                excelLeaves.stream().collect(Collectors.groupingBy(ExcelLeave::getEmployeeId));

        List<ExcelLeave> excelLeaveListList = excelLeaveListMap.get(employeeId);
        if (excelLeaveListList != null && excelLeaveListList.size() > 0) {
            double leaveSum = excelLeaveListList.stream().mapToDouble(ExcelLeave::getDoubleWorkday).sum();
            employeeWorkRecords.add(new EmployeeWorkRecord(employee, new Project("0", "考勤请假"), toMa(leaveSum), manHourToMonth(toMa(leaveSum))));
        }
    }


    private double toMa(double leaveSum) {
        DecimalFormat df = new DecimalFormat("#.00");
        return Double.parseDouble(df.format(leaveSum * 0.75));
    }

    private double manHourToMonth(double manHour) {
        double manMonth = manHour / 7.5 / 21;
        DecimalFormat df = new DecimalFormat("#.00");
        return Double.parseDouble(df.format(manMonth));
    }

    public void saveToExcel(List<Employee> employeeList) {
        if (employeeList == null || employeeList.isEmpty()) {
            throw new IllegalArgumentException("employeeList Cannot be null or null ");
        }

        ExcelWriter excelWriter = EasyExcel.write("报工汇总.xlsx", ExcelWorkCollect.class)
                .writeExcelOnException(true)
                .autoCloseStream(true)
                .build();

        WriteSheet writeSheet = EasyExcel.writerSheet("报工汇总").needHead(Boolean.TRUE).build();

        int rowIndex = 1;
        for (Employee employee : employeeList) {
            WriteTable writeTable = EasyExcel.writerTable().needHead(Boolean.FALSE).build();

            List<ExcelWorkCollect> workRecords = employee.toExcelWorkCollect();
            int workRecordSize = workRecords.size();
            writeTable.getCustomWriteHandlerList().clear();
            if (workRecordSize > 1) {
//                writeTable.getCustomWriteHandlerList().add(new OnceAbsoluteMergeStrategy(rowIndex, rowIndex + workRecordSize - 1, 0, 0));
//                writeTable.getCustomWriteHandlerList().add(new OnceAbsoluteMergeStrategy(rowIndex, rowIndex + workRecordSize - 1, 1, 1));
            }
            rowIndex = rowIndex + workRecordSize;
            excelWriter.write(workRecords, writeSheet, writeTable);
        }
        excelWriter.finish();
    }

    public void saveToExcel2(List<Employee> employeeList) {
        if (employeeList == null || employeeList.isEmpty()) {
            throw new IllegalArgumentException("employeeList Cannot be null or null ");
        }

        WriteSheet writeSheet = EasyExcel.writerSheet("报工汇总").needHead(Boolean.TRUE).build();

        List<ExcelWorkCollect> workRecordsAll = new ArrayList<>();
        for (Employee employee : employeeList) {
            workRecordsAll.addAll(employee.toExcelWorkCollect());
        }

        LoopMergeStrategy loopMergeStrategy = new LoopMergeStrategy(2, 0);

        ExcelWriter excelWriter = EasyExcel.write("报工汇总.xlsx", ExcelWorkCollect.class)
                .writeExcelOnException(true)
                .autoCloseStream(true)
                .build();


        EasyExcel.write("报工汇总.xlsx", ExcelWorkCollect.class)
                .registerWriteHandler(loopMergeStrategy).sheet("模板").doWrite(workRecordsAll);

        excelWriter.finish();
    }
}
