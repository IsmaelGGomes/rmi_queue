import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Hello extends Remote {

    //String sayHello() throws RemoteException;
    public void enqueue(
        int elemento, 
        int fila[],
        int cauda,
        int qtd_elementos
    ) throws RemoteException;
    public int dequeue(
        int fila[],
        int cabeca,
        int qtd_elementos
    ) throws RemoteException;
    
}
