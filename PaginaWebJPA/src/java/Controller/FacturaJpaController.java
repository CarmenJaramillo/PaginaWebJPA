/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Controller.exceptions.IllegalOrphanException;
import Controller.exceptions.NonexistentEntityException;
import Controller.exceptions.RollbackFailureException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Entities.Cliente;
import Entities.Detallefacturaproductoscio;
import Entities.Factura;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author Carmen
 */
public class FacturaJpaController implements Serializable {

    public FacturaJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Factura factura) throws RollbackFailureException, Exception {
        if (factura.getDetallefacturaproductoscioList() == null) {
            factura.setDetallefacturaproductoscioList(new ArrayList<Detallefacturaproductoscio>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Cliente idCliente = factura.getIdCliente();
            if (idCliente != null) {
                idCliente = em.getReference(idCliente.getClass(), idCliente.getIdCliente());
                factura.setIdCliente(idCliente);
            }
            List<Detallefacturaproductoscio> attachedDetallefacturaproductoscioList = new ArrayList<Detallefacturaproductoscio>();
            for (Detallefacturaproductoscio detallefacturaproductoscioListDetallefacturaproductoscioToAttach : factura.getDetallefacturaproductoscioList()) {
                detallefacturaproductoscioListDetallefacturaproductoscioToAttach = em.getReference(detallefacturaproductoscioListDetallefacturaproductoscioToAttach.getClass(), detallefacturaproductoscioListDetallefacturaproductoscioToAttach.getIdDetalleFacturaProductoScio());
                attachedDetallefacturaproductoscioList.add(detallefacturaproductoscioListDetallefacturaproductoscioToAttach);
            }
            factura.setDetallefacturaproductoscioList(attachedDetallefacturaproductoscioList);
            em.persist(factura);
            if (idCliente != null) {
                idCliente.getFacturaList().add(factura);
                idCliente = em.merge(idCliente);
            }
            for (Detallefacturaproductoscio detallefacturaproductoscioListDetallefacturaproductoscio : factura.getDetallefacturaproductoscioList()) {
                Factura oldIdFacturaOfDetallefacturaproductoscioListDetallefacturaproductoscio = detallefacturaproductoscioListDetallefacturaproductoscio.getIdFactura();
                detallefacturaproductoscioListDetallefacturaproductoscio.setIdFactura(factura);
                detallefacturaproductoscioListDetallefacturaproductoscio = em.merge(detallefacturaproductoscioListDetallefacturaproductoscio);
                if (oldIdFacturaOfDetallefacturaproductoscioListDetallefacturaproductoscio != null) {
                    oldIdFacturaOfDetallefacturaproductoscioListDetallefacturaproductoscio.getDetallefacturaproductoscioList().remove(detallefacturaproductoscioListDetallefacturaproductoscio);
                    oldIdFacturaOfDetallefacturaproductoscioListDetallefacturaproductoscio = em.merge(oldIdFacturaOfDetallefacturaproductoscioListDetallefacturaproductoscio);
                }
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

    public void edit(Factura factura) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Factura persistentFactura = em.find(Factura.class, factura.getIdFactura());
            Cliente idClienteOld = persistentFactura.getIdCliente();
            Cliente idClienteNew = factura.getIdCliente();
            List<Detallefacturaproductoscio> detallefacturaproductoscioListOld = persistentFactura.getDetallefacturaproductoscioList();
            List<Detallefacturaproductoscio> detallefacturaproductoscioListNew = factura.getDetallefacturaproductoscioList();
            List<String> illegalOrphanMessages = null;
            for (Detallefacturaproductoscio detallefacturaproductoscioListOldDetallefacturaproductoscio : detallefacturaproductoscioListOld) {
                if (!detallefacturaproductoscioListNew.contains(detallefacturaproductoscioListOldDetallefacturaproductoscio)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Detallefacturaproductoscio " + detallefacturaproductoscioListOldDetallefacturaproductoscio + " since its idFactura field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idClienteNew != null) {
                idClienteNew = em.getReference(idClienteNew.getClass(), idClienteNew.getIdCliente());
                factura.setIdCliente(idClienteNew);
            }
            List<Detallefacturaproductoscio> attachedDetallefacturaproductoscioListNew = new ArrayList<Detallefacturaproductoscio>();
            for (Detallefacturaproductoscio detallefacturaproductoscioListNewDetallefacturaproductoscioToAttach : detallefacturaproductoscioListNew) {
                detallefacturaproductoscioListNewDetallefacturaproductoscioToAttach = em.getReference(detallefacturaproductoscioListNewDetallefacturaproductoscioToAttach.getClass(), detallefacturaproductoscioListNewDetallefacturaproductoscioToAttach.getIdDetalleFacturaProductoScio());
                attachedDetallefacturaproductoscioListNew.add(detallefacturaproductoscioListNewDetallefacturaproductoscioToAttach);
            }
            detallefacturaproductoscioListNew = attachedDetallefacturaproductoscioListNew;
            factura.setDetallefacturaproductoscioList(detallefacturaproductoscioListNew);
            factura = em.merge(factura);
            if (idClienteOld != null && !idClienteOld.equals(idClienteNew)) {
                idClienteOld.getFacturaList().remove(factura);
                idClienteOld = em.merge(idClienteOld);
            }
            if (idClienteNew != null && !idClienteNew.equals(idClienteOld)) {
                idClienteNew.getFacturaList().add(factura);
                idClienteNew = em.merge(idClienteNew);
            }
            for (Detallefacturaproductoscio detallefacturaproductoscioListNewDetallefacturaproductoscio : detallefacturaproductoscioListNew) {
                if (!detallefacturaproductoscioListOld.contains(detallefacturaproductoscioListNewDetallefacturaproductoscio)) {
                    Factura oldIdFacturaOfDetallefacturaproductoscioListNewDetallefacturaproductoscio = detallefacturaproductoscioListNewDetallefacturaproductoscio.getIdFactura();
                    detallefacturaproductoscioListNewDetallefacturaproductoscio.setIdFactura(factura);
                    detallefacturaproductoscioListNewDetallefacturaproductoscio = em.merge(detallefacturaproductoscioListNewDetallefacturaproductoscio);
                    if (oldIdFacturaOfDetallefacturaproductoscioListNewDetallefacturaproductoscio != null && !oldIdFacturaOfDetallefacturaproductoscioListNewDetallefacturaproductoscio.equals(factura)) {
                        oldIdFacturaOfDetallefacturaproductoscioListNewDetallefacturaproductoscio.getDetallefacturaproductoscioList().remove(detallefacturaproductoscioListNewDetallefacturaproductoscio);
                        oldIdFacturaOfDetallefacturaproductoscioListNewDetallefacturaproductoscio = em.merge(oldIdFacturaOfDetallefacturaproductoscioListNewDetallefacturaproductoscio);
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
                Integer id = factura.getIdFactura();
                if (findFactura(id) == null) {
                    throw new NonexistentEntityException("The factura with id " + id + " no longer exists.");
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
            Factura factura;
            try {
                factura = em.getReference(Factura.class, id);
                factura.getIdFactura();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The factura with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Detallefacturaproductoscio> detallefacturaproductoscioListOrphanCheck = factura.getDetallefacturaproductoscioList();
            for (Detallefacturaproductoscio detallefacturaproductoscioListOrphanCheckDetallefacturaproductoscio : detallefacturaproductoscioListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Factura (" + factura + ") cannot be destroyed since the Detallefacturaproductoscio " + detallefacturaproductoscioListOrphanCheckDetallefacturaproductoscio + " in its detallefacturaproductoscioList field has a non-nullable idFactura field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Cliente idCliente = factura.getIdCliente();
            if (idCliente != null) {
                idCliente.getFacturaList().remove(factura);
                idCliente = em.merge(idCliente);
            }
            em.remove(factura);
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

    public List<Factura> findFacturaEntities() {
        return findFacturaEntities(true, -1, -1);
    }

    public List<Factura> findFacturaEntities(int maxResults, int firstResult) {
        return findFacturaEntities(false, maxResults, firstResult);
    }

    private List<Factura> findFacturaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Factura.class));
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

    public Factura findFactura(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Factura.class, id);
        } finally {
            em.close();
        }
    }

    public int getFacturaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Factura> rt = cq.from(Factura.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
