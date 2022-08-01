package zg.per.tool;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import zg.per.tool.domain.Employee;

import zg.per.tool.infrastructure.ExcelReader;
import zg.per.tool.excelmode.ExcelWorkRecord;
import zg.per.tool.servcies.CalculateService;

import java.util.List;

@SpringBootApplication
public class Application {
    public static void main(String[] args) throws Exception {
        SpringApplication.run(Application.class, args);

        ExcelReader<ExcelWorkRecord> excelWorkRecordExcelReader = new ExcelReader<>();
        List<ExcelWorkRecord> excelWorkRecordList = excelWorkRecordExcelReader.findAll(ExcelWorkRecord.class);

        CalculateService calculateService = new CalculateService();
        List<Employee> employeeList = calculateService.calculate(excelWorkRecordList);

        calculateService.saveToExcel(employeeList);
    }
}
