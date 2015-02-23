/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import Entities.Detalleplancliente;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Carmen
 */
@Stateless
public class DetalleplanclienteFacade extends AbstractFacade<Detalleplancliente> {
    @PersistenceContext(unitName = "PaginaTusAnunciosPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public DetalleplanclienteFacade() {
        super(Detalleplancliente.class);
    }
    
}
