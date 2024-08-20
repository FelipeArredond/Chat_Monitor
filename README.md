# Parcial 1 Sistemas Operativos

## Uso de hilos
Los hilos fueron implementados en cada uno de los procesos bloqueantes identificados en el sistema como lo pueden ser recibir mensajes del buffer enviados a traves de sockets para realizar el chat como se ve a continuacion.

![alt](/images/HilosListener.png)

Cada una de las instancias del objeto SocketListener, que tiene multiples proceso bloqueantes se ejecutan en hilos diferentes, dentro de esta propia instancia tambien segmentamos la funcionalidad a traves de hilos por medio de la clase ClientHandler que es el objeto encargado de monitorear todos los mensajes enviados a traves de la aplicacion, esta implementa la interfaz Runnable la cual define un metodo run que este es ejecutado en un nuevo hilo (debido a que aca se define un proceso bloqueante)

![alt](/images/ClientHandlerHilos.png)
![alt](/images/RunRunnable.png)

Para el cliente tambien se segmento el proceso de escucha y envio de mensajes a traves de sockets por hilos, debido a que ambos son procesos bloqueantes que no pueden ser ejecutados al mismo tiempo, por lo que uno de ellos se realiza en segundo plano en un hilo mientras que el segundo se ejecuta en el Main Thread del programa

![alt](/images/ClientThreads.png)

Como podemos ver el metodo de escucha de mensajes del socket (listenMessage) es un metodo que se ejecuta en un hilo aparte del hilo principal del programa

## Uso de semaforos

Para hacer uso de semaforos y el proceso de control de accesos a recursos de cada uno de los multiples hilos se desarrollo un sistema basico de logs que basicamente anexa cada uno de los mensajes a un archivo llamado logs.txt el cual recopilara absolutamente todos los chats manejados por el monitor o en este caso el SocketListener, pero estos mensajes son escritos por las diferentes instancias que se encuentran en ejecucion en los diferentes hilos
![alt](/images/Broadcast.png)
El metodo broadCastMessage es un metodo el cual se encarga de recibir todos los mensajes dentro de la "red" y enviarlos unicamente a los usuarios del chat correspondiente, pero al funcionar de esta manera multiples ClientHandlers que se ejecutan de esta manera realizan el mismo proceso, a su vez que escriben los logs dentro del recurso externo que en este caso es el archivo logs.txt. Para evitar problemas de race condition se implementaron los metodos `semaphore.acquire()` y `semaphore.release()`. El metodo `semaphore.acquire()` lo que sirve es aprovisionar el recurso al que sera accedido y modificado en caso tal de que este se encuentre libre, y si no hacer algo similar a lo que seria ponerse en lista de espera, posterior a este se procede a modificar el archivo logs.txt y luego se ejecuta el metodo `semaphore.release()` que libera ese recurso y da paso a un nuevo hilo para que este pueda modificarlo sin errores algunos, esta tecnica es utilizada para evitar problemas conocidos como el race condition.