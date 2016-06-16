/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.veterinaria.jpa.sessions;

import com.veterinaria.jpa.entities.Ciudad;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author andres
 */
@Stateless
public class CiudadFacade extends AbstractFacade<Ciudad> {
    @PersistenceContext(unitName = "VeterinariaJPAPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CiudadFacade() {
        super(Ciudad.class);
    }
    
    public List<Ciudad> findByNombre(String nombre) {
        Query q = getEntityManager().createNamedQuery("Ciudad.findByNombreCiudad");
        q.setParameter("nombreCiudad", nombre + "%");
        q.setMaxResults(6);
        return q.getResultList();
    }
}
