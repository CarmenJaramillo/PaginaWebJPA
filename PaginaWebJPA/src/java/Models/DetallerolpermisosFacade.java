/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Models;

import Entities.Detallerolpermisos;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Carmen
 */
@Stateless
public class DetallerolpermisosFacade extends AbstractFacade<Detallerolpermisos> {
    @PersistenceContext(unitName = "PaginaWebJPAPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public DetallerolpermisosFacade() {
        super(Detallerolpermisos.class);
    }
    
}
