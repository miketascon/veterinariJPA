/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.veterinaria.jpa.sessions;

import com.veterinaria.jpa.entities.Cliente;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author andres
 */
@Stateless
public class ClienteFacade extends AbstractFacade<Cliente> {

    @PersistenceContext(unitName = "VeterinariaJPAPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ClienteFacade() {
        super(Cliente.class);
    }

    public List<Cliente> findByNumeroDoc(String numeroDocumento) {
        Query q = getEntityManager().createNamedQuery("Cliente.findByNumeroDocumento");
        q.setParameter("numeroDocumento", numeroDocumento + "%");
        q.setMaxResults(10);
        return q.getResultList();
    }

     public Cliente findByNumeroDocumento(String Query) {
        Query q = getEntityManager().createNamedQuery("Cliente.findByNumeroDocumento");
        q.setParameter("numeroDocumento", Query);
        try {
            return (Cliente) q.getSingleResult();
        } catch (NonUniqueResultException ex) {
            throw ex;
        } catch (NoResultException ex) {
            return null;
        }
    }
    
    
    public Cliente finByNumeroDocumento(String numeroDocumento) {
        Query q = getEntityManager().createNamedQuery("Cliente.findByNumeroDocumento");
        q.setParameter("numeroDocumento", numeroDocumento);
        try {
            return (Cliente) q.getSingleResult();
        } catch (NonUniqueResultException ex) {
            throw ex;
        } catch (NoResultException ex) {
            return null;
        }
    }

    public Cliente findBylogin(String numeroDocumento, String password) {

        Query q = getEntityManager().createNamedQuery("Cliente.findByLogin");
        q.setParameter("numeroDocumento", numeroDocumento);
        q.setParameter("password", password);
        try {
            return (Cliente) q.getSingleResult();
        } catch (NonUniqueResultException ex) {
            throw ex;
        } catch (NoResultException ex) {
            return null;
        }
    }

}
