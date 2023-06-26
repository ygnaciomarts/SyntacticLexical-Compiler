/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package CargarArchivo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/**
 *
 * @author Johana Echevarria
 */
public class SaveFile {
    public void guardarFichero(String cadena, File archivo) throws IOException{

    FileWriter escribir;
    try {

        escribir = new FileWriter(archivo, true);
        escribir.write(cadena);
        escribir.close();

    } catch (FileNotFoundException ex) {
        JOptionPane.showMessageDialog(null, "Error al guardar, ponga nombre al archivo");
    } catch (IOException ex) {
        JOptionPane.showMessageDialog(null, "Error al guardar, en la salida");
    }
}
}
