/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import Entities.Detalleusuariocategoria;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Carmen
 */
@Stateless
public class DetalleusuariocategoriaFacade extends AbstractFacade<Detalleusuariocategoria> {
    @PersistenceContext(unitName = "PaginaTusAnunciosPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public DetalleusuariocategoriaFacade() {
        super(Detalleusuariocategoria.class);
    }
    
}
