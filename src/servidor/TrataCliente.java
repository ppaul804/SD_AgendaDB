package servidor;

import java.io.BufferedReader;
import java.io.PrintStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.List;
import model.bean.Agenda;
import model.dao.AgendaDAO;

/**
 * Implementa as operações que tratam o cliente
 *
 * @author Pedro Paul
 */
class TrataCliente implements Runnable {
    /*  Atributos   */

    private Socket skt;

    private BufferedReader doClienteBF;
    private PrintStream paraCliente;

    private boolean inicializando;
    private boolean executando;

    private Thread thread;
    
    private String nome;//nome recebido
    private String telefone;//telefone recebido
    private List<Agenda> listaContatos;
    private Agenda contato = new Agenda();//objeto da agenda
    private AgendaDAO agendaDAO = new AgendaDAO();//objeto para as funções

    /**
     * Construtor que recebe o socket do cliente
     *
     * @param socket Ponto final de um fluxo de comunicação entre processos
     * através de uma rede de computadores
     * @throws Exception
     */
    public TrataCliente(Socket socket) throws Exception {
        this.skt = socket;

        this.inicializando = false;
        this.executando = false;

        open();
    }

    /**
     * Inicia o InputStream, OutputStream e seta o inicializado para verdadeiro
     *
     * @throws Exception
     */
    private void open() throws Exception {
        try {
            doClienteBF = new BufferedReader(new InputStreamReader(skt.getInputStream()));
            paraCliente = new PrintStream(skt.getOutputStream());
            inicializando = true;
        } catch (Exception e) {
            close();//libera os recursos
            throw e;
        }
    }

    /**
     * Libera os recursos alocados pelo TrataCliente
     */
    private void close() {
        if (doClienteBF != null) {
            try {
                doClienteBF.close();
            } catch (Exception e) {
                System.out.println(e);
            }
        }
        if (paraCliente != null) {
            try {
                paraCliente.close();
            } catch (Exception e) {
                System.out.println(e);
            }
        }
        try {
            skt.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        /*  Reiniciando atrubutos   */
        doClienteBF = null;
        paraCliente = null;
        skt = null;

        inicializando = false;
        executando = false;

        thread = null;
    }
    
    /**
     * Cria e inicia a thread auxiliar
     */
    public void start() {
        /*Se não foi inicializado ou estiver executando*/
        if (!inicializando || executando) {
            return;
        }

        executando = true;
        thread = new Thread(this);
        thread.start();
    }

    /**
     * Altera o estado de TrataCLiente para executando = falso,
     * assim encerrando a thread auxiliar
     * @throws Exception 
     */
    public void stop() throws Exception {
        executando = false;
        if (thread != null) {
            thread.join();
        }
    }
    
    /**
     * Envia a mensagem para o cliente. Usa o println.
     * @param mensagem 
     */
    public void send(String mensagem){
        paraCliente.println(mensagem);//envia para o cliente
    }
    
    private void mostraLista(List<Agenda> listaContatos, PrintStream paraCliente) {
        for (Agenda listaContato : listaContatos) {
            paraCliente.printf("Nome:%s\tTelefone:%s\n",listaContato.nome, listaContato.telefone);
        }//fim for
    }

    @Override
    public void run() {

        while (executando) {
            try {
                skt.setSoTimeout(600000);//1min

                String lidoDoCliente = doClienteBF.readLine();
                
                //imprime no console do servidor
                System.out.println(skt.getInetAddress().getHostAddress()
                        + ":"
                        + skt.getPort()
                        + "@Servidor: "
                        + lidoDoCliente);
                
                switch (lidoDoCliente) {
                    case "1"://Armazena/Atualiza um Registro
                        send("0");//envia para o cliente
                        break;
                    case "2"://Remove um Registro
                        send("00");//envia para o cliente
                        break;
                    case "3"://Recupera um Registro
                        send("000");//envia para o cliente
                        break;
                    case "4"://Finaliza a Aplicação
                        send(lidoDoCliente);//envia para o cliente
                        break;
                    case "5":
                        send("00000");
                        listaContatos = agendaDAO.recuperaTodos(contato);
                        if (!listaContatos.isEmpty()) {
                            mostraLista(listaContatos, paraCliente);
                        } else {
                            send("Agenda vazia");
                        }
                        break;
                    default:
                        send(lidoDoCliente + " [Default]");//envia para o cliente
                        break;
                }//fim do switch case
                
            } catch (Exception e) {
                System.out.println(e);
                break;
            }
        }//fim while
        System.out.println("Encerrando conexão.");
        close();
    }

}//fim classe TrataCliente
