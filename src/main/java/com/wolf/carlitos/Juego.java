package com.wolf.carlitos;

import com.wolf.carlitos.Piezas.Alfil;
import com.wolf.carlitos.Piezas.Caballo;
import com.wolf.carlitos.Piezas.Dama;
import com.wolf.carlitos.Piezas.Peon;
import com.wolf.carlitos.Piezas.Pieza;
import com.wolf.carlitos.Piezas.Rey;
import com.wolf.carlitos.Piezas.Torre;
import java.util.ArrayList;
import java.util.Scanner;



public class Juego {
    public static Pieza[][] tablero;
    public static EstadoTablero estadoTablero;
    private static final String filas = "12345678";
    private static final String columnas = "abcdefgh";
   
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
    static public void ImprimirPosicicion(){
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
            ActualizarTablero(movimiento);
            Juego.estadoTablero.TurnoBlanco  = !Juego.estadoTablero.TurnoBlanco;
        }
    }
    public static  String ConvertirANotacion(int[] movimiento){
        var mov = columnas.charAt(movimiento[1]) + "" + filas.charAt(movimiento[0]) +
                  columnas.charAt(movimiento[3]) + "" + filas.charAt(movimiento[2]);
        if(movimiento.length == 5){
            switch(movimiento[4]){
                case 1: return mov + "q";
                case 2: return mov + "r";
                case 3: return mov + "n";
                case 4: return mov + "b";
            }
        }
        return mov;
    }
    public ArrayList<int[]> MovimientosValidos(){
        var movimientos = new ArrayList<int[]>();
        
        int i = 0, j = 0;
        
        for(var fila: tablero){
            for (var pieza : fila){
                
                if(pieza != null && pieza.EsBlanca() == Juego.estadoTablero.TurnoBlanco)
                    movimientos.addAll(pieza.ObtenerMovimientos(i,j));
                j++;
                //Juego.ImprimirPosicicion();
            }
            i++; j = 0;
        }
        return movimientos;
    }
    public int Mini(int nivel,EstadoTablero estado) throws CloneNotSupportedException{
        if(nivel == 0) return Evaluar();
        //System.out.println("Inicia MINI -----------------------------------------");
        int eval = 1000;
        var movimientos = MovimientosValidos();
        if(movimientos.isEmpty()){
           movimientos = MovimientosValidos();
        }
        
        Juego.estadoTablero = estado.clone();
        
        for(var mov : movimientos){
            ActualizarTablero(ConvertirANotacion(mov));
            //ImprimirPosicicion();
            estadoTablero.TurnoBlanco = !estado.TurnoBlanco;
            int evaluacion = Maxi(nivel -1,estadoTablero.clone());
            if(evaluacion < eval)
                eval = evaluacion;
            
            RevertirMovimiento(mov[0],mov[1],mov[2],mov[3],estado.TurnoBlanco,estadoTablero);
            Juego.estadoTablero = (EstadoTablero) estado.clone();
        }
        //System.out.println("mini nivel " + nivel + " retorna  "+ eval);
        return eval;
    }
    public int Maxi(int nivel, EstadoTablero estado) throws CloneNotSupportedException{
        if(nivel == 0) return Evaluar();
        //System.out.println("Inicia MAXI ----------------------------------------- nivel " + nivel);
        int eval = -1000;
        //Juego.ImprimirPosicicion();
        var movimientos = MovimientosValidos();
        if(movimientos.isEmpty()){
           movimientos = MovimientosValidos();
        }
        
        Juego.estadoTablero = estado.clone();
        
        for(var mov : movimientos){
            ActualizarTablero(ConvertirANotacion(mov));
            //ImprimirPosicicion();
            estadoTablero.TurnoBlanco = !estado.TurnoBlanco;
            int evaluacion = Mini(nivel -1,estadoTablero.clone());
            
            if(evaluacion > eval)
                eval = evaluacion;
            
            RevertirMovimiento(mov[0],mov[1],mov[2],mov[3],estado.TurnoBlanco,estadoTablero);
            Juego.estadoTablero = (EstadoTablero) estado.clone();
        }
        
        //System.out.println("maxi nivel " + nivel + " retorna  "+ eval);
        return eval;
    }

    public int Evaluar(){
        Juego.estadoTablero.contador++;
        int total = 0;
        int totalPiezasBlancas  = 0;
        int totalPiezasNegras  = 0;
        for(var fila : tablero)
            for(var pieza : fila)
                if(pieza != null) 
                    if(pieza.EsBlanca())  totalPiezasBlancas += pieza.Valor() ;
                    else 
                        totalPiezasNegras += pieza.Valor();
        //System.out.println("Evaluar ------------------------------------ " + total);
        return totalPiezasBlancas + totalPiezasNegras;
    }
    
   public String Mover(EstadoTablero estado) throws CloneNotSupportedException{
       Juego.estadoTablero.contador = 0;
       int valoracion = estado.TurnoBlanco ? -1000 : 1000;
       int pos = 0;
       
       var movimientos = MovimientosValidos();
       //ystem.out.println("mov validos" + movimientos.size());
       for (int i = 0; i < movimientos.size(); i++) {
           
            var mov = movimientos.get(i);
            
            ActualizarTablero(ConvertirANotacion(mov));
            Juego.estadoTablero.TurnoBlanco = !Juego.estadoTablero.TurnoBlanco;
            //ImprimirPosicicion();
            var estadoLocal = (EstadoTablero) Juego.estadoTablero.clone();
            
            int eval = estado.TurnoBlanco ? Mini(EstadoTablero.deep,estadoLocal) : Maxi(EstadoTablero.deep,estadoLocal);
            
            if(estado.TurnoBlanco){
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
           
           RevertirMovimiento(mov[0],mov[1],mov[2],mov[3],estado.TurnoBlanco,estadoLocal);
           Juego.estadoTablero = (EstadoTablero) estado.clone();
       }
       //System.out.println(valoracion);
       //System.out.println(Juego.estadoTablero.contador);
       System.out.println(Juego.estadoTablero.contador + " " + Juego.estadoTablero.capturas);
       return ConvertirANotacion(movimientos.get(pos));
   }
    private void RevertirMovimiento(int fi, int ci, int fd, int cd,boolean turnoBlanco, EstadoTablero estado){
        
        
        switch(estado.TipoMovimiento){
            case 0: //mov
                Juego.tablero[fi][ci] = Juego.tablero[fd][cd];
                break;
            case 1: //al paso
                Juego.tablero[fi][cd] = new Peon(!turnoBlanco);
                Juego.tablero[fi][ci] = Juego.tablero[fd][cd];
                break;
            case 2: //promocion
                Juego.tablero[fi][ci] = new Peon(turnoBlanco);
                break;
            case 3: //enroque
                Juego.tablero[fi][ci] = Juego.tablero[fd][cd];
                Juego.tablero[fd][cd] = null;
                if(cd > ci) {
                    Juego.tablero[fi][7] = Juego.tablero[fi][cd -1];
                    Juego.tablero[fi][cd - 1] = null;
                }
                else {
                    Juego.tablero[fi][0] = Juego.tablero[fi][cd + 1];
                    Juego.tablero[fi][cd + 1] = null;
                }
                
                
//                System.out.println("Reversion de enroque");
//                
//                ImprimirPosicicion();
//                System.out.println("Fin reversion");
//                ImprimirPosicicion();
//                System.out.println("Fin reversion");
                break;
                
              
        }
        Juego.tablero[fd][cd] = estado.PiezaCapturada;
    }
    private void ActualizarTablero(String movimiento){
        
        int colInicio = columnas.indexOf(movimiento.substring(0,1));
        int filaInicio = filas.indexOf(movimiento.substring(1, 2));
        int colFinal = columnas.indexOf(movimiento.substring(2, 3));
        int filaFinal = filas.indexOf(movimiento.substring(3, 4));
        Juego.estadoTablero.PiezaCapturada = null;
        Juego.estadoTablero.TipoMovimiento = -1;

        var pieza = tablero[filaInicio][colInicio];

         if(pieza instanceof Peon){

            if(Math.abs(filaInicio - filaFinal) == 2){
                Juego.estadoTablero.AlPaso = true;
                Juego.estadoTablero.PiezaALPaso = pieza;
                
                tablero[filaFinal][colFinal] = pieza;
                tablero[filaInicio][colInicio] = null;
                Juego.estadoTablero.TipoMovimiento = 0;
                return;
            }
            if(Juego.estadoTablero.AlPaso){
                if(colFinal > colInicio || colFinal < colInicio){
                    if(tablero[filaInicio][colFinal] == Juego.estadoTablero.PiezaALPaso){
                        tablero[filaInicio][colFinal] = null;
                        estadoTablero.TipoMovimiento = 1;
                    }
                }
                Juego.estadoTablero.AlPaso = false;
            }
            
            if(filaFinal == 7 || filaFinal == 0){
                switch(movimiento.charAt(4)){
                    case 'q':
                        pieza = new Dama(Juego.estadoTablero.TurnoBlanco);
                        break;
                     case 'r':
                        pieza = new Torre(Juego.estadoTablero.TurnoBlanco);
                        break;
                     case 'n':
                        pieza = new Caballo(Juego.estadoTablero.TurnoBlanco);
                        break;
                    case 'b':
                        pieza = new Alfil(Juego.estadoTablero.TurnoBlanco);
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
            }
            if(pieza.EsBlanca()){
                Juego.estadoTablero.EnroqueCBlanco = Juego.estadoTablero.EnroqueLBlanco = false;
                Juego.estadoTablero.PosicionReyBlanco[0] = filaFinal;
                Juego.estadoTablero.PosicionReyBlanco[1] = colFinal;
            }
            else{
                Juego.estadoTablero.EnroqueCNegro  = Juego.estadoTablero.EnroqueLNegro = false;
                Juego.estadoTablero.PosicionReyNegro[0] = filaFinal;
                Juego.estadoTablero.PosicionReyNegro[1] = colFinal;
            }
            
            
         }
         else
         if(pieza instanceof Torre){
             if(colInicio == 7)
             {
                 if(pieza.EsBlanca())
                     Juego.estadoTablero.EnroqueCBlanco = false;
                 else
                     Juego.estadoTablero.EnroqueCNegro = false;
             }else if(colInicio == 0)
                 if(pieza.EsBlanca())
                     Juego.estadoTablero.EnroqueLBlanco = false;
                 else
                     Juego.estadoTablero.EnroqueLNegro = false;
         }
         
       if(tablero[filaFinal][colFinal] != null){
            estadoTablero.PiezaCapturada = tablero[filaFinal][colFinal];
            Juego.estadoTablero.capturas++;
       }
       
       
       tablero[filaFinal][colFinal] = pieza;
       tablero[filaInicio][colInicio] = null;
       
       Juego.estadoTablero.AlPaso = false;
       
       if(estadoTablero.TipoMovimiento == -1)
            estadoTablero.TipoMovimiento = 0;
       
    }
}