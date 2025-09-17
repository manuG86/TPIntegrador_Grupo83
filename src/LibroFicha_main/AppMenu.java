package LibroFicha_main;

import java.util.Scanner;

public class AppMenu {

    private final Scanner scanner = new Scanner(System.in);

    public void mostrarMenu() {
        int opcion;
        do {
            System.out.println("\n=== TPI - Libro ↦ FichaBibliografica ===");
            System.out.println("1. Crear Libro");
            System.out.println("2. Listar Libros");
            System.out.println("3. Buscar Libro por ID");
            System.out.println("4. Actualizar Libro");
            System.out.println("5. Eliminar Libro (baja lógica)");
            System.out.println("0. Salir");
            System.out.print("Elija opción: ");

            if (!scanner.hasNextInt()) {
                System.out.println("Entrada inválida. Intente de nuevo.");
                scanner.nextLine();
                continue;
            }

            opcion = scanner.nextInt();
            scanner.nextLine();

            switch (opcion) {
                case 1 -> System.out.println("CREAR Libro - pendiente de implementación");
                case 2 -> System.out.println("LISTAR Libros - pendiente de implementación");
                case 3 -> System.out.println("BUSCAR Libro - pendiente de implementación");
                case 4 -> System.out.println("ACTUALIZAR Libro - pendiente de implementación");
                case 5 -> System.out.println("ELIMINAR Libro - pendiente de implementación");
                case 0 -> System.out.println("Saliendo...");
                default -> System.out.println("Opción inválida.");
            }
        } while (true);
    }
}
