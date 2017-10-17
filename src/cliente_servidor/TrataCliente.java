package cliente_servidor;

import java.io.BufferedReader;
import java.io.IOException;

public class TrataCliente implements Runnable{
    private BufferedReader cliente;
    private Server servidor;

    public TrataCliente(BufferedReader cliente, Server servidor){
        this.cliente = cliente;
        this.servidor = servidor;
    }

    public void run() {
        try {
            BufferedReader entradaBF = new BufferedReader(cliente);
            while(entradaBF.ready()) {
                
                String opcao = entradaBF.readLine();
                
                
            servidor.distribuiMensagem(entradaBF);
            }
            entradaBF.close();
        } catch (IOException ex) {
        }
    }//fim método run
}//fim classe TrataCliente
