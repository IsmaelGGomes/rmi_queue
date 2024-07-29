import java.rmi.registry.Registry;

public class Client {
	public static void main(String[] args) throws InterruptedException {
		class FIFO {
			//VARIAVEIS
			public int cabeca, cauda, qtd_elementos;
			public int fila[];
			//METODOS
			public FIFO(int tamanho) {
				this.fila = new int[tamanho];
				cauda = 0;
				cabeca = 0;
				qtd_elementos = 0;
			}
			//Funcao que indica fila cheia
			public boolean cheia() {
				if(this.qtd_elementos == this.fila.length) return true;
				else return false;
			}
			//Funcao que indica fila vazia
			public boolean vazia() {
				if(this.qtd_elementos == 0) return true;
				else return false;
			}
		}

		class ConsumerProducer {
			//VARIAVEIS
			FIFO fila;
			//METODOS
			//constructor
			public ConsumerProducer(FIFO fila) {
				this.fila = fila;
			}
			//produtor
			public void produce() {
				int elemento = 0;
				while(true) {
					synchronized(this) {
						while(this.fila.cheia()) {
							try {
								wait();
							} catch (InterruptedException e) {}
						}
						//Se houver espaco na fila
						try {
							RMIClientManager clientManager = new RMIClientManager();
							Registry reg = clientManager.getRegistry();
							Hello objEnqueue = (Hello) reg.lookup("Enqueue");
							objEnqueue.enqueue(elemento, this.fila.fila, this.fila.cauda, this.fila.qtd_elementos);
							System.out.println("Elemento " + elemento + " produzido");
							elemento++;
							notify();
						} catch (Exception e) {
							System.out.println("Exceção do Cliente RMI: " + e.getMessage());
							e.printStackTrace();
						}
					}
				}
			}
			//consumidor
			public void consume() {
				int elemento;
				while(true) {
					synchronized(this) {
						while(this.fila.vazia()) {
							try {
								wait();
							} catch (InterruptedException e) {}
						}
						try {
							RMIClientManager clientManager = new RMIClientManager();
							Registry reg = clientManager.getRegistry();
							Hello objDequeue = (Hello) reg.lookup("Dequeue");
							elemento = objDequeue.dequeue(this.fila.fila, this.fila.cabeca, this.fila.qtd_elementos);
							System.out.println("Elemento " + elemento + " consumido");
							notify();
						} catch (Exception e) {
							System.out.println("Exceção do Cliente RMI: " + e.getMessage());
							e.printStackTrace();
						}
					}
				}
			}
		}
		FIFO fila = new FIFO(3);
		ConsumerProducer programa = new ConsumerProducer(fila); /*fila é passado como buffer para novo Objeto ConsumerProducer*/
		Thread produtor = new Thread //cria um objeto Thread para o metodo produtor
		(new Runnable() {
			public void run() {
				programa.produce();
			}
		});
		Thread consumidor = new Thread(new Runnable() {
			public void run() {
				programa.consume();
			}
		});
		//INICIA PROGRAMA
		produtor.start();
		consumidor.start();
		produtor.join();
		consumidor.join();
	}
}