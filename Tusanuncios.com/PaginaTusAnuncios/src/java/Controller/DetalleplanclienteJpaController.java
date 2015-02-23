/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Controller.exceptions.NonexistentEntityException;
import Controller.exceptions.RollbackFailureException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Entities.Cliente;
import Entities.Detalleplancliente;
import Entities.Plancliente;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author Carmen
 */
public class DetalleplanclienteJpaController implements Serializable {

    public DetalleplanclienteJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Detalleplancliente detalleplancliente) throws RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Cliente idCliente = detalleplancliente.getIdCliente();
            if (idCliente != null) {
                idCliente = em.getReference(idCliente.getClass(), idCliente.getIdCliente());
                detalleplancliente.setIdCliente(idCliente);
            }
            Plancliente idNoplan = detalleplancliente.getIdNoplan();
            if (idNoplan != null) {
                idNoplan = em.getReference(idNoplan.getClass(), idNoplan.getIdNoPlan());
                detalleplancliente.setIdNoplan(idNoplan);
            }
            em.persist(detalleplancliente);
            if (idCliente != null) {
                idCliente.getDetalleplanclienteList().add(detalleplancliente);
                idCliente = em.merge(idCliente);
            }
            if (idNoplan != null) {
                idNoplan.getDetalleplanclienteList().add(detalleplancliente);
                idNoplan = em.merge(idNoplan);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Detalleplancliente detalleplancliente) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Detalleplancliente persistentDetalleplancliente = em.find(Detalleplancliente.class, detalleplancliente.getIdDetallePlanCliente());
            Cliente idClienteOld = persistentDetalleplancliente.getIdCliente();
            Cliente idClienteNew = detalleplancliente.getIdCliente();
            Plancliente idNoplanOld = persistentDetalleplancliente.getIdNoplan();
            Plancliente idNoplanNew = detalleplancliente.getIdNoplan();
            if (idClienteNew != null) {
                idClienteNew = em.getReference(idClienteNew.getClass(), idClienteNew.getIdCliente());
                detalleplancliente.setIdCliente(idClienteNew);
            }
            if (idNoplanNew != null) {
                idNoplanNew = em.getReference(idNoplanNew.getClass(), idNoplanNew.getIdNoPlan());
                detalleplancliente.setIdNoplan(idNoplanNew);
            }
            detalleplancliente = em.merge(detalleplancliente);
            if (idClienteOld != null && !idClienteOld.equals(idClienteNew)) {
                idClienteOld.getDetalleplanclienteList().remove(detalleplancliente);
                idClienteOld = em.merge(idClienteOld);
            }
            if (idClienteNew != null && !idClienteNew.equals(idClienteOld)) {
                idClienteNew.getDetalleplanclienteList().add(detalleplancliente);
                idClienteNew = em.merge(idClienteNew);
            }
            if (idNoplanOld != null && !idNoplanOld.equals(idNoplanNew)) {
                idNoplanOld.getDetalleplanclienteList().remove(detalleplancliente);
                idNoplanOld = em.merge(idNoplanOld);
            }
            if (idNoplanNew != null && !idNoplanNew.equals(idNoplanOld)) {
                idNoplanNew.getDetalleplanclienteList().add(detalleplancliente);
                idNoplanNew = em.merge(idNoplanNew);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = detalleplancliente.getIdDetallePlanCliente();
                if (findDetalleplancliente(id) == null) {
                    throw new NonexistentEntityException("The detalleplancliente with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Detalleplancliente detalleplancliente;
            try {
                detalleplancliente = em.getReference(Detalleplancliente.class, id);
                detalleplancliente.getIdDetallePlanCliente();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The detalleplancliente with id " + id + " no longer exists.", enfe);
            }
            Cliente idCliente = detalleplancliente.getIdCliente();
            if (idCliente != null) {
                idCliente.getDetalleplanclienteList().remove(detalleplancliente);
                idCliente = em.merge(idCliente);
            }
            Plancliente idNoplan = detalleplancliente.getIdNoplan();
            if (idNoplan != null) {
                idNoplan.getDetalleplanclienteList().remove(detalleplancliente);
                idNoplan = em.merge(idNoplan);
            }
            em.remove(detalleplancliente);
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Detalleplancliente> findDetalleplanclienteEntities() {
        return findDetalleplanclienteEntities(true, -1, -1);
    }

    public List<Detalleplancliente> findDetalleplanclienteEntities(int maxResults, int firstResult) {
        return findDetalleplanclienteEntities(false, maxResults, firstResult);
    }

    private List<Detalleplancliente> findDetalleplanclienteEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Detalleplancliente.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Detalleplancliente findDetalleplancliente(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Detalleplancliente.class, id);
        } finally {
            em.close();
        }
    }

    public int getDetalleplanclienteCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Detalleplancliente> rt = cq.from(Detalleplancliente.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
