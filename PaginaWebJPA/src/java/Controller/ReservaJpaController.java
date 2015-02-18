/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Controller.exceptions.IllegalOrphanException;
import Controller.exceptions.NonexistentEntityException;
import Controller.exceptions.PreexistingEntityException;
import Controller.exceptions.RollbackFailureException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Entities.Productoservicio;
import Entities.Detalleusuarioreserva;
import Entities.Reserva;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author Carmen
 */
public class ReservaJpaController implements Serializable {

    public ReservaJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Reserva reserva) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (reserva.getDetalleusuarioreservaList() == null) {
            reserva.setDetalleusuarioreservaList(new ArrayList<Detalleusuarioreserva>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Productoservicio idProductoScio = reserva.getIdProductoScio();
            if (idProductoScio != null) {
                idProductoScio = em.getReference(idProductoScio.getClass(), idProductoScio.getIdProductoScio());
                reserva.setIdProductoScio(idProductoScio);
            }
            List<Detalleusuarioreserva> attachedDetalleusuarioreservaList = new ArrayList<Detalleusuarioreserva>();
            for (Detalleusuarioreserva detalleusuarioreservaListDetalleusuarioreservaToAttach : reserva.getDetalleusuarioreservaList()) {
                detalleusuarioreservaListDetalleusuarioreservaToAttach = em.getReference(detalleusuarioreservaListDetalleusuarioreservaToAttach.getClass(), detalleusuarioreservaListDetalleusuarioreservaToAttach.getIdDetalleUsuarioReserva());
                attachedDetalleusuarioreservaList.add(detalleusuarioreservaListDetalleusuarioreservaToAttach);
            }
            reserva.setDetalleusuarioreservaList(attachedDetalleusuarioreservaList);
            em.persist(reserva);
            if (idProductoScio != null) {
                idProductoScio.getReservaList().add(reserva);
                idProductoScio = em.merge(idProductoScio);
            }
            for (Detalleusuarioreserva detalleusuarioreservaListDetalleusuarioreserva : reserva.getDetalleusuarioreservaList()) {
                Reserva oldIdReservaOfDetalleusuarioreservaListDetalleusuarioreserva = detalleusuarioreservaListDetalleusuarioreserva.getIdReserva();
                detalleusuarioreservaListDetalleusuarioreserva.setIdReserva(reserva);
                detalleusuarioreservaListDetalleusuarioreserva = em.merge(detalleusuarioreservaListDetalleusuarioreserva);
                if (oldIdReservaOfDetalleusuarioreservaListDetalleusuarioreserva != null) {
                    oldIdReservaOfDetalleusuarioreservaListDetalleusuarioreserva.getDetalleusuarioreservaList().remove(detalleusuarioreservaListDetalleusuarioreserva);
                    oldIdReservaOfDetalleusuarioreservaListDetalleusuarioreserva = em.merge(oldIdReservaOfDetalleusuarioreservaListDetalleusuarioreserva);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findReserva(reserva.getIdReserva()) != null) {
                throw new PreexistingEntityException("Reserva " + reserva + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Reserva reserva) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Reserva persistentReserva = em.find(Reserva.class, reserva.getIdReserva());
            Productoservicio idProductoScioOld = persistentReserva.getIdProductoScio();
            Productoservicio idProductoScioNew = reserva.getIdProductoScio();
            List<Detalleusuarioreserva> detalleusuarioreservaListOld = persistentReserva.getDetalleusuarioreservaList();
            List<Detalleusuarioreserva> detalleusuarioreservaListNew = reserva.getDetalleusuarioreservaList();
            List<String> illegalOrphanMessages = null;
            for (Detalleusuarioreserva detalleusuarioreservaListOldDetalleusuarioreserva : detalleusuarioreservaListOld) {
                if (!detalleusuarioreservaListNew.contains(detalleusuarioreservaListOldDetalleusuarioreserva)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Detalleusuarioreserva " + detalleusuarioreservaListOldDetalleusuarioreserva + " since its idReserva field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idProductoScioNew != null) {
                idProductoScioNew = em.getReference(idProductoScioNew.getClass(), idProductoScioNew.getIdProductoScio());
                reserva.setIdProductoScio(idProductoScioNew);
            }
            List<Detalleusuarioreserva> attachedDetalleusuarioreservaListNew = new ArrayList<Detalleusuarioreserva>();
            for (Detalleusuarioreserva detalleusuarioreservaListNewDetalleusuarioreservaToAttach : detalleusuarioreservaListNew) {
                detalleusuarioreservaListNewDetalleusuarioreservaToAttach = em.getReference(detalleusuarioreservaListNewDetalleusuarioreservaToAttach.getClass(), detalleusuarioreservaListNewDetalleusuarioreservaToAttach.getIdDetalleUsuarioReserva());
                attachedDetalleusuarioreservaListNew.add(detalleusuarioreservaListNewDetalleusuarioreservaToAttach);
            }
            detalleusuarioreservaListNew = attachedDetalleusuarioreservaListNew;
            reserva.setDetalleusuarioreservaList(detalleusuarioreservaListNew);
            reserva = em.merge(reserva);
            if (idProductoScioOld != null && !idProductoScioOld.equals(idProductoScioNew)) {
                idProductoScioOld.getReservaList().remove(reserva);
                idProductoScioOld = em.merge(idProductoScioOld);
            }
            if (idProductoScioNew != null && !idProductoScioNew.equals(idProductoScioOld)) {
                idProductoScioNew.getReservaList().add(reserva);
                idProductoScioNew = em.merge(idProductoScioNew);
            }
            for (Detalleusuarioreserva detalleusuarioreservaListNewDetalleusuarioreserva : detalleusuarioreservaListNew) {
                if (!detalleusuarioreservaListOld.contains(detalleusuarioreservaListNewDetalleusuarioreserva)) {
                    Reserva oldIdReservaOfDetalleusuarioreservaListNewDetalleusuarioreserva = detalleusuarioreservaListNewDetalleusuarioreserva.getIdReserva();
                    detalleusuarioreservaListNewDetalleusuarioreserva.setIdReserva(reserva);
                    detalleusuarioreservaListNewDetalleusuarioreserva = em.merge(detalleusuarioreservaListNewDetalleusuarioreserva);
                    if (oldIdReservaOfDetalleusuarioreservaListNewDetalleusuarioreserva != null && !oldIdReservaOfDetalleusuarioreservaListNewDetalleusuarioreserva.equals(reserva)) {
                        oldIdReservaOfDetalleusuarioreservaListNewDetalleusuarioreserva.getDetalleusuarioreservaList().remove(detalleusuarioreservaListNewDetalleusuarioreserva);
                        oldIdReservaOfDetalleusuarioreservaListNewDetalleusuarioreserva = em.merge(oldIdReservaOfDetalleusuarioreservaListNewDetalleusuarioreserva);
                    }
                }
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
                Integer id = reserva.getIdReserva();
                if (findReserva(id) == null) {
                    throw new NonexistentEntityException("The reserva with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Reserva reserva;
            try {
                reserva = em.getReference(Reserva.class, id);
                reserva.getIdReserva();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The reserva with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Detalleusuarioreserva> detalleusuarioreservaListOrphanCheck = reserva.getDetalleusuarioreservaList();
            for (Detalleusuarioreserva detalleusuarioreservaListOrphanCheckDetalleusuarioreserva : detalleusuarioreservaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Reserva (" + reserva + ") cannot be destroyed since the Detalleusuarioreserva " + detalleusuarioreservaListOrphanCheckDetalleusuarioreserva + " in its detalleusuarioreservaList field has a non-nullable idReserva field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Productoservicio idProductoScio = reserva.getIdProductoScio();
            if (idProductoScio != null) {
                idProductoScio.getReservaList().remove(reserva);
                idProductoScio = em.merge(idProductoScio);
            }
            em.remove(reserva);
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

    public List<Reserva> findReservaEntities() {
        return findReservaEntities(true, -1, -1);
    }

    public List<Reserva> findReservaEntities(int maxResults, int firstResult) {
        return findReservaEntities(false, maxResults, firstResult);
    }

    private List<Reserva> findReservaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Reserva.class));
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

    public Reserva findReserva(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Reserva.class, id);
        } finally {
            em.close();
        }
    }

    public int getReservaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Reserva> rt = cq.from(Reserva.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
