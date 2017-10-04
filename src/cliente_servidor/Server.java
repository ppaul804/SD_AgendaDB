package cliente_servidor;

import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server {

    private List<PrintStream> clientes;

    private Server() {
        this.clientes = new ArrayList<>();
    }

    public void exe() {

        try {
            ServerSocket server = new ServerSocket(12345);
            System.out.println("Servidor iniciado na porta 12345");

            while (true) {
                
                //aceita cliente
                Socket cliente = server.accept();
                System.out.println("Cliente conectado do IP " + cliente.getInetAddress().
                        getHostAddress());
                //adiciona o que é recebido do cliente a lista
                PrintStream bytes = new PrintStream(cliente.getOutputStream());
                this.clientes.add(bytes);
                //cria uma thread onde será tratado o cliente
                TrataCliente tc = new TrataCliente(cliente.getInputStream(), this /*<-servidor*/);
                new Thread(tc).start();

            }//fim do while

        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//fim do main

    public void distribuiMensagem(String msg) {
        for (PrintStream cliente : this.clientes) {
            //Distribui as mensagens entre os clientes
            cliente.println(msg);
        }
    }

    public static void main(String[] args) {
        new Server().exe();
    }
}
