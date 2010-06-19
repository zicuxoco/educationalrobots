
/**
 * Write a description of class EjemploHerencia here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class EjemploHerencia
{
    static SerHumano sh;
    static SerHumano sh2;
    static Negro n1;
    
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        System.out.println("Hola mundo");
        sh = new SerHumano();
        sh2 = new SerHumano("Varon","Blanco");
        n1 = new Negro("Hembra");
    }
}
