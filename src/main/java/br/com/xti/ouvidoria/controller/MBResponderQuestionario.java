package br.com.xti.ouvidoria.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

import br.com.xti.ouvidoria.comparator.PerguntaQuestionarioPosicaoComparator;
import br.com.xti.ouvidoria.dao.ComentarioQuestionarioDAO;
import br.com.xti.ouvidoria.dao.ManifestacaoDAO;
import br.com.xti.ouvidoria.dao.PerguntaQuestionarioDAO;
import br.com.xti.ouvidoria.dao.QuestionarioDAO;
import br.com.xti.ouvidoria.dao.RespostaQuestionarioDAO;
import br.com.xti.ouvidoria.exception.InfrastructureException;
import br.com.xti.ouvidoria.model.TbComentarioQuestionario;
import br.com.xti.ouvidoria.model.TbManifestacao;
import br.com.xti.ouvidoria.model.TbPerguntaQuestionario;
import br.com.xti.ouvidoria.model.TbQuestionario;
import br.com.xti.ouvidoria.model.TbRespostaQuestionario;
import br.com.xti.ouvidoria.model.enums.FuncaoUsuarioEnum;
import br.com.xti.ouvidoria.security.SecurityService;

/**
 * @author Samuel Correia Guimarães
 */

@SuppressWarnings("serial")
@ManagedBean(name = "mBResponderQuestionario")
@ViewScoped
public class MBResponderQuestionario implements Serializable{

	@Inject
	private SecurityService securityService;

	@EJB
	private QuestionarioDAO dao;
	
	@EJB
	private PerguntaQuestionarioDAO perguntaDAO;
	
	@EJB
	private RespostaQuestionarioDAO respostaDAO;
	
	@EJB
	private ComentarioQuestionarioDAO comentarioDAO;
	
	@EJB
	private ManifestacaoDAO manifestacaoDAO;
    
    private TbQuestionario questionario;
    private List<TbPerguntaQuestionario> perguntas;
    private List<TbRespostaQuestionario> respostas;
    private int quantidadePerguntas;
    private TbManifestacao manifestacao;
    private String comentario;
    private boolean respondido;
    
	@PostConstruct
    public void init() {
    	questionario = new TbQuestionario();
    	perguntas = new ArrayList<>();
    	respostas = new ArrayList<>();
    	quantidadePerguntas = 0;
    }
    
    public void prepararResponder(TbManifestacao manifestacao) {
		this.manifestacao = manifestacao;
		respondido = Boolean.FALSE;
		
		if(manifestacao.getIdQuestionario() != null) {
			questionario = manifestacao.getIdQuestionario();
			respostas = respostaDAO.getByManifestacao(manifestacao);
			TbComentarioQuestionario c = comentarioDAO.find(manifestacao.getIdManifestacao()); 
			if(c != null) {
				comentario = c.getDsComentario();
			} else {
				comentario = "";
			}
			respondido = Boolean.TRUE;
		} else {
			questionario = dao.getQuestionarioAtivo();
		}
		
		perguntas = new ArrayList<>(questionario.getPerguntas());
		Collections.sort(perguntas, new PerguntaQuestionarioPosicaoComparator());
		quantidadePerguntas = perguntas.size();
    }
    
    public void responder() {
    	// Responde as questões
    	TbRespostaQuestionario resp;
    	for(int i = 0; i < quantidadePerguntas; i++) {
			resp = new TbRespostaQuestionario();
			resp.setManifestacao(manifestacao);
			resp.setPergunta(perguntas.get(i));
//			if(i == 1) resp.setDsResposta(1);
			
			if(i+1 == 1) resp.setDsResposta(Integer.parseInt(resposta1));
			if(i+1 == 2) resp.setDsResposta(Integer.parseInt(resposta2));
			if(i+1 == 3) resp.setDsResposta(Integer.parseInt(resposta3));
			if(i+1 == 4) resp.setDsResposta(Integer.parseInt(resposta4));
			if(i+1 == 5) resp.setDsResposta(Integer.parseInt(resposta5));
			if(i+1 == 6) resp.setDsResposta(Integer.parseInt(resposta6));
			if(i+1 == 7) resp.setDsResposta(Integer.parseInt(resposta7));
			if(i+1 == 8) resp.setDsResposta(Integer.parseInt(resposta8));
			if(i+1 == 9) resp.setDsResposta(Integer.parseInt(resposta9));
			if(i+1 == 10) resp.setDsResposta(Integer.parseInt(resposta10));
			
			try {
				respostaDAO.create(resp);
			} catch (InfrastructureException e) {
				e.printStackTrace();
			}
    	}
    	
    	// Registra o comentário
    	try {
    		TbComentarioQuestionario c = new TbComentarioQuestionario();
    		c.setIdComentario(manifestacao.getIdManifestacao());
    		c.setDsComentario(comentario);
			comentarioDAO.create(c);
		} catch (InfrastructureException e1) {
			e1.printStackTrace();
		}
    	
    	// Seta o Questionário na Manifestação
    	try {
    		manifestacao.setIdQuestionario(questionario);
			manifestacaoDAO.edit(manifestacao);
		} catch (InfrastructureException e) {
			e.printStackTrace();
		}
    }
    
    public String descricaoPergunta(int num) {
    	return perguntas.get(--num).getDsPergunta();
    }
    
    public boolean mostrarPergunta(int num) {
    	return num <= quantidadePerguntas;
    }
    
    public boolean mostrarDados() {
    	if(manifestacao == null) {
    		return false;
    	}
    		
    	try {
	    	FuncaoUsuarioEnum funcao = securityService.getUserProfile();
	    	TbQuestionario q = manifestacao.getIdQuestionario();
	    	if(q == null) {
	    		if(funcao == FuncaoUsuarioEnum.ADMINISTRADOR || funcao == FuncaoUsuarioEnum.OUVIDOR || funcao == FuncaoUsuarioEnum.OPERADOR || funcao == FuncaoUsuarioEnum.INTERLOCUTOR) {
	    			return false;
	    		}
	    	}
    	} catch (Exception e) {
    		return true;
    	}
		return true;
    }

    
    
    // GETTERs e SETTERs
	public String getComentario() {
		return comentario;
	}

	public void setComentario(String comentario) {
		this.comentario = comentario;
	}
	
	public TbQuestionario getQuestionario() {
		return questionario;
	}

	public void setQuestionario(TbQuestionario questionario) {
		this.questionario = questionario;
	}

	public List<TbPerguntaQuestionario> getPerguntas() {
		return perguntas;
	}

	public void setPerguntas(List<TbPerguntaQuestionario> perguntas) {
		this.perguntas = perguntas;
	}

	public int getQuantidadePerguntas() {
		return quantidadePerguntas;
	}

	public void setQuantidadePerguntas(int quantidadePerguntas) {
		this.quantidadePerguntas = quantidadePerguntas;
	}
	
	public List<TbRespostaQuestionario> getRespostas() {
		return respostas;
	}

	public void setRespostas(List<TbRespostaQuestionario> respostas) {
		this.respostas = respostas;
	}

	public boolean isRespondido() {
		return respondido;
	}

	public void setRespondido(boolean respondido) {
		this.respondido = respondido;
	}



	// Respostas
	private String resposta1,resposta2,resposta3,resposta4,resposta5,resposta6,resposta7,resposta8,resposta9,resposta10;

	public String getResposta1() {
		return resposta1;
	}

	public void setResposta1(String resposta1) {
		this.resposta1 = resposta1;
	}

	public String getResposta2() {
		return resposta2;
	}

	public void setResposta2(String resposta2) {
		this.resposta2 = resposta2;
	}

	public String getResposta3() {
		return resposta3;
	}

	public void setResposta3(String resposta3) {
		this.resposta3 = resposta3;
	}

	public String getResposta4() {
		return resposta4;
	}

	public void setResposta4(String resposta4) {
		this.resposta4 = resposta4;
	}

	public String getResposta5() {
		return resposta5;
	}

	public void setResposta5(String resposta5) {
		this.resposta5 = resposta5;
	}

	public String getResposta6() {
		return resposta6;
	}

	public void setResposta6(String resposta6) {
		this.resposta6 = resposta6;
	}

	public String getResposta7() {
		return resposta7;
	}

	public void setResposta7(String resposta7) {
		this.resposta7 = resposta7;
	}

	public String getResposta8() {
		return resposta8;
	}

	public void setResposta8(String resposta8) {
		this.resposta8 = resposta8;
	}

	public String getResposta9() {
		return resposta9;
	}

	public void setResposta9(String resposta9) {
		this.resposta9 = resposta9;
	}

	public String getResposta10() {
		return resposta10;
	}

	public void setResposta10(String resposta10) {
		this.resposta10 = resposta10;
	}
	
}
