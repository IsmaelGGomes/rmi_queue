import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class Server extends UnicastRemoteObject implements Hello {
	public Server() throws RemoteException {
		super();
	}
	public void enqueue(int elemento, int fila[], int cauda, int qtd_elementos) throws RemoteException {
		//SE HOUVER ESPACO NA FILA    
		fila[cauda] = elemento;
		if(cauda == fila.length - 1) cauda = 0;
		else cauda += 1;
		qtd_elementos++;
		//elemento inserido
		//System.out.println("Elemento Server =>>" + elemento);
		//System.out.println("CAUDA Server =>>" + cauda);
		System.out.println("Fila adicionada ! ");
		return;
	}
	//Funcao remocao da fila
	public int dequeue(int fila[], int cabeca, int qtd_elementos) throws RemoteException {
		int valor = fila[cabeca];
		if(cabeca == fila.length - 1) cabeca = 0;
		else cabeca += 1;
		qtd_elementos--;
		System.out.println("Fila removida");
		return valor;
	}
	public static void main(String[] args) {
		try {
			Server obj = new Server();
			LocateRegistry.createRegistry(1099);
			Registry reg = LocateRegistry.getRegistry();
			reg.rebind("Enqueue", obj);
			reg.rebind("Dequeue", obj);
			System.out.println("Servidor RMI ready!!!");
		} catch (RemoteException e) {
			System.out.println("HelloServer exception: " + e.getMessage());
			e.printStackTrace();
		}
	}
}