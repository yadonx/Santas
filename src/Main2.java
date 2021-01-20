import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

/**
 * Created by Emil Johansson
 * Date: 2021-01-14
 * Time: 13:09
 * Project: Tomtar
 * Package: PACKAGE_NAME
 */
public class Main2 {
    /*
    Högsta chefen för allt är "Tomten"
    Under "Tomten" jobbar "Glader" och "Butter"
    Under "Glader" jobbar "Tröger", "Trötter" och "Blyger"
    Under "Butter" jobbar "Rådjuret", "Nyckelpigan", "Haren" och "Räven"
    Under "Trötter" jobbar "Skumtomten"
    Under "Skumtomten" jobbar "Dammråttan"
    Under "Räven" jobbar "Gråsuggan" och "Myran"
    Under "Myran" jobbar "Bladlusen"
    15 st
     */

    private Employee ceo = new Employee("");

    public Main2() {
        employeeFactory();
        List<Employee> e = ceo.getEmployeeList();

    }

    private void employeeFactory() {
        File employeeFile = new File("src/employees.txt");
        char opOne = ':';
        String opTwo = ",";
        boolean first = true;
        try (Scanner sc = new Scanner(employeeFile)) {

            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                String name = line.substring(0, line.indexOf(opOne));
                Employee tempOne = null;
                if (!first) {
                    Optional<Employee> temp = searchForEmployee(name);
                    tempOne = temp.orElseThrow(()-> new IllegalStateException("SUG"));
                }
                else
                    ceo = new Employee(name);
               String[] tempList = line.substring(line.indexOf(opOne) + 1).split(opTwo);
                for (String s :  tempList) {
                    Employee tempTwo = new Employee(s);
                    if (!first){
                        tempTwo.setBoss(tempOne);
                        tempOne.addEmployee(tempTwo);
                    }
                    else {
                        tempTwo.setBoss(ceo);
                        ceo.addEmployee(tempTwo);
                    }
                }
                first = false;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public Optional<Employee> searchForEmployee(String name) {
        return ceo.getEmployeeList().stream().flatMap(employee -> employee.getEmployeeList().stream()).filter(employee -> employee.getName()
                .equalsIgnoreCase(name)).findAny();
    }

    public Employee searchForEmployee2(String name){
        Employee employee;
        for (Employee employees : ceo.getEmployeeList()) {
            employee = se(employees,null,name);
            if (employees.getName().equalsIgnoreCase(name))
                return employees;
            else if (employee != null)
            return employee;
        }
        return null;
    }

    public Employee se(Employee employee,Employee returner,String name){
        if (employee.getName().equalsIgnoreCase(name)) {
            returner = employee;
            return returner;
        }

        Employee finalReturner = returner;
        employee.getEmployeeList().forEach(emp -> se(emp, finalReturner,name));
        return returner;
    }

    public void printBosses(Employee employee) {
            List<Employee> bossList = getBossList(true, employee, new ArrayList<>());
            if (bossList.size() == 0)
                System.out.println("Ingen chef över " + employee.getName());
            else {
                System.out.println("Över " + employee.getName() + " jobbar.");
                bossList.forEach(s -> System.out.println(s.getName()));
            }
    }

    public void checkIfEmployeeExist(String name){
//        Optional<Employee> check = searchForEmployee2(name);
        Optional<Employee> employees = searchForEmployee(name);
        Employee employee;
        if (ceo.getName().equalsIgnoreCase(name))
            employee = ceo;
        else if (employees.isEmpty()) {
            System.out.println(name + " Finns inte");
            return;
        }
        else
            employee = employees.get();

        printBosses(employee);
        printSubordinates(employee);
    }

    public void printSubordinates(Employee employee) {
            List<Employee> subordinatesList = getSubordinatesList(true, employee, new ArrayList<>());
            if (subordinatesList.size() == 0)
                System.out.println("Ingen jobbar under " + employee.getName() + ".");
            else {
                System.out.println("Under " + employee.getName() + " jobbar.");
                subordinatesList.forEach(s -> System.out.println(s.getName()));
            }

    }

    private List<Employee> getSubordinatesList(Boolean first, Employee employee, List<Employee> list) {
        if (!first)
            list.add(employee);
        employee.getEmployeeList().forEach(emp -> getSubordinatesList(false, emp, list));
        return list;
    }

    private List<Employee> getBossList(Boolean first, Employee employee, List<Employee> list) {
        if (!first)
            list.add(employee);
        if (employee.getBoss() == null)
            return list;
        getBossList(false, employee.getBoss(), list);
        return list;
    }

    public void check() {
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("Vilken anställd vill du kolla upp?\nAvsluta med exit.");
            String name = sc.nextLine();

            if (name.equalsIgnoreCase("exit"))
                System.exit(0);
            checkIfEmployeeExist(name);
        }
    }

    public static void main(String[] args) {
        Main2 m = new Main2();
        m.check();
    }

}
