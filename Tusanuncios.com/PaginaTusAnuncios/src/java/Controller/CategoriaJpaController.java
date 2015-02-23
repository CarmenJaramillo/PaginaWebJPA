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
import Entities.Categoria;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Entities.ProductoServicio;
import java.util.ArrayList;
import java.util.List;
import Entities.Detalleusuariocategoria;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author Carmen
 */
public class CategoriaJpaController implements Serializable {

    public CategoriaJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Categoria categoria) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (categoria.getProductoServicioList() == null) {
            categoria.setProductoServicioList(new ArrayList<ProductoServicio>());
        }
        if (categoria.getDetalleusuariocategoriaList() == null) {
            categoria.setDetalleusuariocategoriaList(new ArrayList<Detalleusuariocategoria>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            List<ProductoServicio> attachedProductoServicioList = new ArrayList<ProductoServicio>();
            for (ProductoServicio productoServicioListProductoServicioToAttach : categoria.getProductoServicioList()) {
                productoServicioListProductoServicioToAttach = em.getReference(productoServicioListProductoServicioToAttach.getClass(), productoServicioListProductoServicioToAttach.getIdProductoScio());
                attachedProductoServicioList.add(productoServicioListProductoServicioToAttach);
            }
            categoria.setProductoServicioList(attachedProductoServicioList);
            List<Detalleusuariocategoria> attachedDetalleusuariocategoriaList = new ArrayList<Detalleusuariocategoria>();
            for (Detalleusuariocategoria detalleusuariocategoriaListDetalleusuariocategoriaToAttach : categoria.getDetalleusuariocategoriaList()) {
                detalleusuariocategoriaListDetalleusuariocategoriaToAttach = em.getReference(detalleusuariocategoriaListDetalleusuariocategoriaToAttach.getClass(), detalleusuariocategoriaListDetalleusuariocategoriaToAttach.getDetalleUsuarioCategoria());
                attachedDetalleusuariocategoriaList.add(detalleusuariocategoriaListDetalleusuariocategoriaToAttach);
            }
            categoria.setDetalleusuariocategoriaList(attachedDetalleusuariocategoriaList);
            em.persist(categoria);
            for (ProductoServicio productoServicioListProductoServicio : categoria.getProductoServicioList()) {
                Categoria oldIdCategoriaOfProductoServicioListProductoServicio = productoServicioListProductoServicio.getIdCategoria();
                productoServicioListProductoServicio.setIdCategoria(categoria);
                productoServicioListProductoServicio = em.merge(productoServicioListProductoServicio);
                if (oldIdCategoriaOfProductoServicioListProductoServicio != null) {
                    oldIdCategoriaOfProductoServicioListProductoServicio.getProductoServicioList().remove(productoServicioListProductoServicio);
                    oldIdCategoriaOfProductoServicioListProductoServicio = em.merge(oldIdCategoriaOfProductoServicioListProductoServicio);
                }
            }
            for (Detalleusuariocategoria detalleusuariocategoriaListDetalleusuariocategoria : categoria.getDetalleusuariocategoriaList()) {
                Categoria oldIdCategoriaOfDetalleusuariocategoriaListDetalleusuariocategoria = detalleusuariocategoriaListDetalleusuariocategoria.getIdCategoria();
                detalleusuariocategoriaListDetalleusuariocategoria.setIdCategoria(categoria);
                detalleusuariocategoriaListDetalleusuariocategoria = em.merge(detalleusuariocategoriaListDetalleusuariocategoria);
                if (oldIdCategoriaOfDetalleusuariocategoriaListDetalleusuariocategoria != null) {
                    oldIdCategoriaOfDetalleusuariocategoriaListDetalleusuariocategoria.getDetalleusuariocategoriaList().remove(detalleusuariocategoriaListDetalleusuariocategoria);
                    oldIdCategoriaOfDetalleusuariocategoriaListDetalleusuariocategoria = em.merge(oldIdCategoriaOfDetalleusuariocategoriaListDetalleusuariocategoria);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findCategoria(categoria.getIdCategoria()) != null) {
                throw new PreexistingEntityException("Categoria " + categoria + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Categoria categoria) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Categoria persistentCategoria = em.find(Categoria.class, categoria.getIdCategoria());
            List<ProductoServicio> productoServicioListOld = persistentCategoria.getProductoServicioList();
            List<ProductoServicio> productoServicioListNew = categoria.getProductoServicioList();
            List<Detalleusuariocategoria> detalleusuariocategoriaListOld = persistentCategoria.getDetalleusuariocategoriaList();
            List<Detalleusuariocategoria> detalleusuariocategoriaListNew = categoria.getDetalleusuariocategoriaList();
            List<String> illegalOrphanMessages = null;
            for (ProductoServicio productoServicioListOldProductoServicio : productoServicioListOld) {
                if (!productoServicioListNew.contains(productoServicioListOldProductoServicio)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain ProductoServicio " + productoServicioListOldProductoServicio + " since its idCategoria field is not nullable.");
                }
            }
            for (Detalleusuariocategoria detalleusuariocategoriaListOldDetalleusuariocategoria : detalleusuariocategoriaListOld) {
                if (!detalleusuariocategoriaListNew.contains(detalleusuariocategoriaListOldDetalleusuariocategoria)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Detalleusuariocategoria " + detalleusuariocategoriaListOldDetalleusuariocategoria + " since its idCategoria field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<ProductoServicio> attachedProductoServicioListNew = new ArrayList<ProductoServicio>();
            for (ProductoServicio productoServicioListNewProductoServicioToAttach : productoServicioListNew) {
                productoServicioListNewProductoServicioToAttach = em.getReference(productoServicioListNewProductoServicioToAttach.getClass(), productoServicioListNewProductoServicioToAttach.getIdProductoScio());
                attachedProductoServicioListNew.add(productoServicioListNewProductoServicioToAttach);
            }
            productoServicioListNew = attachedProductoServicioListNew;
            categoria.setProductoServicioList(productoServicioListNew);
            List<Detalleusuariocategoria> attachedDetalleusuariocategoriaListNew = new ArrayList<Detalleusuariocategoria>();
            for (Detalleusuariocategoria detalleusuariocategoriaListNewDetalleusuariocategoriaToAttach : detalleusuariocategoriaListNew) {
                detalleusuariocategoriaListNewDetalleusuariocategoriaToAttach = em.getReference(detalleusuariocategoriaListNewDetalleusuariocategoriaToAttach.getClass(), detalleusuariocategoriaListNewDetalleusuariocategoriaToAttach.getDetalleUsuarioCategoria());
                attachedDetalleusuariocategoriaListNew.add(detalleusuariocategoriaListNewDetalleusuariocategoriaToAttach);
            }
            detalleusuariocategoriaListNew = attachedDetalleusuariocategoriaListNew;
            categoria.setDetalleusuariocategoriaList(detalleusuariocategoriaListNew);
            categoria = em.merge(categoria);
            for (ProductoServicio productoServicioListNewProductoServicio : productoServicioListNew) {
                if (!productoServicioListOld.contains(productoServicioListNewProductoServicio)) {
                    Categoria oldIdCategoriaOfProductoServicioListNewProductoServicio = productoServicioListNewProductoServicio.getIdCategoria();
                    productoServicioListNewProductoServicio.setIdCategoria(categoria);
                    productoServicioListNewProductoServicio = em.merge(productoServicioListNewProductoServicio);
                    if (oldIdCategoriaOfProductoServicioListNewProductoServicio != null && !oldIdCategoriaOfProductoServicioListNewProductoServicio.equals(categoria)) {
                        oldIdCategoriaOfProductoServicioListNewProductoServicio.getProductoServicioList().remove(productoServicioListNewProductoServicio);
                        oldIdCategoriaOfProductoServicioListNewProductoServicio = em.merge(oldIdCategoriaOfProductoServicioListNewProductoServicio);
                    }
                }
            }
            for (Detalleusuariocategoria detalleusuariocategoriaListNewDetalleusuariocategoria : detalleusuariocategoriaListNew) {
                if (!detalleusuariocategoriaListOld.contains(detalleusuariocategoriaListNewDetalleusuariocategoria)) {
                    Categoria oldIdCategoriaOfDetalleusuariocategoriaListNewDetalleusuariocategoria = detalleusuariocategoriaListNewDetalleusuariocategoria.getIdCategoria();
                    detalleusuariocategoriaListNewDetalleusuariocategoria.setIdCategoria(categoria);
                    detalleusuariocategoriaListNewDetalleusuariocategoria = em.merge(detalleusuariocategoriaListNewDetalleusuariocategoria);
                    if (oldIdCategoriaOfDetalleusuariocategoriaListNewDetalleusuariocategoria != null && !oldIdCategoriaOfDetalleusuariocategoriaListNewDetalleusuariocategoria.equals(categoria)) {
                        oldIdCategoriaOfDetalleusuariocategoriaListNewDetalleusuariocategoria.getDetalleusuariocategoriaList().remove(detalleusuariocategoriaListNewDetalleusuariocategoria);
                        oldIdCategoriaOfDetalleusuariocategoriaListNewDetalleusuariocategoria = em.merge(oldIdCategoriaOfDetalleusuariocategoriaListNewDetalleusuariocategoria);
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
                Integer id = categoria.getIdCategoria();
                if (findCategoria(id) == null) {
                    throw new NonexistentEntityException("The categoria with id " + id + " no longer exists.");
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
            Categoria categoria;
            try {
                categoria = em.getReference(Categoria.class, id);
                categoria.getIdCategoria();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The categoria with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<ProductoServicio> productoServicioListOrphanCheck = categoria.getProductoServicioList();
            for (ProductoServicio productoServicioListOrphanCheckProductoServicio : productoServicioListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Categoria (" + categoria + ") cannot be destroyed since the ProductoServicio " + productoServicioListOrphanCheckProductoServicio + " in its productoServicioList field has a non-nullable idCategoria field.");
            }
            List<Detalleusuariocategoria> detalleusuariocategoriaListOrphanCheck = categoria.getDetalleusuariocategoriaList();
            for (Detalleusuariocategoria detalleusuariocategoriaListOrphanCheckDetalleusuariocategoria : detalleusuariocategoriaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Categoria (" + categoria + ") cannot be destroyed since the Detalleusuariocategoria " + detalleusuariocategoriaListOrphanCheckDetalleusuariocategoria + " in its detalleusuariocategoriaList field has a non-nullable idCategoria field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(categoria);
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

    public List<Categoria> findCategoriaEntities() {
        return findCategoriaEntities(true, -1, -1);
    }

    public List<Categoria> findCategoriaEntities(int maxResults, int firstResult) {
        return findCategoriaEntities(false, maxResults, firstResult);
    }

    private List<Categoria> findCategoriaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Categoria.class));
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

    public Categoria findCategoria(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Categoria.class, id);
        } finally {
            em.close();
        }
    }

    public int getCategoriaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Categoria> rt = cq.from(Categoria.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
