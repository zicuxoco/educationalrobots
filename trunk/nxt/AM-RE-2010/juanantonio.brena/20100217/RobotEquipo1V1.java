

/**
*
*
*/
public class RobotEquipo1V1{

	//Area de sensores

	//Area de actuadores
	private Motor motorLeft;
	private Motor motorRight;

	//Constantes
	private int blanco = 0;
	private int negro = 0;
	private int plata = 0;

	public RobotEquipo1V1(){

		//Instanciar los objetos
		motorLeft = Motor.A;
		motorRight = Motor.C;

		//Calibracion
		calibracion();

		while(true){
			steps();
		}
	}

	/*
forward
backward
stop
setPower
rotate
rotateTo
	*/	

	public void steps(){
		//si los dos sensores estan en blaco
			//avanzar
		//sino
			//si luzIzquierda es negro
				//??
			//sino
				//??
	}

	/**
	*
	*/
	public void calibracion(){
		
	}

}
