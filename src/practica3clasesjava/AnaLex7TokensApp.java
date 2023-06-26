/*------------------------------------------------------------------------------------------
:*                         TECNOLOGICO NACIONAL DE MEXICO
:*                               CAMPUS LA LAGUNA
:*                     INGENIERIA EN SISTEMAS COMPUTACIONALES
:*                            LENGUAJES Y AUTÓMATAS I
:*
:*                   SEMESTRE: ENE-JUN/2023    HORA: 11-12 HRS
:*
:*          Clase que muestra la interfaz de usuario, además de la resolución
:*          de la creación de cuádruplos, entre otros.
:*          
:*  Archivo     : AnaLex7TokensApp
:*  Autor(es)   : Ygancio Martínez Sánchez          20130778
:*              : Efrain Montalvo Sánchez           20130810
:*              : Johana Echevarría Gallardo        20130795
:*              : Tomás Alejandro Galván Gándara    20130770
:*              : Daniel Vargas Hernandez           20130780
:*              : Edgar Alberto Sandoval Peña       20130777
:*  F. Entrega  : 14/May/2023
:*
:*========================================================================================== 
:*   
:*------------------------------------------------------------------------------------------*/
package practica3clasesjava;

import CargarArchivo.ExtendFile;
import CargarArchivo.FileContent;
import CargarArchivo.SaveFile;
import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableModel;

public class AnaLex7TokensApp extends javax.swing.JFrame {

    //----------------------------------------------------------Hacia abajo------------------------------------------------------------//
    //--Clases, métodos y variables usadas para la creación de los cuadruplos. ES NECESARIO que la variable tenga al menos un número.--//
    public class RealizaCuadruplos {

        public int auxCount = 1;
        //

        String codigo = txaProgFuente.getText();
        DefaultTableModel modelo = (DefaultTableModel) jTableCuadruplos.getModel();

        public void ComputeCuadruplos() {

            modelo.setRowCount(0);

            List<String> expresions = getAllExpressions(codigo, "[a-z]{1,20}[0-9]{0,20}[\\s]{0,3}=[\\s]{0,3}.*;");
            for (String expresion : expresions) {
                expresion = expresion.replace(" ", "");
                expresion = expresion.replaceAll("sqrt", "√");

                //String[] expresionArr = expresion.split("(?<=[-+*/=()√ª])|(?=[-+*/=()√ª])");
                String[] expresionArr = expresion.split("(?<=[-+*/=()√ª\\±])|(?=[-+*/=()√ª\\±])");

                ArrayList<String> expressionList = new ArrayList<String>(Arrays.asList(expresionArr));
                this.solveOperaciones(expressionList, expresion);
            }
        }

        public void solveOperaciones(ArrayList<String> expressionList, String expresion) {
            //int auxCount = 1;

            if (expressionList.indexOf(")") != -1) {
                int originalIndex;
                int indexIniciParentesis = expressionList.indexOf("(") + 1;
                int indexFinParentesis = expressionList.indexOf(")");
                ArrayList<String> expressionParentesis = new ArrayList<>(expressionList.subList(indexIniciParentesis, indexFinParentesis));
                originalIndex = indexIniciParentesis;

                for (int i = 0; i < expressionParentesis.size(); i++) {
                    String current = expressionParentesis.get(i);
                    String op, oper1, oper2, res;
                    if (current.equals("*") || current.equals("/")) {
                        op = current;
                        oper1 = expressionParentesis.get(i - 1);
                        oper2 = expressionParentesis.get(i + 1);
                        res = "temp" + auxCount;
                        System.out.println(op + " " + oper1 + " " + oper2 + " " + res);
                        modelo.addRow(new Object[]{op, oper1, oper2, res});
                        auxCount++;
                        expressionParentesis.set(i, res);
                        expressionList.set(originalIndex, res);
                        expressionParentesis.remove(i - 1);
                        expressionList.remove(originalIndex - 1);
                        expressionParentesis.remove(i);
                        expressionList.remove(originalIndex);
                        i--;
                        originalIndex--;
                    } else if (current.equals("±")) { // DETECTA si el operador es ±  (MÁS MENOS)
                        op = "+"; // primero generar el cuádruplo para la suma
                        oper1 = expressionParentesis.get(i - 1);
                        oper2 = expressionParentesis.get(i + 1);
                        res = "temp" + auxCount;
                        System.out.println(op + " " + oper1 + " " + oper2 + " " + res);
                        modelo.addRow(new Object[]{op, oper1, oper2, res});
                        auxCount++;

                        op = "-"; // luego generar el cuádruplo para la resta
                        oper1 = expressionParentesis.get(i - 1);
                        oper2 = expressionParentesis.get(i + 1);
                        String res2 = "temp" + auxCount;
                        System.out.println(op + " " + oper1 + " " + oper2 + " " + res2);
                        modelo.addRow(new Object[]{op, oper1, oper2, res2});
                        auxCount++;

                        expressionParentesis.set(i, res); // actualizar la lista de expresiones
                        expressionList.set(originalIndex, res);
                        expressionParentesis.remove(i - 1);
                        expressionList.remove(originalIndex - 1);
                        expressionParentesis.remove(i);
                        expressionList.remove(originalIndex);
                        i--;
                        originalIndex--;
                    }
                    originalIndex++;
                }

                originalIndex = indexIniciParentesis;
                for (int i = 0; i < expressionParentesis.size(); i++) {
                    String current = expressionParentesis.get(i);
                    String op, oper1, oper2, res;

                    if (current.equals("*") || current.equals("/")) {
                        op = current;
                        oper1 = expressionList.get(i - 1);
                        oper2 = expressionList.get(i + 1);
                        res = "temp" + auxCount;
                        System.out.println(op + " " + oper1 + " " + oper2 + " " + res);
                        modelo.addRow(new Object[]{op, oper1, oper2, res});
                        auxCount++;
                        expressionList.set(i, res);
                        expressionList.remove(i - 1);
                        expressionList.remove(i);
                        i--;
                    }
                }

                originalIndex = indexIniciParentesis;
                for (int i = 0; i < expressionParentesis.size(); i++) {
                    String current = expressionParentesis.get(i);
                    String op, oper1, oper2, res;
                    if (current.equals("+") || current.equals("-")) {
                        op = current;
                        oper1 = expressionParentesis.get(i - 1);
                        oper2 = expressionParentesis.get(i + 1);
                        res = "temp" + auxCount;
                        System.out.println(op + " " + oper1 + " " + oper2 + " " + res);
                        modelo.addRow(new Object[]{op, oper1, oper2, res});
                        auxCount++;
                        expressionParentesis.set(i, res);
                        expressionList.set(originalIndex, res);
                        expressionParentesis.remove(i - 1);
                        expressionList.remove(originalIndex - 1);
                        expressionParentesis.remove(i);
                        expressionList.remove(originalIndex);
                        i--;
                        originalIndex--;
                    }
                    originalIndex++;
                }
            }

            int indexIniciParentesis = expressionList.indexOf("(");
            if (indexIniciParentesis != -1) {
                expressionList.remove(indexIniciParentesis);
                int indexFinParentesis = expressionList.indexOf(")");
                expressionList.remove(indexFinParentesis);
            }

            for (int i = 0; i < expressionList.size(); i++) {
                String current = expressionList.get(i);
                String op, oper1, oper2, res = null;
                if (current.equals("*") || current.equals("/")) {
                    op = current;
                    oper1 = expressionList.get(i - 1);
                    oper2 = expressionList.get(i + 1);
                    res = "temp" + auxCount;
                    System.out.println(op + " " + oper1 + " " + oper2 + " " + res);
                    modelo.addRow(new Object[]{op, oper1, oper2, res});
                    auxCount++;
                    expressionList.set(i, res);
                    expressionList.remove(i - 1);
                    expressionList.remove(i);
                    i--;
                }
            }

            // Código para manejar la potencia (ª)
            for (int i = 0; i < expressionList.size(); i++) {
                String current = expressionList.get(i);
                String op, oper1, oper2, res = null;
                if (current.equals("ª")) {
                    op = current;
                    oper1 = expressionList.get(i - 1);
                    oper2 = expressionList.get(i + 1);
                    res = "temp" + auxCount;
                    System.out.println(op + " " + oper1 + " " + oper2 + " " + res);
                    modelo.addRow(new Object[]{op, oper1, oper2, res});
                    auxCount++;
                    expressionList.set(i, res);
                    expressionList.remove(i - 1);
                    expressionList.remove(i);
                    i--;
                }
            }

            for (int i = 0; i < expressionList.size(); i++) {
                String current = expressionList.get(i);
                String op, oper1, oper2, res = null;
                if (current.equals("+") || current.equals("-")) {
                    op = current;
                    oper1 = expressionList.get(i - 1);
                    oper2 = expressionList.get(i + 1);
                    res = "temp" + auxCount;
                    System.out.println(op + " " + oper1 + " " + oper2 + " " + res);
                    modelo.addRow(new Object[]{op, oper1, oper2, res});
                    auxCount++;
                    expressionList.set(i, res);
                    expressionList.remove(i - 1);
                    expressionList.remove(i);
                    i--;
                }
            }
            //EN ESTA PARTE ES CUANDO ESCRIBE EL CÓDIGO DE LOS CUADRUPLOS EN CONSOLA (en realidad solo las variables y el resultado final) //
            modelo.addRow(new Object[]{expressionList.get(1), expressionList.get(2), "", expressionList.get(0)});

            System.out.println(expressionList.get(1) + " " + expressionList.get(2) + " " + "" + " " + expressionList.get(0));

        }

        public void solveParentesis(ArrayList<String> expressionList, int index) {
            int auxCount = 1;
            int originalIndex;
            int indexIniciParentesis = expressionList.indexOf("(") - 1;
            int indexFinParentesis = expressionList.indexOf(")") - 1;
            ArrayList<String> expressionParentesis = new ArrayList<>(expressionList.subList(indexIniciParentesis, indexFinParentesis));
            originalIndex = indexIniciParentesis;

            for (int i = 0; i < expressionParentesis.size(); i++) {
                originalIndex++;
                String current = expressionParentesis.get(i);
                String op, oper1, oper2, res;
                if (current.equals("*") || current.equals("/")) {
                    op = current;
                    oper1 = expressionParentesis.get(i - 1);
                    oper2 = expressionParentesis.get(i + 1);
                    res = "temp" + auxCount;
                    modelo.addRow(new Object[]{op, oper1, oper2, res});
                    System.out.println(op + " " + oper1 + " " + oper2 + " " + res);
                    auxCount++;
                    expressionParentesis.set(i, res);
                    expressionList.set(originalIndex, res);
                    expressionParentesis.remove(i - 1);
                    expressionList.remove(originalIndex - 1);
                    expressionParentesis.remove(i);
                    expressionParentesis.remove(originalIndex);
                    i--;
                    originalIndex--;
                }
            }

            // Código para manejar la potencia (ª)
            for (int i = 0; i < expressionList.size(); i++) {
                String current = expressionList.get(i);
                String op, oper1, oper2, res;
                if (current.equals("ª")) {
                    op = current;
                    oper1 = expressionList.get(i - 1);
                    oper2 = expressionList.get(i + 1);
                    res = "temp" + auxCount;
                    modelo.addRow(new Object[]{op, oper1, oper2, res});
                    System.out.println(op + " " + oper1 + " " + oper2 + " " + res);
                    auxCount++;
                    expressionList.set(i, res);
                    expressionList.remove(i - 1);
                    expressionList.remove(i);
                    i--;
                }
            }

            // Código para manejar la raiz
            for (int i = 0; i < expressionList.size(); i++) {
                String current = expressionList.get(i);
                String op, oper1, res;
                if (current.equals("√")) {
                    op = current;
                    oper1 = expressionList.get(i + 1);
                    res = "temp" + auxCount;
                    modelo.addRow(new Object[]{op, oper1, " ", res});
                    System.out.println(op + " " + oper1 + " " + res);
                    auxCount++;
                    expressionList.set(i, res);
                    expressionList.remove(i + 1);
                    i--;
                }

            }

            for (int i = 0; i < expressionParentesis.size(); i++) {
                originalIndex++;
                String current = expressionParentesis.get(i);
                String op, oper1, oper2, res;
                if (current.equals("+") || current.equals("-")) {
                    op = current;
                    oper1 = expressionParentesis.get(i - 1);
                    oper2 = expressionParentesis.get(i + 1);
                    res = "temp" + auxCount;
                    modelo.addRow(new Object[]{op, oper1, oper2, res});
                    System.out.println(op + " " + oper1 + " " + oper2 + " " + res);
                    auxCount++;
                    expressionParentesis.set(i, res);
                    expressionList.set(originalIndex, res);
                    expressionParentesis.remove(i - 1);
                    expressionList.remove(originalIndex - 1);
                    expressionParentesis.remove(i);
                    expressionParentesis.remove(originalIndex);
                    i--;
                    originalIndex--;
                }
            }
        }

        public List<String> getAllExpressions(String input, String regex) {
            final Matcher m = Pattern.compile(regex).matcher(input);

            final List<String> matches = new ArrayList<>();
            while (m.find()) {
                matches.add(m.group(0));
            }

            return matches;
        }

    }

    //--Clases, métodos y variables usadas para la creación de los cuadruplos. ES NECESARIO que la variable tenga al menos un número.--//
    //----------------------------------------------------------Hacia arriba-----------------------------------------------------------//
    Lexico anaLex = new Lexico();
    SintDescNRP anaSintNRP = new SintDescNRP();

    //------------------------------------------------Hacia abajo--------------------------------------------------//
    //---------Variables, clases y métodos para la creación de el cuádruplo en base a la formula general.---------//
    //Se crean los valores que se tomarán para la fórmula general.
//    int valor1;    
//    int valor2;    
//    int valor3;
//    int tempCount = 1;
//    
//    //Se crea un arrayList que servirá para guardar la información del
//    //proceso de los cuádruplos.
//    ArrayList<String> cuadruplosStrings = new ArrayList<String>();
//    
//   class Symbol {
//    String name;
//    double value;
//    public Symbol(String name, double value) {
//        this.name = name;
//        this.value = value;
//    }
//}
//
//enum Operator {
//    ASSIGN, ADD, SUBTRACT, MULTIPLY, DIVIDE, POWER, SQRT
//}
//
//class Quadruple {
//    Operator op;
//    String arg1;
//    String arg2;
//    String result;
//    public Quadruple(Operator op, String arg1, String arg2, String result) {
//        this.op = op;
//        this.arg1 = arg1;
//        this.arg2 = arg2;
//        this.result = result;
//    }
//}
//
//class QuadrupleGenerator {
//    private List<Symbol> symbolTable;
//    private List<Quadruple> quadruples;
//    private int tempCount;
//    
//    public QuadrupleGenerator() {
//        symbolTable = new ArrayList<>();
//        quadruples = new ArrayList<>();
//    }
//    
//    public void generate(Symbol a, Symbol b, Symbol c) {
//        
//        // Reiniciar tempCount a 1 para la tabla
//        tempCount = 1;
//        
//        // Agregar las variables a la tabla de símbolos
//        symbolTable.add(a);
//        symbolTable.add(b);
//        symbolTable.add(c);
//        
//        // Generar cuádruplos para asignar valores iniciales
//        Quadruple q1 = new Quadruple(Operator.ASSIGN, Double.toString(a.value), "-", a.name);
//        Quadruple q2 = new Quadruple(Operator.ASSIGN, Double.toString(b.value), "-", b.name);
//        Quadruple q3 = new Quadruple(Operator.ASSIGN, Double.toString(c.value), "-", c.name);
//        quadruples.add(q1);
//        quadruples.add(q2);
//        quadruples.add(q3);
//        
//        // Genera Strings de cuádruplos para la expresión de la fórmula general
//        //(-b ± sqrt(b^2 - 4*a*c)) / (2*a)
//        String t1 = newTemp();
//        String t2 = newTemp();
//        String t3 = newTemp();
//        String t4 = newTemp();
//        String t5 = newTemp();
//        String t6 = newTemp();
//        String t7 = newTemp();
//        String t8 = newTemp();
//        String t9 = newTemp();
//        String t10 = newTemp();
//        String t11 = newTemp();
//        String t12 = newTemp();
//        
//        //Comienza el proceso de los cuádruplos para hacer los pasos
//        
//        // b^2
//        Quadruple q4 = new Quadruple(Operator.POWER, b.name, "2", t1);
//        quadruples.add(q4);
//        
//        // 4*a*c
//        Quadruple q5 = new Quadruple(Operator.MULTIPLY, "4", a.name, t2);
//        quadruples.add(q5);
//        Quadruple q6 = new Quadruple(Operator.MULTIPLY, t2, c.name, t3);
//        quadruples.add(q6);
//        
//        // b^2 - 4*a*c
//        Quadruple q7 = new Quadruple(Operator.SUBTRACT, t1, t3, t4);
//        quadruples.add(q7);
//        
//        // sqrt(b^2 - 4*a*c)
//        Quadruple q8 = new Quadruple(Operator.SQRT, t4, "-", t5);
//        quadruples.add(q8);
//    
//        // -b
//        Quadruple q9 = new Quadruple(Operator.SUBTRACT, "0", b.name, t6);
//        quadruples.add(q9);
//    
//        // -b + sqrt(b^2 - 4*a*c)
//        Quadruple q10 = new Quadruple(Operator.ADD, t6, t5, t7);
//        quadruples.add(q10);
//    
//        // -b - sqrt(b^2 - 4*a*c)
//        Quadruple q11 = new Quadruple(Operator.SUBTRACT, t6, t5, t8);
//        quadruples.add(q11);
//
//        // 2*a
//        Quadruple q12 = new Quadruple(Operator.MULTIPLY, "2", a.name, t9);
//        quadruples.add(q12);
//    
//        // (-b + sqrt(b^2 - 4*a*c)) / (2*a)
//        Quadruple q13 = new Quadruple(Operator.DIVIDE, t7, t9, t10);
//        quadruples.add(q13);
//    
//        // (-b - sqrt(b^2 - 4*a*c)) / (2*a)
//        Quadruple q14 = new Quadruple(Operator.DIVIDE, t8, t9, t11);
//        quadruples.add(q14);
//
//        // Agregar el cuádruplo para asignar el resultado a x
//        Quadruple q15 = new Quadruple(Operator.ASSIGN, t10, "-", "x");
//        quadruples.add(q15);
//    }   
//
//
//    private String newTemp() {
//        // Generar un nuevo nombre temporal
//        String temp = "t" + tempCount;
//        tempCount++;
//        return temp;
//    }
//
//    //Método para imprimir los pasos de los cuádruplos, solamente en Consola
//    public void printQuadruples() {
//        // Imprimir la tabla de símbolos y los cuádruplos generados
//        System.out.println("Tabla de simbolos:");
//        for (Symbol symbol : symbolTable) {
//            System.out.println(symbol.name + " = " + symbol.value);
//        }
//    
//    System.out.println("\nCuadruplos generados:");
//    for (Quadruple quadruple : quadruples) {
//    String cuadruploString = quadruple.op + " " + quadruple.arg1 + " " + quadruple.arg2 + " " + quadruple.result;
//    System.out.println(cuadruploString);
//    cuadruplosStrings.add(cuadruploString);
//    }
//    
//   }
//}
    //---------Variables, clases y métodos para la creación de el cuádruplo en base a la formula general.---------//
    //------------------------------------------------Hacia arriba--------------------------------------------------//
    public AnaLex7TokensApp() {
        initComponents();
        this.setSize(1170, 650);

    }

    public boolean Examen(String lex) {
        if (lex.length() != 3) {
            return false;
        }
        char c = lex.charAt(0);
        if ("AEIOUaeiou".indexOf(c) < 0) {
            return false;
        }
        if (Character.isDigit(lex.charAt(1)) || Character.isDigit(lex.charAt(2))) {
            return true;
        }
        return false;
    }

    //Regresa números pares que no sean exponenciales
    private boolean Boton1(String lexema[], int i) {
        if (!(lexema[i].indexOf("E") > 0) || !(lexema[i].indexOf("e") > 0)) {
            try {
                int num = Integer.parseInt(lexema[i]);
                if (num % 2 == 0) {
                    lexema[i] = Integer.toString(num);
                    return true;
                } else {
                    return false;
                }
            } catch (NumberFormatException e) {
                return false;
            }
        } else {
            return false;
        }
    }

    //Regresa los ids que sean de máximo cinco de longitud y tengan al menos una vocal
    private boolean Boton2(String lexema[], int j, String token) {
        String Vocales = "AEIOUaeiou";
        if (token.equals("id")) {
            if (lexema[j].length() <= 5) {
                for (int i = 0; i < lexema[j].length(); i++) {
                    if (Vocales.indexOf(lexema[j].charAt(i)) > 0) {
                        return true;
                    } else {
                        return true;
                    }
                }
            } else {
                return false;
            }
        } else if (lexema[j].charAt(0) == '"') {
            String txt = lexema[j].substring(1, lexema[j].length() - 1);
            lexema[j] = txt;
            return true;
        } else {
            return false;
        }
        return false;
    }

    //Métodos para números mayores que 100
    private boolean Boton3(String lexema) {
        try {
            double num = Double.parseDouble(lexema);
            if (num > 100) {
                return true;
            } else {
                return false;
            }
        } catch (NumberFormatException e) {
            return false;
        }

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txaProgFuente = new javax.swing.JTextArea();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblParejasTL = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        btnAnaSin = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        txtaResultado = new javax.swing.JTextArea();
        jLabelNumLineas = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTableCuadruplos = new javax.swing.JTable();
        jLabel3 = new javax.swing.JLabel();
        jbtnGenCuadruplo = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItemAbrir = new javax.swing.JMenuItem();
        jMenuItemGuardar = new javax.swing.JMenuItem();
        jMenuItemSalir = new javax.swing.JMenuItem();
        jMenu8 = new javax.swing.JMenu();
        jMenuItemEjecutar = new javax.swing.JMenuItem();
        jMenuItemLimpiar = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenu3 = new javax.swing.JMenu();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenuItem4 = new javax.swing.JMenuItem();
        jMenuItem1 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Proyecto Final Lenguajes y Automatas I: Compilador");
        setSize(new java.awt.Dimension(1141, 642));
        getContentPane().setLayout(null);

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel1.setText("PROGRAMA FUENTE");
        getContentPane().add(jLabel1);
        jLabel1.setBounds(24, 50, 136, 17);

        txaProgFuente.setColumns(20);
        txaProgFuente.setRows(5);
        jScrollPane1.setViewportView(txaProgFuente);

        getContentPane().add(jScrollPane1);
        jScrollPane1.setBounds(24, 73, 349, 480);

        tblParejasTL.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "TOKENS", "LEXEMAS"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(tblParejasTL);

        getContentPane().add(jScrollPane2);
        jScrollPane2.setBounds(390, 70, 250, 320);

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel2.setText("PAREJAS TOKENS-LEXEMAS ");
        getContentPane().add(jLabel2);
        jLabel2.setBounds(390, 50, 200, 17);

        btnAnaSin.setText("Análisis");
        btnAnaSin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAnaSinActionPerformed(evt);
            }
        });
        getContentPane().add(btnAnaSin);
        btnAnaSin.setBounds(570, 400, 150, 30);

        txtaResultado.setBackground(new java.awt.Color(241, 237, 179));
        txtaResultado.setColumns(20);
        txtaResultado.setRows(5);
        txtaResultado.setBorder(javax.swing.BorderFactory.createTitledBorder("Resultado del análisis"));
        jScrollPane3.setViewportView(txtaResultado);

        getContentPane().add(jScrollPane3);
        jScrollPane3.setBounds(390, 440, 730, 110);

        jLabelNumLineas.setBackground(new java.awt.Color(51, 255, 51));
        getContentPane().add(jLabelNumLineas);
        jLabelNumLineas.setBounds(30, 560, 340, 20);

        jTableCuadruplos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Operacion", "Operador 1", "Operador 2", "Resultado"
            }
        ));
        jScrollPane4.setViewportView(jTableCuadruplos);

        getContentPane().add(jScrollPane4);
        jScrollPane4.setBounds(670, 70, 440, 320);

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("COMPILADOR");
        getContentPane().add(jLabel3);
        jLabel3.setBounds(440, 10, 270, 16);

        jbtnGenCuadruplo.setText("Generar Cuadruplo");
        jbtnGenCuadruplo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnGenCuadruploActionPerformed(evt);
            }
        });
        getContentPane().add(jbtnGenCuadruplo);
        jbtnGenCuadruplo.setBounds(820, 400, 140, 30);

        jButton3.setText("ª");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton3);
        jButton3.setBounds(40, 20, 50, 22);

        jButton4.setText("√");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton4);
        jButton4.setBounds(90, 20, 50, 22);

        jButton5.setText("±");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton5);
        jButton5.setBounds(140, 20, 40, 22);

        jMenu1.setText("Archivo");

        jMenuItemAbrir.setText("Abrir");
        jMenuItemAbrir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemAbrirActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItemAbrir);

        jMenuItemGuardar.setText("Guardar");
        jMenuItemGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemGuardarActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItemGuardar);

        jMenuItemSalir.setText("Salir");
        jMenuItemSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemSalirActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItemSalir);

        jMenuBar1.add(jMenu1);

        jMenu8.setText("Ejecutar");

        jMenuItemEjecutar.setText("Ejecutar");
        jMenuItemEjecutar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemEjecutarActionPerformed(evt);
            }
        });
        jMenu8.add(jMenuItemEjecutar);

        jMenuItemLimpiar.setText("Limpiar");
        jMenuItemLimpiar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemLimpiarActionPerformed(evt);
            }
        });
        jMenu8.add(jMenuItemLimpiar);

        jMenuBar1.add(jMenu8);

        jMenu2.setText("Ayuda");

        jMenu3.setText("Ejemplos");

        jMenuItem2.setText("Ejemplo 1");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem2);

        jMenuItem3.setText("Ejemplo 2");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem3);

        jMenuItem4.setText("Ejemplo 3");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem4);

        jMenuItem1.setText("Ejemplo 4");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem1);

        jMenu2.add(jMenu3);

        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnAnaSinActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAnaSinActionPerformed
        /**
         * == Modificado el 07/05/2023, modificado por última vez el 12/05/2023
         * == Botón que realiza ambos análisis.
         */
        anaLex.Inicia();
        int errorLine = anaLex.Analiza(txaProgFuente.getText());
        if (errorLine == 0) { // No hay errores léxicos
            // Procede a validar si hay errores sintácticos, apoyándose
            // del contenido original del TextArea para encontrar la línea
            // exacta en caso de encontrar alguno)
            anaSintNRP.Inicia();
            String resultadoAnalisis = anaSintNRP.Analiza(anaLex, txaProgFuente.getText());
            txtaResultado.setText(resultadoAnalisis);
        } else { // Hay errores léxicos
            txtaResultado.setText("Error léxico en la línea " + errorLine);
        }

        DefaultTableModel modelo = (DefaultTableModel) tblParejasTL.getModel();
        modelo.setRowCount(0);
        for (int i = 0; i < anaLex.NoTokens(); i++) {
            Object o[] = {anaLex.Tokens()[i], anaLex.Lexemas()[i]};
            modelo.addRow(o);
        }
    }//GEN-LAST:event_btnAnaSinActionPerformed

    private void jMenuItemAbrirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemAbrirActionPerformed

        JFileChooser jFileChooser1 = new JFileChooser();
        jFileChooser1.setAcceptAllFileFilterUsed(true);
        ExtendFile ef = new ExtendFile();
        jFileChooser1.setFileFilter(ef);
        jFileChooser1.setDialogType(JFileChooser.OPEN_DIALOG);
        jFileChooser1.setDialogTitle("Abrir");
        jFileChooser1.showOpenDialog(this);
        File fi = jFileChooser1.getSelectedFile();

        CargarArchivo.OpenFile openfi = new CargarArchivo.OpenFile();
        FileContent fileContent = openfi.CargarArchivo(fi);

        txaProgFuente.setText(fileContent.getContent());

        // Mostrar el número de líneas en un JLabel
        int numLines = fileContent.getNumLines();
        jLabelNumLineas.setText("Número de líneas del archivo original: " + numLines);
        jLabelNumLineas.setForeground(Color.BLUE);
    }//GEN-LAST:event_jMenuItemAbrirActionPerformed

    private void jMenuItemGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemGuardarActionPerformed

        JFileChooser guardar = new JFileChooser();
        guardar.showSaveDialog(null);
        guardar.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

        File archivo = guardar.getSelectedFile();

        SaveFile sf = new SaveFile();
        try {
            sf.guardarFichero(txaProgFuente.getText(), archivo);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
    }//GEN-LAST:event_jMenuItemGuardarActionPerformed

    private void jMenuItemEjecutarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemEjecutarActionPerformed

        if (txaProgFuente.getText().equals("")) {
            JOptionPane.showMessageDialog(this,
                    "Primero escribe el código en el area destinada,\n"
                    + " o bien, abre un archivo de tu preferencia.",
                    "Alerta",
                    JOptionPane.INFORMATION_MESSAGE);
        } else {
            btnAnaSin.doClick();
            jbtnGenCuadruplo.doClick();
        }

    }//GEN-LAST:event_jMenuItemEjecutarActionPerformed

    private void jMenuItemLimpiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemLimpiarActionPerformed

        txaProgFuente.setText("");
        DefaultTableModel modelo = (DefaultTableModel) tblParejasTL.getModel();
        DefaultTableModel modeloTablaCuadruplos = (DefaultTableModel) jTableCuadruplos.getModel();
        modelo.setRowCount(0);
        modeloTablaCuadruplos.setRowCount(0);
        txtaResultado.setText("Resultado del Análisis Léxico");

    }//GEN-LAST:event_jMenuItemLimpiarActionPerformed

    private void jMenuItemSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemSalirActionPerformed

        this.dispose();
    }//GEN-LAST:event_jMenuItemSalirActionPerformed

    private void jbtnGenCuadruploActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnGenCuadruploActionPerformed

        //Código que realiza los cuádruplos que están en la clase RealizaCuadruplos al inicio del código.
        RealizaCuadruplos realizaCuadruplos = new RealizaCuadruplos();
        realizaCuadruplos.ComputeCuadruplos();

        //------------------------------Código que genera la fórmula general en base a 3 variables int------------------------------//
//        // Se obtiene el código del JTextArea
//        String codigo = txaProgFuente.getText();
//
//        // Se crea un "Map" para asociar nombres de variables con sus valores
//        Map<String, Integer> variables = new HashMap<>();
//
//        // Pattern y matcher para buscar los nombres de las variables. 
//        //Se usan expresiones regulares yluego se agregan al Map
//        Pattern pattern = Pattern.compile("int\\s+(\\w+)\\s*=\\s*([-]?\\d+);");
//        Matcher matcher = pattern.matcher(codigo);
//        while (matcher.find()) {
//            String nombre = matcher.group(1);
//            int valor = Integer.parseInt(matcher.group(2));
//            variables.put(nombre, valor);
//        }
//
//        //Se obtienenn los valores de las variables para el uso en los cuádruplos
//        valor1 = variables.get("a");
//        valor2 = variables.get("b"); 
//        valor3 = variables.get("c");
//        
//        QuadrupleGenerator generator = new QuadrupleGenerator();
//
//        // Se crean los símbolos a, b y c en base a el valor que se obtuvo.
//        Symbol a = new Symbol("a", valor1);
//        Symbol b = new Symbol("b", valor2);
//        Symbol c = new Symbol("c", valor3);
//    
//        // Genera los cuádruplos para la expresión
//        generator.generate(a, b, c);
//    
//        // Mostrar los cuádruplos generados (solamente en consola)
//        generator.printQuadruples();
//
//
//        
//        // Obtener modelo de tabla del jTable en el que se colocará (en este caso
//        //en la tabla de cuádruplos.)
//         DefaultTableModel modeloTabla = (DefaultTableModel) jTableCuadruplos.getModel();
//
//        // Recorre el ArrayList y agrega los elemento en sus respectivas columnas.
//        for (int i = 0; i < cuadruplosStrings.size(); i++) {
//            Object dato = cuadruplosStrings.get(i); // Obtener el dato del elemento en el ArrayList
//            String temp = "tmp" + (i+1); // Generar el texto con el aumento correspondiente
//            modeloTabla.setValueAt(temp, i, 0); // Agrega el texto en la columna 0
//            modeloTabla.setValueAt(dato, i, 1); // Agrega el dato a la segunda columna
//        }
//
//        // Actualiza la tabla
//        jTableCuadruplos.setModel(modeloTabla);
        //------------------------------Código que genera la fórmula general en base a 3 variables int------------------------------//

    }//GEN-LAST:event_jbtnGenCuadruploActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        // TODO add your handling code here:

        txaProgFuente.setText("public class Ejemplo1 {\n"
                + "    int var1 = 1;\n"
                + "    int var2 = 2;\n"
                + "    int var3 = 3;\n"
                + "    int var4 = 4;\n"
                + "    int var5 = 5;\n"
                + "\n"
                + "    int varRes1 = var1 + var2 / var3 * var4;\n"
                + "}");

    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        // TODO add your handling code here:

        txaProgFuente.setText("public class Ejemplo2 {\n"
                + "    int a1 = 2;\n"
                + "    int b1 = 6;\n"
                + "    int c1 = 4;\n"
                + "    int d1 = 8;\n"
                + "\n"
                + "    int x1 = a1 - b1 / (d1 * c1);\n"
                + "}");

    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed
        // TODO add your handling code here:

        txaProgFuente.setText("public class Ejemplo3 {\n"
                + "int a1 = 4;\n"
                + "int b1 = 2;\n"
                + "int c1 = 1;\n"
                + "int x1 = (-b ± sqrt(bª2 - 4*a*c)) / (2*a);\n"
                + "}");

    }//GEN-LAST:event_jMenuItem4ActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        // TODO add your handling code here:

        txaProgFuente.setText("public class Ejemplo4 {\n"
                + "int w4 = 4;\n"
                + "int x4 = 2;\n"
                + "int y4 = 5;\n"
                + "int z1 = 8;\n"
                + "int res4 = w4 ª x4 / √(y4 * z4);\n"
                + "}");

    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        txaProgFuente.append("ª");
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        txaProgFuente.append("√");
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        txaProgFuente.append("±");
    }//GEN-LAST:event_jButton5ActionPerformed

    public void ContarProducciones() {
        DefaultTableModel modelo = (DefaultTableModel) tblParejasTL.getModel();
        while (modelo.getRowCount() > 0) {
            modelo.removeRow(0);
        }
        int ocurrencias;
        int indiceProd;
        int noProd = 0;
        for (int i = 1; i < anaSintNRP.Vns().length; i++) {
            ocurrencias = 0;
            for (int j = 0; j < anaSintNRP.NoDis(); j++) {
                indiceProd = anaSintNRP.Di()[j];
                noProd = anaSintNRP.Prod()[indiceProd][0];
                if (i == noProd) {
                    ocurrencias++;
                }
                if (ocurrencias >= 2) {
                    Object[] ren = new Object[2];
                    ren[0] = noProd;
                    ren[1] = "SÍ";
                    modelo.addRow(ren);
                    break;
                }
            }
        }
        /*int ocurrencias;
        for (int i = 0; i < anaSintNRP.Di().length; i++) {
            ocurrencias = 0;
            int noProd=0;
            int noDi = anaSintNRP.Di()[i];
            for (int j = 0; j < anaSintNRP.NoProd(); i++) {
                noProd = anaSintNRP.Prod()[noDi][0];
                if (noProd == anaSintNRP.Prod()[j][0]) {
                    ocurrencias++;
                }
            }
            if(ocurrencias>=2) {
                Object[] ren = new Object[2];
                ren[0] = noProd;
                ren[1] = ocurrencias;
                modelo.addRow(ren);
            }
            
        }*/
 /*int noProd=0;
        for(int i=1; i<anaSintNRP.NoProd(); i++) {
            if(noProd != anaSintNRP.Prod()[i][0]) 
                noProd=anaSintNRP.Prod()[i][0];
            int contProd=0;
            for(int j=0; j<anaSintNRP.Di().length; j++) {
                if(noProd==anaSintNRP.Di()[j]) {
                    contProd++;
                }
            }
            if(contProd>=2) {
                DefaultTableModel modelo = (DefaultTableModel) tblDosDI.getModel();
                Object[] ren = new Object[2];
                ren[0] = noProd;
                ren[1] = contProd;
                modelo.addRow(ren);
            }
        }*/
    }

    //Método usado en btnEmptyDIActionPerformed
    /*public void EmptyDIs() {
        int contador=0;
        for(int i=0; i<anaSintNRP.NoDis(); i++) {
            if(anaSintNRP.Prod()[anaSintNRP.Di()[i]][1] == 0) {
                contador++;
            }
        }
        lblNoEmpty.setText(""+contador);
    }
    
    //PARTE DE btnDIActionPerformed
    /*public void VisuaDI() {
        lbl3.setText("DERIVACIÓN A LA IZQUIERDA");
        DefaultTableModel modelo = (DefaultTableModel) tblProducciones.getModel();
        while(modelo.getRowCount()>0) {
            modelo.removeRow(0);
        }
        for(int i=0; i<anaSintNRP.NoDis(); i++) {
            Object[] o = new Object[2];
            o[0]=i+1;
            o[1]=anaSintNRP.ProdCad(anaSintNRP.Di()[i]);
            modelo.addRow(o);
        }
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(AnaLex7TokensApp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AnaLex7TokensApp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AnaLex7TokensApp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AnaLex7TokensApp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new AnaLex7TokensApp().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAnaSin;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabelNumLineas;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu8;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItemAbrir;
    private javax.swing.JMenuItem jMenuItemEjecutar;
    private javax.swing.JMenuItem jMenuItemGuardar;
    private javax.swing.JMenuItem jMenuItemLimpiar;
    private javax.swing.JMenuItem jMenuItemSalir;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTable jTableCuadruplos;
    private javax.swing.JButton jbtnGenCuadruplo;
    private javax.swing.JTable tblParejasTL;
    private javax.swing.JTextArea txaProgFuente;
    private javax.swing.JTextArea txtaResultado;
    // End of variables declaration//GEN-END:variables

    private void caracter(String[] Lexemas, int i) {
        if (Lexemas[i].length() > 3) {
            //Si su longitud es mayor que tres, es que tenemos un octal.
            //o hexadecimal
            String tex = Lexemas[i].substring(1, Lexemas[i].length() - 1);
            int k = 0;
            if (tex.charAt(1) == 'x') {
                //Si tiene una x, es hexadecimal y tomamos todo, desde
                //después de la x (por ello el substring(2), toma toda
                //la cadena desde el indice dos hasta el final), 
                //
                //el 16, para usar el sistema hexadecimal.
                k = Integer.parseInt(tex.substring(2), 16);
                //O también puede ser que tenga una h al final, para.
                //Aplica lo mismo
            } else if (tex.charAt(tex.length() - 1) == 'h') {
                k = Integer.parseInt(tex.substring(0, tex.length() - 1), 16);
            } else
                            //Aquí llegamos si es un octal. Por suerte, la clase
                            //Integer con su parse se encarga de esto.
                            try {
                k = Integer.parseInt(tex, 8);
            } catch (NumberFormatException e) {
                System.out.println("Introduce un caracter valido" + e.getMessage());
            }
            //char c = 1;
            //Lo convertimos a char para almacenarlo
            char c = (char) k;
            Lexemas[i] = Character.toString(c);
        }
    }
}
