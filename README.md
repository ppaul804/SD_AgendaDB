# SD_AgendaDB
Projeto de desenvolvimento de uma aplicação de agenda de contatos telefônicos.

## Descrição do Projeto
O projeto consiste no desenvolvimento de uma aplicação de agenda de contatos telefônicos, distribuída em três camadas. O **cliente** provê a interface com o usuário e facilidades de comunicação com o servidor da aplicação. O **servidor** recebe requisições de Clientes e as executa. Os dados, manipulados pelo servidor a pedido dos clientes, são armazenados em uma **base de dados**. A aplicação consiste em uma agenda eletrônica onde são armazenados nomes e números de telefones. Os registros da agenda são acessados pelo nome completo do contato, isto é, o nome é a chave de acesso. A aplicação deve apresentar ao usuário, pelo menos, as seguintes operações:

1. Armazena/Atualiza um Registro;
2. Remove um Registro;
3. Recupera um Registro; e,
4. Finaliza a Aplicação.

Na operação 1, o programa pede o nome e o número do telefone do contato. Caso um registro com o nome já esteja armazenado, o programa alerta o usuário solicitando permissão para atualizar o registro. Caso contrário, um novo registro é criado.

Nas operações 2 e 3, apenas o nome é solicitado. A opção 2 retorna um indicativo de sucesso, se o registro foi removido, ou fracasso, se o registro não foi encontrado. Na opção 3, o programa retorna o número do telefone ou um indicativo de fracasso, se o registro não foi encontrado.

O IP e Porta do Servidor para contato é passado como parâmetro para o Cliente no momento da inicialização. A operação 4 fecha eventuais conexões abertas e encerra o Cliente.

A base de dados deve ser armazenada usando-se DBM - DataBase Manager de sua escolha (MySQL e.g.). Ao receber uma requisição, via rede, o servidor deverá executar a operação correspondente no DBM e, ao final da execução de cada requisição, encaminha uma resposta ao Cliente.

As etapas para a realização deste projeto e que deverão ser demonstradas na avaliação, são:
- especificação de um protocolo de comunicação Cliente/Servidor que atenda às necessidades acima descritas;
- implementar uma biblioteca de funções que executem operações no DBM correspondentes às operações disponíveis no Cliente;
- implementar um servidor que receba e responda à requisições via protocolo especificado no primeiro item, utilizando a biblioteca desenvolvida no segundo item; e
- testar a versão distribuída em uma Rede LAN, usando mais de um cliente em um cenário no qual clientes podem concorrentemente invocar requisições no Servidor.

A comunicação Cliente/Servidor deverá se processar via Biblioteca de Sockets TCP e UDP em Java.