import java.io.BufferedReader;

class TestCode {

    static String cadenas = "";

    public static void main(String[] args) {

        // String string = "a,b,c";
        // char keyword = 't';
        // boolean found =
        // Arrays.asList(string.split(",")).contains(String.valueOf(keyword));
        // System.out.println(found);

        // String test = "a,b,c";
        // ArrayList<String> ar = new ArrayList<String>(Arrays.asList(test.split(",")));
        // int result = ar.indexOf("a");
        // System.out.println(result);
        recibirTexto();
        System.out.println(cadenas);

    }

    static void recibirTexto() {

        try {
            BufferedReader reader = new BufferedReader(new java.io.InputStreamReader(System.in));
            System.out.print("Ingrese la cadena: ");
            cadenas += reader.readLine()+"\n";
            String otraCadena = "n";

            System.out.print("Ingresar otra cadena? (y/n): ");
            otraCadena = reader.readLine();

            if (otraCadena.equals("y") || otraCadena.equals("Y")) {
                recibirTexto();
                return;
            }

            // return cadenas;
        } catch (Exception e) {
            System.out.println("Error al recibir la informacion de la consola");
            System.exit(1);
        }

    }

}