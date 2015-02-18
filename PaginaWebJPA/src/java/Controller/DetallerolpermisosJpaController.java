/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Controller.exceptions.NonexistentEntityException;
import Controller.exceptions.RollbackFailureException;
import Entities.Detallerolpermisos;
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
public class DetallerolpermisosJpaController implements Serializable {

    public DetallerolpermisosJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Detallerolpermisos detallerolpermisos) throws RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Permisos idPermisos = detallerolpermisos.getIdPermisos();
            if (idPermisos != null) {
                idPermisos = em.getReference(idPermisos.getClass(), idPermisos.getIdPermisos());
                detallerolpermisos.setIdPermisos(idPermisos);
            }
            Rol idRol = detallerolpermisos.getIdRol();
            if (idRol != null) {
                idRol = em.getReference(idRol.getClass(), idRol.getIdRol());
                detallerolpermisos.setIdRol(idRol);
            }
            em.persist(detallerolpermisos);
            if (idPermisos != null) {
                idPermisos.getDetallerolpermisosList().add(detallerolpermisos);
                idPermisos = em.merge(idPermisos);
            }
            if (idRol != null) {
                idRol.getDetallerolpermisosList().add(detallerolpermisos);
                idRol = em.merge(idRol);
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

    public void edit(Detallerolpermisos detallerolpermisos) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Detallerolpermisos persistentDetallerolpermisos = em.find(Detallerolpermisos.class, detallerolpermisos.getIdDetalleRolesPermisos());
            Permisos idPermisosOld = persistentDetallerolpermisos.getIdPermisos();
            Permisos idPermisosNew = detallerolpermisos.getIdPermisos();
            Rol idRolOld = persistentDetallerolpermisos.getIdRol();
            Rol idRolNew = detallerolpermisos.getIdRol();
            if (idPermisosNew != null) {
                idPermisosNew = em.getReference(idPermisosNew.getClass(), idPermisosNew.getIdPermisos());
                detallerolpermisos.setIdPermisos(idPermisosNew);
            }
            if (idRolNew != null) {
                idRolNew = em.getReference(idRolNew.getClass(), idRolNew.getIdRol());
                detallerolpermisos.setIdRol(idRolNew);
            }
            detallerolpermisos = em.merge(detallerolpermisos);
            if (idPermisosOld != null && !idPermisosOld.equals(idPermisosNew)) {
                idPermisosOld.getDetallerolpermisosList().remove(detallerolpermisos);
                idPermisosOld = em.merge(idPermisosOld);
            }
            if (idPermisosNew != null && !idPermisosNew.equals(idPermisosOld)) {
                idPermisosNew.getDetallerolpermisosList().add(detallerolpermisos);
                idPermisosNew = em.merge(idPermisosNew);
            }
            if (idRolOld != null && !idRolOld.equals(idRolNew)) {
                idRolOld.getDetallerolpermisosList().remove(detallerolpermisos);
                idRolOld = em.merge(idRolOld);
            }
            if (idRolNew != null && !idRolNew.equals(idRolOld)) {
                idRolNew.getDetallerolpermisosList().add(detallerolpermisos);
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
                Integer id = detallerolpermisos.getIdDetalleRolesPermisos();
                if (findDetallerolpermisos(id) == null) {
                    throw new NonexistentEntityException("The detallerolpermisos with id " + id + " no longer exists.");
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
            Detallerolpermisos detallerolpermisos;
            try {
                detallerolpermisos = em.getReference(Detallerolpermisos.class, id);
                detallerolpermisos.getIdDetalleRolesPermisos();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The detallerolpermisos with id " + id + " no longer exists.", enfe);
            }
            Permisos idPermisos = detallerolpermisos.getIdPermisos();
            if (idPermisos != null) {
                idPermisos.getDetallerolpermisosList().remove(detallerolpermisos);
                idPermisos = em.merge(idPermisos);
            }
            Rol idRol = detallerolpermisos.getIdRol();
            if (idRol != null) {
                idRol.getDetallerolpermisosList().remove(detallerolpermisos);
                idRol = em.merge(idRol);
            }
            em.remove(detallerolpermisos);
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

    public List<Detallerolpermisos> findDetallerolpermisosEntities() {
        return findDetallerolpermisosEntities(true, -1, -1);
    }

    public List<Detallerolpermisos> findDetallerolpermisosEntities(int maxResults, int firstResult) {
        return findDetallerolpermisosEntities(false, maxResults, firstResult);
    }

    private List<Detallerolpermisos> findDetallerolpermisosEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Detallerolpermisos.class));
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

    public Detallerolpermisos findDetallerolpermisos(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Detallerolpermisos.class, id);
        } finally {
            em.close();
        }
    }

    public int getDetallerolpermisosCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Detallerolpermisos> rt = cq.from(Detallerolpermisos.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
