# Ambiente de Desenvolvimento

## Montagem do pacote para disponibilização (deploy)
O *"deploy"* da aplicação deve ser realizado com a geração de uma arquivo do tipo '*.war' (padrão de arquivo para aplicações web desenvolvidas em java).

O arquivo de *"deploy"* pode ser gerado de duas formas diferentes:

### Montagem do arquivo de deploy na IDE Eclipse
O deploy pode ser coordenado e gerado com o auxílio dos plugins do Eclipse.

> **Nota**

> Para montar o arquivo 'GOG.war' seguir os seguintes passos
>> 1. Selecionar a opção "Export" no projeto
>>    * Abrir o menu de contexto do projeto (clicar com o botão direito do mouse no projeto)
>>      * Selecionar a opção "Export"
>>    * OU, com o projeto selecionado, abrir a opção de menu ```<File>``` e em seguida selecionar ```<Export>```
>> 2. Informar opção para "Select an export destination"
>>    * Selecionar ```<Web>``` e em seguida ```<WAR file>```
>>    * Selecionar o botão ```"Next>"```
>> 3. Informar o destino onde o arquivo será gerado
>> 4. Selecionar a opção ```"Finish"```

### Montagem do arquivo de deploy com comando linux
O deploy pode ser gerado com comandos do maven, que podem ser executados diretamente no terminal.

> **Nota**
>
> Para montar o arquivo 'GOG.war' utilizando aplicativo mvn na linha de comando
>> * Instale o maven no seu Sistema Operacional
```console
$ > sudo apt-get install maven2
```
>> * Execute o comando do Maven *(mvn package)* no diretório do projeto, para gerar o arquivo de deploy
```console
$ > cd <diretorio raiz do projeto>
$ > mvn package
```
>> * O arquivo 'GOG.war' será gerado no diretório *```"<raiz do projeto>/<target/>"```*

### Realizando o deploy e utilizando a aplicação
O deploy do sistema pode ser realizado utilizando linha de comando.

Caso queira configurar o seu ambiente para executar o jboss como um servidor,  [siga as instruções da equipe do Jboss](https://developer.jboss.org/wiki/HowToInstallJBossAS7AsALinuxService)

Se o serviço do jboss já está iniciado, utilize os seguintes comandos para realizar o deploy:
```console
   $ sudo -i
   $ cd /opt/jboss/bin
   $ ./jboss-cli.sh 
   $ connect 10.0.0.225:9999
   $ deploy --force /opt/tmp/gog/target/GOG.war
```
> **Nota**
>> O deploy e o teste da aplicação podem ser realizados na IDE do Eclipse também, configurando um **Server**. Com estas configurações é possível executar a aplicação em modo *DEBUG*.

>> Após a realização do deploy, com sucesso, deve ser possível acessar a aplicação utilizando em um *Browser* o link

>> * http://localhost:8080/GOG
