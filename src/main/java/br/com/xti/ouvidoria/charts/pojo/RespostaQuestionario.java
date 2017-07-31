package br.com.xti.ouvidoria.charts.pojo;


public class RespostaQuestionario implements IResultadoGrafico,Comparable<RespostaQuestionario> {
	
	private int idPergunta;
	private String nomePergunta;
	private int posicao;
	private int concordoTotalmente;
	private int concordo;
	private int discordo;
	private int discordoTotalmente;
	private String concordoTotalmenteNrManifestacoes;
	private String concordoNrManifestacoes;
	private String discordoNrManifestacoes;
	private String discordoTotalmenteNrManifestacoes;
	
	public RespostaQuestionario() {
	}
	
	public RespostaQuestionario(int idPergunta, int posicao, int concordoTotalmente, int concordo, int discordo, int discordoTotalmente) {
		this.idPergunta = idPergunta;
		this.posicao = posicao;
		this.concordoTotalmente = concordoTotalmente;
		this.concordo = concordo;
		this.discordo = discordo;
		this.discordoTotalmente = discordoTotalmente;
	}
	
	public String getNomePergunta() {
		return nomePergunta;
	}

	public void setNomePergunta(String nomePergunta) {
		this.nomePergunta = nomePergunta;
	}

	public int getIdPergunta() {
		return idPergunta;
	}

	public void setIdPergunta(int idPergunta) {
		this.idPergunta = idPergunta;
	}

	public int getPosicao() {
		return posicao;
	}

	public void setPosicao(int posicao) {
		this.posicao = posicao;
	}

	public int getConcordoTotalmente() {
		return concordoTotalmente;
	}
	
	public void addConcordoTotalmente() {
		concordoTotalmente++;
	}

	public void setConcordoTotalmente(int concordoTotalmente) {
		this.concordoTotalmente = concordoTotalmente;
	}

	public int getConcordo() {
		return concordo;
	}
	
	public void addConcordo() {
		concordo++;
	}

	public void setConcordo(int concordo) {
		this.concordo = concordo;
	}

	public int getDiscordo() {
		return discordo;
	}
	
	public void addDiscordo() {
		discordo++;
	}

	public void setDiscordo(int discordo) {
		this.discordo = discordo;
	}

	public void addDiscordoTotalmente() {
		discordoTotalmente++;
	}
	
	public int getDiscordoTotalmente() {
		return discordoTotalmente;
	}

	public void setDiscordoTotalmente(int discordoTotalmente) {
		this.discordoTotalmente = discordoTotalmente;
	}
	
	public String getConcordoTotalmenteNrManifestacoes() {
		return concordoTotalmenteNrManifestacoes;
	}

	public void setConcordoTotalmenteNrManifestacoes(String concordoTotalmenteNrManifestacoes) {
		this.concordoTotalmenteNrManifestacoes = concordoTotalmenteNrManifestacoes;
	}
	
	public void addConcordoTotalmenteNrManifestacao(int numero) {
		if(this.concordoTotalmenteNrManifestacoes == null || this.concordoTotalmenteNrManifestacoes.isEmpty())
			this.concordoTotalmenteNrManifestacoes = String.valueOf(numero);
		else
			this.concordoTotalmenteNrManifestacoes = this.concordoTotalmenteNrManifestacoes.concat(",").concat(String.valueOf(numero));
	}

	public String getConcordoNrManifestacoes() {
		return concordoNrManifestacoes;
	}

	public void setConcordoNrManifestacoes(String concordoNrManifestacoes) {
		this.concordoNrManifestacoes = concordoNrManifestacoes;
	}
	
	public void addConcordoNrManifestacao(int numero) {
		if(this.concordoNrManifestacoes == null || this.concordoNrManifestacoes.isEmpty())
			this.concordoNrManifestacoes = String.valueOf(numero);
		else
			this.concordoNrManifestacoes = this.concordoNrManifestacoes.concat(",").concat(String.valueOf(numero));
	}

	public String getDiscordoNrManifestacoes() {
		return discordoNrManifestacoes;
	}

	public void setDiscordoNrManifestacoes(String discordoNrManifestacoes) {
		this.discordoNrManifestacoes = discordoNrManifestacoes;
	}
	
	public void addDiscordoNrManifestacao(int numero) {
		if(this.discordoNrManifestacoes == null || this.discordoNrManifestacoes.isEmpty())
			this.discordoNrManifestacoes = String.valueOf(numero);
		else
			this.discordoNrManifestacoes = this.discordoNrManifestacoes.concat(",").concat(String.valueOf(numero));
	}

	public String getDiscordoTotalmenteNrManifestacoes() {
		return discordoTotalmenteNrManifestacoes;
	}

	public void setDiscordoTotalmenteNrManifestacoes(String discordoTotalmenteNrManifestacoes) {
		this.discordoTotalmenteNrManifestacoes = discordoTotalmenteNrManifestacoes;
	}
	
	public void addDiscordoTotalmenteNrManifestacao(int numero) {
		if(this.discordoTotalmenteNrManifestacoes == null || this.discordoTotalmenteNrManifestacoes.isEmpty())
			this.discordoTotalmenteNrManifestacoes = String.valueOf(numero);
		else
			this.discordoTotalmenteNrManifestacoes = this.discordoTotalmenteNrManifestacoes.concat(",").concat(String.valueOf(numero));
	}

	@Override
	public int getQuantidade() {
		return 0;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + idPergunta;
		result = prime * result + posicao;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RespostaQuestionario other = (RespostaQuestionario) obj;
		if (idPergunta != other.idPergunta)
			return false;
		return true;
	}

	public int compareTo(RespostaQuestionario o) {
		return new Integer(posicao).compareTo(o.getPosicao());
	}
	
	
}
