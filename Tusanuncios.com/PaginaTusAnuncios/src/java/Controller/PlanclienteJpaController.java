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
import Entities.Factura;
import java.util.ArrayList;
import java.util.List;
import Entities.Detalleplancliente;
import Entities.Plancliente;
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
        if (plancliente.getFacturaList() == null) {
            plancliente.setFacturaList(new ArrayList<Factura>());
        }
        if (plancliente.getDetalleplanclienteList() == null) {
            plancliente.setDetalleplanclienteList(new ArrayList<Detalleplancliente>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            List<Factura> attachedFacturaList = new ArrayList<Factura>();
            for (Factura facturaListFacturaToAttach : plancliente.getFacturaList()) {
                facturaListFacturaToAttach = em.getReference(facturaListFacturaToAttach.getClass(), facturaListFacturaToAttach.getIdFactura());
                attachedFacturaList.add(facturaListFacturaToAttach);
            }
            plancliente.setFacturaList(attachedFacturaList);
            List<Detalleplancliente> attachedDetalleplanclienteList = new ArrayList<Detalleplancliente>();
            for (Detalleplancliente detalleplanclienteListDetalleplanclienteToAttach : plancliente.getDetalleplanclienteList()) {
                detalleplanclienteListDetalleplanclienteToAttach = em.getReference(detalleplanclienteListDetalleplanclienteToAttach.getClass(), detalleplanclienteListDetalleplanclienteToAttach.getIdDetallePlanCliente());
                attachedDetalleplanclienteList.add(detalleplanclienteListDetalleplanclienteToAttach);
            }
            plancliente.setDetalleplanclienteList(attachedDetalleplanclienteList);
            em.persist(plancliente);
            for (Factura facturaListFactura : plancliente.getFacturaList()) {
                Plancliente oldIdNoPlanOfFacturaListFactura = facturaListFactura.getIdNoPlan();
                facturaListFactura.setIdNoPlan(plancliente);
                facturaListFactura = em.merge(facturaListFactura);
                if (oldIdNoPlanOfFacturaListFactura != null) {
                    oldIdNoPlanOfFacturaListFactura.getFacturaList().remove(facturaListFactura);
                    oldIdNoPlanOfFacturaListFactura = em.merge(oldIdNoPlanOfFacturaListFactura);
                }
            }
            for (Detalleplancliente detalleplanclienteListDetalleplancliente : plancliente.getDetalleplanclienteList()) {
                Plancliente oldIdNoplanOfDetalleplanclienteListDetalleplancliente = detalleplanclienteListDetalleplancliente.getIdNoplan();
                detalleplanclienteListDetalleplancliente.setIdNoplan(plancliente);
                detalleplanclienteListDetalleplancliente = em.merge(detalleplanclienteListDetalleplancliente);
                if (oldIdNoplanOfDetalleplanclienteListDetalleplancliente != null) {
                    oldIdNoplanOfDetalleplanclienteListDetalleplancliente.getDetalleplanclienteList().remove(detalleplanclienteListDetalleplancliente);
                    oldIdNoplanOfDetalleplanclienteListDetalleplancliente = em.merge(oldIdNoplanOfDetalleplanclienteListDetalleplancliente);
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
            List<Factura> facturaListOld = persistentPlancliente.getFacturaList();
            List<Factura> facturaListNew = plancliente.getFacturaList();
            List<Detalleplancliente> detalleplanclienteListOld = persistentPlancliente.getDetalleplanclienteList();
            List<Detalleplancliente> detalleplanclienteListNew = plancliente.getDetalleplanclienteList();
            List<String> illegalOrphanMessages = null;
            for (Factura facturaListOldFactura : facturaListOld) {
                if (!facturaListNew.contains(facturaListOldFactura)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Factura " + facturaListOldFactura + " since its idNoPlan field is not nullable.");
                }
            }
            for (Detalleplancliente detalleplanclienteListOldDetalleplancliente : detalleplanclienteListOld) {
                if (!detalleplanclienteListNew.contains(detalleplanclienteListOldDetalleplancliente)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Detalleplancliente " + detalleplanclienteListOldDetalleplancliente + " since its idNoplan field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Factura> attachedFacturaListNew = new ArrayList<Factura>();
            for (Factura facturaListNewFacturaToAttach : facturaListNew) {
                facturaListNewFacturaToAttach = em.getReference(facturaListNewFacturaToAttach.getClass(), facturaListNewFacturaToAttach.getIdFactura());
                attachedFacturaListNew.add(facturaListNewFacturaToAttach);
            }
            facturaListNew = attachedFacturaListNew;
            plancliente.setFacturaList(facturaListNew);
            List<Detalleplancliente> attachedDetalleplanclienteListNew = new ArrayList<Detalleplancliente>();
            for (Detalleplancliente detalleplanclienteListNewDetalleplanclienteToAttach : detalleplanclienteListNew) {
                detalleplanclienteListNewDetalleplanclienteToAttach = em.getReference(detalleplanclienteListNewDetalleplanclienteToAttach.getClass(), detalleplanclienteListNewDetalleplanclienteToAttach.getIdDetallePlanCliente());
                attachedDetalleplanclienteListNew.add(detalleplanclienteListNewDetalleplanclienteToAttach);
            }
            detalleplanclienteListNew = attachedDetalleplanclienteListNew;
            plancliente.setDetalleplanclienteList(detalleplanclienteListNew);
            plancliente = em.merge(plancliente);
            for (Factura facturaListNewFactura : facturaListNew) {
                if (!facturaListOld.contains(facturaListNewFactura)) {
                    Plancliente oldIdNoPlanOfFacturaListNewFactura = facturaListNewFactura.getIdNoPlan();
                    facturaListNewFactura.setIdNoPlan(plancliente);
                    facturaListNewFactura = em.merge(facturaListNewFactura);
                    if (oldIdNoPlanOfFacturaListNewFactura != null && !oldIdNoPlanOfFacturaListNewFactura.equals(plancliente)) {
                        oldIdNoPlanOfFacturaListNewFactura.getFacturaList().remove(facturaListNewFactura);
                        oldIdNoPlanOfFacturaListNewFactura = em.merge(oldIdNoPlanOfFacturaListNewFactura);
                    }
                }
            }
            for (Detalleplancliente detalleplanclienteListNewDetalleplancliente : detalleplanclienteListNew) {
                if (!detalleplanclienteListOld.contains(detalleplanclienteListNewDetalleplancliente)) {
                    Plancliente oldIdNoplanOfDetalleplanclienteListNewDetalleplancliente = detalleplanclienteListNewDetalleplancliente.getIdNoplan();
                    detalleplanclienteListNewDetalleplancliente.setIdNoplan(plancliente);
                    detalleplanclienteListNewDetalleplancliente = em.merge(detalleplanclienteListNewDetalleplancliente);
                    if (oldIdNoplanOfDetalleplanclienteListNewDetalleplancliente != null && !oldIdNoplanOfDetalleplanclienteListNewDetalleplancliente.equals(plancliente)) {
                        oldIdNoplanOfDetalleplanclienteListNewDetalleplancliente.getDetalleplanclienteList().remove(detalleplanclienteListNewDetalleplancliente);
                        oldIdNoplanOfDetalleplanclienteListNewDetalleplancliente = em.merge(oldIdNoplanOfDetalleplanclienteListNewDetalleplancliente);
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
            List<Factura> facturaListOrphanCheck = plancliente.getFacturaList();
            for (Factura facturaListOrphanCheckFactura : facturaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Plancliente (" + plancliente + ") cannot be destroyed since the Factura " + facturaListOrphanCheckFactura + " in its facturaList field has a non-nullable idNoPlan field.");
            }
            List<Detalleplancliente> detalleplanclienteListOrphanCheck = plancliente.getDetalleplanclienteList();
            for (Detalleplancliente detalleplanclienteListOrphanCheckDetalleplancliente : detalleplanclienteListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Plancliente (" + plancliente + ") cannot be destroyed since the Detalleplancliente " + detalleplanclienteListOrphanCheckDetalleplancliente + " in its detalleplanclienteList field has a non-nullable idNoplan field.");
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
