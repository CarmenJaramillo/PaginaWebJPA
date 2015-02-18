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
import Entities.Detalleplancliente;
import Entities.Plancliente;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author Carmen
 */
public class PlanclienteJpaController implements Serializable {

    public PlanclienteJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Plancliente plancliente) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (plancliente.getDetalleplanclienteList() == null) {
            plancliente.setDetalleplanclienteList(new ArrayList<Detalleplancliente>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            List<Detalleplancliente> attachedDetalleplanclienteList = new ArrayList<Detalleplancliente>();
            for (Detalleplancliente detalleplanclienteListDetalleplanclienteToAttach : plancliente.getDetalleplanclienteList()) {
                detalleplanclienteListDetalleplanclienteToAttach = em.getReference(detalleplanclienteListDetalleplanclienteToAttach.getClass(), detalleplanclienteListDetalleplanclienteToAttach.getIdDetallePlanCliente());
                attachedDetalleplanclienteList.add(detalleplanclienteListDetalleplanclienteToAttach);
            }
            plancliente.setDetalleplanclienteList(attachedDetalleplanclienteList);
            em.persist(plancliente);
            for (Detalleplancliente detalleplanclienteListDetalleplancliente : plancliente.getDetalleplanclienteList()) {
                Plancliente oldIdNoPlanOfDetalleplanclienteListDetalleplancliente = detalleplanclienteListDetalleplancliente.getIdNoPlan();
                detalleplanclienteListDetalleplancliente.setIdNoPlan(plancliente);
                detalleplanclienteListDetalleplancliente = em.merge(detalleplanclienteListDetalleplancliente);
                if (oldIdNoPlanOfDetalleplanclienteListDetalleplancliente != null) {
                    oldIdNoPlanOfDetalleplanclienteListDetalleplancliente.getDetalleplanclienteList().remove(detalleplanclienteListDetalleplancliente);
                    oldIdNoPlanOfDetalleplanclienteListDetalleplancliente = em.merge(oldIdNoPlanOfDetalleplanclienteListDetalleplancliente);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findPlancliente(plancliente.getIdNoPlan()) != null) {
                throw new PreexistingEntityException("Plancliente " + plancliente + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Plancliente plancliente) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Plancliente persistentPlancliente = em.find(Plancliente.class, plancliente.getIdNoPlan());
            List<Detalleplancliente> detalleplanclienteListOld = persistentPlancliente.getDetalleplanclienteList();
            List<Detalleplancliente> detalleplanclienteListNew = plancliente.getDetalleplanclienteList();
            List<String> illegalOrphanMessages = null;
            for (Detalleplancliente detalleplanclienteListOldDetalleplancliente : detalleplanclienteListOld) {
                if (!detalleplanclienteListNew.contains(detalleplanclienteListOldDetalleplancliente)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Detalleplancliente " + detalleplanclienteListOldDetalleplancliente + " since its idNoPlan field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Detalleplancliente> attachedDetalleplanclienteListNew = new ArrayList<Detalleplancliente>();
            for (Detalleplancliente detalleplanclienteListNewDetalleplanclienteToAttach : detalleplanclienteListNew) {
                detalleplanclienteListNewDetalleplanclienteToAttach = em.getReference(detalleplanclienteListNewDetalleplanclienteToAttach.getClass(), detalleplanclienteListNewDetalleplanclienteToAttach.getIdDetallePlanCliente());
                attachedDetalleplanclienteListNew.add(detalleplanclienteListNewDetalleplanclienteToAttach);
            }
            detalleplanclienteListNew = attachedDetalleplanclienteListNew;
            plancliente.setDetalleplanclienteList(detalleplanclienteListNew);
            plancliente = em.merge(plancliente);
            for (Detalleplancliente detalleplanclienteListNewDetalleplancliente : detalleplanclienteListNew) {
                if (!detalleplanclienteListOld.contains(detalleplanclienteListNewDetalleplancliente)) {
                    Plancliente oldIdNoPlanOfDetalleplanclienteListNewDetalleplancliente = detalleplanclienteListNewDetalleplancliente.getIdNoPlan();
                    detalleplanclienteListNewDetalleplancliente.setIdNoPlan(plancliente);
                    detalleplanclienteListNewDetalleplancliente = em.merge(detalleplanclienteListNewDetalleplancliente);
                    if (oldIdNoPlanOfDetalleplanclienteListNewDetalleplancliente != null && !oldIdNoPlanOfDetalleplanclienteListNewDetalleplancliente.equals(plancliente)) {
                        oldIdNoPlanOfDetalleplanclienteListNewDetalleplancliente.getDetalleplanclienteList().remove(detalleplanclienteListNewDetalleplancliente);
                        oldIdNoPlanOfDetalleplanclienteListNewDetalleplancliente = em.merge(oldIdNoPlanOfDetalleplanclienteListNewDetalleplancliente);
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
                Integer id = plancliente.getIdNoPlan();
                if (findPlancliente(id) == null) {
                    throw new NonexistentEntityException("The plancliente with id " + id + " no longer exists.");
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
            Plancliente plancliente;
            try {
                plancliente = em.getReference(Plancliente.class, id);
                plancliente.getIdNoPlan();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The plancliente with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Detalleplancliente> detalleplanclienteListOrphanCheck = plancliente.getDetalleplanclienteList();
            for (Detalleplancliente detalleplanclienteListOrphanCheckDetalleplancliente : detalleplanclienteListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Plancliente (" + plancliente + ") cannot be destroyed since the Detalleplancliente " + detalleplanclienteListOrphanCheckDetalleplancliente + " in its detalleplanclienteList field has a non-nullable idNoPlan field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(plancliente);
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

    public List<Plancliente> findPlanclienteEntities() {
        return findPlanclienteEntities(true, -1, -1);
    }

    public List<Plancliente> findPlanclienteEntities(int maxResults, int firstResult) {
        return findPlanclienteEntities(false, maxResults, firstResult);
    }

    private List<Plancliente> findPlanclienteEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Plancliente.class));
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

    public Plancliente findPlancliente(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Plancliente.class, id);
        } finally {
            em.close();
        }
    }

    public int getPlanclienteCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Plancliente> rt = cq.from(Plancliente.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
