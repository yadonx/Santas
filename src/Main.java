import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * Created by Emil Johansson
 * Date: 2021-01-14
 * Time: 13:09
 * Project: Tomtar
 * Package: PACKAGE_NAME
 */
public class Main {
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

    private final List<Employee> employees = new ArrayList<>();

    public Main() {
        employeeFactory();
    }

    private void employeeFactory() {
        File employeeFile = new File("src/employees.txt");
        char opOne = ':';
        String opTwo = ",";
        try (Scanner sc = new Scanner(employeeFile)) {

            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                String name = line.substring(0, line.indexOf(opOne));
                Optional<Employee> temp = searchForEmployee(name);
                Employee tempOne = temp.orElse(new Employee(name));
                if (temp.isEmpty()) {
                    employees.add(tempOne);
                }

                String[] p = line.substring(line.indexOf(opOne) + 1).split(opTwo);
                for (String s : p) {
                    Employee tempTwo = new Employee(s);
                    tempTwo.setBoss(tempOne);
                    employees.add(tempTwo);
                    tempOne.addEmployee(tempTwo);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public Optional<Employee> searchForEmployee(String name) {
        return employees.stream().filter(employee -> employee.getName()
                .equalsIgnoreCase(name)).findAny();
    }

    public void checkIfEmployeeExist(String name){
        Optional<Employee> optionalEmployee = searchForEmployee(name);
        if (optionalEmployee.isEmpty())
            System.out.println(name + " Finns inte");
        else {
            printBosses(optionalEmployee.get());
            printSubordinates(optionalEmployee.get());
        }
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
        Main m = new Main();
        m.check();
    }

}
