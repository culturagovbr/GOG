# Montagem do ambiente de desenvolvimento

## Executar carga inicial dos dados

A carga inicial do sistema, com o povoamento dos dados das tabelas de domínio utilizadas no sistema, deve ser realizada com a execução do script disponibilizado no arquivo 
src/main/resources/ScriptCargaDominio.sql


### Ajustar as configurações
#### Criar as views sql utilizadas pela aplicação

* Deletar as tabelas criadas na execução da DLL durante o primeiro deploy
```sql
DROP TABLE vwestatisticasmanifestacao;
DROP TABLE vwultimotramite;
```
- Executar o script disponibilizado nos arquivos listados a seguir, para criação das views que serão utilizadas:
  - src/main/resources/CREATE VwEstatisticasManifestacao.sql 
  - src/main/resources/CREATE vwUltimoTramite.sql


#### Ajustar a tabela de parâmetros da aplicação

##### Tabela 'TbPreferenciaSistema' 

A tabela 'TbPreferenciaSistema' deve ser ajustada para configuração de alguns parâmetros da aplicação. 
```sql
INSERT INTO TbPreferenciaSistema
	(idPreferenciaSistema, nomeOuvidoria, emailOuvidoria, hostEmail, portaEmail, usuarioEmail, 
	senhaEmail, sslEmail, encerrarTramiteEncaminhada, retornarTramiteOuvidoria, ctlPrazoManifSoluc,
	RespostasImediatas, prazoEntrada, prazoAreaSolucionadora, prazoRespostaCidadao)
VALUES 
	(1, 'Ouvidoria MinC', 'naoresponda.ouvidoria@cultura.gov.br', '10.0.0.54', 25, 'ouvidoria@cultura.gov.br',
	'', '2', '1', '1', '1', 
	'1', 1, 28, 1);
```

A tabela 'TbPreferenciaSistema' também pode ser editada internamente no sistema, utilizando-se de um usuário cadastrado como "Administrador" e acessando o menu "Sistema" - "Preferências do Sistema".

##### Tabela 'tbParametro'

A tabela 'TbParametro' é utilizada para configuração mais alguns parâmetros da aplicação. 

```sql
INSERT INTO tbparametro (idParametro, nmparametro, vlrparametro) VALUES (1, 'Sequencial da Manifestação', '1');
INSERT INTO tbparametro (idParametro, nmparametro, vlrparametro) VALUES (2, 'Ano atual', '2015');
INSERT INTO tbparametro (idParametro, nmparametro, vlrparametro) VALUES (3, 'Diretório para onde serão enviados os arquivos anexados', '/var/arquivos/');
INSERT INTO tbparametro (idParametro, nmparametro, vlrparametro) VALUES (4, 'URL base do Sistema', 'http://localhost:8080/GOG');
INSERT INTO tbparametro (idParametro, nmparametro, vlrparametro) VALUES (5, 'Email do Monitoramento', 'email@email.com');
INSERT INTO tbparametro (idParametro, nmparametro, vlrparametro) VALUES (6, 'Caminho do arquivo de propriedades de publicação dos arquivos de relatórios', '/var/arquivos/arquivos-ouvidoria/publicacaoArquivo.xml');
```

> **Nota:** 
>> Certifique-se que o valor do campo *idParametro* está devidamente ajustado, conforme o exemplo anterior. A tabela *TbParametro* deve ser entendida e utilizada como uma tabela de domínio.
>>
>> A aplicação vai utilizar estes valores para garantir a realização de algumas funcionalidades, como:
>> - geração do numero da manifestação
>> - gerenciamento de arquivos anexados
>> - montagem da URL e links para manifestações nas mensagens de e-mail
>> - configuração do e-mail que receberá as mensagens de monitoramento 
>>
>> Você deve oferecer valores adequados a estas strings, conforme a sua realidade 

#### Realizar a configuração/criação de usuários do sistema
A tabela 'tbUsuario' deve ser carregada com um usuário com função de Administrador. 
Esta configuração é oferecida no script de carga de dados iniciais (disponibilizado no arquivo src/main/resources/ScriptCargaDominio.sql), pelo seguinte trecho:

```sql
--tbUsuario
/*
Insere um usuário Administrador (tpFuncao=5) com a senha 123456 e login = 'root'. Este usuário deve ser criado para permitir a configuração interna do sistema,
como a inclusão de novos usuários.
*/
INSERT INTO tbUsuario 
	(idUsuario, nmUsuario, stStatus, eeEmail, idUnidade, tpUsuario, nmLogin, 
	numTelefone, nmSenha, tpFuncao) 
VALUES 
	(1, 'root', 1, 'email@email.com', 1, '1', 'root', 
	'(61)99999999', 'E10ADC3949BA59ABBE56E057F20F883E', '5');

```

> **Nota:** 

>> - Um usuário com função de Administrador tem permissão de editar o cadastro de usuários do sistema e de acessar as funcionalidades disponíveis.

