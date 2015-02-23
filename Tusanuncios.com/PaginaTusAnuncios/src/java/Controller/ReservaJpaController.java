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
import Entities.ProductoServicio;
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
            ProductoServicio idProducto = reserva.getIdProducto();
            if (idProducto != null) {
                idProducto = em.getReference(idProducto.getClass(), idProducto.getIdProductoScio());
                reserva.setIdProducto(idProducto);
            }
            List<Detalleusuarioreserva> attachedDetalleusuarioreservaList = new ArrayList<Detalleusuarioreserva>();
            for (Detalleusuarioreserva detalleusuarioreservaListDetalleusuarioreservaToAttach : reserva.getDetalleusuarioreservaList()) {
                detalleusuarioreservaListDetalleusuarioreservaToAttach = em.getReference(detalleusuarioreservaListDetalleusuarioreservaToAttach.getClass(), detalleusuarioreservaListDetalleusuarioreservaToAttach.getIdDetalleUsuarioReserva());
                attachedDetalleusuarioreservaList.add(detalleusuarioreservaListDetalleusuarioreservaToAttach);
            }
            reserva.setDetalleusuarioreservaList(attachedDetalleusuarioreservaList);
            em.persist(reserva);
            if (idProducto != null) {
                idProducto.getReservaList().add(reserva);
                idProducto = em.merge(idProducto);
            }
            for (Detalleusuarioreserva detalleusuarioreservaListDetalleusuarioreserva : reserva.getDetalleusuarioreservaList()) {
                Reserva oldIdreservaOfDetalleusuarioreservaListDetalleusuarioreserva = detalleusuarioreservaListDetalleusuarioreserva.getIdreserva();
                detalleusuarioreservaListDetalleusuarioreserva.setIdreserva(reserva);
                detalleusuarioreservaListDetalleusuarioreserva = em.merge(detalleusuarioreservaListDetalleusuarioreserva);
                if (oldIdreservaOfDetalleusuarioreservaListDetalleusuarioreserva != null) {
                    oldIdreservaOfDetalleusuarioreservaListDetalleusuarioreserva.getDetalleusuarioreservaList().remove(detalleusuarioreservaListDetalleusuarioreserva);
                    oldIdreservaOfDetalleusuarioreservaListDetalleusuarioreserva = em.merge(oldIdreservaOfDetalleusuarioreservaListDetalleusuarioreserva);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findReserva(reserva.getIdreserva()) != null) {
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
            Reserva persistentReserva = em.find(Reserva.class, reserva.getIdreserva());
            ProductoServicio idProductoOld = persistentReserva.getIdProducto();
            ProductoServicio idProductoNew = reserva.getIdProducto();
            List<Detalleusuarioreserva> detalleusuarioreservaListOld = persistentReserva.getDetalleusuarioreservaList();
            List<Detalleusuarioreserva> detalleusuarioreservaListNew = reserva.getDetalleusuarioreservaList();
            List<String> illegalOrphanMessages = null;
            for (Detalleusuarioreserva detalleusuarioreservaListOldDetalleusuarioreserva : detalleusuarioreservaListOld) {
                if (!detalleusuarioreservaListNew.contains(detalleusuarioreservaListOldDetalleusuarioreserva)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Detalleusuarioreserva " + detalleusuarioreservaListOldDetalleusuarioreserva + " since its idreserva field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idProductoNew != null) {
                idProductoNew = em.getReference(idProductoNew.getClass(), idProductoNew.getIdProductoScio());
                reserva.setIdProducto(idProductoNew);
            }
            List<Detalleusuarioreserva> attachedDetalleusuarioreservaListNew = new ArrayList<Detalleusuarioreserva>();
            for (Detalleusuarioreserva detalleusuarioreservaListNewDetalleusuarioreservaToAttach : detalleusuarioreservaListNew) {
                detalleusuarioreservaListNewDetalleusuarioreservaToAttach = em.getReference(detalleusuarioreservaListNewDetalleusuarioreservaToAttach.getClass(), detalleusuarioreservaListNewDetalleusuarioreservaToAttach.getIdDetalleUsuarioReserva());
                attachedDetalleusuarioreservaListNew.add(detalleusuarioreservaListNewDetalleusuarioreservaToAttach);
            }
            detalleusuarioreservaListNew = attachedDetalleusuarioreservaListNew;
            reserva.setDetalleusuarioreservaList(detalleusuarioreservaListNew);
            reserva = em.merge(reserva);
            if (idProductoOld != null && !idProductoOld.equals(idProductoNew)) {
                idProductoOld.getReservaList().remove(reserva);
                idProductoOld = em.merge(idProductoOld);
            }
            if (idProductoNew != null && !idProductoNew.equals(idProductoOld)) {
                idProductoNew.getReservaList().add(reserva);
                idProductoNew = em.merge(idProductoNew);
            }
            for (Detalleusuarioreserva detalleusuarioreservaListNewDetalleusuarioreserva : detalleusuarioreservaListNew) {
                if (!detalleusuarioreservaListOld.contains(detalleusuarioreservaListNewDetalleusuarioreserva)) {
                    Reserva oldIdreservaOfDetalleusuarioreservaListNewDetalleusuarioreserva = detalleusuarioreservaListNewDetalleusuarioreserva.getIdreserva();
                    detalleusuarioreservaListNewDetalleusuarioreserva.setIdreserva(reserva);
                    detalleusuarioreservaListNewDetalleusuarioreserva = em.merge(detalleusuarioreservaListNewDetalleusuarioreserva);
                    if (oldIdreservaOfDetalleusuarioreservaListNewDetalleusuarioreserva != null && !oldIdreservaOfDetalleusuarioreservaListNewDetalleusuarioreserva.equals(reserva)) {
                        oldIdreservaOfDetalleusuarioreservaListNewDetalleusuarioreserva.getDetalleusuarioreservaList().remove(detalleusuarioreservaListNewDetalleusuarioreserva);
                        oldIdreservaOfDetalleusuarioreservaListNewDetalleusuarioreserva = em.merge(oldIdreservaOfDetalleusuarioreservaListNewDetalleusuarioreserva);
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
                Integer id = reserva.getIdreserva();
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
                reserva.getIdreserva();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The reserva with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Detalleusuarioreserva> detalleusuarioreservaListOrphanCheck = reserva.getDetalleusuarioreservaList();
            for (Detalleusuarioreserva detalleusuarioreservaListOrphanCheckDetalleusuarioreserva : detalleusuarioreservaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Reserva (" + reserva + ") cannot be destroyed since the Detalleusuarioreserva " + detalleusuarioreservaListOrphanCheckDetalleusuarioreserva + " in its detalleusuarioreservaList field has a non-nullable idreserva field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            ProductoServicio idProducto = reserva.getIdProducto();
            if (idProducto != null) {
                idProducto.getReservaList().remove(reserva);
                idProducto = em.merge(idProducto);
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
