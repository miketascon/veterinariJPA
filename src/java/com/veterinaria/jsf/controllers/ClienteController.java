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
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.validator.ValidatorException;
import org.apache.commons.codec.digest.DigestUtils;

/**
 *
 * @author andres
 */
@ManagedBean
@SessionScoped
public class ClienteController implements Serializable {

    /**
     * Creates a new instance of ClienteController
     */
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

    public ClienteController() {
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
    public String prepareCreate() {
        clienteActual = new Cliente();
        return "/faces/clientes/crearClientes";
    }

    public String prepareEdit() {
        return "/faces/admin/UsuarioEdit";
    }

    public String prepareView() {
        return "/faces/usuario/UsuarioView";
    }

    public String prepareList() {
        recargarLista();
        return "/faces/clientes/clientesList";
    }

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

    public String addCliente() {
        try {
            
            clienteActual.setPassword(DigestUtils.sha1Hex(password));
            clienteActual.setFechaCreacionCliente(new Date());
            clienteActual.setEstado(true);

            getClienteFacade().create(clienteActual);

            addSuccesMessage("CLIENTE CREADO", "Cliente Creado Exitosamente.");
            recargarLista();

            FacesContext.getCurrentInstance().getExternalContext().redirect("clientesList.xhtml");
            return "/faces/clientes/clientesList";
        } catch (IOException e) {
            addErrorMessage("Error closing resource " + e.getClass().getName(), "Message: " + e.getMessage());
            return null;
        }

    }

    public String updateCliente() {
        try {
            getClienteFacade().edit(clienteActual);
            addSuccesMessage("Actualizar Cliente", "Cliente Actualizado Exitosamente.");
            recargarLista();
            return "/faces/usuario/UsuarioView";

        } catch (Exception e) {
            addErrorMessage("Error closing resource " + e.getClass().getName(), "Message: " + e.getMessage());
            return null;
        }

    }

    public String deleteCliente() {
        try {
            getClienteFacade().remove(clienteActual);
            addSuccesMessage("Eliminar Cliente", "Cliente Eliminado Exitosamente.");
            recargarLista();

        } catch (Exception e) {
            addErrorMessage("Error closing resource " + e.getClass().getName(), "Message: " + e.getMessage());

        }
        return "/faces/admin/UsuarioList";
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

    public Cliente getCliente(java.lang.Integer id) {
        return getClienteFacade().find(id);
    }

    @FacesConverter(forClass = Cliente.class, value = "clienteConverter")
    public static class ClienteControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            ClienteController controller = (ClienteController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "clienteController");
            return controller.getCliente(getKey(value));
        }

        java.lang.Integer getKey(String value) {
            java.lang.Integer key;
            key = Integer.valueOf(value);
            return key;
        }

        String getStringKey(java.lang.Integer value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Cliente) {
                Cliente o = (Cliente) object;
                return getStringKey(o.getIdCliente());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Cliente.class.getName());
            }
        }

    }
}
