import lejos.nxt.Button;
import lejos.nxt.Sound;
 
public class BusquedaActiva {
       public static void main(String [] args){
             Robot9 robot = new Robot9();
 
             int estado = 0;
             boolean sentido = false;
             int contador = 0;
             int limite = 5;
             int angulo = 15;
 
             while(!Button.ESCAPE.isPressed()){
                 //Posicion neutral
                 if(estado == 0){
                     Sound.beep();
                     robot.stop();
                     estado = 1;
                //Girar a la izquierda
                 }else if(estado == 1){
                     robot.rotate(angulo);
                     contador++;
 
                     if(contador >= limite){
                         estado = 2;
                         contador = 0;
                     }
                //Girar a la derecha
                 }else if(estado == 2){
                     robot.rotate(-angulo);
                     contador++;
 
                     if(contador >= limite){
                         estado = 3;
                         contador = 0;
                     }
 
                //Girar a la derecha
                 }else if(estado == 3){
                     robot.rotate(-angulo);
                     contador++;
 
                     if(contador >= limite){
                         estado = 4;
                         contador = 0;
                     }
 
                 //Girar a la izquierda
                 }else if(estado == 4){
                         robot.rotate(angulo);
                         contador++;
 
                     if(contador >= limite){
                         estado = 0;
                         contador = 0;
                     }
                 }
                 //Posicion neutral
                 if(estado == 0){
                     robot.stop();
                     estado = 1;
                 }
             }
       }
}
