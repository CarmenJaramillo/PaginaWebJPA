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
import Entities.Barrio;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Entities.Cliente;
import java.util.ArrayList;
import java.util.List;
import Entities.Empleado;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author Carmen
 */
public class BarrioJpaController implements Serializable {

    public BarrioJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Barrio barrio) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (barrio.getClienteList() == null) {
            barrio.setClienteList(new ArrayList<Cliente>());
        }
        if (barrio.getEmpleadoList() == null) {
            barrio.setEmpleadoList(new ArrayList<Empleado>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            List<Cliente> attachedClienteList = new ArrayList<Cliente>();
            for (Cliente clienteListClienteToAttach : barrio.getClienteList()) {
                clienteListClienteToAttach = em.getReference(clienteListClienteToAttach.getClass(), clienteListClienteToAttach.getIdCliente());
                attachedClienteList.add(clienteListClienteToAttach);
            }
            barrio.setClienteList(attachedClienteList);
            List<Empleado> attachedEmpleadoList = new ArrayList<Empleado>();
            for (Empleado empleadoListEmpleadoToAttach : barrio.getEmpleadoList()) {
                empleadoListEmpleadoToAttach = em.getReference(empleadoListEmpleadoToAttach.getClass(), empleadoListEmpleadoToAttach.getIdEmpleado());
                attachedEmpleadoList.add(empleadoListEmpleadoToAttach);
            }
            barrio.setEmpleadoList(attachedEmpleadoList);
            em.persist(barrio);
            for (Cliente clienteListCliente : barrio.getClienteList()) {
                Barrio oldIdBarrioOfClienteListCliente = clienteListCliente.getIdBarrio();
                clienteListCliente.setIdBarrio(barrio);
                clienteListCliente = em.merge(clienteListCliente);
                if (oldIdBarrioOfClienteListCliente != null) {
                    oldIdBarrioOfClienteListCliente.getClienteList().remove(clienteListCliente);
                    oldIdBarrioOfClienteListCliente = em.merge(oldIdBarrioOfClienteListCliente);
                }
            }
            for (Empleado empleadoListEmpleado : barrio.getEmpleadoList()) {
                Barrio oldIdBarrioOfEmpleadoListEmpleado = empleadoListEmpleado.getIdBarrio();
                empleadoListEmpleado.setIdBarrio(barrio);
                empleadoListEmpleado = em.merge(empleadoListEmpleado);
                if (oldIdBarrioOfEmpleadoListEmpleado != null) {
                    oldIdBarrioOfEmpleadoListEmpleado.getEmpleadoList().remove(empleadoListEmpleado);
                    oldIdBarrioOfEmpleadoListEmpleado = em.merge(oldIdBarrioOfEmpleadoListEmpleado);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findBarrio(barrio.getIdBarrio()) != null) {
                throw new PreexistingEntityException("Barrio " + barrio + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Barrio barrio) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Barrio persistentBarrio = em.find(Barrio.class, barrio.getIdBarrio());
            List<Cliente> clienteListOld = persistentBarrio.getClienteList();
            List<Cliente> clienteListNew = barrio.getClienteList();
            List<Empleado> empleadoListOld = persistentBarrio.getEmpleadoList();
            List<Empleado> empleadoListNew = barrio.getEmpleadoList();
            List<String> illegalOrphanMessages = null;
            for (Cliente clienteListOldCliente : clienteListOld) {
                if (!clienteListNew.contains(clienteListOldCliente)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Cliente " + clienteListOldCliente + " since its idBarrio field is not nullable.");
                }
            }
            for (Empleado empleadoListOldEmpleado : empleadoListOld) {
                if (!empleadoListNew.contains(empleadoListOldEmpleado)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Empleado " + empleadoListOldEmpleado + " since its idBarrio field is not nullable.");
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
            barrio.setClienteList(clienteListNew);
            List<Empleado> attachedEmpleadoListNew = new ArrayList<Empleado>();
            for (Empleado empleadoListNewEmpleadoToAttach : empleadoListNew) {
                empleadoListNewEmpleadoToAttach = em.getReference(empleadoListNewEmpleadoToAttach.getClass(), empleadoListNewEmpleadoToAttach.getIdEmpleado());
                attachedEmpleadoListNew.add(empleadoListNewEmpleadoToAttach);
            }
            empleadoListNew = attachedEmpleadoListNew;
            barrio.setEmpleadoList(empleadoListNew);
            barrio = em.merge(barrio);
            for (Cliente clienteListNewCliente : clienteListNew) {
                if (!clienteListOld.contains(clienteListNewCliente)) {
                    Barrio oldIdBarrioOfClienteListNewCliente = clienteListNewCliente.getIdBarrio();
                    clienteListNewCliente.setIdBarrio(barrio);
                    clienteListNewCliente = em.merge(clienteListNewCliente);
                    if (oldIdBarrioOfClienteListNewCliente != null && !oldIdBarrioOfClienteListNewCliente.equals(barrio)) {
                        oldIdBarrioOfClienteListNewCliente.getClienteList().remove(clienteListNewCliente);
                        oldIdBarrioOfClienteListNewCliente = em.merge(oldIdBarrioOfClienteListNewCliente);
                    }
                }
            }
            for (Empleado empleadoListNewEmpleado : empleadoListNew) {
                if (!empleadoListOld.contains(empleadoListNewEmpleado)) {
                    Barrio oldIdBarrioOfEmpleadoListNewEmpleado = empleadoListNewEmpleado.getIdBarrio();
                    empleadoListNewEmpleado.setIdBarrio(barrio);
                    empleadoListNewEmpleado = em.merge(empleadoListNewEmpleado);
                    if (oldIdBarrioOfEmpleadoListNewEmpleado != null && !oldIdBarrioOfEmpleadoListNewEmpleado.equals(barrio)) {
                        oldIdBarrioOfEmpleadoListNewEmpleado.getEmpleadoList().remove(empleadoListNewEmpleado);
                        oldIdBarrioOfEmpleadoListNewEmpleado = em.merge(oldIdBarrioOfEmpleadoListNewEmpleado);
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
                Integer id = barrio.getIdBarrio();
                if (findBarrio(id) == null) {
                    throw new NonexistentEntityException("The barrio with id " + id + " no longer exists.");
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
            Barrio barrio;
            try {
                barrio = em.getReference(Barrio.class, id);
                barrio.getIdBarrio();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The barrio with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Cliente> clienteListOrphanCheck = barrio.getClienteList();
            for (Cliente clienteListOrphanCheckCliente : clienteListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Barrio (" + barrio + ") cannot be destroyed since the Cliente " + clienteListOrphanCheckCliente + " in its clienteList field has a non-nullable idBarrio field.");
            }
            List<Empleado> empleadoListOrphanCheck = barrio.getEmpleadoList();
            for (Empleado empleadoListOrphanCheckEmpleado : empleadoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Barrio (" + barrio + ") cannot be destroyed since the Empleado " + empleadoListOrphanCheckEmpleado + " in its empleadoList field has a non-nullable idBarrio field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(barrio);
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

    public List<Barrio> findBarrioEntities() {
        return findBarrioEntities(true, -1, -1);
    }

    public List<Barrio> findBarrioEntities(int maxResults, int firstResult) {
        return findBarrioEntities(false, maxResults, firstResult);
    }

    private List<Barrio> findBarrioEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Barrio.class));
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

    public Barrio findBarrio(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Barrio.class, id);
        } finally {
            em.close();
        }
    }

    public int getBarrioCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Barrio> rt = cq.from(Barrio.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
