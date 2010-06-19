
/**
 * Write a description of class Maquina_nueva here.
 * 
 * @author (Justo Araque) 
 * @version (a version number or a date)
 */
public class Maquina_nueva
{
    //Precio de los tickets de la maquina
    private int price;
    //Balance del dinero ingresado
    private int balance;
    //Precio total dentro de la maquina
    private int total;

    /**
     * Constructor for objects of class Maquina_nueva
     */
    public Maquina_nueva(int ticketCost)
    {
        price = ticketCost;
        balance = 0;
        total = 0;
    }
    /** Precio del tike */
    
       public int getPrice()
    {
         return price;
    }
    /** */
    public int getBalance()
    {
        return balance;
    }
    
    /** */
    public void insertMoney(int amount)
    {
        if(amount >0){
            balance = balance + amount;
        }
        else{
            System.out.println("use a positive amount: " + 
                                amount);
                            }
                        }
                        
   /** Impresion del Tike */
   public void printTicket()
   {
       if(balance >= price) {
           // Simulacion de la impresion del ticke.
           System.out.println("*******************");
           System.out.println("*El Ave");
           System.out.println("* Ticket");
           System.out.println("* " + price + " cents.");
           System.out.println("*******************");
           System.out.println();
           
           // precio total.
           total = total + price;
           //Reduce el precio
           balance = balance - price;
        }
        else{
            System.out.println("Debe insertar mas dinero: " +
            (price - balance) + " euros mas.");
            
        }
    }
    
    /** */
    
    public int refundBalance()
    {
        int amountToRefund;
        amountToRefund = balance;
        balance = 0;
        return amountToRefund;
        
    }
}

    
