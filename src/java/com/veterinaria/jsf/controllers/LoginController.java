/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.veterinaria.jsf.controllers;

import com.veterinaria.jpa.entities.Cliente;
import com.veterinaria.jpa.sessions.ClienteFacade;
import java.awt.Event;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import org.apache.commons.codec.digest.DigestUtils;

/**
 *
 * @author Maicol
 */
@ManagedBean
@SessionScoped
public class LoginController implements Serializable {

    @EJB
    private ClienteFacade EJBcliente;
    private Cliente cliente;
    private String numeroDocumento;
    private String password;
    private List<Cliente> listaCliente;
    private String logueado;
    private String message;
    FacesContext context = FacesContext.getCurrentInstance();
    
    /**
     * Creates a new instance of LoginController
     */
    public LoginController() {

    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public ClienteFacade getEJBcliente() {
        return EJBcliente;
    }

    public void setEJBcliente(ClienteFacade EJBcliente) {
        this.EJBcliente = EJBcliente;
    }

    public String getNumeroDocumento() {
        return numeroDocumento;
    }

    public void setNumeroDocumento(String numeroDocumento) {
        this.numeroDocumento = numeroDocumento;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = DigestUtils.sha1Hex(password);
    }

    public List<Cliente> getListaCliente() {
        return listaCliente;
    }

    public String getLogueado() {
        return logueado;
    }

    public void setLogueado(String logueado) {
        this.logueado = logueado;
    }

    public FacesContext getContext() {
        return context;
    }
    
    public String getMessage() {
        return message;
    }
 
    public void setMessage(String message) {
        this.message = message;
    }
    
    

 
    public String iniciarSesion() {
        String redireccion = null;
        try {
            
            System.out.println(" " + password);
            Cliente us = EJBcliente.findBylogin(numeroDocumento, password);
          
            
            if (us != null){                   
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                        "Bienvenid@ " + us.toString(), null));
               
                context.getExternalContext().getSessionMap().put("usuario", us);
                redireccion = "/clientes/clientesList?faces-redirect=true";
                

            } else {
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
                        "Aviso", "El número de documento o la contraseña son incorretas"));
            }

        } catch (Exception e) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Aviso", "Error"));
        }

        return redireccion;

    }

       

    public void VerificarSesion(Event event) {
        try {

            Cliente us = (Cliente) context.getExternalContext().getSessionMap().get("usuario");
            if (us == null) {
                context.getExternalContext().redirect("./../index.xhtml");

            }
        } catch (IOException e) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Aviso", "Error"));
        }
    }

    public void cerrarSesion() {
        context.getExternalContext().invalidateSession();
    }
}
