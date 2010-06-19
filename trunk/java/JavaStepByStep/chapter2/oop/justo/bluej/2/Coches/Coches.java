
/**
 * Write a description of class Coches here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Coches
{
    private String marca;
    private boolean maleteroDisponible;
    private double capacidadMaletero;
    private double autonomia;
    private boolean active;


    /**
     * Constructor for objects of class Coches
     */
    public Coches()
    {
        // initialise instance variables
 
    }
    
    public Coches(String _marca,boolean _maleteroDisponible,double _capacidadMaletero,double _autonomia)
    {
        // initialise instance variables
        marca = _marca;
        maleteroDisponible = _maleteroDisponible;
        capacidadMaletero = _capacidadMaletero;
        autonomia = _autonomia;
        active = true;
        
    }
    
    
   public double getAutonomia(){
       
       if(active){
                 return autonomia;
       }else{
           return 0;
        }
   
  
    }
   
    public void repostar(double gasolina){
        autonomia += gasolina;
    }
    
    public void setActive(boolean _active){
        active = _active;
    }
}
   