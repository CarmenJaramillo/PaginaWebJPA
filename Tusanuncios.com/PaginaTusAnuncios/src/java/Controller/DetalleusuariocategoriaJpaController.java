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
import Entities.Usuario;
import Entities.Categoria;
import Entities.Detalleusuariocategoria;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author Carmen
 */
public class DetalleusuariocategoriaJpaController implements Serializable {

    public DetalleusuariocategoriaJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Detalleusuariocategoria detalleusuariocategoria) throws PreexistingEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Usuario idusuario = detalleusuariocategoria.getIdusuario();
            if (idusuario != null) {
                idusuario = em.getReference(idusuario.getClass(), idusuario.getIdusuario());
                detalleusuariocategoria.setIdusuario(idusuario);
            }
            Categoria idCategoria = detalleusuariocategoria.getIdCategoria();
            if (idCategoria != null) {
                idCategoria = em.getReference(idCategoria.getClass(), idCategoria.getIdCategoria());
                detalleusuariocategoria.setIdCategoria(idCategoria);
            }
            em.persist(detalleusuariocategoria);
            if (idusuario != null) {
                idusuario.getDetalleusuariocategoriaList().add(detalleusuariocategoria);
                idusuario = em.merge(idusuario);
            }
            if (idCategoria != null) {
                idCategoria.getDetalleusuariocategoriaList().add(detalleusuariocategoria);
                idCategoria = em.merge(idCategoria);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findDetalleusuariocategoria(detalleusuariocategoria.getDetalleUsuarioCategoria()) != null) {
                throw new PreexistingEntityException("Detalleusuariocategoria " + detalleusuariocategoria + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Detalleusuariocategoria detalleusuariocategoria) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Detalleusuariocategoria persistentDetalleusuariocategoria = em.find(Detalleusuariocategoria.class, detalleusuariocategoria.getDetalleUsuarioCategoria());
            Usuario idusuarioOld = persistentDetalleusuariocategoria.getIdusuario();
            Usuario idusuarioNew = detalleusuariocategoria.getIdusuario();
            Categoria idCategoriaOld = persistentDetalleusuariocategoria.getIdCategoria();
            Categoria idCategoriaNew = detalleusuariocategoria.getIdCategoria();
            if (idusuarioNew != null) {
                idusuarioNew = em.getReference(idusuarioNew.getClass(), idusuarioNew.getIdusuario());
                detalleusuariocategoria.setIdusuario(idusuarioNew);
            }
            if (idCategoriaNew != null) {
                idCategoriaNew = em.getReference(idCategoriaNew.getClass(), idCategoriaNew.getIdCategoria());
                detalleusuariocategoria.setIdCategoria(idCategoriaNew);
            }
            detalleusuariocategoria = em.merge(detalleusuariocategoria);
            if (idusuarioOld != null && !idusuarioOld.equals(idusuarioNew)) {
                idusuarioOld.getDetalleusuariocategoriaList().remove(detalleusuariocategoria);
                idusuarioOld = em.merge(idusuarioOld);
            }
            if (idusuarioNew != null && !idusuarioNew.equals(idusuarioOld)) {
                idusuarioNew.getDetalleusuariocategoriaList().add(detalleusuariocategoria);
                idusuarioNew = em.merge(idusuarioNew);
            }
            if (idCategoriaOld != null && !idCategoriaOld.equals(idCategoriaNew)) {
                idCategoriaOld.getDetalleusuariocategoriaList().remove(detalleusuariocategoria);
                idCategoriaOld = em.merge(idCategoriaOld);
            }
            if (idCategoriaNew != null && !idCategoriaNew.equals(idCategoriaOld)) {
                idCategoriaNew.getDetalleusuariocategoriaList().add(detalleusuariocategoria);
                idCategoriaNew = em.merge(idCategoriaNew);
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
                Integer id = detalleusuariocategoria.getDetalleUsuarioCategoria();
                if (findDetalleusuariocategoria(id) == null) {
                    throw new NonexistentEntityException("The detalleusuariocategoria with id " + id + " no longer exists.");
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
            Detalleusuariocategoria detalleusuariocategoria;
            try {
                detalleusuariocategoria = em.getReference(Detalleusuariocategoria.class, id);
                detalleusuariocategoria.getDetalleUsuarioCategoria();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The detalleusuariocategoria with id " + id + " no longer exists.", enfe);
            }
            Usuario idusuario = detalleusuariocategoria.getIdusuario();
            if (idusuario != null) {
                idusuario.getDetalleusuariocategoriaList().remove(detalleusuariocategoria);
                idusuario = em.merge(idusuario);
            }
            Categoria idCategoria = detalleusuariocategoria.getIdCategoria();
            if (idCategoria != null) {
                idCategoria.getDetalleusuariocategoriaList().remove(detalleusuariocategoria);
                idCategoria = em.merge(idCategoria);
            }
            em.remove(detalleusuariocategoria);
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

    public List<Detalleusuariocategoria> findDetalleusuariocategoriaEntities() {
        return findDetalleusuariocategoriaEntities(true, -1, -1);
    }

    public List<Detalleusuariocategoria> findDetalleusuariocategoriaEntities(int maxResults, int firstResult) {
        return findDetalleusuariocategoriaEntities(false, maxResults, firstResult);
    }

    private List<Detalleusuariocategoria> findDetalleusuariocategoriaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Detalleusuariocategoria.class));
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

    public Detalleusuariocategoria findDetalleusuariocategoria(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Detalleusuariocategoria.class, id);
        } finally {
            em.close();
        }
    }

    public int getDetalleusuariocategoriaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Detalleusuariocategoria> rt = cq.from(Detalleusuariocategoria.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
