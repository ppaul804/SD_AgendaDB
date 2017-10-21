package servidor;

import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class Servidor implements Runnable {

    /*  Atributos   */
    private ServerSocket server;//recebe conexões de clientes

    private List<TrataCliente> clientes;//lista de threads clientes do servidor
    private boolean inicializado;//servidor inicializado?
    private boolean executando;//servidor executando?

    private Thread thread;//controla e representa a thread auxiliar para recebimento de conexões do servidor

    /**
     * Construtor para inicializar o novo objeto Servidor
     *
     * @param porta ponto físico (hardware) ou lógico (software), no qual podem
     * ser feitas conexões
     * @throws Exception
     */
    public Servidor(int porta) throws Exception {
        clientes = new ArrayList<TrataCliente>();

        inicializado = false;
        executando = false;

        open(porta);
    }

    /**
     * Cria o objeto ServerSocket e marca o objeto Servidor como inicializado
     *
     * @param porta ponto físico (hardware) ou lógico (software), no qual podem
     * ser feitas conexões
     * @throws Exception
     */
    private void open(int porta) throws Exception {
        server = new ServerSocket(porta);
        inicializado = true;
    }

    /**
     * Libera os recursos alocados pelo objeto Servidor
     */
    private void close() {
        for (TrataCliente cliente : clientes) {//encerra cada um dos clientes
            try {
                cliente.stop();
            } catch (Exception e) {
                System.out.println(e);
            }
        }

        try {
            server.close();
        } catch (Exception e) {
            System.out.println(e);
        }

        /*  Reinicialização dos atrubutos   */
        server = null;

        inicializado = false;
        executando = false;

        thread = null;
    }

    /**
     * Inicia a execução da thread auxiliar do servidor
     */
    public void start() {
        if (!inicializado || executando) {
            return;
        }
        executando = true;
        thread = new Thread(this);//cria a thread passando o objeto que implementa a interface Runnable, assim executará o método run() do objeto
        thread.start();
    }

    /**
     * Altera do estado do servidor e aguarda a thread finalizar.
     *
     * @throws Exception
     */
    public void stop() throws Exception {
        executando = false;
        if (thread != null) {
            thread.join();//bloqueia a thread atual até que a thread auxiliar seja finalizada
        }
    }

    @Override
    public void run() {
        System.out.println("Aguardando conexão.");
        while (executando) {
            try {
                server.setSoTimeout(600000);//limite de 1min de bloqueio da thread para que de tempos e tempos ela possa reavaliar a condição do laço

                Socket skt = server.accept();

                System.out.println(
                        skt.getInetAddress().getHostAddress()
                        + ":"
                        + skt.getPort()
                        + "@Servidor: Conectado");

                TrataCliente trCliente = new TrataCliente(skt);
                //Objetos da classe TrataCliente seriam threads,
                //para que pudessem executar em separado,
                //liberando a thread auxiliar do servidor
                //para receber novas conexões
                trCliente.start();

                clientes.add(trCliente);

            } catch (SocketException e) {
                //ignora
            } catch (Exception e) {
                System.out.println(e);
                break;
            }
        }// fim while
        close();
    }

    public static void main(String[] args) throws Exception {
        System.out.println("Iniciando servidor.");
        Servidor servidor = new Servidor(12345);
        System.out.println("Servidor iniciado na porta 12345.");
        servidor.start();

        /*Aperta ENTER para encerrar o servidor (thread principal)*/
        new Scanner(System.in).nextLine();
        System.out.println("Encerrando servidor.");
        servidor.close();
    }

}// fim classe Servidor
