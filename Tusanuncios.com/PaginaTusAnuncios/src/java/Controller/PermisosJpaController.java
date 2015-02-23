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
import Entities.Detallesrolespermisos;
import Entities.Permisos;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author Carmen
 */
public class PermisosJpaController implements Serializable {

    public PermisosJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Permisos permisos) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (permisos.getDetallesrolespermisosList() == null) {
            permisos.setDetallesrolespermisosList(new ArrayList<Detallesrolespermisos>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            List<Detallesrolespermisos> attachedDetallesrolespermisosList = new ArrayList<Detallesrolespermisos>();
            for (Detallesrolespermisos detallesrolespermisosListDetallesrolespermisosToAttach : permisos.getDetallesrolespermisosList()) {
                detallesrolespermisosListDetallesrolespermisosToAttach = em.getReference(detallesrolespermisosListDetallesrolespermisosToAttach.getClass(), detallesrolespermisosListDetallesrolespermisosToAttach.getIdDetalleRolesPermisos());
                attachedDetallesrolespermisosList.add(detallesrolespermisosListDetallesrolespermisosToAttach);
            }
            permisos.setDetallesrolespermisosList(attachedDetallesrolespermisosList);
            em.persist(permisos);
            for (Detallesrolespermisos detallesrolespermisosListDetallesrolespermisos : permisos.getDetallesrolespermisosList()) {
                Permisos oldIdPermisosOfDetallesrolespermisosListDetallesrolespermisos = detallesrolespermisosListDetallesrolespermisos.getIdPermisos();
                detallesrolespermisosListDetallesrolespermisos.setIdPermisos(permisos);
                detallesrolespermisosListDetallesrolespermisos = em.merge(detallesrolespermisosListDetallesrolespermisos);
                if (oldIdPermisosOfDetallesrolespermisosListDetallesrolespermisos != null) {
                    oldIdPermisosOfDetallesrolespermisosListDetallesrolespermisos.getDetallesrolespermisosList().remove(detallesrolespermisosListDetallesrolespermisos);
                    oldIdPermisosOfDetallesrolespermisosListDetallesrolespermisos = em.merge(oldIdPermisosOfDetallesrolespermisosListDetallesrolespermisos);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findPermisos(permisos.getIdPermisos()) != null) {
                throw new PreexistingEntityException("Permisos " + permisos + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Permisos permisos) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Permisos persistentPermisos = em.find(Permisos.class, permisos.getIdPermisos());
            List<Detallesrolespermisos> detallesrolespermisosListOld = persistentPermisos.getDetallesrolespermisosList();
            List<Detallesrolespermisos> detallesrolespermisosListNew = permisos.getDetallesrolespermisosList();
            List<String> illegalOrphanMessages = null;
            for (Detallesrolespermisos detallesrolespermisosListOldDetallesrolespermisos : detallesrolespermisosListOld) {
                if (!detallesrolespermisosListNew.contains(detallesrolespermisosListOldDetallesrolespermisos)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Detallesrolespermisos " + detallesrolespermisosListOldDetallesrolespermisos + " since its idPermisos field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Detallesrolespermisos> attachedDetallesrolespermisosListNew = new ArrayList<Detallesrolespermisos>();
            for (Detallesrolespermisos detallesrolespermisosListNewDetallesrolespermisosToAttach : detallesrolespermisosListNew) {
                detallesrolespermisosListNewDetallesrolespermisosToAttach = em.getReference(detallesrolespermisosListNewDetallesrolespermisosToAttach.getClass(), detallesrolespermisosListNewDetallesrolespermisosToAttach.getIdDetalleRolesPermisos());
                attachedDetallesrolespermisosListNew.add(detallesrolespermisosListNewDetallesrolespermisosToAttach);
            }
            detallesrolespermisosListNew = attachedDetallesrolespermisosListNew;
            permisos.setDetallesrolespermisosList(detallesrolespermisosListNew);
            permisos = em.merge(permisos);
            for (Detallesrolespermisos detallesrolespermisosListNewDetallesrolespermisos : detallesrolespermisosListNew) {
                if (!detallesrolespermisosListOld.contains(detallesrolespermisosListNewDetallesrolespermisos)) {
                    Permisos oldIdPermisosOfDetallesrolespermisosListNewDetallesrolespermisos = detallesrolespermisosListNewDetallesrolespermisos.getIdPermisos();
                    detallesrolespermisosListNewDetallesrolespermisos.setIdPermisos(permisos);
                    detallesrolespermisosListNewDetallesrolespermisos = em.merge(detallesrolespermisosListNewDetallesrolespermisos);
                    if (oldIdPermisosOfDetallesrolespermisosListNewDetallesrolespermisos != null && !oldIdPermisosOfDetallesrolespermisosListNewDetallesrolespermisos.equals(permisos)) {
                        oldIdPermisosOfDetallesrolespermisosListNewDetallesrolespermisos.getDetallesrolespermisosList().remove(detallesrolespermisosListNewDetallesrolespermisos);
                        oldIdPermisosOfDetallesrolespermisosListNewDetallesrolespermisos = em.merge(oldIdPermisosOfDetallesrolespermisosListNewDetallesrolespermisos);
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
                Integer id = permisos.getIdPermisos();
                if (findPermisos(id) == null) {
                    throw new NonexistentEntityException("The permisos with id " + id + " no longer exists.");
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
            Permisos permisos;
            try {
                permisos = em.getReference(Permisos.class, id);
                permisos.getIdPermisos();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The permisos with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Detallesrolespermisos> detallesrolespermisosListOrphanCheck = permisos.getDetallesrolespermisosList();
            for (Detallesrolespermisos detallesrolespermisosListOrphanCheckDetallesrolespermisos : detallesrolespermisosListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Permisos (" + permisos + ") cannot be destroyed since the Detallesrolespermisos " + detallesrolespermisosListOrphanCheckDetallesrolespermisos + " in its detallesrolespermisosList field has a non-nullable idPermisos field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(permisos);
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

    public List<Permisos> findPermisosEntities() {
        return findPermisosEntities(true, -1, -1);
    }

    public List<Permisos> findPermisosEntities(int maxResults, int firstResult) {
        return findPermisosEntities(false, maxResults, firstResult);
    }

    private List<Permisos> findPermisosEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Permisos.class));
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

    public Permisos findPermisos(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Permisos.class, id);
        } finally {
            em.close();
        }
    }

    public int getPermisosCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Permisos> rt = cq.from(Permisos.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
