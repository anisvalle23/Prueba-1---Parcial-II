import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class ControlEmpleado {

    public static void main(String[] args) {
        EmpleadoManager manager = new EmpleadoManager();
        Scanner read = new Scanner(System.in);
        int option = 0;

        do {
            System.out.println("\n========================================");
            System.out.println("           **** MENÚ PRINCIPAL ****           ");
            System.out.println("========================================");
            System.out.println("  1 - Agregar Empleado");
            System.out.println("  2 - Listar Empleados No Despedidos");
            System.out.println("  3 - Despedir Empleado");
            System.out.println("  4 - Buscar Empleado Activo");
            System.out.println("  5 - Salir");
            System.out.println("========================================");
            System.out.print("Seleccione una opción: ");
            
            try {
                option = read.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Error: Entrada inválida. Por favor, ingrese un número entero.");
                read.nextLine(); 
                continue; 
            }

            System.out.println("\n----------------------------------------");

            switch (option) {
                case 1:
                    String name = "";
                    read.nextLine(); 
                    while (true) {
                        System.out.print("Ingrese el nombre del empleado: ");
                        name = read.nextLine();
                        if (name.matches("[a-zA-Z\\s]+")) {
                            break; 
                        } else {
                            System.out.println("Error: El nombre solo debe contener letras y espacios.");
                        }
                    }
                    
                    double salary = 0;
                    while (true) {
                        System.out.print("Ingrese el salario del empleado: ");
                        try {
                            salary = read.nextDouble();
                            break; 
                        } catch (InputMismatchException e) {
                            System.out.println("Error: Entrada inválida. Por favor, ingrese un número decimal para el salario.");
                            read.nextLine(); 
                        }
                    }
                    try {
                        manager.addEmployee(name, salary);
                        System.out.println("\nEmpleado agregado exitosamente.");
                    } catch (IOException e) {
                        System.out.println("\nError al agregar empleado.");
                    }
                    break;
                case 2:
                    try {
                        manager.printActiveEmployees();
                    } catch (IOException e) {
                        System.out.println("\nError al listar empleados.");
                    }
                    break;
                case 3:
                    System.out.print("Ingrese el código del empleado a despedir: ");
                    int codeDismiss = 0;
                    while (true) {
                        try {
                            codeDismiss = read.nextInt();
                            break; 
                        } catch (InputMismatchException e) {
                            System.out.println("Error: Entrada inválida. Por favor, ingrese un número entero para el código.");
                            read.nextLine(); 
                        }
                    }
                    try {
                        if (manager.dismissEmployee(codeDismiss)) {
                            System.out.println("\nEmpleado despedido correctamente.");
                        } else {
                            System.out.println("\nEmpleado no encontrado o ya está despedido.");
                        }
                    } catch (IOException e) {
                        System.out.println("\nError al despedir empleado.");
                    }
                    break;
                case 4:
                    System.out.print("Ingrese el código del empleado a buscar: ");
                    int codeSearch = 0;
                    while (true) {
                        try {
                            codeSearch = read.nextInt();
                            break; 
                        } catch (InputMismatchException e) {
                            System.out.println("Error: Entrada inválida. Por favor, ingrese un número entero para el código.");
                            read.nextLine(); 
                        }
                    }
                    try {
                        if (manager.isActiveEmployee(codeSearch)) {
                            manager.printEmployeeDetails(codeSearch);
                        } else {
                            System.out.println("\nEmpleado no encontrado o no está activo.");
                        }
                    } catch (IOException e) {
                        System.out.println("\nError al buscar empleado.");
                    }
                    break;
                case 5:
                    System.out.println("\nBye, Bye.");
                    break;
                default:
                    System.out.println("\nOpción no válida. Intente de nuevo.");
                    break;
            }
        } while (option != 5);
    }
}
