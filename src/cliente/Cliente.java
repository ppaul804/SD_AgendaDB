package cliente;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

public class Cliente {

    public static void main(String[] args) throws Exception {
        System.out.println("Inciando Cliente.");
        System.out.println("Inciando Conexão com Servidor.");
        Socket socket = new Socket("localhost", 12345);
        System.out.println("Conexão estabelecida.");
        InputStream input = socket.getInputStream();
        OutputStream output = socket.getOutputStream();
        BufferedReader tecladoBR = new BufferedReader(new InputStreamReader(input));
        PrintStream paraServidor = new PrintStream(output);
        Scanner scanner = new Scanner(System.in);
        boolean logout = false;
        while (!logout) {
            //Qual vai ser a opção escolhida?
            mostraMenu();
            String mensagem = scanner.nextLine();

            paraServidor.println(mensagem);//envia para o servidor
            if (mensagem.equals("4")) {
                logout = true;
                break;
            }

            mensagem = tecladoBR.readLine();//recebe do servidor
            System.out.println("Servidor diz: " + mensagem);

        }//fim while
        System.out.println("Encerrando conexão.");
        tecladoBR.close();//BufferedReader
        paraServidor.close();//PrintStream
        socket.close();

    }

    private static void mostraMenu() {
        System.out.println("\t\tMENU\n"
                + "1 - Armazena/Atualiza um Registro;\n"
                + "2 - Remove um Registro;\n"
                + "3 - Recupera um Registro;\n"
                + "4 - Finaliza a Aplicação;");
        System.out.print("> ");
    }

}//fim classe client
