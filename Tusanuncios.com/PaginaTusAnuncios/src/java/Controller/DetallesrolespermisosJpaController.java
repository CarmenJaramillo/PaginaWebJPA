/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Controller.exceptions.NonexistentEntityException;
import Controller.exceptions.PreexistingEntityException;
import Controller.exceptions.RollbackFailureException;
import Entities.Detallesrolespermisos;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Entities.Permisos;
import Entities.Rol;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author Carmen
 */
public class DetallesrolespermisosJpaController implements Serializable {

    public DetallesrolespermisosJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Detallesrolespermisos detallesrolespermisos) throws PreexistingEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Permisos idPermisos = detallesrolespermisos.getIdPermisos();
            if (idPermisos != null) {
                idPermisos = em.getReference(idPermisos.getClass(), idPermisos.getIdPermisos());
                detallesrolespermisos.setIdPermisos(idPermisos);
            }
            Rol idRol = detallesrolespermisos.getIdRol();
            if (idRol != null) {
                idRol = em.getReference(idRol.getClass(), idRol.getIdRol());
                detallesrolespermisos.setIdRol(idRol);
            }
            em.persist(detallesrolespermisos);
            if (idPermisos != null) {
                idPermisos.getDetallesrolespermisosList().add(detallesrolespermisos);
                idPermisos = em.merge(idPermisos);
            }
            if (idRol != null) {
                idRol.getDetallesrolespermisosList().add(detallesrolespermisos);
                idRol = em.merge(idRol);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findDetallesrolespermisos(detallesrolespermisos.getIdDetalleRolesPermisos()) != null) {
                throw new PreexistingEntityException("Detallesrolespermisos " + detallesrolespermisos + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Detallesrolespermisos detallesrolespermisos) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Detallesrolespermisos persistentDetallesrolespermisos = em.find(Detallesrolespermisos.class, detallesrolespermisos.getIdDetalleRolesPermisos());
            Permisos idPermisosOld = persistentDetallesrolespermisos.getIdPermisos();
            Permisos idPermisosNew = detallesrolespermisos.getIdPermisos();
            Rol idRolOld = persistentDetallesrolespermisos.getIdRol();
            Rol idRolNew = detallesrolespermisos.getIdRol();
            if (idPermisosNew != null) {
                idPermisosNew = em.getReference(idPermisosNew.getClass(), idPermisosNew.getIdPermisos());
                detallesrolespermisos.setIdPermisos(idPermisosNew);
            }
            if (idRolNew != null) {
                idRolNew = em.getReference(idRolNew.getClass(), idRolNew.getIdRol());
                detallesrolespermisos.setIdRol(idRolNew);
            }
            detallesrolespermisos = em.merge(detallesrolespermisos);
            if (idPermisosOld != null && !idPermisosOld.equals(idPermisosNew)) {
                idPermisosOld.getDetallesrolespermisosList().remove(detallesrolespermisos);
                idPermisosOld = em.merge(idPermisosOld);
            }
            if (idPermisosNew != null && !idPermisosNew.equals(idPermisosOld)) {
                idPermisosNew.getDetallesrolespermisosList().add(detallesrolespermisos);
                idPermisosNew = em.merge(idPermisosNew);
            }
            if (idRolOld != null && !idRolOld.equals(idRolNew)) {
                idRolOld.getDetallesrolespermisosList().remove(detallesrolespermisos);
                idRolOld = em.merge(idRolOld);
            }
            if (idRolNew != null && !idRolNew.equals(idRolOld)) {
                idRolNew.getDetallesrolespermisosList().add(detallesrolespermisos);
                idRolNew = em.merge(idRolNew);
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
                Integer id = detallesrolespermisos.getIdDetalleRolesPermisos();
                if (findDetallesrolespermisos(id) == null) {
                    throw new NonexistentEntityException("The detallesrolespermisos with id " + id + " no longer exists.");
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
            Detallesrolespermisos detallesrolespermisos;
            try {
                detallesrolespermisos = em.getReference(Detallesrolespermisos.class, id);
                detallesrolespermisos.getIdDetalleRolesPermisos();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The detallesrolespermisos with id " + id + " no longer exists.", enfe);
            }
            Permisos idPermisos = detallesrolespermisos.getIdPermisos();
            if (idPermisos != null) {
                idPermisos.getDetallesrolespermisosList().remove(detallesrolespermisos);
                idPermisos = em.merge(idPermisos);
            }
            Rol idRol = detallesrolespermisos.getIdRol();
            if (idRol != null) {
                idRol.getDetallesrolespermisosList().remove(detallesrolespermisos);
                idRol = em.merge(idRol);
            }
            em.remove(detallesrolespermisos);
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

    public List<Detallesrolespermisos> findDetallesrolespermisosEntities() {
        return findDetallesrolespermisosEntities(true, -1, -1);
    }

    public List<Detallesrolespermisos> findDetallesrolespermisosEntities(int maxResults, int firstResult) {
        return findDetallesrolespermisosEntities(false, maxResults, firstResult);
    }

    private List<Detallesrolespermisos> findDetallesrolespermisosEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Detallesrolespermisos.class));
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

    public Detallesrolespermisos findDetallesrolespermisos(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Detallesrolespermisos.class, id);
        } finally {
            em.close();
        }
    }

    public int getDetallesrolespermisosCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Detallesrolespermisos> rt = cq.from(Detallesrolespermisos.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
