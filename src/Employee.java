import java.util.ArrayList;
import java.util.List;

/**
 * Created by Emil Johansson
 * Date: 2021-01-14
 * Time: 13:08
 * Project: Tomtar
 * Package: PACKAGE_NAME
 */
public class Employee {
    private String name;
    private List<Employee> employeeList = new ArrayList<>();
    private Employee boss;

    public Employee(String name){
        this.name = name;
    }

    public void addEmployee(Employee employee){
        employeeList.add(employee);
    }

    public String getName() {
        return this.name;
    }

    public void setBoss(Employee boss){
        this.boss = boss;
    }

    public Employee getBoss(){
        return boss;
    }

    public List<Employee> getEmployeeList(){
        return this.employeeList;
    }

}
