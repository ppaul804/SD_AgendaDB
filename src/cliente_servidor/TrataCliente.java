package cliente_servidor;

import java.io.InputStream;
import java.util.Scanner;

public class TrataCliente implements Runnable{
    private InputStream cliente;
    private Server servidor;

    public TrataCliente(InputStream cliente, Server servidor){
        this.cliente = cliente;
        this.servidor = servidor;
    }

    public void run() {
        
        Scanner entrada = new Scanner(this.cliente);
        while(entrada.hasNextLine()) {
            
            String opcao = entrada.nextLine();
            
            switch (opcao){
                case "1":
                    System.out.println("TrataCliente"+cliente);
                    break;
                case "2":
                    System.out.println("TrataCliente"+cliente);
                    break;
                case "3":
                    System.out.println("TrataCliente"+cliente);
                    break;
            }
            
//            servidor.distribuiMensagem(entrada.nextLine());
        }
        entrada.close();
    }
}
