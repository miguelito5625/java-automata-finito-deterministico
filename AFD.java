import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

class AFD {

    static String contenidoArchivoAfd = "";
    static String contenidoArchivoCuerdas = "";
    static String alfabetoEntrada = "";
    static int cantidadEstados;
    static int estadoFinal;
    
    static String cuerdasConsole = "";
    static String tituloCuerdas = "";

    static ArrayList<String> transiciones = new ArrayList<String>();

    static int estadoActual = 1;

    public static void main(String[] args) {
        System.out.println("PROGRAMA HECHO POR MIGUEL ARCHILA");

        if (args.length < 2) {
            System.out.println("Error: faltan argumentos para la ejecucion");
            System.exit(1);
        }

        String afdPath = args[0];
        String flag = args[1];
        

        if (!flag.equals("-i") && !flag.equals("-f")) {
            System.out.println("Error: las banderas permitidas son -i o -f");
            System.exit(1);
        }

        AFD afd = new AFD(afdPath);

        if (flag.equals("-i")) {

            if (args.length < 2) {
                System.out.println("Error: faltan argumentos para la ejecucion");
                System.exit(1);
            }

            tituloCuerdas = "CUERDAS";
            recibirTextoConsola();
            accept(cuerdasConsole);

        } else if (flag.equals("-f")) {

            if (args.length < 3) {
                System.out.println("Error: faltan argumentos para la ejecucion");
                System.exit(1);
            }

            tituloCuerdas = "ARCHIVO DE CUERDAS";
            String cuerdasPath = args[2];
            contenidoArchivoCuerdas = String.valueOf(leerArchivoAfd(cuerdasPath));
            accept(contenidoArchivoCuerdas);
        }

    }

    AFD(String afdPath) {
        System.out.println();
        System.out.println("AFD");
        // La ruta del archivo del afd
        String rutaArchivo = afdPath;
        // numero de lineas del archivo afd
        long numeroLineas = contarLineas(rutaArchivo);
        // Contenido del archivo afd
        contenidoArchivoAfd = String.valueOf(leerArchivoAfd(rutaArchivo));
        System.out.println("Numro de lineas del archivo afd: " + numeroLineas);

        // Separamos cada linea en un elemento del array del archivo afd
        String[] arrayContenidoAfd = contenidoArchivoAfd.split("\n");
        // for (String linea : arrayContenidoAfd) {
        // System.out.println(linea);
        // }

        validacionArchivoAfd(arrayContenidoAfd);

        System.out.println();
    }

    static void accept(String cuerdas) {
        System.out.println("");
        System.out.println(tituloCuerdas);
        String[] arrCuerdas = cuerdas.split("\n");
        System.out.println("cantidad de cuerdas: " + arrCuerdas.length);

        for (int i = 0; i < arrCuerdas.length; i++) {
            String[] arrUnidadCuerda = arrCuerdas[i].split("");
            // System.out.println("cuerdad no." + (i+1));
            for (String symbol : arrUnidadCuerda) {
                // System.out.println(unidad);
                estadoActual = getTransition(estadoActual, symbol.charAt(0));
                if (estadoActual == 0) {
                    break;
                }
            }

            String respuestaAfd = "";
            if (estadoActual == estadoFinal) {
                respuestaAfd = "Aceptada";
            } else {
                respuestaAfd = "Rechazada";
            }
            estadoActual = 1;
            System.out.println("Cuerda no." + (i + 1) + " = " + arrCuerdas[i] + " = " + respuestaAfd);
        }

        cuerdasConsole = "";
    }

    static int getTransition(int currentState, char symbol) {

        // Validar si el caracter se encuentra dentro del alfabeto
        boolean found = Arrays.asList(alfabetoEntrada.split(",")).contains(String.valueOf(symbol));
        if (!found) {
            return 0;
        }

        // System.out.println("Test Actual");
        ArrayList<String> arrAlfabetoEntrada = new ArrayList<String>(Arrays.asList(alfabetoEntrada.split(",")));
        int posicionAlfabetoEntrada = arrAlfabetoEntrada.indexOf(String.valueOf(symbol));
        // System.out.println("posi: " + posicionAlfabetoEntrada);
        // System.out.println("transicion:");
        String transicion = transiciones.get(posicionAlfabetoEntrada);
        // System.out.println(transicion);

        int resultadoTransicion = Integer.parseInt(transicion.split(",")[estadoActual]);

        return resultadoTransicion;
    }

    void validacionArchivoAfd(String[] arrayContenidoAfd) {

        if (arrayContenidoAfd.length < 4) {
            System.out.println("Error: archivo afd invalido");
            System.exit(1);
        }

        int tamanioAlfabeto = arrayContenidoAfd[0].split(",").length;
        alfabetoEntrada = arrayContenidoAfd[0];
        cantidadEstados = Integer.parseInt(arrayContenidoAfd[1]);
        estadoFinal = Integer.parseInt(arrayContenidoAfd[2]);
        System.out.println("tamaño del alfabeto:" + tamanioAlfabeto);
        System.out.println("Cantidad de estados:" + cantidadEstados);
        System.out.println("Estado final:" + estadoFinal);
        int lineasDeTransiciones = arrayContenidoAfd.length - 3;
        System.out.println("Lineas de transiciones: " + lineasDeTransiciones);
        if (lineasDeTransiciones != tamanioAlfabeto) {
            System.out.println("Error: la cantidad de transiciones deberian ser igual al tamanio del alfabeto");
            System.exit(1);
        }

        // System.out.println("Comienza for");
        for (int i = 3; i < arrayContenidoAfd.length; i++) {
            String transicion = arrayContenidoAfd[i];
            transiciones.add(transicion);
            // System.out.println(transicion);
            if (transicion.split(",").length != cantidadEstados) {
                System.out.println("las transiones de la linea " + (i + 1)
                        + " del afd no concuerdan con la cantidad de estados: " + cantidadEstados);
                System.exit(1);
            }
        }

    }

    // Metodo para leer el archivo afd
    static char[] leerArchivoAfd(String rutaArchivo) {

        try {

            // Variable donde almacenamos el texto del archivo
            String cadenaDeTexto = "";

            // Hacemos uso de la clase file para leer el archivo
            File archivo = new File(rutaArchivo);

            // La clase scanner nos servira para leer las lineas de texto
            Scanner scanner = new Scanner(archivo);

            // Leemos linea a linea el archivo
            cadenaDeTexto = scanner.nextLine();
            while (scanner.hasNextLine()) {
                cadenaDeTexto = cadenaDeTexto + "\n" + scanner.nextLine();
            }

            // Convertimos la cadena de texto en un array de caracteres para poder trabajar
            // las vocales y cerramos el scanner
            char[] charArray = cadenaDeTexto.toCharArray();
            scanner.close();

            // Imprimimos en consola lo que contiene el archivo
            // System.out.println(charArray);
            return charArray;

        } catch (IOException e) {
            // Imprimimos los errores que puedan ocurrir durante el programa
            // e.printStackTrace();
            System.out.println("Error al abrir el archivo afd");
            System.exit(1);
            return null;
        }

    }

    // Metodo para leer el archivo
    char[] leerArchivoCuerdas(String rutaArchivo) {

        try {

            // Variable donde almacenamos el texto del archivo
            String cadenaDeTexto = "";
            // Hacemos uso de la clase file para leer el archivo
            File archivo = new File(rutaArchivo);
            // La clase scanner nos servira para leer las lineas de texto
            Scanner scanner = new Scanner(archivo);
            // Leemos linea a linea el archivo
            cadenaDeTexto = scanner.nextLine();
            while (scanner.hasNextLine()) {
                cadenaDeTexto = cadenaDeTexto + "\n" + scanner.nextLine();
            }
            // Convertimos la cadena de texto en un array de caracteres para poder trabajar
            // las vocales y cerramos el scanner
            char[] charArray = cadenaDeTexto.toCharArray();
            scanner.close();
            // Imprimimos en consola lo que contiene el archivo
            // System.out.println(charArray);
            return charArray;

        } catch (IOException e) {
            // Imprimimos los errores que puedan ocurrir durante el programa
            e.printStackTrace();
            System.out.println("Error al abrir el archivo de cuerdas");
            System.exit(1);
            return null;
        }

    }

    // Metodo para contar las lineas dentro de un archiivo
    long contarLineas(String rutaArchivo) {

        // La clase Path no ayudara a leer el archivo
        Path path = Paths.get(rutaArchivo);

        long lineas = 0;
        try {

            // El metodo count nos devuelve el numero de lineas que tiene un archivo
            lineas = Files.lines(path).count();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return lineas;

    }

    static void recibirTextoConsola() {

        try {
            BufferedReader reader = new BufferedReader(new java.io.InputStreamReader(System.in));
            System.out.print("Ingrese la cuerda: ");
            cuerdasConsole += reader.readLine()+"\n";
            String otraCadena = "n";

            System.out.print("Ingresar otra cuerda? (y/n): ");
            otraCadena = reader.readLine();

            if (otraCadena.equals("y") || otraCadena.equals("Y")) {
                recibirTextoConsola();
                return;
            }

            // return cadenas;
        } catch (Exception e) {
            System.out.println("Error al recibir la informacion de la consola");
            System.exit(1);
        }

    }

}