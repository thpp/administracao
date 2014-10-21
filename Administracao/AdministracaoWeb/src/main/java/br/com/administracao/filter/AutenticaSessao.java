package br.com.administracao.filter;

import javax.faces.application.NavigationHandler;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import br.com.administracao.enumeracao.CidadesEnum;

public class AutenticaSessao implements PhaseListener {

	/**
	 * 
//	 */
	private static final long serialVersionUID = -359955384700617111L;

	@Override
	public void afterPhase(PhaseEvent event) {
		
		FacesContext facesContext = event.getFacesContext();
        String currentPage = facesContext.getViewRoot().getViewId();
        HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(true);

        Integer idCidadeSessao = (Integer) session.getAttribute("idCidade");

        ExternalContext externalContext = facesContext.getExternalContext();
        String idCidadeURL = externalContext.getRequestParameterMap().get("idCidade");
        System.out.println("Página: " + currentPage);
        System.out.println("AUTENTICA SESSAO!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
		
        
        System.out.println("ID DA CIDADE PELA URAL = " + idCidadeURL);

        if (idCidadeURL == null) {
            if (idCidadeSessao == null) {
                NavigationHandler nh = facesContext.getApplication().getNavigationHandler();
                nh.handleNavigation(facesContext, null, "semsessao.xhtml");
            }
        } else {
            CidadesEnum cidade = CidadesEnum.getValue(Integer.parseInt(idCidadeURL));
            if (cidade == null) {
                NavigationHandler nh = facesContext.getApplication().getNavigationHandler();
                nh.handleNavigation(facesContext, null, "semsessao.xhtml");
            } else {
                session.setAttribute("idCidade", cidade.getCodigo());
            }
        }
        
        boolean isLoginPage = (currentPage.lastIndexOf("login.xhtml") > -1);
        boolean isSemSessao = (currentPage.lastIndexOf("semsessao.xhtml") > -1);
        
        Object idCidade = session.getAttribute("idCidade");
        Object isLogado = session.getAttribute("isLogado");

        if (!isLoginPage && !isSemSessao && idCidade == null && isLogado == null) {
            String errorPageLocation = "/pages/public/semsessao.xhtml?faces-redirect=true";
            facesContext.setViewRoot(facesContext.getApplication().getViewHandler().createView(facesContext, errorPageLocation));
            facesContext.getPartialViewContext().setRenderAll(true);
            facesContext.renderResponse();
        }

        
	}

	@Override
	public void beforePhase(PhaseEvent event) {
		FacesContext facesContext = event.getFacesContext();
        HttpServletResponse response = (HttpServletResponse) facesContext.getExternalContext().getResponse();

        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache,");
        response.setHeader("Cache-Control", "no-store");
        response.setHeader("Cache-Control", "private");
        response.setHeader("Cache-Control", "must-revalidate");
        response.setHeader("Cache-Control", "max-stale=0");
        response.setHeader("Cache-Control", "max-age=0");
        response.setDateHeader("Expires", 0);
		
	}

	@Override
	public PhaseId getPhaseId() {
		return PhaseId.RESTORE_VIEW;
	}

}
