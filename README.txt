Integrantes:
	Javier Ramos
	Ignacio Cofré

Instrucciones:

En cada m�quina virtual se debe ingresar a la carpeta Redes-2 por consola, esta tendr�
el c�digo de la entrega:

	cd Redes-2/

Luego se de debe ejecutar make con el fin de verificar de se crearon todos los .class
necesarios para ejecutar la entrega:

	make

Despu�s de esto, para ejecutar la entrega como tal, se debe escribir por consola: 

	make Server_part


Luego, se le pedir� que ingrese:

	Y: si es que la maquina actual debe iniciar las llamadas.

	N: si no realiza las llamadas iniciales y queda esperando a que otra m�quina
	   realice las llamadas iniciales.

Se debe tener en cuenta que UNA SOLA maquina debe realizar las llamadas iniciales. Despu�s
se le pedir� presionar ENTER una ves de que se tenga todos los servidores iniciados en las
dem�s maquinas, para luego dar unos 8 segundos extras para presionar ENTER en las consolas
de las dem�s m�quinas. 

Consideraciones especiales:
	
	Se cambio en formato de requerimientos, el cual se indica a continuaci�n:

##########################################################################################

{"requerimientos": [

    {"id": 1,

    "cargo": "doctor",

    "pacientes": [{"1": "recetar metformina"},

        {"2": "recetar ibuprofeno"},

        {"3": "pedir tomografia"},

        {"4": "colocar cateter"}

      ]},

    {"id": 3,

      "cargo": "doctor",

      "pacientes": [{"2": "recetar paracetamol"}

      ]}

  ]
}

###########################################################################################

Comparaciones:

Respecto al n�mero de mensajes, se tiene que el m�todo secuencial env�a una menor cantidad
de mensajes en comparaci�n con m�todo de propagaci�n, dado que el m�todo secuencial cada
m�quina env�a un mensaje al coordinador y este se encarga de enviar los cambios a las dem�s
m�quinas.

Respecto a la latencia, se tiene que el primer m�todo presenta una menor latencia de los 
mensajes, dado las mismas razones que con respecto al n�mero de mensajes de cada m�todo.

Por �ltimo, con respecto a la consistencia, se tiene que el segundo m�todo de propagaci�n 
de mensajes es m�s consistente que el secuencial dado que, al propagar la informaci�n de 
los logs a las otras m�quinas, como lo hace el segundo m�todo, permiten que los cambios sean 
conocidos de manera m�s directa por las otras m�quinas. 

En esta entrega se realiz� el primer m�todo (Secuencial) para actualizar los logs,
dado que este m�todo presenta una menor complejidad respecto al segundo (propagaci�n),
adem�s, que para este caso de estudio, donde se tienen solo cuatro m�quinas comunic�ndose
entre s�, no se apreciar� una gran diferencia en los tiempos de ejecuci�n.

