package com.demo.app.view;

import com.demo.app.view.util.JsfUtil;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.security.enterprise.AuthenticationStatus;
import javax.security.enterprise.SecurityContext;
import javax.security.enterprise.authentication.mechanism.http.AuthenticationParameters;
import javax.security.enterprise.credential.UsernamePasswordCredential;
import javax.security.enterprise.identitystore.Pbkdf2PasswordHash;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotEmpty;

/**
 *
 * @author misanchez
 */
@Named
@RequestScoped
public class LoginView implements Serializable {

    @NotEmpty
    private String usuario;
    @NotEmpty
    private String claveAcceso;

    @Inject
    private Pbkdf2PasswordHash passwordHash;

    @Inject
    private SecurityContext securityContext;

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getClaveAcceso() {
        return claveAcceso;
    }

    public void setClaveAcceso(String claveAcceso) {
        this.claveAcceso = claveAcceso;
    }

    public String validarUsuario() {
        switch (processAuthentication()) {
            case SEND_CONTINUE:
                break;
            case SEND_FAILURE:
                JsfUtil.mensajeError("Usuario y/o contraseña inválidos.");
                break;
            case SUCCESS:
                return "/app/index?faces-redirect=true";
            default:
                break;
        }
        return null;
    }

    private AuthenticationStatus processAuthentication() {
        return securityContext.authenticate(
                (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest(),
                (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse(),
                AuthenticationParameters.withParams().credential(
                        new UsernamePasswordCredential(usuario, claveAcceso))
        );
    }
}
