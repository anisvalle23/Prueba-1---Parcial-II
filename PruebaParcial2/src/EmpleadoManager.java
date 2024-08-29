
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class EmpleadoManager {

    private RandomAccessFile rcods, remps;

    public EmpleadoManager() {
        try {

            File mf = new File("company");
            mf.mkdir();

            rcods = new RandomAccessFile("company/codigos.emp", "rw");
            remps = new RandomAccessFile("company/empleados.emp", "rw");

            initCodes();
        } catch (IOException e) {
            System.out.println("Error");
        }
    }

    private void initCodes() throws IOException {
        if (rcods.length() == 0) {
            rcods.writeInt(1);
        }
    }

    private int getCode() throws IOException {
        rcods.seek(0);
        int codigo = rcods.readInt();
        rcods.seek(0);
        rcods.writeInt(codigo + 1);
        return codigo;
    }

    public void addEmployee(String name, double monto) throws IOException {
        remps.seek(remps.length());
        int code = getCode();
        remps.writeInt(code);
        remps.writeUTF(name);
        remps.writeDouble(monto);
        remps.writeLong(Calendar.getInstance().getTimeInMillis());
        remps.writeLong(0);
        this.createEmployeeFolders(code);
        this.createYearSalesFileFor(code);
    }

    private String employeeFolder(int code) {
        return "company/empleado" + code;
    }

    private void createEmployeeFolders(int code) throws IOException {
        File edir = new File(employeeFolder(code));
        edir.mkdir();
    }

    private RandomAccessFile salesFileFor(int code) throws IOException {
        String dirPadre = employeeFolder(code);
        int yearActual = Calendar.getInstance().get(Calendar.YEAR);
        String path = dirPadre + "/ventas" + yearActual + ".emp";
        return new RandomAccessFile(path, "rw");
    }

    private void createYearSalesFileFor(int code) throws IOException {
        RandomAccessFile raf = salesFileFor(code);
        if (raf.length() == 0) {
            for (int mes = 0; mes < 12; mes++) {
                raf.writeDouble(0);
                raf.writeBoolean(false);
            }
        }
    }

    public void printActiveEmployees() throws IOException {
        remps.seek(0);
        System.out.println("**** LISTA DE EMPLEADOS ****");
        int count = 1;
        while (remps.getFilePointer() < remps.length()) {
            int code = remps.readInt();
            String name = remps.readUTF();
            double salary = remps.readDouble();
            long fechaContratacion = remps.readLong();
            long fechaDespido = remps.readLong();

            if (fechaDespido == 0) {
                String fechaContratacionStr = new SimpleDateFormat("dd/MM/yyyy").format(new Date(fechaContratacion));
                System.out.printf("%d. Código: %d - Nombre: %s - Salario: Lps %.2f - Fecha de Contratación: %s\n",
                        count++, code, name, salary, fechaContratacionStr);
            }
        }
    }

    public boolean isActiveEmployee(int code) throws IOException {
        remps.seek(0);
        while (remps.getFilePointer() < remps.length()) {
            int empCode = remps.readInt();
            String name = remps.readUTF();
            double salary = remps.readDouble();
            long fechaContratacion = remps.readLong();
            long fechaDespido = remps.readLong();

            if (empCode == code && fechaDespido == 0) {
                remps.seek(remps.getFilePointer() - 20);
                return true;
            }
        }
        return false;
    }

  public boolean dismissEmployee(int code) throws IOException {
    remps.seek(0); 
    while (remps.getFilePointer() < remps.length()) {
        int empCode = remps.readInt();
        String name = remps.readUTF();
        double salary = remps.readDouble();
        long fechaContratacion = remps.readLong();
        long fechaDespido = remps.readLong();

        if (empCode == code && fechaDespido == 0) {
            remps.seek(remps.getFilePointer() - 8); 
            remps.writeLong(Calendar.getInstance().getTimeInMillis());
            System.out.println("Empleado Despedido: " + name);
            return true;
        }
    }
    return false;
}


    public void printEmployeeDetails(int code) throws IOException {
        remps.seek(0);
        while (remps.getFilePointer() < remps.length()) {
            int empCode = remps.readInt();
            String name = remps.readUTF();
            double salary = remps.readDouble();
            long fechaContratacion = remps.readLong();
            long fechaDespido = remps.readLong();

            if (empCode == code) {
                String fechaContratacionStr = new SimpleDateFormat("dd/MM/yyyy").format(new Date(fechaContratacion));
                System.out.printf("Código: %d - Nombre: %s - Salario: Lps %.2f - Fecha de Contratación: %s\n",
                        empCode, name, salary, fechaContratacionStr);

                if (fechaDespido != 0) {
                    String fechaDespidoStr = new SimpleDateFormat("dd/MM/yyyy").format(new Date(fechaDespido));
                    System.out.printf("Fecha de Despido: %s\n", fechaDespidoStr);
                } else {
                    System.out.println("Estado: Activo");
                }
                return;
            }
        }
        System.out.println("Empleado no encontrado.");
    }
}
