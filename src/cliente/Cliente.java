package cliente;

import java.io.BufferedReader;
import java.io.PrintStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Scanner;

public class Cliente implements Runnable {

    /*  Atributos   */
    private Socket socket;

    private BufferedReader tecladoBR;
    private PrintStream paraServidor;

    private boolean inicializado;
    private boolean executando;

    private Thread thread;

    public Cliente(String endereco, int porta) throws Exception {
        inicializado = false;
        executando = false;

        open(endereco, porta);
    }

    private void open(String endereco, int porta) throws Exception {
        try {
            socket = new Socket(endereco, porta);

            tecladoBR = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            paraServidor = new PrintStream(socket.getOutputStream());

            inicializado = true;
        } catch (Exception e) {
            System.out.println(e);
            close();
            throw e;
        }
    }

    private void close() {
        if (tecladoBR != null) {
            try {
                tecladoBR.close();
            } catch (Exception e) {
                System.out.println(e);
            }
        }
        if (paraServidor != null) {
            try {
                paraServidor.close();
            } catch (Exception e) {
                System.out.println(e);
            }
        }
        if (socket != null) {
            try {
                socket.close();
            } catch (Exception e) {
                System.out.println(e);
            }
        }

        /*  Reinicializando atributos   */
        tecladoBR = null;
        paraServidor = null;

        socket = null;

        inicializado = false;
        executando = false;

        thread = null;
    }

    public void start() {
        if (!inicializado || executando) {
            return;
        }
        executando = true;
        thread = new Thread(this);
        thread.start();
    }

    public void stop() throws Exception {
        executando = false;

        if (thread != null) {
            thread.join();
        }

    }

    /**
     * Verifica se o cliente está executando
     *
     * @return Estado do cliente
     */
    public boolean isExecutando() {
        return executando;
    }

    /**
     * Envia a mensagem para o servidor
     *
     * @param mensagem
     */
    public void send(String mensagem) {
        paraServidor.println(mensagem);//envia para o servidor
    }

    @Override
    public void run() {
        while (executando) {
            try {
                socket.setSoTimeout(600000);//Definição de um timeout para a operação de leitura e a leitura da mensagem enviada pelo servidor.

                String mensagem = tecladoBR.readLine();

                //Quando a conexão do lado do servidor cai ou é encerrada, 
                //recebemos um null. Neste caso, finalizamos o laço e 
                //caminhamos para a finalização da thread.
                if (mensagem == null) {
                    break;
                }

                //Apresentação da mensagem recebida do Servidor para o usuário.
                System.out.println(mensagem);

            } catch (SocketTimeoutException e) {
                //ignorar
            } catch (Exception e) {
                System.out.println(e);
                break;
            }
        }
        close();
    }

    public static void main(String[] args) throws Exception {
        System.out.println("Iniciando Cliente.");
        System.out.println("Iniciando conexão com servidor.");
        Cliente cliente = new Cliente("localhost", 12345);
        System.out.println("Conexão estabelecida.");

        cliente.start();

        Scanner scanner = new Scanner(System.in);
        boolean logout = false;

        while (!logout) {
            //O Servidor mostra o menu
            String mensagem = scanner.nextLine();

            if (!cliente.isExecutando()) {
                break;
            }

            cliente.send(mensagem);

            if (mensagem.equals("4")) {
                //sai do loop e encerra o cliente
                logout = true;
                break;
            }

        }//fim while
        System.out.println("Cliente encerrando conexão.");
        cliente.stop();
    }

}//fim classe client
