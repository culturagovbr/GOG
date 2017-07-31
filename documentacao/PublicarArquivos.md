
# Publicação Dinâmica de Arquivos
Neste documento apresentamos como configurar e utilizar a funcionalidade para publicação dinâmica de arquivos no sistema GOG.

> **Nota**
>> Os arquivos podem ser publicados dinamicamente na aplicação, permitindo o acesso aos usuários, sem a necessidade de autenticação. 
>> O link de acesso aos arquivos será exibido no rodapé da página inicial do sistema, na lista de "Links importantes" no link "Relatórios da Ouvidoria".

## Passos para publicar arquivos no GOG

### Configurar os parâmetros da aplicação
   * Para publicação dos arquivos devemos utilizar dois **Parâmetros**
     * DIRETORIO_ANEXOS(3,"Diretório para onde serão enviados os arquivos anexados")
     * CAMINHO_PUBLICACAO_ARQUIVOS(6,"Caminho do arquivo de propriedades de publicação dos arquivos de relatórios")
   * Estes **Parâmetros** devem estar configurados na Tabela **TbParametro** com os devidos valores inseridos nas colunas ```idParametro, nmParametro e vlrParametro```, conforme o exemplo a seguir:

| idParametro | nmParametro | vlrParametro |
|-------------|-------------|--------------|
| 3	| Diretório para onde serão enviados os arquivos anexados	| /var/arquivos/arquivos-ouvidoria/ |
| 6	| Caminho do arquivo de propriedades de publicação dos arquivos de relatórios	| /var/arquivos/arquivos-ouvidoria/publicacaoArquivo.xml |

> **Nota**
>> Certifique-se que o valor do campo idParametro está devidamente ajustado, conforme o exemplo anterior. A tabela TbParametro deve ser entendida e utilizada como uma tabela de domínio.

   
   
### Disponibilizar o arquivo de publicação
   * O arquivo de publicação deve ser um arquivo escrito no padrão xml 
   * O conteúdo do arquivo de publicação deve seguir o seguinte padrão:
   
```xml   
<?xml version="1.0" encoding="UTF-8"?>
<publicacao>
	<grupo titulo="" >
		<arquivo url="" titulo="" />
	</grupo>
	<grupo titulo="" >
		<arquivo url="" titulo="" />
		<arquivo url="" titulo="" />
		<arquivo url="" titulo="" />
	</grupo>

</publicacao>
```

No exemplo a seguir demonstramos a publicação de cinco arquivos diferentes, organizados em dois grupos

```xml   
<?xml version="1.0" encoding="UTF-8"?>
<publicacao>
	<grupo titulo="Ações da Ouvidoria" >
		<arquivo url="anexos/Acao2010.pdf" 
		  titulo="Relatório de Ações da Ouvidoria em 2010" />
		<arquivo url="anexos/Acao2011.pdf" 
		  titulo="Relatório de Ações da Ouvidoria em 2011" />
	</grupo>
	<grupo titulo="Indicadores da Ouvidoria" >
		<arquivo url="anexos/IndicadoresOuvidoria_textos.pdf" 
		  titulo="Informações sobre os indicadores da Ouvidoria" />
		<arquivo url="anexos/IndicadoresOuvidoria_tabela.pdf" 
		  titulo="Tabela de indicadores da Ouvidoria" />
		<arquivo url="anexos/IndicadoresOuvidoria_graficos.pdf" 
		  titulo="Gráfico de indicadores da Ouvidoria" />
	</grupo>
</publicacao>
```

### Garantir o acesso aos arquivos

* A aplicação deve ter acesso permitido aos diretórios e arquivos configurados na tabela de parâmetros   
* Os arquivos referenciados no arquivo xml devem ser depositados em um diretório que deve ser composta da seguinte forma
  * ```<parametroArquivoAnexo>/<urlArquivo>```, onde
    * ```<parametroArquivoAnexo>```: o valor configurado no parâmetro com ID = 3, da tabela TbParametro
    * ```<urlArquivo>```: o valor definido na atributo "url" da tag "```<arquivo ...```" do arquivo xml de publicação
    
### Testar a publicação

* A publicação dos arquivos pode ser verificada na aplicação utilizando o link oferecido no rodapé da página inicial do sistema, na lista de "Links importantes" no link "Relatórios da Ouvidoria".

* O código de publicação dos arquivos está disponibilizado a partir do arquivo ```GOG/src/main/webapp/pages/externo/listarAnexos.xhtml```


