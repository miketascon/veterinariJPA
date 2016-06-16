/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.veterinaria.jsf.controllers;

import com.veterinaria.jpa.entities.Genero;
import com.veterinaria.jpa.sessions.GeneroFacade;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 *
 * @author andres
 */
@ManagedBean
@ViewScoped
public class GeneroController implements Serializable{

    @EJB
  private GeneroFacade generoFacade;
    public GeneroController() {
    }

    public GeneroFacade getGeneroFacade() {
        return generoFacade;
    }
    public Genero getGenero(java.lang.Short id) {
        return getGeneroFacade().find(id);
    }

    @FacesConverter(forClass = Genero.class)
    public static class GeneroControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            GeneroController controller = (GeneroController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "generoController");
            return controller.getGenero(getKey(value));
        }

        java.lang.Short getKey(String value) {
            java.lang.Short key;
            key = Short.valueOf(value);
            return key;
        }

        String getStringKey(java.lang.Short value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Genero) {
                Genero o = (Genero) object;
                return getStringKey(o.getIdGenero());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Genero.class.getName());
            }
        }

    }
    
}
