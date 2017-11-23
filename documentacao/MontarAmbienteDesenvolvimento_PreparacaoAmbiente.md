# Montagem do ambiente de desenvolvimento
1. Preparação do ambiente
   * Configuração do sistema operacional
     * Instalação do Git
     * Instalação do Java JDK 7
   * Instalação do JBoss AS 7.1.1.Final
   * Instalação do Eclipse
     * Instalar o plugin Jbosstools
     * Configurar projeto no Eclipse

## Preparação do ambiente
Este documento tem o objetivo de oferecer informações referentes à documentação sobre a preparação do ambiente.

Aqui você pode obter mais informações sobre a configuração do Sistema Operacional e a instalação do JDK, do Eclipse e do Jboss.

### Configuração do sistema operacional
Utilizamos o linux como sistema operacional e adotamos o Ubuntu como distribuição preferida. 

#### Instalação do Git
Faz-se necessária a instalação do git para integração com o repositório, o que pode ser configurado com a utilização dos seguintes comandos:

> **Instalação do GIT:**
```
$ sudo apt-get install git
```

> **Definição do repositório**

> * Definir o diretório do sistema
```
$ sudo mkdir /opt/desenv/GOG
$ cd /opt/desenv/GOG
```

> * Clonar o git no diretório definido
>   * *Utilizar o texto "**HTTPS clone URL**" oferecido no repositório do projeto*
```
$ git clone https://github.com/culturagovbr/GOG.git
```

#### Instalação do Java JDK 7

- Incluir repositório, atualizar o apt-get e instalar o Java7

```
$ sudo add-apt-repository ppa:webupd8team/java -y
$ sudo apt-get update
$ sudo apt-get install oracle-java7-installer
```

> *Note*
>> Ainda estamos utilizando o JDK 7 porque, apesar do Java já estar na versão 8, o Jboss 7.1 ainda não está funcionando adequadamente nesta nova versão do Java


### Instalação do JBoss AS 7.1.1.Final

O Jboss pode ser instalado a partir do download do arquivo disponibilizado no [site do Jboss](http://jbossas.jboss.org/downloads)

> Utilizamos a versão **JBoss AS 7.1.1.Final**. 
> Basta realizar o download do arquivo e descompactá-lo em um diretório de sua preferência.

### Instalação do Eclipse

Instalar a versão desejada do eclipse. 
Sugerimos dar preferência à versão [**Eclipse Luna Package - Eclipse IDE for Java Developers**](http://www.eclipse.org/downloads/packages/eclipse-ide-java-developers/lunasr2) disponível para desenvolvedores Java JEE
> Basta realizar o download do arquivo e descompactá-lo em um diretório de sua preferência.

#### Instalar o plugin JbossTools

Instalar o plugin JbossTools (*JBoss Tools 4.2.3.Final*), para utilizar o servidor de aplicação na IDE, dentre outras facilidades. 
> Siga as instruções da [página de instalação do plugin](http://tools.jboss.org/downloads/jbosstools/luna/4.2.3.Final.html) para instalar o plugin JbossTools

#### Configuração do projeto no Eclipse
O projeto foi desenvolvido em Java, com uso do Maven. O arquivo "pom.xml" pode ser adequamente utilizado para importar o projeto em um IDE.

No Eclipse basta solicitar a importação de um novo projeto (Import ==> Import Existing Maven Projects) e apontar para a raiz do projeto.
