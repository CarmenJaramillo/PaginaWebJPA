/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Controller.exceptions.NonexistentEntityException;
import Controller.exceptions.PreexistingEntityException;
import Controller.exceptions.RollbackFailureException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Entities.Cliente;
import Entities.Detalleclienteproducto;
import Entities.ProductoServicio;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author Carmen
 */
public class DetalleclienteproductoJpaController implements Serializable {

    public DetalleclienteproductoJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Detalleclienteproducto detalleclienteproducto) throws PreexistingEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Cliente idCliente = detalleclienteproducto.getIdCliente();
            if (idCliente != null) {
                idCliente = em.getReference(idCliente.getClass(), idCliente.getIdCliente());
                detalleclienteproducto.setIdCliente(idCliente);
            }
            ProductoServicio idProductoScio = detalleclienteproducto.getIdProductoScio();
            if (idProductoScio != null) {
                idProductoScio = em.getReference(idProductoScio.getClass(), idProductoScio.getIdProductoScio());
                detalleclienteproducto.setIdProductoScio(idProductoScio);
            }
            em.persist(detalleclienteproducto);
            if (idCliente != null) {
                idCliente.getDetalleclienteproductoList().add(detalleclienteproducto);
                idCliente = em.merge(idCliente);
            }
            if (idProductoScio != null) {
                idProductoScio.getDetalleclienteproductoList().add(detalleclienteproducto);
                idProductoScio = em.merge(idProductoScio);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findDetalleclienteproducto(detalleclienteproducto.getIdDetalleClienteProducto()) != null) {
                throw new PreexistingEntityException("Detalleclienteproducto " + detalleclienteproducto + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Detalleclienteproducto detalleclienteproducto) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Detalleclienteproducto persistentDetalleclienteproducto = em.find(Detalleclienteproducto.class, detalleclienteproducto.getIdDetalleClienteProducto());
            Cliente idClienteOld = persistentDetalleclienteproducto.getIdCliente();
            Cliente idClienteNew = detalleclienteproducto.getIdCliente();
            ProductoServicio idProductoScioOld = persistentDetalleclienteproducto.getIdProductoScio();
            ProductoServicio idProductoScioNew = detalleclienteproducto.getIdProductoScio();
            if (idClienteNew != null) {
                idClienteNew = em.getReference(idClienteNew.getClass(), idClienteNew.getIdCliente());
                detalleclienteproducto.setIdCliente(idClienteNew);
            }
            if (idProductoScioNew != null) {
                idProductoScioNew = em.getReference(idProductoScioNew.getClass(), idProductoScioNew.getIdProductoScio());
                detalleclienteproducto.setIdProductoScio(idProductoScioNew);
            }
            detalleclienteproducto = em.merge(detalleclienteproducto);
            if (idClienteOld != null && !idClienteOld.equals(idClienteNew)) {
                idClienteOld.getDetalleclienteproductoList().remove(detalleclienteproducto);
                idClienteOld = em.merge(idClienteOld);
            }
            if (idClienteNew != null && !idClienteNew.equals(idClienteOld)) {
                idClienteNew.getDetalleclienteproductoList().add(detalleclienteproducto);
                idClienteNew = em.merge(idClienteNew);
            }
            if (idProductoScioOld != null && !idProductoScioOld.equals(idProductoScioNew)) {
                idProductoScioOld.getDetalleclienteproductoList().remove(detalleclienteproducto);
                idProductoScioOld = em.merge(idProductoScioOld);
            }
            if (idProductoScioNew != null && !idProductoScioNew.equals(idProductoScioOld)) {
                idProductoScioNew.getDetalleclienteproductoList().add(detalleclienteproducto);
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
                Integer id = detalleclienteproducto.getIdDetalleClienteProducto();
                if (findDetalleclienteproducto(id) == null) {
                    throw new NonexistentEntityException("The detalleclienteproducto with id " + id + " no longer exists.");
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
            Detalleclienteproducto detalleclienteproducto;
            try {
                detalleclienteproducto = em.getReference(Detalleclienteproducto.class, id);
                detalleclienteproducto.getIdDetalleClienteProducto();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The detalleclienteproducto with id " + id + " no longer exists.", enfe);
            }
            Cliente idCliente = detalleclienteproducto.getIdCliente();
            if (idCliente != null) {
                idCliente.getDetalleclienteproductoList().remove(detalleclienteproducto);
                idCliente = em.merge(idCliente);
            }
            ProductoServicio idProductoScio = detalleclienteproducto.getIdProductoScio();
            if (idProductoScio != null) {
                idProductoScio.getDetalleclienteproductoList().remove(detalleclienteproducto);
                idProductoScio = em.merge(idProductoScio);
            }
            em.remove(detalleclienteproducto);
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

    public List<Detalleclienteproducto> findDetalleclienteproductoEntities() {
        return findDetalleclienteproductoEntities(true, -1, -1);
    }

    public List<Detalleclienteproducto> findDetalleclienteproductoEntities(int maxResults, int firstResult) {
        return findDetalleclienteproductoEntities(false, maxResults, firstResult);
    }

    private List<Detalleclienteproducto> findDetalleclienteproductoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Detalleclienteproducto.class));
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

    public Detalleclienteproducto findDetalleclienteproducto(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Detalleclienteproducto.class, id);
        } finally {
            em.close();
        }
    }

    public int getDetalleclienteproductoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Detalleclienteproducto> rt = cq.from(Detalleclienteproducto.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
