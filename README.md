# ControlZ - Controle de finanças pessoais.

## 💻 O projeto foi desenvolvido com:

- [x] Java 17
- [x] Spring Boot 2.5.2
- [x] Spring security
- [x] Token JWT
- [x] Sendinblue - Integração para envio de e-mail´s
- [x] Swagger - Após iniciar o projeto acesse: http://localhost:8080/swagger-ui/index.html
- [x] SQL para criação das tables e da base https://github.com/georgepiter/controlz/blob/main/docs/create_data_base_sql.sql
- [x] Banco de dados - MYSQL 8.0.30 - Modelagem do banco de dados no workbench: https://github.com/georgepiter/controlz/blob/main/docs/modelagem%20do%20projeto.mwb
- [x] Link para o front end: https://github.com/georgepiter/my-finances

## 💻 Pré-requisitos

Antes de começar, verifique se você atendeu aos seguintes requisitos:

* Você instalou a versão do Java`< JDK17 / requeridos>`
* Você instalou a versão`< MYSQL 8.0.30 / requeridos>`

## 🚀 Instalando

Após efetuar o clone do Back End adicione o POM do projeto ao Mavem e aguarde o dowmload das dependências.

Em seguida adicione as environments variables com suas configurações:

## URL e credenciais do Banco. 
- [x] DATABASE_URL
- [x] DATABASE_USERNAME
- [x] DATABASE_PASSWORD

##  Infos de Token.
- [x] JWT_SECRET - Chave secreta a sua escolha para gerar assinatura do token 
- [x] JWT_EXPIRATION - Tempo de expiração do token em milissegundos 

## SENDIBLUE - API de envio de e-mail personalizados.
- [x] SENDINBLUE_API_KEY - Chave gerada para integrar com a API de e-mail´s da sendiBlue.
- [x] SENDINBLUE_URL - URL de conexão para Integrar com a API do sendiBlue. 
- [x] É necessário cadastro para geração da API key e url, segue o link da documentação https://developers.sendinblue.com/reference/sendtransacemail

## Instruções para utilizar o template de e-mails.

O sistema possue dois templates:

Aviso de reset de senha usuário utilizador

<img src="https://user-images.githubusercontent.com/68233141/231508158-f8d2cf4b-65e0-4523-af3b-bb6b9737a23a.png" alt="Aviso de reset de senha usuário utilizador" width="500" />

Aviso de vencimento de débito

<img src="https://user-images.githubusercontent.com/68233141/231507544-89f15db0-0e75-4b03-bd96-eaf191ab7499.png" alt="Aviso de vencimento de débito" width="500" />

## O sistema possue 3 jobs:

- [x] Aviso de vencimento de débitos, que é executado todos os dias e avisa via e-mail os débitos que estão 2 dias antes de vencer.
- [x] Gera histórico do balanço mensal, todo último dia de cada mês o job cria um balanço de todo o mês.
- [x] Reenvio de e-mail, todo dia 01:00 é efetuado uma tentativa de reenvio dos e-mails com erro;

- Existe um webHook que a SENDIBLUE faz um post caso tenha um sucesso ao entregar o e-mail ou falhas.

## 📫 Contribuindo com o projeto:

Siga estas etapas:

1. Bifurque este repositório.
2. Crie um branch: `git checkout -b <nome_branch>`.
3. Faça suas alterações e confirme-as: `git commit -m '<mensagem_commit>'`
4. Envie para o branch original: `git push origin <nome_do_projeto> / <local>`
5. Crie a solicitação de pull.

Como alternativa, consulte a documentação do GitHub em [como criar uma solicitação pull](https://help.github.com/en/github/collaborating-with-issues-and-pull-requests/creating-a-pull-request).

## 😄 Seja um contribuidor<br>

Quer fazer parte desse projeto? Clique [AQUI](CONTRIBUTING.md) e leia como contribuir.

## 📝 Licença

Esse projeto está sob licença. Veja o arquivo [LICENÇA](LICENSE.md) para mais detalhes.

[⬆ Voltar ao topo](https://github.com/georgepiter/controlz)<br>
