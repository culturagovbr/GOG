# Montagem do ambiente de desenvolvimento

## Configurar o Banco de Dados
O projeto GOG utiliza para o mapeamento objeto relacional - ORM - a especificação JPA (com hibernate), mantendo o mapeamento objeto relacional mais transparente. 

Com o mapeamento JPA, podemos utilizar qualquer gerenciador de banco de dados, bastando ajustar os drivers e os arquivos de configuração. Com isto, podemos utilizar o SGBD Postgresql como Banco de Dados do projeto GOG.

### Configuração do dataSource no projeto (arquivo "persistence.xml") 
Path: /GOG/src/main/resources/META-INF/persistence.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<persistence xmlns="http://java.sun.com/xml/ns/persistence"
             xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
             xsi:schemaLocation="http://java.sun.com/xml/ns/persistence http://java.sun.com/xml/ns/persistence/persistence_2_0.xsd"
             version="2.0">
	<persistence-unit name="ouvidoriaPersistenceUnit">
		<jta-data-source>java:/datasources/GOGDSPostreSQL</jta-data-source>
		<properties>
			<!-- Properties for Hibernate -->
			<property name="hibernate.dialect" value="org.hibernate.dialect.PostgreSQLDialect" />
			<property name="hibernate.hbm2ddl.auto" value="create-drop" /> 
			<property name="hibernate.format_sql" value="true" />
			<property name="hibernate.show_sql" value="true" />
			<property name="hibernate.enable_lazy_load_no_trans" value="true" />
		</properties>
	</persistence-unit>
</persistence>
```

> **Nota:** 

> - A criação da DDL do banco de dados (contendo a definição da estrutura do banco) pode ser comentada no arquivo de persistence, após a primeira disponibilização do projeto. Este procedimento garante a instalação do banco de dados durante a primeira execução do sistema.
```
<!-- 			<property name="hibernate.hbm2ddl.auto" value="create-drop" /> -->
```

> - Pode-se configurar a aplicação para não exibir os scritps SQL durante a execução do sistema, alterando o valor da propriedade.
```
 			<property name="hibernate.show_sql" value="false" /> 
```
