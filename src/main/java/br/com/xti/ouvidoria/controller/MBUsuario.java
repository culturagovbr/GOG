package br.com.xti.ouvidoria.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;

import org.primefaces.model.DualListModel;

import br.com.xti.ouvidoria.dao.PerfilDAO;
import br.com.xti.ouvidoria.dao.UnidadeDAO;
import br.com.xti.ouvidoria.dao.UsuarioDAO;
import br.com.xti.ouvidoria.helper.EnumHelper;
import br.com.xti.ouvidoria.model.TbPerfil;
import br.com.xti.ouvidoria.model.TbUnidade;
import br.com.xti.ouvidoria.model.TbUsuario;
import br.com.xti.ouvidoria.model.enums.FuncaoUsuarioEnum;
import br.com.xti.ouvidoria.model.enums.StatusUsuarioEnum;
import br.com.xti.ouvidoria.model.enums.TipoUsuarioEnum;
import br.com.xti.ouvidoria.model.enums.UnidadeEnum;
import br.com.xti.ouvidoria.security.SecurityService;
import br.com.xti.ouvidoria.util.JSFUtils;
import br.com.xti.ouvidoria.util.PasswordUtils;

/**
 * @author renato
 */
@SuppressWarnings("serial")
@ManagedBean(name = "mBUsuario")
@ViewScoped
public class MBUsuario implements Serializable {
	
	@Inject
	private SecurityService securityService;
	
	@EJB
	private PerfilDAO perfilDAO;
	@EJB
	private UsuarioDAO usuarioDAO;
	@EJB
	private UnidadeDAO unidadeDAO;
	
	
    // Objetos Tbusuario
    private TbUsuario usuario = new TbUsuario();
    private TbUsuario usuarioNovo = new TbUsuario();
    private TbUsuario usuarioExterno = new TbUsuario();
    // PickList de seleção de perfis
    private DualListModel<TbPerfil> perfis;
    private DualListModel<TbPerfil> perfisNovo;
    // Outros
    private FuncaoUsuarioEnum funcaoSelecionada;
    private String senhaAntiga;
    private String senhaConfirmar;
    private String email;

    public String getSenhaConfirmar() {
        return senhaConfirmar;
    }

    public void setSenhaConfirmar(String senhaConfirmar) {
        this.senhaConfirmar = senhaConfirmar;
    }
    

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    @PostConstruct
    public void init() {
        limpar();
    }

    public List<TbUsuario> getTodosInternos() {
    	List<TbUsuario> list = usuarioDAO.getUsuariosInternos();
    	Collections.sort(list);
        return list;
    }
    
    public List<TbUsuario> getTodosUsuariosOuvidor() {
        return usuarioDAO.getUsuarioPorFuncao(FuncaoUsuarioEnum.OUVIDOR);
    }
    
    public List<TbUsuario> getTodosUsuariosOperadorInterlocutor() {
        return usuarioDAO.getUsuarioPorFuncao(FuncaoUsuarioEnum.OPERADOR, FuncaoUsuarioEnum.INTERLOCUTOR);
    }

    public List<TbUsuario> getTodosExternos() {
    	List<TbUsuario> list = usuarioDAO.getUsuariosExternos();
    	Collections.sort(list);
    	return list;
    }

    public List<TbUnidade> getUnidades() {
    	List<TbUnidade> list = unidadeDAO.findAll();
    	Collections.sort(list);
        return list;
    }

    public void cadastrar(ActionEvent actionEvent) {
        try {
            if (!usuarioNovo.getNmSenha().equals(senhaConfirmar)) {
                MensagemFaceUtil.erro("Erro", "Senhas não conferem");
                return;
            }
            // cadastra o usuário na base de dados
            usuarioNovo.setTpUsuario(TipoUsuarioEnum.INTERNO.getId());
            usuarioNovo.setTpFuncao(funcaoSelecionada.getId());
            usuarioNovo.setNmSenha(PasswordUtils.getMD5(usuarioNovo.getNmSenha()).toUpperCase());
            usuarioNovo.setIdUnidade(unidadeDAO.find(usuarioNovo.getIdUnidade().getIdUnidade()));
            usuarioNovo.setStStatus(StatusUsuarioEnum.NOVA_SENHA.getId());

            usuarioDAO.create(usuarioNovo);

            // Registra os perfis selecionados
            List<TbPerfil> perfisSelecionados = perfisNovo.getTarget();
            perfilDAO.atualizarPerfis(usuarioNovo, perfisSelecionados);
            MensagemFaceUtil.info("Inclusão realizada com sucesso.", "");
        } catch (Exception e) {
            e.printStackTrace();
            MensagemFaceUtil.erro("Erro", "Ocorreu um erro. Entre em contato com o administrador do sistema.");
        }
        limpar();
    }

    public void cadastrarExterno(ActionEvent actionEvent) {
        try {
            usuarioExterno.setAtivo(Boolean.TRUE);
            usuarioExterno.setTpUsuario(TipoUsuarioEnum.EXTERNO.getId());
            usuarioExterno.setTpFuncao(FuncaoUsuarioEnum.MANIFESTANTE.getId());
            usuarioExterno.setNmSenha(PasswordUtils.getMD5(usuarioExterno.getNmSenha()).toUpperCase());
            usuarioDAO.create(usuarioExterno);

            MensagemFaceUtil.info("Inclusão realizada com sucesso.", "");
        } catch (Exception e) {
            e.printStackTrace();
            MensagemFaceUtil.erro("Erro", "Ocorreu um erro. Entre em contato com o administrador do sistema.");
        }
        limpar();
    }

    public void alterar(ActionEvent actionEvent) throws Exception {
        try {
            if (getUsuario() != null && getUsuario().getIdUsuario() != null) {
                // atualiza o usuário na base de dados
                if (!email.equals(getUsuario().getEeEmail())) {
                    TbUsuario user = usuarioDAO.findByEmail(email);
                    if (user != null) {
                        MensagemFaceUtil.erro("E-mail já cadastrado", "E-mail já cadastrado");

                        return;
                    }
                }

                usuario.setEeEmail(email);
                if (senhaAntiga.equals(usuario.getNmSenha())) {
                	usuario.setNmSenha(senhaAntiga);
                } else {
                	usuario.setNmSenha(PasswordUtils.getMD5(usuario.getNmSenha()).toUpperCase());
                }
                usuario.setTpFuncao(funcaoSelecionada.getId());
                usuario.setIdUnidade(unidadeDAO.find(usuario.getIdUnidade().getIdUnidade()));
                usuarioDAO.edit(getUsuario());

                // Registra os perfis selecionados
                List<TbPerfil> perfisSelecionados = perfis.getTarget();
                perfilDAO.atualizarPerfis(usuario, perfisSelecionados);
                MensagemFaceUtil.info("Alteração realizada com sucesso.", "");
                JSFUtils.closeDialog("dlgEditarUsuario");
            }
        } catch (Exception e) {
            e.printStackTrace();
            MensagemFaceUtil.erro("Erro", "Ocorreu um erro. Entre em contato com o administrador do sistema.");
        }
        limpar();
    }

    public void prepararAlterar(TbUsuario usuario) {
        try {
            senhaAntiga = usuario.getNmSenha();
            funcaoSelecionada = EnumHelper.getFuncaoUsuarioEnum(usuario.getTpFuncao());

            // Recupera todos os perfis personalizados
            List<TbPerfil> disponiveis = perfilDAO.findAll();
            // Recupera os perfis ativos do usuário atual
            List<TbPerfil> selecionados = perfilDAO.getPerfisAtivos(usuario);
            // Retira da lista 'disponiveis' os perfis já ativos atualmente
            disponiveis.removeAll(selecionados);

            // Seta os perfis em seus respectivos campos
            perfis.setSource(disponiveis);
            perfis.setTarget(selecionados);
        } catch (Exception ex) {
            ex.printStackTrace();
            MensagemFaceUtil.erro("Erro", "Ocorreu um erro ao carregar os perfis desse usuário");
        }
    }
    
    public void remover(ActionEvent actionEvent) {
        try {
            if (usuario != null && usuario.getIdUsuario() != null) {
                usuarioDAO.remove(usuario);
                MensagemFaceUtil.info("Exclusão realizada com sucesso.", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            String msgError = "Usuário não pode ser excluido, possui vínculo com registros de outras tabelas";
            MensagemFaceUtil.erro(msgError, msgError);
        }
        JSFUtils.showGrowl();
        limpar();
    }
    
    public List<StatusUsuarioEnum> statusDisponiveis() {
    	List<StatusUsuarioEnum> list = new ArrayList<>();
    	list.add(StatusUsuarioEnum.INATIVO);
    	
    	if(StatusUsuarioEnum.NOVA_SENHA.getId() == usuario.getStStatus())
    		list.add(StatusUsuarioEnum.NOVA_SENHA);
		else 
			list.add(0, StatusUsuarioEnum.ATIVO);
    	
    	return list;
    }

    public void onChangeUnidadeNovo() {
        if (usuarioNovo.getIdUnidade().getIdUnidade().equals(UnidadeEnum.OUVIDORIA.getId())) {
            funcaoSelecionada = FuncaoUsuarioEnum.OUVIDOR;
        } else {
            funcaoSelecionada = null;
        }
    }

    public void onChangeUnidade() {
        if (usuario.getIdUnidade().getIdUnidade().equals(UnidadeEnum.OUVIDORIA.getId())) {
            funcaoSelecionada = FuncaoUsuarioEnum.OUVIDOR;
        } else {
            funcaoSelecionada = null;
        }
    }

    public Boolean disableSlcFuncao() {
        if (funcaoSelecionada == FuncaoUsuarioEnum.OUVIDOR) {
            return true;
        }
        return false;
    }

    public Collection<FuncaoUsuarioEnum> getFuncoesUsuario() {
        Collection<FuncaoUsuarioEnum> c = new ArrayList<>();
        if (funcaoSelecionada == FuncaoUsuarioEnum.OUVIDOR) {
            c.add(FuncaoUsuarioEnum.OUVIDOR);
        } else {
            c.add(FuncaoUsuarioEnum.INTERLOCUTOR);
            c.add(FuncaoUsuarioEnum.OPERADOR);
        }

        return c;
    }
    
    public boolean showEditButton(TbUsuario u) {
        if(securityService.getUserProfile() == FuncaoUsuarioEnum.ADMINISTRADOR)
            return true;
        
        return !(u.getTpFuncao().equals(FuncaoUsuarioEnum.ADMINISTRADOR.getId()));
    }
    
    public void limpar() {
        usuario = new TbUsuario();
        usuarioNovo = new TbUsuario();
        usuarioExterno = new TbUsuario();
        perfis = new DualListModel<>();
        perfisNovo = new DualListModel<>(perfilDAO.findAll(), new ArrayList<TbPerfil>());
        funcaoSelecionada = null;
        senhaAntiga = null;
    }

    public TbUsuario getUsuario() {
        return usuario;
    }

    public void setUsuario(TbUsuario usuario) {
        this.usuario = usuario;
        this.email = usuario.getEeEmail();
    }

    public TbUsuario getUsuarioExterno() {
        return usuarioExterno;
    }

    public void setUsuarioExterno(TbUsuario usuarioExterno) {
        this.usuarioExterno = usuarioExterno;
    }

    public TbUsuario getUsuarioNovo() {
        return usuarioNovo;
    }

    public void setUsuarioNovo(TbUsuario usuarioNovo) {
        this.usuarioNovo = usuarioNovo;
    }

    public DualListModel<TbPerfil> getPerfis() {
        return perfis;
    }

    public void setPerfis(DualListModel<TbPerfil> perfis) {
        this.perfis = perfis;
    }

    public DualListModel<TbPerfil> getPerfisNovo() {
        return perfisNovo;
    }

    public void setPerfisNovo(DualListModel<TbPerfil> perfisNovo) {
        this.perfisNovo = perfisNovo;
    }

    public String isAtivo(TbUsuario usuario) {
        if (usuario != null) {
            return usuario.getStStatus() == 0 ? "Inativo" : "Ativo";
        }
        return "";
    }

    public FuncaoUsuarioEnum getFuncaoSelecionada() {
        return funcaoSelecionada;
    }

    public void setFuncaoSelecionada(FuncaoUsuarioEnum funcaoSelecionada) {
        this.funcaoSelecionada = funcaoSelecionada;
    }
}
