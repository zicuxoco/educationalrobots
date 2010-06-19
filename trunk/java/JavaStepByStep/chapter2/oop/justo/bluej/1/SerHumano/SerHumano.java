
/**
 * Write a description of class SerHumano here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class SerHumano
{

    private String sexo  ="";
    private String raza = "";

    public SerHumano(){
        privateMessage();
    }
    
    public SerHumano(String _sexo, String _raza){
        this.sexo = _sexo;
        this.raza = _raza;
        privateMessage();
        profile();
    }

    
    private void privateMessage(){
        System.out.println("Ser Humano creado");
    }
    
    private void profile(){
        System.out.println("Sexo: " + this.sexo);
        System.out.println("Raza: " + this.raza);
    }
    
    public void setSexo(String _sexo){
        this.sexo = _sexo;
    }
    
    public void setRaza(String _raza){
        this.raza = _raza;
    }
    
    
}
