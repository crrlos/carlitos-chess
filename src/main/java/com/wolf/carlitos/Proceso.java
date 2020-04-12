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
    
    private static Proceso proceso;
    private BufferedReader reader;
    private OutputStreamWriter writer;
    
    
    private Proceso(){
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
    
    public static Proceso getInstance(){
        if(proceso == null)
            proceso = new Proceso();
        
        return proceso;
    }
    
    public ArrayList<String> movimentosValidos(List<String> movs){
        
        var movimientosValidos =  new ArrayList<String>();
        
        var builder = new StringBuilder();
        
        movs.forEach(s -> {
        
            builder.append(s).append(" ");
            
        });
        
        try {
            writer.write("position startpos moves "+builder.toString()+"\ngo perft 1 \n isready\n");
            writer.flush();
            
            
            while(!reader.ready()){}
            Thread.sleep(1);
            while(reader.ready()){
                var p = reader.readLine().split(":")[0];
                
                if(p.matches("\\w+\\d+\\w+\\d+")){
                    movimientosValidos.add(p);
                }
                
            }
           
            
        } catch (IOException ex) {
            Logger.getLogger(Proceso.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) { 
            Logger.getLogger(Proceso.class.getName()).log(Level.SEVERE, null, ex);
        }
        return movimientosValidos;
        
    }
    
    public void test() throws IOException, InterruptedException{
           
            
          
               
              while(true){
                   writer.write("go perft 1 \n");
               writer.flush();
               var contador = 0;
               
            while(!reader.ready()){}
            Thread.sleep(1);
            while(reader.ready()){
                
                var p = reader.readLine();
                
               if(p.split(":")[0].matches("\\w+\\d+\\w+\\d+")){
                   contador++;
                }
                
            }
            
                System.out.println(contador);
           }
              }
    
    
    
    
}