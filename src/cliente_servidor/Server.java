package cliente_servidor;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server {

    private List<DataOutputStream> clientes;

    private Server() {
        this.clientes = new ArrayList<>();
    }

    public void exe() {

        try {
            //Crie uma instancia da class SocketServer, especializada para servidores (listen embutido)
            ServerSocket server = new ServerSocket(12345);
            System.out.println("Servidor iniciado na porta 12345");

            while (true) {
                
                //aceita cliente
                Socket skt = server.accept();
                System.out.println("Cliente conectado do IP " + skt.getInetAddress().
                        getHostAddress());
                //Cria uma stream de entrada
                BufferedReader doClienteBF = new BufferedReader(new InputStreamReader(skt.getInputStream()));
                // e uma stream de saída
               	DataOutputStream paraCliente = new DataOutputStream(skt.getOutputStream());
                //Lê o a entrada do cliente
                String lidoDoCliente = doClienteBF.readLine();
                // Mostra no servidor o que o cliente mandou
                System.out.println("Cliente escreveu: " + lidoDoCliente);
                
                paraCliente.writeBytes(lidoDoCliente = "Confirmacao de Recepcao(" + lidoDoCliente + ")\n");
                //adiciona o que é recebido do cliente a lista
//                this.clientes.add(paraCliente);
                //cria uma thread onde será tratado o cliente
//                TrataCliente tc = new TrataCliente(doClienteBF, this /*<-servidor*/);
//                new Thread(tc).start();

            }//fim do while

        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//fim do main

    public void distribuiMensagem(BufferedReader doClienteBF) throws IOException {
        String lidoDoCliente = doClienteBF.readLine();
        for (DataOutputStream paraCliente : this.clientes) {
            //Distribui as mensagens entre os clientes
            paraCliente.writeBytes(lidoDoCliente = "Confirmacao de Recepcao(" + lidoDoCliente + ")\n");
        }
    }

    public static void main(String[] args) {
        new Server().exe();
    }
}
