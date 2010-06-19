import java.util.*;

/**
 * Write a description of class Juego here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Juego
{
    public static Renault coche1;
    public static Opel coche2;
    public static Ford coche3;
    public static Viaje rally;

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        System.out.println("Juego Rally");
        
        //Instanciamos los coches del Rally
        coche1 = new Renault(true,300.0,300.0);
        coche2 = new Opel(true,700.0,500.0);
        coche3 = new Ford(false,0.0,400.0);
        //Instanciamos el viaje
        rally = new Viaje(100.0);
        
        for(int i = 0; i <= 50; i++){
               updateStatus();
               inferir(i);
        }

    }
    
    public static void inferir(int iteration){
        
        String resultado = "";
        
        //Inferimos para conocer el ganador si es posible
        double autonomia1 = coche1.getAutonomia();
        double autonomia2 = coche2.getAutonomia();
        double autonomia3 = coche3.getAutonomia();
        double distancia = rally.getDistancia();
        
        //Alguno de estos coches, va a llegar
        if(
            (autonomia1 >= distancia) || 
            (autonomia2 >= distancia) || 
            (autonomia3 >= distancia)){
            //Cual es el que tiene mas autonomia
            
            if(autonomia1 > autonomia2){
                if(autonomia1 > autonomia3){
                    //Coche1 es el que llega
                    resultado = "El coche1 Gana";
                }else{
                    //Coche3 es el que llega
                    resultado = "El coche3 Gana";
                }
            }else{
                if(autonomia2 > autonomia3){
                    //Coche2 es el que llega
                    resultado = "El coche2 Gana";
                }else{
                    //Coche3 es el que llega
                    resultado = "El coche3 Gana";
                }   
            }
        }else{
            //Ninguno llega a la meta
            resultado = "Ninguno de los coches gana";
        }
        
        System.out.println("[" + iteration + "] " + "Resultado:" + resultado);
    }
    
    private static void updateStatus(){
        Random generator = new Random();
        int accidente1 = generator.nextInt(2);
        //System.out.println("" + accidente1);
        int accidente2 = generator.nextInt(2);
        //System.out.println("" + accidente2);
        int accidente3 = generator.nextInt(2);
        //System.out.println(""+ accidente3);
        
        if(accidente1 == 1){
            coche1.setActive(false); 
            System.out.println("Accidente Coche 1");
        }else{
            coche1.setActive(true); 
            System.out.println("Reparación Coche 1");
        }
        
        if(accidente2 == 1){
            coche2.setActive(false); 
            System.out.println("Accidente Coche 2");
        }else{
            coche2.setActive(true); 
            System.out.println("Reparación Coche 2");            
        } 
        
        if(accidente3 == 1){
            coche3.setActive(false);  
            System.out.println("Accidente Coche 3");            
        }else{
            coche3.setActive(true); 
            System.out.println("Reparación Coche 3");
        }
    }
    
}
