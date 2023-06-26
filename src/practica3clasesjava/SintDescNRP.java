/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package practica3clasesjava;

import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author Han-S
 */
public class SintDescNRP {

    public final int NODIS = 5000;

    private final Pila _pila;
    private final String[] _vts = {"", "id", "=", ";", "+", "-", "*", "/", "num", "(", ")", "$"};
    private final String[] _vns = {"", "A", "A'", "E", "E'", "T", "T'", "F"};

    //La primera columna es el número de producción, la segunda es el No de Yes y los otros son tokens (posición en el arreglo, núm negativos)
    //y variables terminales (posición en el arreglo, núm positivo)
    private final int[][] _prod = {
        {1, 5, -1, -2, 3, -3, 2}, // A->id = E ; A' 
        {2, 5, -1, -2, 3, -3, 2}, // A'->id = E ; A' 
        {2, 0, 0, 0, 0, 0, 0}, // A'->£
        {3, 2, 5, 4, 0, 0, 0}, // E->T E' 
        {4, 3, -4, 5, 4, 0, 0}, // E'->+ T E' 
        {4, 3, -5, 5, 4, 0, 0}, // E'->- T E' 
        {4, 0, 0, 0, 0, 0, 0}, // E'->£
        {5, 2, 7, 6, 0, 0, 0}, // T->F T' 
        {6, 3, -6, 7, 6, 0, 0}, // T'->* F T' 
        {6, 3, -7, 7, 6, 0, 0}, // T'->/ F T' 
        {6, 0, 0, 0, 0, 0, 0}, // T'->£
        {7, 1, -1, 0, 0, 0, 0}, // F->id 
        {7, 1, -8, 0, 0, 0, 0}, // F->num 
        {7, 3, -9, 3, -10, 0, 0} // F->( E ) 
    };

    //3 columnas: primera vns, la segunda los vts y la tercera el número de la producción
    private final int[][] _m = {
        {1, 1, 0},
        {2, 1, 1},
        {2, 11, 2},
        {3, 1, 3},
        {3, 8, 3},
        {3, 9, 3},
        {4, 4, 4},
        {4, 5, 5},
        {4, 3, 6},
        {4, 10, 6},
        {5, 1, 7},
        {5, 8, 7},
        {5, 9, 7},
        {6, 6, 8},
        {6, 7, 9},
        {6, 4, 10},
        {6, 5, 10},
        {6, 3, 10},
        {6, 10, 10},
        {7, 1, 11},
        {7, 8, 12},
        {7, 9, 13}
    };

    public void tienePesos() {
        for (int i = 0; i < _noEnt; i++) {
            String simb = _vts[_m[i][1]];
            if (simb.equals("$")) {
                System.out.println("Tiene");
            }
        }
    }

    private final int _noVts;
    private final int _noVns;
    private final int _noProd;
    private final int _noEnt;
    private final int[] _di;
    private int _noDis;

    // Metodos 
    public SintDescNRP() // Constructor -----------------------
    {
        _pila = new Pila();
        _noVts = _vts.length;
        _noVns = _vns.length;
        _noProd = 14;
        _noEnt = 22;
        _di = new int[NODIS];
        _noDis = 0;
    }  // Fin del Constructor ---------------------------------------

    public void Inicia() // Constructor -----------------------
    {
        _pila.Inicia();
        _noDis = 0;
    }

    public int[][] Prod() {
        return _prod;
    }

    /**
     * == Modificado el 07/05/2023, modificado por última vez el 12/05/2023 ==
     * Método que realiza el análisis sintáctico del programa fuente.
     */
    public String Analiza(Lexico oAnaLex, String programaFuente) {
        if (programaFuente.isEmpty()) { // Primero se valida que el programa fuente
                                        // no venga vacío, en caso de que sí,
                                        // aplica un return
            JOptionPane.showMessageDialog(
                    null, 
                    "El programa fuente está vacío. Por favor, verifique...", 
                    "Advertencia", 
                    JOptionPane.WARNING_MESSAGE
            );
            return "Programa fuente vacío.";
        }
        
        SimbGram x; // Apuntará al símbolo gramatical del tope de la pila
        String a; // Apuntará al siguiente símbolo en w$
        int noProd; // Referencia al número de producción si necesitamos 
        // buscarlo en la tabla M

        _pila.Inicia(); //Metemos el $
        _pila.Push(new SimbGram("$"));
        _pila.Push(new SimbGram(_vns[1])); // Mete el símbolo de inicio

        int ae = 0; // Variable que indica la posición actual en la cadena de tokens
        int lineaActual = 1; // Variable que almacena la línea actual del programa fuente
        List<Integer> lineasConError = new ArrayList<>(); // Lista de líneas con error

        String[] lineas = programaFuente.split("\n"); // Divide el programa fuente en líneas
        String[] tokens = oAnaLex.Tokens(); // Obtiene los tokens del analizador léxico

        do {
            x = _pila.Tope(); // Símbolo gramátical en el tope de la pila, primero será A
            a = tokens[ae]; // Regresa el arreglo de tokens y agarramos el elemento 0

            if (EsTerminal(x.Elem())) { // Verifica si el símbolo en la cima de la pila es terminal
                // Si X==a saco al tope de la pila e incremento ae
                if (x.Elem().equals(a)) {
                    _pila.Pop();
                    ae++; // Avanza a la siguiente posición de token
                } else { // Error de tipo 1
                    lineasConError.add(obtenerNumeroLinea(programaFuente.substring(0, ae), lineas));
                    return "Error sintáctico (1) en la línea " + lineasConError.get(0);
                }
            } else {
                // Revisa si existe esa producción, pasando a 'X' y a 'a'
                if ((noProd = BusqProd(x.Elem(), a)) >= 0) { // Sí existe, sacamos el tope de la pila
                    _pila.Pop();
                    MeterYes(noProd); // Y metemos todas las yes de la anterior producción empezando desde el final
                    _di[_noDis++] = noProd; // En derivación hacia la izquierda mete el no prod
                } else { // Error de tipo 2
                    lineasConError.add(obtenerNumeroLinea(programaFuente.substring(0, ae), lineas));
                    return "Error sintáctico (2) en la línea " + lineasConError.get(0);
                }
            }

            if (ae < tokens.length && programaFuente.contains(tokens[ae])) {
                // Verifica si aún quedan tokens por analizar y si el programa fuente contiene el token actual
                int indice = programaFuente.indexOf(tokens[ae], ae); // Obtiene el índice de la primera aparición del token en el programa fuente
                lineaActual = obtenerNumeroLinea(programaFuente.substring(0, indice), lineas);
                // Obtiene la subcadena del programa fuente desde el inicio hasta la posición del token
                // Luego, obtiene el número de línea correspondiente a esa subcadena
            }

        } while (!x.Elem().equals("$")); // Mientras no sea igual a $

        if (lineasConError.isEmpty()) {
            return "Análisis exitoso";
        } else {
            return "Error desconocido en la línea: " + lineasConError.toString();
        }
    }

    /**
     * == Método nuevo, añadido el 12/05/2023 == Método que obtiene el número de
     * línea correspondiente a una posición en el subprograma, proveniente desde
     * el TextArea.
     */
    private int obtenerNumeroLinea(String subProgramaFuente, String[] lineas) {
        int contador = 1; // Contador de líneas
        int posicion = 0; // Variable para rastrear la posición

        for (int i = 0; i < lineas.length; i++) {
            posicion = subProgramaFuente.indexOf(lineas[i], posicion); // Busca la posición de la línea en el subprograma fuente
            if (posicion >= 0) {
                contador++; // Incrementa el contador de líneas
                posicion += lineas[i].length(); // Actualiza la posición agregando la longitud de la línea
            } else {
                break; // Si no se encuentra la línea, se detiene el bucle
            }
        }

        return contador; // Devuelve el número de línea correspondiente
    }

    //Recibe un string y recorre el arrelgo de terminales, si lo encuentra regresa verdadero,
    //si no falso
    public boolean EsTerminal(String x) {
        for (int i = 1; i < _noVts; i++) {
            if (_vts[i].equals(x)) {
                return true;
            }
        }
        return false;
    }

    //Recibe dos strings
    public int BusqProd(String x, String a) {
        int indiceX = 0;
        //Busca el índice de x en las variables no terminales
        for (int i = 1; i < _noVns; i++) {
            if (_vns[i].equals(x)) {
                indiceX = i;
                break;
            }
        }
        ///Busca el índice de a en las variables terminales
        int indiceA = 0;
        for (int i = 1; i < _noVts; i++) {
            if (_vts[i].equals(a)) {
                indiceA = i;
                break;
            }
        }
        //Checamos si en las entradas de la tabla m existe la entrada
        for (int i = 0; i < _noEnt; i++) {
            //Revisa si existe alguna entrada en la tabla m que tenga ese vns y ese vns
            if (indiceX == _m[i][0] && indiceA == _m[i][1]) {
                //Regresa el indice de la producción
                return _m[i][2];
            }
        }
        //-1 si no existe
        return -1;
    }

    //Recibe el número de la producción
    public void MeterYes(int noProd) {
        //Obtenemos el número de yes para esa producción especifica
        int noYes = _prod[noProd][1];
        //Recorremos desde 1 hasta el número de yes
        for (int i = 1; i <= noYes; i++) {
            //Tomamos de prod el renglón y el último
            if (_prod[noProd][noYes + 2 - i] < 0) {
                //Si es menor que cero, tomamos el índice en vts, pero antes hay que hacerlo positivo
                _pila.Push(new SimbGram(_vts[-_prod[noProd][noYes + 2 - i]]));
                //Si es mayor que 0 quiere decir que es variable sintáctica
                //no hay necesidad de hacerlo positivo
            } else {
                _pila.Push(new SimbGram(_vns[_prod[noProd][noYes + 2 - i]]));
            }
        }
    }

    public String[] Vts() {
        return _vts;
    }

    public String[] Vns() {
        return _vns;

    }

    public int[][] M() {
        return _m;

    }

    public int NoDis() {
        return _noDis;
    }

    public int[] Di() {

        return _di;

    }

    public int IndiceVn(String vn) {
        for (int i = 1; i < _noVns; i++) {
            if (_vns[i].equals(vn)) {
                return i;
            }
        }
        return 0;
    }

    public int IndiceVt(String vt) {
        for (int i = 1; i < _noVts; i++) {
            if (_vts[i].equals(vt)) {
                return i;
            }
        }
        return 0;
    }

    public int NoProd() {
        return _noProd;
    }

    public String ProdCad(int noProd) {
        //[_prod[noProd][0]] Nos sirve para obtener el número de la variable sintactica en el arreglo _vns
        String aux = "" + _vns[_prod[noProd][0]] + "->";
        int noYes = _prod[noProd][1];
        if (noYes > 0) {
            for (int i = 2; i <= noYes + 1; i++) {
                if (_prod[noProd][i] < 0) {
                    aux += " " + _vts[-_prod[noProd][i]];
                } else {
                    aux += " " + _vns[_prod[noProd][i]];
                }
            }
        } else {
            aux += " £";
        }
        return aux;
    }

    public int noVts() {
        return _noVts;
    }

    public int noEnt() {
        return _noEnt;
    }
}
