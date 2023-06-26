package CargarArchivo;

import CargarArchivo.FileContent;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;

public class OpenFile {
    public FileContent CargarArchivo(File file) {
        FileContent fileContent = new FileContent();
        try {
            FileReader fr = new FileReader(file);
            LineNumberReader lnr = new LineNumberReader(fr);

            StringBuilder textFile = new StringBuilder();
            String line;
            while ((line = lnr.readLine()) != null) {
                textFile.append(line).append("\n");
            }

            fileContent.setContent(textFile.toString());
            fileContent.setNumLines(lnr.getLineNumber());

            lnr.close();
            fr.close();
        } catch (IOException e) {
            System.out.println("Error al abrir o leer el archivo");
        }
        return fileContent;
    }
}

