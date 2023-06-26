/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package CargarArchivo;
import java.io.File;

/**
 *
 * @author Johana Echevarria
 */
public class ExtendFile extends javax.swing.filechooser.FileFilter{
   
    public boolean accept(File fi) {
        return fi.isDirectory() | fi.getName().toLowerCase().endsWith(".ara");
    }

    public String getDescription() {
        return ".ara Lenguaje Alberto";
    } 
}
