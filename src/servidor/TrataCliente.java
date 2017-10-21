package servidor;

import java.io.BufferedReader;
import java.io.IOException;
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
     * Altera o estado de TrataCLiente para executando = falso, assim encerrando
     * a thread auxiliar
     *
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
     *
     * @param mensagem
     */
    public void send(String mensagem) {
        paraCliente.println(mensagem);//envia para o cliente
    }

    /**
     * Mostra o menu no Cliente
     */
    private void mostraMenu() {
        paraCliente.println("\t\tMENU\n"
                + "Escolha uma das opções:\n"
                + "1 - Armazena/Atualiza um Registro;\n"
                + "2 - Remove um Registro;\n"
                + "3 - Recupera um Registro;\n"
                + "4 - Finaliza a Aplicação;");
    }

    private void mostraLista(List<Agenda> listaContatos, PrintStream paraCliente) {
        for (Agenda listaContato : listaContatos) {
            paraCliente.printf("Nome:%s\tTelefone:%s\n", listaContato.nome, listaContato.telefone);
        }//fim for
    }

    private String lerNome(BufferedReader teclado) throws IOException {
        paraCliente.printf("Digite um nome:\n");
        String leitura = teclado.readLine();//le o nome
        return leitura;
    }

    private String lerTelefone(BufferedReader teclado) throws IOException {
        paraCliente.printf("Digite um telefone:\n");
        String leitura = teclado.readLine();//le o nome
        return leitura;
    }

    private boolean verificaNomeLista(Agenda contato, List<Agenda> listaContatos) {
        boolean existe = false;
        for (Agenda contatoAmostra : listaContatos) {
            if (contatoAmostra.nome.equals(contato.nome)) {
                existe = true;
            }
        }//fim for
        return existe;
    }

    @Override
    public void run() {

        while (executando) {
            mostraMenu();
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
                        nome = lerNome(doClienteBF);
                        telefone = lerTelefone(doClienteBF);
                        contato.setNome(nome);
                        contato.setTelefone(telefone);
                        listaContatos = agendaDAO.recupera(contato);
                        boolean existe = verificaNomeLista(contato, listaContatos);
                        /*Caso um registro com o nome já esteja armazenado, 
                         o programa alerta o usuário solicitando permissão 
                         para atualizar o registro.*/
                        if (existe) {
                            paraCliente.printf("Deseja atualizar contato? Sim (s) ou não (n)?\n");
                            String opcao = doClienteBF.readLine();
                            if (opcao.equals("s")) {
                                agendaDAO.atualiza(contato);
                                send("atualizado!");
                            }
                        } else {
                            agendaDAO.insere(contato);
                            send("inserido!");
                        }
                        break;
                    case "2"://Remove um Registro
                        nome = lerNome(doClienteBF);
                        contato.setNome(nome);
                        listaContatos = agendaDAO.recupera(contato);
                        if (!listaContatos.isEmpty()) {
                            agendaDAO.remove(contato);
                            send("excluido!");
                        } else {
                            send("Não encontrado");
                        }
                        break;
                    case "3"://Recupera um Registro
                        nome = lerNome(doClienteBF);
                        contato.setNome(nome);
                        listaContatos = agendaDAO.recupera(contato);
                        if (!listaContatos.isEmpty()) {
                            mostraLista(listaContatos, paraCliente);
                        } else {
                            send("Não encontrado");
                        }
                        break;
                    case "4"://Finaliza a Aplicação
                        executando = false;
                        break;
                    case "5":
                        listaContatos = agendaDAO.recuperaTodos(contato);
                        if (!listaContatos.isEmpty()) {
                            mostraLista(listaContatos, paraCliente);
                        } else {
                            send("Agenda vazia");
                        }
                        break;
                    default:
                        send("opção inválida!");//envia para o cliente
                        break;
                }//fim do switch case

            } catch (Exception e) {
                System.out.println(e);
                break;
            }
        }//fim while
        System.out.println("Servidor encerrando conexão.");
        close();
    }

}//fim classe TrataCliente
