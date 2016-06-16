/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.veterinaria.jsf.controllers;

import com.veterinaria.jpa.entities.Ciudad;
import com.veterinaria.jpa.entities.Cliente;
import com.veterinaria.jpa.entities.Genero;
import com.veterinaria.jpa.entities.TipoDocumento;
import com.veterinaria.jpa.sessions.CiudadFacade;
import com.veterinaria.jpa.sessions.ClienteFacade;
import com.veterinaria.jpa.sessions.GeneroFacade;
import com.veterinaria.jpa.sessions.TipoDocumentoFacade;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import org.apache.commons.codec.digest.DigestUtils;

/**
 *
 * @author andres
 */
@ManagedBean
@ViewScoped
public class RegistroPersonaController implements Serializable {

    private Cliente clienteActual;
    private List<Cliente> listaCliente;
    @EJB
    private ClienteFacade clienteFacade;
    @EJB
    private CiudadFacade ciudadFacade;
    @EJB
    private TipoDocumentoFacade tipoDocumentoDacade;
    @EJB
    private GeneroFacade generoFacade;
    private String password;

    public RegistroPersonaController() {
    }

    public TipoDocumentoFacade getTipoDocumentoDacade() {
        return tipoDocumentoDacade;
    }

    public void setTipoDocumentoDacade(TipoDocumentoFacade tipoDocumentoDacade) {
        this.tipoDocumentoDacade = tipoDocumentoDacade;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    
    
    public Cliente getClienteActual() {
        if (clienteActual == null) {
            clienteActual = new Cliente();
        }
        return clienteActual;
    }

    public CiudadFacade getCiudadFacade() {
        return ciudadFacade;
    }

    public TipoDocumentoFacade getTipoDocumentoFacade() {
        return tipoDocumentoDacade;
    }

    public GeneroFacade getGeneroFacade() {
        return generoFacade;
    }

    public void setClienteActual(Cliente clienteActual) {
        this.clienteActual = clienteActual;
    }

    private ClienteFacade getClienteFacade() {
        return clienteFacade;

    }

    /* public List<Cliente> getListaCliente() {
     return listaCliente;
     }*/
    private void recargarLista() {
        listaCliente = null;
    }

    public List<Cliente> getListaCliente() {
        if (listaCliente == null) {
            try {
                listaCliente = getClienteFacade().findAll();
            } catch (Exception e) {
                addErrorMessage("Error closing resource " + e.getClass().getName(), "Message: " + e.getMessage());
            }
        }
        return listaCliente;
    }

    public List<Ciudad> getListCiudadesAutoComplete(String query) {
        try {
            return getCiudadFacade().findByNombre(query);
        } catch (Exception ex) {
            Logger.getLogger(ClienteController.class
                    .getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public List<TipoDocumento> getListTipoDocumentoSelectOne() {
        return getTipoDocumentoFacade().findAll();
    }

    public List<Genero> getListGeneroSelectOne() {
        return getGeneroFacade().findAll();
    }

    public void registrarCliente() {
        try {
             clienteActual.setPassword(DigestUtils.sha1Hex(password));  
             clienteActual.setFechaCreacionCliente(new Date());
             clienteActual.setEstado(true);
            getClienteFacade().create(clienteActual);
            addSuccesMessage("Registrado", "Registro Exitoso.");
            recargarLista();
            FacesContext.getCurrentInstance().getExternalContext().redirect("registroPag.xhtml");
        } catch (IOException e) {
            addErrorMessage("Error closing resource " + e.getClass().getName(), "Message: " + e.getMessage());

        }

    }

    public void validarDocumento(FacesContext contex, UIComponent component, Object value)
            throws ValidatorException {
        if (getClienteFacade().finByNumeroDocumento((String) value) != null) {
            throw new ValidatorException(new FacesMessage(
                    FacesMessage.SEVERITY_ERROR, "Documento repetido", "El documento ya existe en la base de datos"));
        } else {
            clienteActual.setNumeroDocumento((String) value);
        }
    }

    private void addErrorMessage(String title, String msq) {
        FacesMessage facesMsg
                = new FacesMessage(FacesMessage.SEVERITY_ERROR, title, msq);
        FacesContext.getCurrentInstance().addMessage(null, facesMsg);
    }

    private void addSuccesMessage(String title, String msq) {
        FacesMessage facesMsg
                = new FacesMessage(FacesMessage.SEVERITY_INFO, title, msq);
        FacesContext.getCurrentInstance().addMessage("successInfo", facesMsg);
    }

}
