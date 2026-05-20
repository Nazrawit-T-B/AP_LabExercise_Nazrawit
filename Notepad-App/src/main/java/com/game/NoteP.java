package com.game;

import java.io.*;

public class NoteP {

    public String readFile(String filename){
        StringBuilder filecontent=new StringBuilder();
        try(  BufferedReader read=new BufferedReader(new FileReader(filename));){
            String line;
            while((line=read.readLine())!=null){
                filecontent.append(line).append("\n");
            }
        }catch(IOException e){
            System.out.println("Error reading file"+ e.getMessage());
        }

        return filecontent.toString();
    }

    public void writeFile(String filename, String text){
        File file = new File(filename);
        if (file.exists() && !file.canWrite()) {
            System.out.println("This is a Read only file");
            return;
        }
            try(BufferedWriter writer=new BufferedWriter(new FileWriter(filename));){
                writer.write(text);
            }catch(IOException e){
                System.out.println("Cannot open file: "+ e.getMessage());
            }



    }
    public void setAsReadOnly(File filename){
        filename.setReadOnly();
    }


}
