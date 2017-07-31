package br.com.xti.ouvidoria.controller;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import net.sf.jxls.exception.ParsePropertyException;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import br.com.xti.ouvidoria.dao.ParametroDAO;
import br.com.xti.ouvidoria.dto.DTOArquivo;
import br.com.xti.ouvidoria.exception.InfrastructureException;
import br.com.xti.ouvidoria.helper.FileHelper;

/**
 * @author Clelson Salles Rodrigues
 */
@ManagedBean(name = "mBRelatoriosOuvidoria")
@ViewScoped
public class MBRelatoriosOuvidoria implements Serializable {
	private static final long serialVersionUID = 1L;

	@EJB
	private ParametroDAO parametroDAO;

	private List<DTOArquivo> arquivosPublicados = new ArrayList<DTOArquivo>();


	/**
	 * Recupera a lista de arquivos que devem ser publicados
	 */
	private void recuperaArquivos() {
		try {
			String urlPublicacaoArquivo = parametroDAO
					.getCaminhoPublicacaoArquivos();
			arquivosPublicados = recuperaArquivosPublicacao(urlPublicacaoArquivo);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * Recupera a lista de arquivos que devem ser publicados
	 * 
	 * @param urlArquivoXML
	 * @return
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 * @throws InfrastructureException
	 */
	private List<DTOArquivo> recuperaArquivosPublicacao(String urlArquivoXML)
			throws ParserConfigurationException, SAXException, IOException,
			InfrastructureException {
		List<DTOArquivo> arquivosPublicacao = new ArrayList<DTOArquivo>();

		File arquivoPublicacaoXML = new File(urlArquivoXML);

		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(arquivoPublicacaoXML);

		doc.getDocumentElement().normalize();

		NodeList elementosGrupo = doc.getElementsByTagName("grupo");

		for (int indexGrupo = 0; indexGrupo < elementosGrupo.getLength(); indexGrupo++) {

			Node nodeGrupo = elementosGrupo.item(indexGrupo);

			if (nodeGrupo.getNodeType() == Node.ELEMENT_NODE) {

				Element grupo = (Element) nodeGrupo;
				DTOArquivo arquivoDTO = new DTOArquivo();
				arquivoDTO.setTitulo(grupo.getAttribute("titulo"));

				NodeList elementosArquivo = grupo
						.getElementsByTagName("arquivo");

				for (int indexArquivo = 0; indexArquivo < elementosArquivo
						.getLength(); indexArquivo++) {
					Node nodeArquivo = elementosArquivo.item(indexArquivo);
					Element arquivo = (Element) nodeArquivo;

					DTOArquivo arquivoLink = new DTOArquivo();
					arquivoLink.setCaminho(parametroDAO.getDiretorioAnexo()
							+ arquivo.getAttribute("url"));
					arquivoLink.setTitulo(arquivo.getAttribute("titulo"));
					// Adiciona o link na lista de arquivos
					arquivoDTO.adicionaArquivo(arquivoLink);
				}

				arquivosPublicacao.add(arquivoDTO);
			}
		}

		return arquivosPublicacao;
	}

	/**
	 * @return the arquivosPublicados
	 */
	public List<DTOArquivo> getArquivosPublicados() {
		if (arquivosPublicados == null || arquivosPublicados.isEmpty())
			recuperaArquivos();

		return arquivosPublicados;
	}

	/**
	 * @param arquivosPublicados
	 *            the arquivosPublicados to set
	 */
	public void setArquivosPublicados(List<DTOArquivo> arquivosPublicados) {
		this.arquivosPublicados = arquivosPublicados;
	}

	
	public void downloadArquivo(){
		try {
			String localizacaoArquivo = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("urlArquivo");
			
			String[] arrayNomeArquivo = localizacaoArquivo.split("/");
			String nomeArquivo = arrayNomeArquivo[arrayNomeArquivo.length - 1];

			FileHelper.download(localizacaoArquivo, nomeArquivo);
		} catch (ParsePropertyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}