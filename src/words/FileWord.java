/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package words;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Random;
import main.Hangman;

/**
 * @author Sebastian152
 */
public class FileWord {
    
    RandomAccessFile file;
    String filepath;
    public FileWord(String filepath) {
        try {
            this.filepath = filepath;
            this.file = new RandomAccessFile(new File(filepath), "rw");
        } 
        catch (FileNotFoundException ex) 
        {
            Hangman.showError(ex);
        }
    }
    
    private static boolean isDelimiter(byte b) {
        return b == ' ' || b == '\n' || b == '\r' || b == '\t';
    }
    
    /**
     * It will return a random word at the file
     * @return string at position
     */
    public String getWord() throws IOException {
        long fileLength = file.length();
        Random random = new Random();
        long randomIndex = (long) (random.nextDouble() * fileLength);
        file.seek(randomIndex);
        // Buscar el inicio de la línea
        while (file.getFilePointer() > 0 && file.readByte() != '\n') {
            file.seek(file.getFilePointer() - 2);
        }
        // Leer la línea completa
        StringBuilder lineBuilder = new StringBuilder();
        String line = file.readLine();
        while (line == null) {
            file.seek(0);
            line = file.readLine();
        }
        lineBuilder.append(line);
        return lineBuilder.toString();
    }
    
    public void closeFile() throws IOException {
        file.close();
    }
         
}
