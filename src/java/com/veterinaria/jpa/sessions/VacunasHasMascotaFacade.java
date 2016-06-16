/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.veterinaria.jpa.sessions;

import com.veterinaria.jpa.entities.VacunasHasMascota;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author andres
 */
@Stateless
public class VacunasHasMascotaFacade extends AbstractFacade<VacunasHasMascota> {
    @PersistenceContext(unitName = "VeterinariaJPAPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public VacunasHasMascotaFacade() {
        super(VacunasHasMascota.class);
    }
    
}
