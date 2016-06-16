/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.veterinaria.jsf.controllers;

import com.veterinaria.jpa.entities.TipoDocumento;
import com.veterinaria.jpa.sessions.TipoDocumentoFacade;
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
public class TipoDocumentoController implements Serializable{

    @EJB
  private TipoDocumentoFacade tipoDocumentoFacade;
    public TipoDocumentoController() {
    }

    public TipoDocumentoFacade getTipoDocumentoFacade() {
        return tipoDocumentoFacade;
    }
      public TipoDocumento getTipoDocumento(java.lang.Short id) {
        return getTipoDocumentoFacade().find(id);
    }

    @FacesConverter(forClass = TipoDocumento.class)
    public static class TipoDocumentoControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            TipoDocumentoController controller = (TipoDocumentoController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "tipoDocumentoController");
            return controller.getTipoDocumento(getKey(value));
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
            if (object instanceof TipoDocumento) {
                TipoDocumento o = (TipoDocumento) object;
                return getStringKey(o.getIdTipoDocumento());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + TipoDocumento.class.getName());
            }
        }

    }
}
