package com.wolf.carlitos;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Proceso{
    private BufferedReader reader;
    private OutputStreamWriter writer;
    
    
    public Proceso(){
        try {
            var process = Runtime.getRuntime().exec("/Users/carlos/Desktop/stockfish");
            reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            writer = new OutputStreamWriter(process.getOutputStream());
            
            while(!reader.ready()){}
            
            while(reader.ready()){
              reader.readLine();
            } 
            
        } catch (IOException ex) {
            Logger.getLogger(Proceso.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public ArrayList<String> movimentosValidos(List<String> movs){
        
        var movimientosValidos =  new ArrayList<String>();
        
        var builder = new StringBuilder();
        
        movs.forEach(s -> {
        
            builder.append(s).append(" ");
            
        });
        
        try {
            writer.write("position startpos moves "+builder.toString()+"\ngo perft 1 \n");
            writer.flush();
            
            
            while(!reader.ready()){}
            Thread.sleep(5);
            while(reader.ready()){
                var p = reader.readLine().split(":")[0];
                
                if(p.matches("\\w+\\d+\\w+\\d+")){
                    if(!movimientosValidos.contains(p))
                        movimientosValidos.add(p);
                }
                
            }
           
            
        } catch (IOException | InterruptedException ex) { 
            Logger.getLogger(Proceso.class.getName()).log(Level.SEVERE, null, ex);
        }
        return movimientosValidos;
        
    }
}