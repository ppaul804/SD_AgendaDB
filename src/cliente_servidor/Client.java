package cliente_servidor;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Scanner;
import model.bean.Agenda;
import model.dao.AgendaDAO;

public class Client {

    public void exe() {

        try {
            Socket cliente = new Socket("127.0.0.2", 12345);
            System.out.println(" Sou o cliente ");

            System.out.println("O cliente se conectou ao servidor!");

            Receptor r = new Receptor(cliente.getInputStream());
            new Thread(r).start();

            try (Scanner teclado = new Scanner(System.in);
                    PrintStream saida = new PrintStream(cliente.getOutputStream())) {

                // lê msgs do teclado e manda pro servidor
                boolean cond;//condicao usada no while
                String opcao;//opcao escolhida
                String nome;//nome recebido
                String telefone;//telefone recebido
                List<Agenda> listaContatos;
                Agenda contato = new Agenda();//objeto da agenda
                AgendaDAO agendaDAO = new AgendaDAO();//objeto para as funções
                mostraMenu();
                while (cond = teclado.hasNextLine()) {
                    opcao = teclado.nextLine();
                    
                    switch (opcao){
                        case "1"://Armazena/Atualiza um Registro
                            nome = lerNome(teclado);
                            telefone = lerTelefone(teclado);
                            contato.setNome(nome);
                            contato.setTelefone(telefone);
                            saida.println(opcao);//envia para o servidor
//                            saida.println(contato);
                            listaContatos = agendaDAO.recupera(contato);
                            boolean existe = verificaNomeLista(contato, listaContatos);
                            /*Caso um registro com o nome já esteja armazenado, 
                            o programa alerta o usuário solicitando permissão 
                            para atualizar o registro.*/
                            if (existe) {
                                System.out.printf("Deseja atualizar contato?\n> ");
                                opcao = teclado.nextLine();
                                if (opcao.equals("sim")||opcao.equals('s'))
                                    agendaDAO.atualiza(contato);
                            } else {
                                agendaDAO.insere(contato);
                            }
                            break;
                        case "2"://Remove um Registro
                            nome = lerNome(teclado);
                            contato.setNome(nome);//define o nome do objeto da classe Agenda
                            listaContatos = agendaDAO.recupera(contato);
                            if (!listaContatos.isEmpty()) {
                                agendaDAO.remove(contato);
                            } else {
                                System.out.println("Não encontrado");
                            }
                            break;
                        case "3"://Recupera um Registro
                            nome = lerNome(teclado);
                            contato.setNome(nome);
                            listaContatos = agendaDAO.recupera(contato);
                            if (!listaContatos.isEmpty()) {
                                mostraLista(listaContatos);
                            } else {
                                System.out.println("Não encontrado");
                            }
                            break;
                        case "4"://Finaliza a Aplicação
                            saida.close();
                            teclado.close();
                            cliente.close();
                            System.out.println("saindo...");
                            System.exit(0);//fecha o programa
                            break;
                        case "5":
                            listaContatos = agendaDAO.recuperaTodos(contato);
                            if (!listaContatos.isEmpty()) {
                                mostraLista(listaContatos);
                            } else {
                                System.out.println("Agenda vazia");
                            }
                            break;
                        default:
                            System.out.println("Escolha uma das opções do menu");
                            break;
                    }
                    mostraMenu();//menu
                }//fim while
            }

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

    private String lerNome(Scanner teclado) {
        System.out.printf("Digite um nome:\n> ");
        String leitura = teclado.nextLine();//le o nome
        return leitura;
    }

    private String lerTelefone(Scanner teclado) {
        System.out.printf("Digite um telefone:\n> ");
        String leitura = teclado.nextLine();//le o nome
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
