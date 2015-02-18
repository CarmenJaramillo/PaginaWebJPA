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
import Entities.Detalleusuariocategoria;
import java.util.ArrayList;
import java.util.List;
import Entities.Productoservicio;
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
        if (categoria.getDetalleusuariocategoriaList() == null) {
            categoria.setDetalleusuariocategoriaList(new ArrayList<Detalleusuariocategoria>());
        }
        if (categoria.getProductoservicioList() == null) {
            categoria.setProductoservicioList(new ArrayList<Productoservicio>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            List<Detalleusuariocategoria> attachedDetalleusuariocategoriaList = new ArrayList<Detalleusuariocategoria>();
            for (Detalleusuariocategoria detalleusuariocategoriaListDetalleusuariocategoriaToAttach : categoria.getDetalleusuariocategoriaList()) {
                detalleusuariocategoriaListDetalleusuariocategoriaToAttach = em.getReference(detalleusuariocategoriaListDetalleusuariocategoriaToAttach.getClass(), detalleusuariocategoriaListDetalleusuariocategoriaToAttach.getDetalleUsuarioCategoria());
                attachedDetalleusuariocategoriaList.add(detalleusuariocategoriaListDetalleusuariocategoriaToAttach);
            }
            categoria.setDetalleusuariocategoriaList(attachedDetalleusuariocategoriaList);
            List<Productoservicio> attachedProductoservicioList = new ArrayList<Productoservicio>();
            for (Productoservicio productoservicioListProductoservicioToAttach : categoria.getProductoservicioList()) {
                productoservicioListProductoservicioToAttach = em.getReference(productoservicioListProductoservicioToAttach.getClass(), productoservicioListProductoservicioToAttach.getIdProductoScio());
                attachedProductoservicioList.add(productoservicioListProductoservicioToAttach);
            }
            categoria.setProductoservicioList(attachedProductoservicioList);
            em.persist(categoria);
            for (Detalleusuariocategoria detalleusuariocategoriaListDetalleusuariocategoria : categoria.getDetalleusuariocategoriaList()) {
                Categoria oldIdCategoriaOfDetalleusuariocategoriaListDetalleusuariocategoria = detalleusuariocategoriaListDetalleusuariocategoria.getIdCategoria();
                detalleusuariocategoriaListDetalleusuariocategoria.setIdCategoria(categoria);
                detalleusuariocategoriaListDetalleusuariocategoria = em.merge(detalleusuariocategoriaListDetalleusuariocategoria);
                if (oldIdCategoriaOfDetalleusuariocategoriaListDetalleusuariocategoria != null) {
                    oldIdCategoriaOfDetalleusuariocategoriaListDetalleusuariocategoria.getDetalleusuariocategoriaList().remove(detalleusuariocategoriaListDetalleusuariocategoria);
                    oldIdCategoriaOfDetalleusuariocategoriaListDetalleusuariocategoria = em.merge(oldIdCategoriaOfDetalleusuariocategoriaListDetalleusuariocategoria);
                }
            }
            for (Productoservicio productoservicioListProductoservicio : categoria.getProductoservicioList()) {
                Categoria oldIdCategoriaOfProductoservicioListProductoservicio = productoservicioListProductoservicio.getIdCategoria();
                productoservicioListProductoservicio.setIdCategoria(categoria);
                productoservicioListProductoservicio = em.merge(productoservicioListProductoservicio);
                if (oldIdCategoriaOfProductoservicioListProductoservicio != null) {
                    oldIdCategoriaOfProductoservicioListProductoservicio.getProductoservicioList().remove(productoservicioListProductoservicio);
                    oldIdCategoriaOfProductoservicioListProductoservicio = em.merge(oldIdCategoriaOfProductoservicioListProductoservicio);
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
            List<Detalleusuariocategoria> detalleusuariocategoriaListOld = persistentCategoria.getDetalleusuariocategoriaList();
            List<Detalleusuariocategoria> detalleusuariocategoriaListNew = categoria.getDetalleusuariocategoriaList();
            List<Productoservicio> productoservicioListOld = persistentCategoria.getProductoservicioList();
            List<Productoservicio> productoservicioListNew = categoria.getProductoservicioList();
            List<String> illegalOrphanMessages = null;
            for (Detalleusuariocategoria detalleusuariocategoriaListOldDetalleusuariocategoria : detalleusuariocategoriaListOld) {
                if (!detalleusuariocategoriaListNew.contains(detalleusuariocategoriaListOldDetalleusuariocategoria)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Detalleusuariocategoria " + detalleusuariocategoriaListOldDetalleusuariocategoria + " since its idCategoria field is not nullable.");
                }
            }
            for (Productoservicio productoservicioListOldProductoservicio : productoservicioListOld) {
                if (!productoservicioListNew.contains(productoservicioListOldProductoservicio)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Productoservicio " + productoservicioListOldProductoservicio + " since its idCategoria field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Detalleusuariocategoria> attachedDetalleusuariocategoriaListNew = new ArrayList<Detalleusuariocategoria>();
            for (Detalleusuariocategoria detalleusuariocategoriaListNewDetalleusuariocategoriaToAttach : detalleusuariocategoriaListNew) {
                detalleusuariocategoriaListNewDetalleusuariocategoriaToAttach = em.getReference(detalleusuariocategoriaListNewDetalleusuariocategoriaToAttach.getClass(), detalleusuariocategoriaListNewDetalleusuariocategoriaToAttach.getDetalleUsuarioCategoria());
                attachedDetalleusuariocategoriaListNew.add(detalleusuariocategoriaListNewDetalleusuariocategoriaToAttach);
            }
            detalleusuariocategoriaListNew = attachedDetalleusuariocategoriaListNew;
            categoria.setDetalleusuariocategoriaList(detalleusuariocategoriaListNew);
            List<Productoservicio> attachedProductoservicioListNew = new ArrayList<Productoservicio>();
            for (Productoservicio productoservicioListNewProductoservicioToAttach : productoservicioListNew) {
                productoservicioListNewProductoservicioToAttach = em.getReference(productoservicioListNewProductoservicioToAttach.getClass(), productoservicioListNewProductoservicioToAttach.getIdProductoScio());
                attachedProductoservicioListNew.add(productoservicioListNewProductoservicioToAttach);
            }
            productoservicioListNew = attachedProductoservicioListNew;
            categoria.setProductoservicioList(productoservicioListNew);
            categoria = em.merge(categoria);
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
            for (Productoservicio productoservicioListNewProductoservicio : productoservicioListNew) {
                if (!productoservicioListOld.contains(productoservicioListNewProductoservicio)) {
                    Categoria oldIdCategoriaOfProductoservicioListNewProductoservicio = productoservicioListNewProductoservicio.getIdCategoria();
                    productoservicioListNewProductoservicio.setIdCategoria(categoria);
                    productoservicioListNewProductoservicio = em.merge(productoservicioListNewProductoservicio);
                    if (oldIdCategoriaOfProductoservicioListNewProductoservicio != null && !oldIdCategoriaOfProductoservicioListNewProductoservicio.equals(categoria)) {
                        oldIdCategoriaOfProductoservicioListNewProductoservicio.getProductoservicioList().remove(productoservicioListNewProductoservicio);
                        oldIdCategoriaOfProductoservicioListNewProductoservicio = em.merge(oldIdCategoriaOfProductoservicioListNewProductoservicio);
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
            List<Detalleusuariocategoria> detalleusuariocategoriaListOrphanCheck = categoria.getDetalleusuariocategoriaList();
            for (Detalleusuariocategoria detalleusuariocategoriaListOrphanCheckDetalleusuariocategoria : detalleusuariocategoriaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Categoria (" + categoria + ") cannot be destroyed since the Detalleusuariocategoria " + detalleusuariocategoriaListOrphanCheckDetalleusuariocategoria + " in its detalleusuariocategoriaList field has a non-nullable idCategoria field.");
            }
            List<Productoservicio> productoservicioListOrphanCheck = categoria.getProductoservicioList();
            for (Productoservicio productoservicioListOrphanCheckProductoservicio : productoservicioListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Categoria (" + categoria + ") cannot be destroyed since the Productoservicio " + productoservicioListOrphanCheckProductoservicio + " in its productoservicioList field has a non-nullable idCategoria field.");
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
