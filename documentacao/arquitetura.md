#GOG – Gestão de Ouvidoria Governamental - MinC/CGTI

Este documento apresenta informações técnicas sobre a arquitetura utilizada na construção do sistema, um software desenvolvido sob a gestão da Coordenação Geral de Tecnologia da Informação do Ministério da Cultura – CGTI/MinC, para atender necessidades e requisitos definidos pela Ouvidoria desta Pasta.

##Tecnologias envolvidas
A aplicação foi desenvolvida para a plataforma JEE, utilizando as seguintes tecnologias:

Tecnologia | Utilização
---------- | -----------
Java | Linguagem de programação utilizada
JEE | Especificação ou padrão arquitetural utilizado 
Primefaces | JSF – implementação interface dinâmica 
JPA | API de persistência
PostgreSQL 9.3 | Banco de Dados - SGDB
Jboss 7.1.1 | (servidor de aplicação)
Maven | Automação de build e controle de dependências 
Eclipse | IDE – Desenvolvimento 

## Arquitetura
A imagem a seguir ilustra a arquitetura utilizada:
![Arquitetura simplificada](/documentacao/ArquiteturaGOG.jpg)


## Mais detalhes:

* A aplicação utiliza o Maven, para geração de pacotes de entregas (builds) e gerenciamento de dependências, o que facilita o controle das APIs utilizadas e a definição de suas versões.
* A camada de persitência foi desenvolvida com a API JPA
* O Servidor de Aplicação utilizado aqui no MinC é o Jboss 7.1.1 
* Utilizamos configuração de módulos do Jboss e a integração com Apache para publicação na intranet
* Quanto ao Servidor de Banco de Dados:
  * O sistema GOG está codificado e testado para utilizar o SGBD PostgreSQL 9.3, sendo esta versão com PostgreSQL  utilizada por outros órgãos para os quais a aplicação foi cedida.
  * O mapeamento objeto-relacional foi desenvolvido independentemente de tecnologia de SGDB
    1. utiliza queries no padrão SQL ANSI
    2. permite a utilização de “ddl-generation” de forma transparente e direta
    3. facilita a geração dos objetos de banco de dados para outras tecnologias de banco além do PostgreSQL
* A autenticação de usuários é realizada na própria aplicação. 
  * Os dados de usuário estão modelados na tabela TBUsuario. 
  * O campo senha está gravado na tabela TBUsuario com HASH MD5.
* IDE de desenvolvimento
  * O ambiente de desenvolvimento faz uso do Eclipse Luna - acrescido do JBoss Tools 4.2.3.Final

##Perguntas & Respostas

A seguir apresentamos algumas perguntas que foram encaminhadas a esta CGTI e as respectivas respostas oferecidas por nossos técnicos:

- Qual é o Sistema Operacional utilizado?

> Linux. Utilizamos o modelo de configuração web baseado na integração entre as tecnologias Linux – Apache – Jboss.

> No ambiente de desenvolvimento utilizamos a distribuição Linux Ubuntu.

> Já no ambiente de PRODUÇÃO utilizamos CentOS 6

- Se utiliza Jboss ou Tomcat e qual a versão? Qual a versão do Java?

> Jboss 7.1.1. A aplicação utiliza o maven (maven-compiler-plugin 3.1 e maven-war-plugin 2.4). Para compilar estamos utilizando o java 1.7 (JDK).

- Utiliza algum arquivo de data source para conexão com banco?

> Sim. Como a aplicação está sendo disponibilizada no Jboss 7.1.1, utilizamos o arquivo “\jboss-7.1.1\standalone\configuration\standalone.xml” para configurar o dataSource

- Alguma configuração do serviço de envio de e-mail para o servidor SMTP?

> O envio de e-mail é realizado pela classe “br.com.xti.ouvidoria.helper.EmailHelper”, que acessa a tabela de configuração do sistema “TbPreferenciaSistema”.

> Na tabela são armazenados parâmetros para o envio de e-mail, tais como:
>  * HostEmail
>  * EmailOuvidoria (remetente)
>  * NomeOuvidoria (remetente)
>  * UsuarioEmail
>  * SenhaEmail
>  * PortaEmail
>  * SslEmail

- É possível integrar a autenticação com LDAP, nos acessos internos?

> O sistema não utiliza LDAP para autenticação.

> A autenticação de usuários é realizada na própria aplicação. Os dados de usuário estão modelados na tabela TBUsuario. O campo senha está gravado na tabela TBUsuario com HASH MD5.
