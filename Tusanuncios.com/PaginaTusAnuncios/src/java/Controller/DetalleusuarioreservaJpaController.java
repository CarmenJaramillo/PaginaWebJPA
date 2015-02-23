/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Controller.exceptions.NonexistentEntityException;
import Controller.exceptions.PreexistingEntityException;
import Controller.exceptions.RollbackFailureException;
import Entities.Detalleusuarioreserva;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Entities.Reserva;
import Entities.Usuario;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author Carmen
 */
public class DetalleusuarioreservaJpaController implements Serializable {

    public DetalleusuarioreservaJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Detalleusuarioreserva detalleusuarioreserva) throws PreexistingEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Reserva idreserva = detalleusuarioreserva.getIdreserva();
            if (idreserva != null) {
                idreserva = em.getReference(idreserva.getClass(), idreserva.getIdreserva());
                detalleusuarioreserva.setIdreserva(idreserva);
            }
            Usuario idusuario = detalleusuarioreserva.getIdusuario();
            if (idusuario != null) {
                idusuario = em.getReference(idusuario.getClass(), idusuario.getIdusuario());
                detalleusuarioreserva.setIdusuario(idusuario);
            }
            em.persist(detalleusuarioreserva);
            if (idreserva != null) {
                idreserva.getDetalleusuarioreservaList().add(detalleusuarioreserva);
                idreserva = em.merge(idreserva);
            }
            if (idusuario != null) {
                idusuario.getDetalleusuarioreservaList().add(detalleusuarioreserva);
                idusuario = em.merge(idusuario);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findDetalleusuarioreserva(detalleusuarioreserva.getIdDetalleUsuarioReserva()) != null) {
                throw new PreexistingEntityException("Detalleusuarioreserva " + detalleusuarioreserva + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Detalleusuarioreserva detalleusuarioreserva) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Detalleusuarioreserva persistentDetalleusuarioreserva = em.find(Detalleusuarioreserva.class, detalleusuarioreserva.getIdDetalleUsuarioReserva());
            Reserva idreservaOld = persistentDetalleusuarioreserva.getIdreserva();
            Reserva idreservaNew = detalleusuarioreserva.getIdreserva();
            Usuario idusuarioOld = persistentDetalleusuarioreserva.getIdusuario();
            Usuario idusuarioNew = detalleusuarioreserva.getIdusuario();
            if (idreservaNew != null) {
                idreservaNew = em.getReference(idreservaNew.getClass(), idreservaNew.getIdreserva());
                detalleusuarioreserva.setIdreserva(idreservaNew);
            }
            if (idusuarioNew != null) {
                idusuarioNew = em.getReference(idusuarioNew.getClass(), idusuarioNew.getIdusuario());
                detalleusuarioreserva.setIdusuario(idusuarioNew);
            }
            detalleusuarioreserva = em.merge(detalleusuarioreserva);
            if (idreservaOld != null && !idreservaOld.equals(idreservaNew)) {
                idreservaOld.getDetalleusuarioreservaList().remove(detalleusuarioreserva);
                idreservaOld = em.merge(idreservaOld);
            }
            if (idreservaNew != null && !idreservaNew.equals(idreservaOld)) {
                idreservaNew.getDetalleusuarioreservaList().add(detalleusuarioreserva);
                idreservaNew = em.merge(idreservaNew);
            }
            if (idusuarioOld != null && !idusuarioOld.equals(idusuarioNew)) {
                idusuarioOld.getDetalleusuarioreservaList().remove(detalleusuarioreserva);
                idusuarioOld = em.merge(idusuarioOld);
            }
            if (idusuarioNew != null && !idusuarioNew.equals(idusuarioOld)) {
                idusuarioNew.getDetalleusuarioreservaList().add(detalleusuarioreserva);
                idusuarioNew = em.merge(idusuarioNew);
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
                Integer id = detalleusuarioreserva.getIdDetalleUsuarioReserva();
                if (findDetalleusuarioreserva(id) == null) {
                    throw new NonexistentEntityException("The detalleusuarioreserva with id " + id + " no longer exists.");
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
            Detalleusuarioreserva detalleusuarioreserva;
            try {
                detalleusuarioreserva = em.getReference(Detalleusuarioreserva.class, id);
                detalleusuarioreserva.getIdDetalleUsuarioReserva();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The detalleusuarioreserva with id " + id + " no longer exists.", enfe);
            }
            Reserva idreserva = detalleusuarioreserva.getIdreserva();
            if (idreserva != null) {
                idreserva.getDetalleusuarioreservaList().remove(detalleusuarioreserva);
                idreserva = em.merge(idreserva);
            }
            Usuario idusuario = detalleusuarioreserva.getIdusuario();
            if (idusuario != null) {
                idusuario.getDetalleusuarioreservaList().remove(detalleusuarioreserva);
                idusuario = em.merge(idusuario);
            }
            em.remove(detalleusuarioreserva);
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

    public List<Detalleusuarioreserva> findDetalleusuarioreservaEntities() {
        return findDetalleusuarioreservaEntities(true, -1, -1);
    }

    public List<Detalleusuarioreserva> findDetalleusuarioreservaEntities(int maxResults, int firstResult) {
        return findDetalleusuarioreservaEntities(false, maxResults, firstResult);
    }

    private List<Detalleusuarioreserva> findDetalleusuarioreservaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Detalleusuarioreserva.class));
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

    public Detalleusuarioreserva findDetalleusuarioreserva(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Detalleusuarioreserva.class, id);
        } finally {
            em.close();
        }
    }

    public int getDetalleusuarioreservaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Detalleusuarioreserva> rt = cq.from(Detalleusuarioreserva.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
