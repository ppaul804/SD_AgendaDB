package cliente_servidor;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;
import model.bean.Agenda;
import model.dao.AgendaDAO;

public class Client {

    public void exe() {

        try {

            
            // lê msgs do teclado e manda pro servidor
            boolean cond;//condicao usada no while
            String opcao;//opcao escolhida
            String nome;//nome recebido
            String telefone;//telefone recebido
            List<Agenda> listaContatos;
            Agenda contato = new Agenda();//objeto da agenda
            AgendaDAO agendaDAO = new AgendaDAO();//objeto para as funções
            mostraMenu();
            boolean logout = false;
            while (true!=logout) {
                Socket skt = new Socket("127.0.0.2", 12345);

                //Define variavel para armazenar string
                BufferedReader tecladoBR = new BufferedReader(new InputStreamReader(System.in));
                // Cria um stream de saída 
                DataOutputStream paraServidor = new DataOutputStream(skt.getOutputStream());
                // e um de entrada, do socket
                BufferedReader doServidor = new BufferedReader(new InputStreamReader(skt.getInputStream()));
                opcao = tecladoBR.readLine();

                switch (opcao){
                    case "1"://Armazena/Atualiza um Registro
                        nome = lerNome(tecladoBR);
                        telefone = lerTelefone(tecladoBR);
                        contato.setNome(nome);
                        contato.setTelefone(telefone);
                        paraServidor.writeBytes(opcao+"\n");//envia a opcao para o servidor
                        // Recebe resposta do servidor
                        String socketStr = doServidor.readLine();
                        System.out.println("Cliente@ Resposta: " + socketStr);
                        //------------------------------------------
                        listaContatos = agendaDAO.recupera(contato);
                        boolean existe = verificaNomeLista(contato, listaContatos);
                        /*Caso um registro com o nome já esteja armazenado, 
                        o programa alerta o usuário solicitando permissão 
                        para atualizar o registro.*/
                        if (existe) {
                            System.out.printf("Deseja atualizar contato?\n> ");
                            opcao = tecladoBR.readLine();
                            if (opcao.equals("sim")||opcao.equals('s'))
                                agendaDAO.atualiza(contato);
                        } else {
                            agendaDAO.insere(contato);
                        }
                        break;
                    case "2"://Remove um Registro
                        nome = lerNome(tecladoBR);
                        contato.setNome(nome);//define o nome do objeto da classe Agenda
                        paraServidor.writeBytes(opcao+"\n");//envia a opcao para o servidor

                        //------------------------------------------
                        listaContatos = agendaDAO.recupera(contato);
                        if (!listaContatos.isEmpty()) {
                            agendaDAO.remove(contato);
                        } else {
                            System.out.println("Não encontrado");
                        }
                        break;
                    case "3"://Recupera um Registro
                        nome = lerNome(tecladoBR);
                        contato.setNome(nome);
                        paraServidor.writeBytes(opcao+"\n");//envia a opcao para o servidor
                        //------------------------------------------
                        listaContatos = agendaDAO.recupera(contato);
                        if (!listaContatos.isEmpty()) {
                            mostraLista(listaContatos);
                        } else {
                            System.out.println("Não encontrado");
                        }
                        break;
                    case "4"://Finaliza a Aplicação
                        paraServidor.close();
                        tecladoBR.close();
                        skt.close();
                        System.out.println("saindo...");
                        logout = true;
                        break;
                    case "5":
                        paraServidor.writeBytes(opcao+"\n");//envia a opcao para o servidor
                        //------------------------------------------
                        listaContatos = agendaDAO.recuperaTodos(contato);
                        if (!listaContatos.isEmpty()) {
                            mostraLista(listaContatos);
                        } else {
                            System.out.println("Agenda vazia");
                        }
                        break;
                    default:
                        paraServidor.writeBytes(opcao+"\n");//envia a opcao para o servidor
                        System.out.println("Escolha uma das opções do menu");
                        break;
                }
                mostraMenu();//menu
            }//fim while
        } catch (UnknownHostException e) {
        } catch (IOException e) {
        }
    }

    public static void main(String[] args) throws UnknownHostException, IOException {
        // dispara cliente
        new Client().exe();
    }

    private void mostraMenu() {
        System.out.println("\t\tMENU\n" + 
                "1 - Armazena/Atualiza um Registro;\n" +
                "2 - Remove um Registro;\n" +
                "3 - Recupera um Registro;\n" +
                "4 - Finaliza a Aplicação;");
        System.out.print("> ");
    }

    private String lerNome(BufferedReader teclado) throws IOException {
        System.out.printf("Digite um nome:\n> ");
        String leitura = teclado.readLine();//le o nome
        return leitura;
    }

    private String lerTelefone(BufferedReader teclado) throws IOException {
        System.out.printf("Digite um telefone:\n> ");
        String leitura = teclado.readLine();//le o nome
        return leitura;
    }

    private void mostraLista(List<Agenda> listaContatos) {
        for (Agenda listaContato : listaContatos) {
            System.out.printf("Nome:%s\tTelefone:%s\n",listaContato.nome, listaContato.telefone);
        }//fim for
    }

    private boolean verificaNomeLista(Agenda contato, List<Agenda> listaContatos) {
        boolean existe = false;
        for (Agenda contatoAmostra : listaContatos) {
            if (contatoAmostra.nome.equals(contato.nome))
                existe = true;
        }//fim for
        return existe;
    }

}//fim classe client
