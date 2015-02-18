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
import Entities.Cliente;
import Entities.Metodopago;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author Carmen
 */
public class MetodopagoJpaController implements Serializable {

    public MetodopagoJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Metodopago metodopago) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (metodopago.getClienteList() == null) {
            metodopago.setClienteList(new ArrayList<Cliente>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            List<Cliente> attachedClienteList = new ArrayList<Cliente>();
            for (Cliente clienteListClienteToAttach : metodopago.getClienteList()) {
                clienteListClienteToAttach = em.getReference(clienteListClienteToAttach.getClass(), clienteListClienteToAttach.getIdCliente());
                attachedClienteList.add(clienteListClienteToAttach);
            }
            metodopago.setClienteList(attachedClienteList);
            em.persist(metodopago);
            for (Cliente clienteListCliente : metodopago.getClienteList()) {
                Metodopago oldIdMetodoPagoOfClienteListCliente = clienteListCliente.getIdMetodoPago();
                clienteListCliente.setIdMetodoPago(metodopago);
                clienteListCliente = em.merge(clienteListCliente);
                if (oldIdMetodoPagoOfClienteListCliente != null) {
                    oldIdMetodoPagoOfClienteListCliente.getClienteList().remove(clienteListCliente);
                    oldIdMetodoPagoOfClienteListCliente = em.merge(oldIdMetodoPagoOfClienteListCliente);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findMetodopago(metodopago.getIdMetodoPago()) != null) {
                throw new PreexistingEntityException("Metodopago " + metodopago + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Metodopago metodopago) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Metodopago persistentMetodopago = em.find(Metodopago.class, metodopago.getIdMetodoPago());
            List<Cliente> clienteListOld = persistentMetodopago.getClienteList();
            List<Cliente> clienteListNew = metodopago.getClienteList();
            List<String> illegalOrphanMessages = null;
            for (Cliente clienteListOldCliente : clienteListOld) {
                if (!clienteListNew.contains(clienteListOldCliente)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Cliente " + clienteListOldCliente + " since its idMetodoPago field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Cliente> attachedClienteListNew = new ArrayList<Cliente>();
            for (Cliente clienteListNewClienteToAttach : clienteListNew) {
                clienteListNewClienteToAttach = em.getReference(clienteListNewClienteToAttach.getClass(), clienteListNewClienteToAttach.getIdCliente());
                attachedClienteListNew.add(clienteListNewClienteToAttach);
            }
            clienteListNew = attachedClienteListNew;
            metodopago.setClienteList(clienteListNew);
            metodopago = em.merge(metodopago);
            for (Cliente clienteListNewCliente : clienteListNew) {
                if (!clienteListOld.contains(clienteListNewCliente)) {
                    Metodopago oldIdMetodoPagoOfClienteListNewCliente = clienteListNewCliente.getIdMetodoPago();
                    clienteListNewCliente.setIdMetodoPago(metodopago);
                    clienteListNewCliente = em.merge(clienteListNewCliente);
                    if (oldIdMetodoPagoOfClienteListNewCliente != null && !oldIdMetodoPagoOfClienteListNewCliente.equals(metodopago)) {
                        oldIdMetodoPagoOfClienteListNewCliente.getClienteList().remove(clienteListNewCliente);
                        oldIdMetodoPagoOfClienteListNewCliente = em.merge(oldIdMetodoPagoOfClienteListNewCliente);
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
                Integer id = metodopago.getIdMetodoPago();
                if (findMetodopago(id) == null) {
                    throw new NonexistentEntityException("The metodopago with id " + id + " no longer exists.");
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
            Metodopago metodopago;
            try {
                metodopago = em.getReference(Metodopago.class, id);
                metodopago.getIdMetodoPago();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The metodopago with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Cliente> clienteListOrphanCheck = metodopago.getClienteList();
            for (Cliente clienteListOrphanCheckCliente : clienteListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Metodopago (" + metodopago + ") cannot be destroyed since the Cliente " + clienteListOrphanCheckCliente + " in its clienteList field has a non-nullable idMetodoPago field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(metodopago);
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

    public List<Metodopago> findMetodopagoEntities() {
        return findMetodopagoEntities(true, -1, -1);
    }

    public List<Metodopago> findMetodopagoEntities(int maxResults, int firstResult) {
        return findMetodopagoEntities(false, maxResults, firstResult);
    }

    private List<Metodopago> findMetodopagoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Metodopago.class));
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

    public Metodopago findMetodopago(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Metodopago.class, id);
        } finally {
            em.close();
        }
    }

    public int getMetodopagoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Metodopago> rt = cq.from(Metodopago.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
