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
import Entities.Actividadeconomica;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Entities.Cliente;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author Carmen
 */
public class ActividadeconomicaJpaController implements Serializable {

    public ActividadeconomicaJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Actividadeconomica actividadeconomica) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (actividadeconomica.getClienteList() == null) {
            actividadeconomica.setClienteList(new ArrayList<Cliente>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            List<Cliente> attachedClienteList = new ArrayList<Cliente>();
            for (Cliente clienteListClienteToAttach : actividadeconomica.getClienteList()) {
                clienteListClienteToAttach = em.getReference(clienteListClienteToAttach.getClass(), clienteListClienteToAttach.getIdCliente());
                attachedClienteList.add(clienteListClienteToAttach);
            }
            actividadeconomica.setClienteList(attachedClienteList);
            em.persist(actividadeconomica);
            for (Cliente clienteListCliente : actividadeconomica.getClienteList()) {
                Actividadeconomica oldIdActEconOfClienteListCliente = clienteListCliente.getIdActEcon();
                clienteListCliente.setIdActEcon(actividadeconomica);
                clienteListCliente = em.merge(clienteListCliente);
                if (oldIdActEconOfClienteListCliente != null) {
                    oldIdActEconOfClienteListCliente.getClienteList().remove(clienteListCliente);
                    oldIdActEconOfClienteListCliente = em.merge(oldIdActEconOfClienteListCliente);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findActividadeconomica(actividadeconomica.getIdActEcon()) != null) {
                throw new PreexistingEntityException("Actividadeconomica " + actividadeconomica + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Actividadeconomica actividadeconomica) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Actividadeconomica persistentActividadeconomica = em.find(Actividadeconomica.class, actividadeconomica.getIdActEcon());
            List<Cliente> clienteListOld = persistentActividadeconomica.getClienteList();
            List<Cliente> clienteListNew = actividadeconomica.getClienteList();
            List<String> illegalOrphanMessages = null;
            for (Cliente clienteListOldCliente : clienteListOld) {
                if (!clienteListNew.contains(clienteListOldCliente)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Cliente " + clienteListOldCliente + " since its idActEcon field is not nullable.");
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
            actividadeconomica.setClienteList(clienteListNew);
            actividadeconomica = em.merge(actividadeconomica);
            for (Cliente clienteListNewCliente : clienteListNew) {
                if (!clienteListOld.contains(clienteListNewCliente)) {
                    Actividadeconomica oldIdActEconOfClienteListNewCliente = clienteListNewCliente.getIdActEcon();
                    clienteListNewCliente.setIdActEcon(actividadeconomica);
                    clienteListNewCliente = em.merge(clienteListNewCliente);
                    if (oldIdActEconOfClienteListNewCliente != null && !oldIdActEconOfClienteListNewCliente.equals(actividadeconomica)) {
                        oldIdActEconOfClienteListNewCliente.getClienteList().remove(clienteListNewCliente);
                        oldIdActEconOfClienteListNewCliente = em.merge(oldIdActEconOfClienteListNewCliente);
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
                Integer id = actividadeconomica.getIdActEcon();
                if (findActividadeconomica(id) == null) {
                    throw new NonexistentEntityException("The actividadeconomica with id " + id + " no longer exists.");
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
            Actividadeconomica actividadeconomica;
            try {
                actividadeconomica = em.getReference(Actividadeconomica.class, id);
                actividadeconomica.getIdActEcon();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The actividadeconomica with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Cliente> clienteListOrphanCheck = actividadeconomica.getClienteList();
            for (Cliente clienteListOrphanCheckCliente : clienteListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Actividadeconomica (" + actividadeconomica + ") cannot be destroyed since the Cliente " + clienteListOrphanCheckCliente + " in its clienteList field has a non-nullable idActEcon field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(actividadeconomica);
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

    public List<Actividadeconomica> findActividadeconomicaEntities() {
        return findActividadeconomicaEntities(true, -1, -1);
    }

    public List<Actividadeconomica> findActividadeconomicaEntities(int maxResults, int firstResult) {
        return findActividadeconomicaEntities(false, maxResults, firstResult);
    }

    private List<Actividadeconomica> findActividadeconomicaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Actividadeconomica.class));
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

    public Actividadeconomica findActividadeconomica(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Actividadeconomica.class, id);
        } finally {
            em.close();
        }
    }

    public int getActividadeconomicaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Actividadeconomica> rt = cq.from(Actividadeconomica.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
