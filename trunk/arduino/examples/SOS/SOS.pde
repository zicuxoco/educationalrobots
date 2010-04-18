int corto=200;          
int pausa=400;
int largo=400;
int espera=1000;
int n=0;
int led=13;
int zumb=10;

void setup (){
 pinMode(led,OUTPUT);
 pinMode(zumb,OUTPUT);
}

void S(){                    //definición del procedimiento que envía la letra S
 for(n=0;n<3;n++){               
  digitalWrite(led, HIGH); 
  digitalWrite(zumb, HIGH);        
  delay(corto);                    
  digitalWrite(led,LOW); 
  digitalWrite(zumb,LOW);          
  delay(corto);
 }

}

void O(){                   //definición del procedimiento que envía la letra O
 for(n=0;n<3;n++){
  digitalWrite(led, HIGH);
  digitalWrite(zumb, HIGH);
  delay(largo);
  digitalWrite(led,LOW);
  digitalWrite(zumb,LOW);
  delay(largo);
 }
}

void loop(){
 S();                        //Ejecución del procedimiento que envía la letra S
 delay(pausa);
 O();                        //Ejecución del procedimiento que envía la letra O
 delay(pausa);
 S();                        //Ejecución del procedimiento que envía la letra S
 delay(espera);
}
