# Montagem do ambiente de desenvolvimento

## Configuração do Jboss 
> Este documento tem o objetivo de oferecer informações sobre a configuração do Jboss para o funcionamento do sistema GOG.
>> Esta documentação foi desenvolvida para realizar as configuração na versão **'jboss-7.1.1'**

> Arquivo "standalone.xml"
>> * A configuração do Jboss é feita basicamente em um único arquivo de propriedades, o 'standalone.xml', para ajustar a conexão com o banco de dados e para configurar os parâmetros de autenticação e segurança, conforme os passos que se seguem
>> * O arquivo 'standalone.xml' fica armazenado na pasta '.../jboss-7.1.1/standalone/configuration' *(adequar ao padrão utilizado na instalação de seu Jboss)*.

### Criar o datasource

A configuração do *datasource* deve ser realizada com a inclusão do texto seguinte no arquivo 
'.../jboss-7.1.1/standalone/configuration/standalone.xml'.


```xml
                <datasource jndi-name="java:/datasources/GOGDSPostreSQL" pool-name="GOGDSPostreSQL" enabled="true" use-java-context="true" use-ccm="true">
                    <connection-url>jdbc:postgresql://localhost:5432/GOG</connection-url>
                    <driver>org.postgresql</driver>
                    <transaction-isolation>TRANSACTION_READ_COMMITTED</transaction-isolation>
                    <pool>
                        <min-pool-size>5</min-pool-size>
                        <max-pool-size>50</max-pool-size>
                        <prefill>true</prefill>
                        <use-strict-min>false</use-strict-min>
                        <flush-strategy>FailingConnectionOnly</flush-strategy>
                    </pool>
                    <security>
                        <user-name>java</user-name>
                        <password>java</password>
                    </security>
                    <timeout>
                        <set-tx-query-timeout>true</set-tx-query-timeout>
                        <blocking-timeout-millis>5000</blocking-timeout-millis>
                        <idle-timeout-minutes>15</idle-timeout-minutes>
                    </timeout>
                    <statement>
                        <track-statements>false</track-statements>
                        <prepared-statement-cache-size>100</prepared-statement-cache-size>
                        <share-prepared-statements>true</share-prepared-statements>
                    </statement>
                </datasource>
```

> Notas:
>> O data source precisa definir o jndi-name="java:/datasources/GOGDSPostreSQL" e as configurações de conexão com o banco de dados.

>> O jndi-name deve ser o mesmo definido na tag *`<jta-data-source>`* do arquivo *persistence.xml*

### Configuração dos drivers de conexão 

Ainda no arquivo '.../jboss-7.1.1/standalone/configuration/standalone.xml', logo após a declaração do  *`<jta-data-source>`*, inclua a configuração dos drivers de conexão com o banco inserindo texto conforme o exemplo:

```
                <drivers>
                    <driver name="org.postgresql" module="org.postgresql">
                        <driver-class>org.postgresql.Driver</driver-class>
                    </driver>
                </drivers>
```
Observe que o driver referencia um módulo - 'module'. 

O módulo é uma referência ao pacote contendo os componentes de software para o funcionamento do driver.

O "module" será representado por um 'jar', contendo as classes para acesso ao banco.

>Note
>> O arquivo .jar do Postgresql pode ser obtido em https://jdbc.postgresql.org/download.html, acessando a versão que optar. 

>> A versão citada aqui pode ser obtida em https://jdbc.postgresql.org/download/postgresql-9.3-1102.jdbc3.jar




**Criação do módulo para conexão com Banco de Dados Postgres**

> *Nota*
>> Será necessário criar e configurar o "module" para o funcionamento do driver do Banco de Dados
>> * O driver para o Postgresql deve referenciar o módulo a ser carregado com o necessário arquivo '*.jar' de conexão.
>> * O módulo deve conter um arquivo de configuração 'module.xml' e o arquivo '*.jar' com os componentes necessários

- Crie uma estrutura de diretórios correspondente ao jar do driver do Postgres, a partir do diretório "modules" do Jboss. 
 - Exemplo: jboss-7.1.1/modules/org/postgresql/main
- Inclua o arquivo do driver do Postgres no diretório criado (o arquivo .jar do driver)
- Crie um arquivo "module.xml" com o seguinte conteúdo:

```xml
<?xml version="1.0" encoding="UTF-8"?>
  <module xmlns="urn:jboss:module:1.0" name="org.postgresql">
  <resources>
    <resource-root path="postgresql-9.3-1102.jdbc4.jar"/>
  </resources>
  <dependencies>
    <module name="javax.api"/>
    <module name="javax.transaction.api"/>
  </dependencies>
</module>
```

> *Importante*
>> O diretório criado para configuração do módulo devem ser composto de dois arquivos:
>> 1. O arquivo '*.jar' do driver de conexão
>> 2. O arquivo 'module.xml' de configuração do módulo


### Configuração complementar - domínio de segurança e autenticação

É necessário criar o **"security domain"**, para registrar o módulo de login do sistema de ouvidoria.

A configuração do **"security domain"** deve ser realizada com a inclusão do texto seguinte no arquivo 
'.../jboss-7.1.1/standalone/configuration/standalone.xml'.

```xml
<security-domain name="OuvidoriaSecurityDomain">
	<authentication>
		<login-module code="br.com.xti.ouvidoria.security.OuvidoriaLoginModule" flag="required"/>
	</authentication>
</security-domain>
```
