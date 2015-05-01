/**
 * 
 */
package servidor;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.security.Security;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;


/**
 * Esta clase implementa el servidor que atiende a los clientes. El servidor 
 * esta implemntado como un pool de threads. Cada vez que un cliente crea
 * una conexion al servidor, un thread se encarga de atenderlo el tiempo que
 * dure la sesion. 
 * Infraestructura Computacional Universidad de los Andes. 
 * Las tildes han sido eliminadas por cuestiones de compatibilidad.
 * 
 * @author Michael Andres Carrillo Pinzon 	-  201320.
 * @author José Miguel Suárez Lopera 		-  201510
 */
public class Servidor extends Thread {

	/**
	 * Constante que especifica el tiempo máximo en milisegundos que se esperara 
	 * por la respuesta de un cliente en cada una de las partes de la comunicación
	 */
	private static final int TIME_OUT = 500;

	/**
	 * Constante que especifica el numero de threads que se usan en el pool de conexiones.
	 */
	public static final int N_THREADS = 14;

	/**
	 * Puerto en el cual escucha el servidor.
	 */
	public static final int PUERTO = 8080;

	/**
	 * El socket que permite recibir requerimientos por parte de clientes.
	 */
	private static ServerSocket socket;
	
	/**
	 * El socket del thread.
	 */
	private Socket s1;
	/**
	 * Pool de threads del servidor.
	 */
	private static ExecutorService pool;

	/**
	 * Metodo main del servidor con seguridad que inicializa un 
	 * pool de threads determinado por la constante nThreads.
	 * @param args Los argumentos del metodo main (vacios para este ejemplo).
	 * @throws IOException Si el socket no pudo ser creado.
	 */
	public static void main(String[] args) throws IOException{

		// Adiciona la libreria como un proveedor de seguridad.
		// Necesario para crear llaves.
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());		

		// Crea el socket que escucha en el puerto seleccionado.
		socket = new ServerSocket(PUERTO);
		pool = Executors.newFixedThreadPool(N_THREADS);
		Socket s = null;
		System.out.println("El servidor esta listo para aceptar conexiones.");
		while (true){
			try {
				s = socket.accept();
				s.setSoTimeout(TIME_OUT);
				pool.execute(new Servidor(s));
			}catch(IOException e){
				e.printStackTrace();
				continue;
			}
		}
	}

	/**
	 * Metodo que inicializa un thread y lo pone a correr.
	 * 
	 * @param socket
	 *            El socket por el cual llegan las conexiones.
	 */
	public Servidor(Socket s){
		s1 = s;
	}

	/**
	 * Metodo que atiende a los usuarios.
	 */
	@Override
	public void run() {
		// ////////////////////////////////////////////////////////////////////////
		// Recibe una conexion del socket.
		// ////////////////////////////////////////////////////////////////////////
		System.out.println("Thread recibe a un cliente.");
		Protocolo.atenderCliente(s1);
	}
}
