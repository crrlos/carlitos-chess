package com.wolf.carlitos;

import com.wolf.carlitos.Piezas.Alfil;
import com.wolf.carlitos.Piezas.Caballo;
import com.wolf.carlitos.Piezas.Dama;
import com.wolf.carlitos.Piezas.Peon;
import com.wolf.carlitos.Piezas.Pieza;
import com.wolf.carlitos.Piezas.Rey;
import com.wolf.carlitos.Piezas.Torre;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;



public class Juego {
    public  Pieza[][] tablero;
    public  EstadoTablero estadoTablero;
   
   
    public Pieza[] piezasBlancas;
    public Pieza[] piezasNegras;
    public int[] posicionReyNegro;
    public int[] posicionReyBlanco;

    
   public Juego(){
       this.piezasBlancas = new Pieza[16];
       this.piezasNegras = new Pieza[16];
        estadoTablero = new  EstadoTablero();
        tablero = new Pieza[8][8];
        
        tablero[0][0] = piezasBlancas[0] = new Torre(true);
        tablero[0][7] = piezasBlancas[1] = new Torre(true);
        tablero[7][0] = new Torre(false);
        tablero[7][7] = new Torre(false);   
        
        tablero[0][1] = piezasBlancas[2] = new Caballo(true);
        tablero[0][6] = piezasBlancas[3] = new Caballo(true);
        tablero[7][1] = new Caballo(false);
        tablero[7][6] = new Caballo(false);   

        tablero[0][2] = piezasBlancas[4] = new Alfil(true);
        tablero[0][5] = piezasBlancas[5] = new Alfil(true);
        tablero[7][2] = new Alfil(false);
        tablero[7][5] = new Alfil(false);   

        tablero[0][3] = piezasBlancas[6] = new Dama(true);
        tablero[7][3] = new Dama(false);   

        tablero[0][4] = new Rey(true);
        tablero[7][4] = new Rey(false);   

        for(int i = 0 ; i < 8; i++){
            tablero[1][i] = new Peon(true);
            tablero[6][i] = new Peon(false);
        }
    
    }
     public void ImprimirPosicicion(){
        for (int i = 7; i >=0 ; i--) {
            for (int j = 0; j < 8 ; j++) {
                var pieza = tablero[i][j];
                System.out.print(pieza != null ? pieza.Nombre() : " ");
                System.out.print("|");
            }
            System.out.println();
        }
        System.out.println("");
    }
    public void EstablecerPosicion(String... movimientos){
        for (var movimiento : movimientos) {
            
            EstadoTablero.ultimoMov.add(movimiento);
            
            ActualizarTablero(movimiento);
            estadoTablero.TurnoBlanco  = !estadoTablero.TurnoBlanco;
        }
    }
   
   
    public int Mini(int nivel,EstadoTablero estado) throws CloneNotSupportedException, IOException{
        
        
        
        if(nivel == 0) return Evaluar();
        
        int eval = 1000;
        var movimientos = Generador.generarMovimientos(tablero, estado);
       
       
            
        
        if(nivel == 1)
            comparar(movimientos, EstadoTablero.ultimoMov);
        
        estadoTablero = estado.clone();
        
        for(var mov : movimientos){
            //debug
            //EstadoTablero.ultimoMov.add(ConvertirANotacion(mov));
            
            ActualizarTablero(mov);
            //ImprimirPosicicion();
            estadoTablero.TurnoBlanco = !estado.TurnoBlanco;
            int evaluacion = Maxi(nivel -1,estadoTablero.clone());
            if(evaluacion < eval)
                eval = evaluacion;
            
            RevertirMovimiento(mov[0],mov[1],mov[2],mov[3],estado.TurnoBlanco,estadoTablero);
            estadoTablero = (EstadoTablero) estado.clone();
           //ƒmover  EstadoTablero.ultimoMov.remove(EstadoTablero.ultimoMov.size() -1);
        }
       
        return eval;
    }
    public int Maxi(int nivel, EstadoTablero estado) throws CloneNotSupportedException, IOException{
        
        if(nivel == 0) return Evaluar();
        
        int eval = -1000;
        
        
        var movimientos = Generador.generarMovimientos(tablero, estado);
        
        
        
//        if(nivel == 1)
//            comparar(movimientos, EstadoTablero.ultimoMov);
        
       
        
        estadoTablero = estado.clone();
        
        for(var mov : movimientos){
            //debug
           // EstadoTablero.ultimoMov.add(ConvertirANotacion(mov));
            
            ActualizarTablero(mov);
            //ImprimirPosicicion();
            estadoTablero.TurnoBlanco = !estado.TurnoBlanco;
            int evaluacion = Mini(nivel -1,estadoTablero.clone());
            
            if(evaluacion > eval)
                eval = evaluacion;
            
            RevertirMovimiento(mov[0],mov[1],mov[2],mov[3],estado.TurnoBlanco,estadoTablero);
            estadoTablero = (EstadoTablero) estado.clone();
            
            //EstadoTablero.ultimoMov.remove(EstadoTablero.ultimoMov.size() -1);
        }
        
        //System.out.println("maxi nivel " + nivel + " retorna  "+ eval);
        return eval;
    }

    public int Evaluar(){
        EstadoTablero.contador++;
        EstadoTablero.contadorPorMovimiento++;
        return 0;
//        int total = 0;
//        int totalPiezasBlancas  = 0;
//        int totalPiezasNegras  = 0;
//        for(var fila : tablero)
//            for(var pieza : fila)
//                if(pieza != null) 
//                    if(pieza.EsBlanca())  totalPiezasBlancas += pieza.Valor() ;
//                    else 
//                        totalPiezasNegras += pieza.Valor();
//        //System.out.println("Evaluar ------------------------------------ " + total);
//        return totalPiezasBlancas + totalPiezasNegras;
    }
    
   private void comparar(List<int[]> movimientos, List<String> movs) throws IOException{  
    if(!EstadoTablero.DEBUG) return;
    
    var movimientosCopia = movimientos.stream()
            .map((int[] e) -> {
               if(e.length == 4){
                   return new int[]{e[0],e[1],e[2],e[3]};
               }
                 return new int[]{e[0],e[1],e[2],e[3],e[4]};
            
            }).collect(Collectors.toList());
    
    var movsCopia = movs.stream().map(e -> e).collect(Collectors.toList());
    
    Hilos.comparar(movimientosCopia, movsCopia,estadoTablero);
   }
    
   public String Mover() throws CloneNotSupportedException, IOException{
       
       var estadoOriginal = estadoTablero.clone();//copia original
       
       
       EstadoTablero.contador = 0;
       int valoracion = estadoOriginal.TurnoBlanco ? -1000 : 1000;
       int pos = 0;
       
       var movimientos = Generador.generarMovimientos(tablero, estadoOriginal);
       for (int i = 0; i < movimientos.size() ; i++) {
           
            var mov = movimientos.get(i);
            
            ActualizarTablero(mov);//modifica el original
            
            
            estadoTablero.TurnoBlanco = !estadoTablero.TurnoBlanco;
            
            var estadoLocal = (EstadoTablero) estadoTablero.clone();
            
            int eval = estadoOriginal.TurnoBlanco ? Mini(EstadoTablero.deep,estadoLocal) : Maxi(EstadoTablero.deep,estadoLocal);
            
            if(estadoOriginal.TurnoBlanco){
                if(eval > valoracion){
                   valoracion = eval;
                   pos = i;
                }
            }
            else{
                 if(eval < valoracion){
                   valoracion = eval;
                   pos = i;
                }
            }
           
           RevertirMovimiento(mov[0],mov[1],mov[2],mov[3],estadoOriginal.TurnoBlanco,estadoLocal);
           estadoTablero =  estadoOriginal.clone(); //vuelve a ser el original
           
           System.out.println(Utilidades.ConvertirANotacion(mov) + " " + EstadoTablero.contadorPorMovimiento);
           EstadoTablero.contadorPorMovimiento = 0;
       }
      
       System.out.println(EstadoTablero.contador);
       return Utilidades.ConvertirANotacion(movimientos.get(pos));
   }
    private void RevertirMovimiento(int fi, int ci, int fd, int cd,boolean turnoBlanco, EstadoTablero estado){
        
       
        
        
        switch(estado.TipoMovimiento){
            case 0: //mov
                tablero[fi][ci] = tablero[fd][cd];
                break;
            case 1: //al paso
                tablero[fi][cd] = new Peon(!turnoBlanco);
                tablero[fi][ci] = tablero[fd][cd];
                break;
            case 2: //promocion
                tablero[fi][ci] = new Peon(turnoBlanco);
                break;
            case 3: //enroque
                tablero[fi][ci] = tablero[fd][cd];
                tablero[fd][cd] = null;
                if(cd > ci) {
                    tablero[fi][7] = tablero[fi][cd -1];
                    tablero[fi][cd - 1] = null;
                }
                else {
                    tablero[fi][0] = tablero[fi][cd + 1];
                    tablero[fi][cd + 1] = null;
                }
                
                if(turnoBlanco){
                    estadoTablero.PosicionReyBlanco[0] = fi;
                    estadoTablero.PosicionReyBlanco[1] =ci;
                }else{
                    estadoTablero.PosicionReyNegro[0] =fi;
                    estadoTablero.PosicionReyNegro[1] = ci;
                }
                
                break;
            case 100:
                tablero[fi][ci] = tablero[fd][cd];
                if(turnoBlanco){
                    estadoTablero.PosicionReyBlanco[0] = fi;
                    estadoTablero.PosicionReyBlanco[1] =ci;
                }else{
                    estadoTablero.PosicionReyNegro[0] =fi;
                    estadoTablero.PosicionReyNegro[1] = ci;
                }
                
              
        }
       
        tablero[fd][cd] = estado.PiezaCapturada;
    }
    private void ActualizarTablero(String movimiento){
        
        int colInicio = "abcdefgh".indexOf(movimiento.substring(0,1));
        int filaInicio = "12345678".indexOf(movimiento.substring(1, 2));
        int colFinal =  "abcdefgh".indexOf(movimiento.substring(2, 3));
        int filaFinal = "12345678".indexOf(movimiento.substring(3, 4));
        
        estadoTablero.PiezaCapturada = null;
        estadoTablero.TipoMovimiento = -1;

        var pieza = tablero[filaInicio][colInicio];

         if(pieza instanceof Peon){

            if(Math.abs(filaInicio - filaFinal) == 2){
                estadoTablero.AlPaso = true;
                estadoTablero.PiezaALPaso = pieza;
                
                tablero[filaFinal][colFinal] = pieza;
                tablero[filaInicio][colInicio] = null;
                estadoTablero.TipoMovimiento = 0;
                return;
            }
            if(estadoTablero.AlPaso){
                if(colFinal > colInicio || colFinal < colInicio){
                    if(tablero[filaInicio][colFinal] == estadoTablero.PiezaALPaso){
                        tablero[filaInicio][colFinal] = null;
                        estadoTablero.TipoMovimiento = 1;
                    }
                }
                estadoTablero.AlPaso = false;
            }
            
            if(filaFinal == 7 || filaFinal == 0){
                switch(movimiento.charAt(4)){
                    case 'q':
                        pieza = new Dama(estadoTablero.TurnoBlanco);
                        break;
                     case 'r':
                        pieza = new Torre(estadoTablero.TurnoBlanco);
                        break;
                     case 'n':
                        pieza = new Caballo(estadoTablero.TurnoBlanco);
                        break;
                    case 'b':
                        pieza = new Alfil(estadoTablero.TurnoBlanco);
                        break;
                }
                estadoTablero.TipoMovimiento = 2;
            }
            
         }
         else
         if(pieza instanceof Rey){
            // en los enroques solo se mueven las torres por ser el movimiento especial
            if(Math.abs(colInicio - colFinal) == 2){
                if(pieza.EsBlanca()){
                    if(colFinal == 6){//enroque corto
                        tablero[0][5] = tablero[0][7];
                        tablero[0][7] = null;
                    }else {//enroque largo
                        tablero[0][3] = tablero[0][0];
                        tablero[0][0] = null;
                    }
                }else{
                    if(colFinal == 6){//enroque corto
                        tablero[7][5] = tablero[7][7];
                        tablero[7][7] = null;
                    }else {//enroque largo
                        tablero[7][3] = tablero[7][0];
                        tablero[7][0] = null;
                    }
                }
                estadoTablero.TipoMovimiento = 3;
            }else{
                estadoTablero.TipoMovimiento = 100;
            }
            if(pieza.EsBlanca()){
                estadoTablero.EnroqueCBlanco = estadoTablero.EnroqueLBlanco = false;
                estadoTablero.PosicionReyBlanco[0] = filaFinal;
                estadoTablero.PosicionReyBlanco[1] = colFinal;
            }
            else{
                estadoTablero.EnroqueCNegro  = estadoTablero.EnroqueLNegro = false;
                estadoTablero.PosicionReyNegro[0] = filaFinal;
                estadoTablero.PosicionReyNegro[1] = colFinal;
            }
            
            
         }
         else
         if(pieza instanceof Torre){
             if(colInicio == 7)
             {
                 if(pieza.EsBlanca())
                     estadoTablero.EnroqueCBlanco = false;
                 else
                     estadoTablero.EnroqueCNegro = false;
             }else if(colInicio == 0)
                 if(pieza.EsBlanca())
                     estadoTablero.EnroqueLBlanco = false;
                 else
                     estadoTablero.EnroqueLNegro = false;
         }
         
       if(tablero[filaFinal][colFinal] != null){
            estadoTablero.PiezaCapturada = tablero[filaFinal][colFinal];
            estadoTablero.capturas++;
       }
       
       
       tablero[filaFinal][colFinal] = pieza;
       tablero[filaInicio][colInicio] = null;
       
       estadoTablero.AlPaso = false;
       
       if(estadoTablero.TipoMovimiento == -1)
            estadoTablero.TipoMovimiento = 0;
       
       
      
    }
    private void ActualizarTablero(int[] movimiento){
        
        int filaInicio = movimiento[0];
        int colInicio = movimiento[1];
        int filaFinal = movimiento[2];
        int colFinal = movimiento[3];
        
        
        estadoTablero.PiezaCapturada = null;
        estadoTablero.TipoMovimiento = -1;

        var pieza = tablero[filaInicio][colInicio];

         if(pieza instanceof Peon){

            if(Math.abs(filaInicio - filaFinal) == 2){
                estadoTablero.AlPaso = true;
                estadoTablero.PiezaALPaso = pieza;
                
                tablero[filaFinal][colFinal] = pieza;
                tablero[filaInicio][colInicio] = null;
                estadoTablero.TipoMovimiento = 0;
                return;
            }
            if(estadoTablero.AlPaso){
                if(colFinal > colInicio || colFinal < colInicio){
                    if(tablero[filaInicio][colFinal] == estadoTablero.PiezaALPaso){
                        tablero[filaInicio][colFinal] = null;
                        estadoTablero.TipoMovimiento = 1;
                    }
                }
                estadoTablero.AlPaso = false;
            }
            
            if(filaFinal == 7 || filaFinal == 0){
                switch(movimiento[4]){
                    case 1:
                        pieza = new Dama(estadoTablero.TurnoBlanco);
                        break;
                     case 2:
                        pieza = new Torre(estadoTablero.TurnoBlanco);
                        break;
                     case 3:
                        pieza = new Caballo(estadoTablero.TurnoBlanco);
                        break;
                    case 4:
                        pieza = new Alfil(estadoTablero.TurnoBlanco);
                        break;
                }
                estadoTablero.TipoMovimiento = 2;
            }
            
         }
         else
         if(pieza instanceof Rey){
            // en los enroques solo se mueven las torres por ser el movimiento especial
            if(Math.abs(colInicio - colFinal) == 2){
                if(pieza.EsBlanca()){
                    if(colFinal == 6){//enroque corto
                        tablero[0][5] = tablero[0][7];
                        tablero[0][7] = null;
                    }else {//enroque largo
                        tablero[0][3] = tablero[0][0];
                        tablero[0][0] = null;
                    }
                }else{
                    if(colFinal == 6){//enroque corto
                        tablero[7][5] = tablero[7][7];
                        tablero[7][7] = null;
                    }else {//enroque largo
                        tablero[7][3] = tablero[7][0];
                        tablero[7][0] = null;
                    }
                }
                estadoTablero.TipoMovimiento = 3;
            }else{
                estadoTablero.TipoMovimiento = 100;
            }
            if(pieza.EsBlanca()){
                estadoTablero.EnroqueCBlanco = estadoTablero.EnroqueLBlanco = false;
                estadoTablero.PosicionReyBlanco[0] = filaFinal;
                estadoTablero.PosicionReyBlanco[1] = colFinal;
            }
            else{
                estadoTablero.EnroqueCNegro  = estadoTablero.EnroqueLNegro = false;
                estadoTablero.PosicionReyNegro[0] = filaFinal;
                estadoTablero.PosicionReyNegro[1] = colFinal;
            }
            
            
         }
         else
         if(pieza instanceof Torre){
             if(colInicio == 7)
             {
                 if(pieza.EsBlanca())
                     estadoTablero.EnroqueCBlanco = false;
                 else
                     estadoTablero.EnroqueCNegro = false;
             }else if(colInicio == 0)
                 if(pieza.EsBlanca())
                     estadoTablero.EnroqueLBlanco = false;
                 else
                     estadoTablero.EnroqueLNegro = false;
         }
         
       if(tablero[filaFinal][colFinal] != null){
            estadoTablero.PiezaCapturada = tablero[filaFinal][colFinal];
            estadoTablero.capturas++;
       }
       
       
       tablero[filaFinal][colFinal] = pieza;
       tablero[filaInicio][colInicio] = null;
       
       estadoTablero.AlPaso = false;
       
       if(estadoTablero.TipoMovimiento == -1)
            estadoTablero.TipoMovimiento = 0;
       
       
      
    }
    
   public void perft(int n) throws CloneNotSupportedException, IOException{
       var search = new Search(tablero, estadoTablero);
       search.perft(n);
       
   }
}