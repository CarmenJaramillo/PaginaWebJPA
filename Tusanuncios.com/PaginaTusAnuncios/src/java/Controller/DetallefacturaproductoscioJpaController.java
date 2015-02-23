/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Controller.exceptions.NonexistentEntityException;
import Controller.exceptions.RollbackFailureException;
import Entities.Detallefacturaproductoscio;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Entities.Factura;
import Entities.ProductoServicio;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author Carmen
 */
public class DetallefacturaproductoscioJpaController implements Serializable {

    public DetallefacturaproductoscioJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Detallefacturaproductoscio detallefacturaproductoscio) throws RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Factura idFactura = detallefacturaproductoscio.getIdFactura();
            if (idFactura != null) {
                idFactura = em.getReference(idFactura.getClass(), idFactura.getIdFactura());
                detallefacturaproductoscio.setIdFactura(idFactura);
            }
            ProductoServicio idProductoScio = detallefacturaproductoscio.getIdProductoScio();
            if (idProductoScio != null) {
                idProductoScio = em.getReference(idProductoScio.getClass(), idProductoScio.getIdProductoScio());
                detallefacturaproductoscio.setIdProductoScio(idProductoScio);
            }
            em.persist(detallefacturaproductoscio);
            if (idFactura != null) {
                idFactura.getDetallefacturaproductoscioList().add(detallefacturaproductoscio);
                idFactura = em.merge(idFactura);
            }
            if (idProductoScio != null) {
                idProductoScio.getDetallefacturaproductoscioList().add(detallefacturaproductoscio);
                idProductoScio = em.merge(idProductoScio);
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

    public void edit(Detallefacturaproductoscio detallefacturaproductoscio) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Detallefacturaproductoscio persistentDetallefacturaproductoscio = em.find(Detallefacturaproductoscio.class, detallefacturaproductoscio.getIdDetalleFacturaProductoScio());
            Factura idFacturaOld = persistentDetallefacturaproductoscio.getIdFactura();
            Factura idFacturaNew = detallefacturaproductoscio.getIdFactura();
            ProductoServicio idProductoScioOld = persistentDetallefacturaproductoscio.getIdProductoScio();
            ProductoServicio idProductoScioNew = detallefacturaproductoscio.getIdProductoScio();
            if (idFacturaNew != null) {
                idFacturaNew = em.getReference(idFacturaNew.getClass(), idFacturaNew.getIdFactura());
                detallefacturaproductoscio.setIdFactura(idFacturaNew);
            }
            if (idProductoScioNew != null) {
                idProductoScioNew = em.getReference(idProductoScioNew.getClass(), idProductoScioNew.getIdProductoScio());
                detallefacturaproductoscio.setIdProductoScio(idProductoScioNew);
            }
            detallefacturaproductoscio = em.merge(detallefacturaproductoscio);
            if (idFacturaOld != null && !idFacturaOld.equals(idFacturaNew)) {
                idFacturaOld.getDetallefacturaproductoscioList().remove(detallefacturaproductoscio);
                idFacturaOld = em.merge(idFacturaOld);
            }
            if (idFacturaNew != null && !idFacturaNew.equals(idFacturaOld)) {
                idFacturaNew.getDetallefacturaproductoscioList().add(detallefacturaproductoscio);
                idFacturaNew = em.merge(idFacturaNew);
            }
            if (idProductoScioOld != null && !idProductoScioOld.equals(idProductoScioNew)) {
                idProductoScioOld.getDetallefacturaproductoscioList().remove(detallefacturaproductoscio);
                idProductoScioOld = em.merge(idProductoScioOld);
            }
            if (idProductoScioNew != null && !idProductoScioNew.equals(idProductoScioOld)) {
                idProductoScioNew.getDetallefacturaproductoscioList().add(detallefacturaproductoscio);
                idProductoScioNew = em.merge(idProductoScioNew);
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
                Integer id = detallefacturaproductoscio.getIdDetalleFacturaProductoScio();
                if (findDetallefacturaproductoscio(id) == null) {
                    throw new NonexistentEntityException("The detallefacturaproductoscio with id " + id + " no longer exists.");
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
            Detallefacturaproductoscio detallefacturaproductoscio;
            try {
                detallefacturaproductoscio = em.getReference(Detallefacturaproductoscio.class, id);
                detallefacturaproductoscio.getIdDetalleFacturaProductoScio();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The detallefacturaproductoscio with id " + id + " no longer exists.", enfe);
            }
            Factura idFactura = detallefacturaproductoscio.getIdFactura();
            if (idFactura != null) {
                idFactura.getDetallefacturaproductoscioList().remove(detallefacturaproductoscio);
                idFactura = em.merge(idFactura);
            }
            ProductoServicio idProductoScio = detallefacturaproductoscio.getIdProductoScio();
            if (idProductoScio != null) {
                idProductoScio.getDetallefacturaproductoscioList().remove(detallefacturaproductoscio);
                idProductoScio = em.merge(idProductoScio);
            }
            em.remove(detallefacturaproductoscio);
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

    public List<Detallefacturaproductoscio> findDetallefacturaproductoscioEntities() {
        return findDetallefacturaproductoscioEntities(true, -1, -1);
    }

    public List<Detallefacturaproductoscio> findDetallefacturaproductoscioEntities(int maxResults, int firstResult) {
        return findDetallefacturaproductoscioEntities(false, maxResults, firstResult);
    }

    private List<Detallefacturaproductoscio> findDetallefacturaproductoscioEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Detallefacturaproductoscio.class));
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

    public Detallefacturaproductoscio findDetallefacturaproductoscio(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Detallefacturaproductoscio.class, id);
        } finally {
            em.close();
        }
    }

    public int getDetallefacturaproductoscioCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Detallefacturaproductoscio> rt = cq.from(Detallefacturaproductoscio.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
