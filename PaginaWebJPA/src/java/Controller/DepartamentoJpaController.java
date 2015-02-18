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
import Entities.Departamento;
import java.util.ArrayList;
import java.util.List;
import Entities.Empleado;
import Entities.Usuario;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author Carmen
 */
public class DepartamentoJpaController implements Serializable {

    public DepartamentoJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Departamento departamento) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (departamento.getClienteList() == null) {
            departamento.setClienteList(new ArrayList<Cliente>());
        }
        if (departamento.getEmpleadoList() == null) {
            departamento.setEmpleadoList(new ArrayList<Empleado>());
        }
        if (departamento.getUsuarioList() == null) {
            departamento.setUsuarioList(new ArrayList<Usuario>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            List<Cliente> attachedClienteList = new ArrayList<Cliente>();
            for (Cliente clienteListClienteToAttach : departamento.getClienteList()) {
                clienteListClienteToAttach = em.getReference(clienteListClienteToAttach.getClass(), clienteListClienteToAttach.getIdCliente());
                attachedClienteList.add(clienteListClienteToAttach);
            }
            departamento.setClienteList(attachedClienteList);
            List<Empleado> attachedEmpleadoList = new ArrayList<Empleado>();
            for (Empleado empleadoListEmpleadoToAttach : departamento.getEmpleadoList()) {
                empleadoListEmpleadoToAttach = em.getReference(empleadoListEmpleadoToAttach.getClass(), empleadoListEmpleadoToAttach.getIdEmpleado());
                attachedEmpleadoList.add(empleadoListEmpleadoToAttach);
            }
            departamento.setEmpleadoList(attachedEmpleadoList);
            List<Usuario> attachedUsuarioList = new ArrayList<Usuario>();
            for (Usuario usuarioListUsuarioToAttach : departamento.getUsuarioList()) {
                usuarioListUsuarioToAttach = em.getReference(usuarioListUsuarioToAttach.getClass(), usuarioListUsuarioToAttach.getIdUsuario());
                attachedUsuarioList.add(usuarioListUsuarioToAttach);
            }
            departamento.setUsuarioList(attachedUsuarioList);
            em.persist(departamento);
            for (Cliente clienteListCliente : departamento.getClienteList()) {
                Departamento oldIdDepartamentoOfClienteListCliente = clienteListCliente.getIdDepartamento();
                clienteListCliente.setIdDepartamento(departamento);
                clienteListCliente = em.merge(clienteListCliente);
                if (oldIdDepartamentoOfClienteListCliente != null) {
                    oldIdDepartamentoOfClienteListCliente.getClienteList().remove(clienteListCliente);
                    oldIdDepartamentoOfClienteListCliente = em.merge(oldIdDepartamentoOfClienteListCliente);
                }
            }
            for (Empleado empleadoListEmpleado : departamento.getEmpleadoList()) {
                Departamento oldIdDepartamentoOfEmpleadoListEmpleado = empleadoListEmpleado.getIdDepartamento();
                empleadoListEmpleado.setIdDepartamento(departamento);
                empleadoListEmpleado = em.merge(empleadoListEmpleado);
                if (oldIdDepartamentoOfEmpleadoListEmpleado != null) {
                    oldIdDepartamentoOfEmpleadoListEmpleado.getEmpleadoList().remove(empleadoListEmpleado);
                    oldIdDepartamentoOfEmpleadoListEmpleado = em.merge(oldIdDepartamentoOfEmpleadoListEmpleado);
                }
            }
            for (Usuario usuarioListUsuario : departamento.getUsuarioList()) {
                Departamento oldIdDepartamentoOfUsuarioListUsuario = usuarioListUsuario.getIdDepartamento();
                usuarioListUsuario.setIdDepartamento(departamento);
                usuarioListUsuario = em.merge(usuarioListUsuario);
                if (oldIdDepartamentoOfUsuarioListUsuario != null) {
                    oldIdDepartamentoOfUsuarioListUsuario.getUsuarioList().remove(usuarioListUsuario);
                    oldIdDepartamentoOfUsuarioListUsuario = em.merge(oldIdDepartamentoOfUsuarioListUsuario);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findDepartamento(departamento.getIdDepartamento()) != null) {
                throw new PreexistingEntityException("Departamento " + departamento + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Departamento departamento) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Departamento persistentDepartamento = em.find(Departamento.class, departamento.getIdDepartamento());
            List<Cliente> clienteListOld = persistentDepartamento.getClienteList();
            List<Cliente> clienteListNew = departamento.getClienteList();
            List<Empleado> empleadoListOld = persistentDepartamento.getEmpleadoList();
            List<Empleado> empleadoListNew = departamento.getEmpleadoList();
            List<Usuario> usuarioListOld = persistentDepartamento.getUsuarioList();
            List<Usuario> usuarioListNew = departamento.getUsuarioList();
            List<String> illegalOrphanMessages = null;
            for (Cliente clienteListOldCliente : clienteListOld) {
                if (!clienteListNew.contains(clienteListOldCliente)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Cliente " + clienteListOldCliente + " since its idDepartamento field is not nullable.");
                }
            }
            for (Empleado empleadoListOldEmpleado : empleadoListOld) {
                if (!empleadoListNew.contains(empleadoListOldEmpleado)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Empleado " + empleadoListOldEmpleado + " since its idDepartamento field is not nullable.");
                }
            }
            for (Usuario usuarioListOldUsuario : usuarioListOld) {
                if (!usuarioListNew.contains(usuarioListOldUsuario)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Usuario " + usuarioListOldUsuario + " since its idDepartamento field is not nullable.");
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
            departamento.setClienteList(clienteListNew);
            List<Empleado> attachedEmpleadoListNew = new ArrayList<Empleado>();
            for (Empleado empleadoListNewEmpleadoToAttach : empleadoListNew) {
                empleadoListNewEmpleadoToAttach = em.getReference(empleadoListNewEmpleadoToAttach.getClass(), empleadoListNewEmpleadoToAttach.getIdEmpleado());
                attachedEmpleadoListNew.add(empleadoListNewEmpleadoToAttach);
            }
            empleadoListNew = attachedEmpleadoListNew;
            departamento.setEmpleadoList(empleadoListNew);
            List<Usuario> attachedUsuarioListNew = new ArrayList<Usuario>();
            for (Usuario usuarioListNewUsuarioToAttach : usuarioListNew) {
                usuarioListNewUsuarioToAttach = em.getReference(usuarioListNewUsuarioToAttach.getClass(), usuarioListNewUsuarioToAttach.getIdUsuario());
                attachedUsuarioListNew.add(usuarioListNewUsuarioToAttach);
            }
            usuarioListNew = attachedUsuarioListNew;
            departamento.setUsuarioList(usuarioListNew);
            departamento = em.merge(departamento);
            for (Cliente clienteListNewCliente : clienteListNew) {
                if (!clienteListOld.contains(clienteListNewCliente)) {
                    Departamento oldIdDepartamentoOfClienteListNewCliente = clienteListNewCliente.getIdDepartamento();
                    clienteListNewCliente.setIdDepartamento(departamento);
                    clienteListNewCliente = em.merge(clienteListNewCliente);
                    if (oldIdDepartamentoOfClienteListNewCliente != null && !oldIdDepartamentoOfClienteListNewCliente.equals(departamento)) {
                        oldIdDepartamentoOfClienteListNewCliente.getClienteList().remove(clienteListNewCliente);
                        oldIdDepartamentoOfClienteListNewCliente = em.merge(oldIdDepartamentoOfClienteListNewCliente);
                    }
                }
            }
            for (Empleado empleadoListNewEmpleado : empleadoListNew) {
                if (!empleadoListOld.contains(empleadoListNewEmpleado)) {
                    Departamento oldIdDepartamentoOfEmpleadoListNewEmpleado = empleadoListNewEmpleado.getIdDepartamento();
                    empleadoListNewEmpleado.setIdDepartamento(departamento);
                    empleadoListNewEmpleado = em.merge(empleadoListNewEmpleado);
                    if (oldIdDepartamentoOfEmpleadoListNewEmpleado != null && !oldIdDepartamentoOfEmpleadoListNewEmpleado.equals(departamento)) {
                        oldIdDepartamentoOfEmpleadoListNewEmpleado.getEmpleadoList().remove(empleadoListNewEmpleado);
                        oldIdDepartamentoOfEmpleadoListNewEmpleado = em.merge(oldIdDepartamentoOfEmpleadoListNewEmpleado);
                    }
                }
            }
            for (Usuario usuarioListNewUsuario : usuarioListNew) {
                if (!usuarioListOld.contains(usuarioListNewUsuario)) {
                    Departamento oldIdDepartamentoOfUsuarioListNewUsuario = usuarioListNewUsuario.getIdDepartamento();
                    usuarioListNewUsuario.setIdDepartamento(departamento);
                    usuarioListNewUsuario = em.merge(usuarioListNewUsuario);
                    if (oldIdDepartamentoOfUsuarioListNewUsuario != null && !oldIdDepartamentoOfUsuarioListNewUsuario.equals(departamento)) {
                        oldIdDepartamentoOfUsuarioListNewUsuario.getUsuarioList().remove(usuarioListNewUsuario);
                        oldIdDepartamentoOfUsuarioListNewUsuario = em.merge(oldIdDepartamentoOfUsuarioListNewUsuario);
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
                Integer id = departamento.getIdDepartamento();
                if (findDepartamento(id) == null) {
                    throw new NonexistentEntityException("The departamento with id " + id + " no longer exists.");
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
            Departamento departamento;
            try {
                departamento = em.getReference(Departamento.class, id);
                departamento.getIdDepartamento();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The departamento with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Cliente> clienteListOrphanCheck = departamento.getClienteList();
            for (Cliente clienteListOrphanCheckCliente : clienteListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Departamento (" + departamento + ") cannot be destroyed since the Cliente " + clienteListOrphanCheckCliente + " in its clienteList field has a non-nullable idDepartamento field.");
            }
            List<Empleado> empleadoListOrphanCheck = departamento.getEmpleadoList();
            for (Empleado empleadoListOrphanCheckEmpleado : empleadoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Departamento (" + departamento + ") cannot be destroyed since the Empleado " + empleadoListOrphanCheckEmpleado + " in its empleadoList field has a non-nullable idDepartamento field.");
            }
            List<Usuario> usuarioListOrphanCheck = departamento.getUsuarioList();
            for (Usuario usuarioListOrphanCheckUsuario : usuarioListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Departamento (" + departamento + ") cannot be destroyed since the Usuario " + usuarioListOrphanCheckUsuario + " in its usuarioList field has a non-nullable idDepartamento field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(departamento);
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

    public List<Departamento> findDepartamentoEntities() {
        return findDepartamentoEntities(true, -1, -1);
    }

    public List<Departamento> findDepartamentoEntities(int maxResults, int firstResult) {
        return findDepartamentoEntities(false, maxResults, firstResult);
    }

    private List<Departamento> findDepartamentoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Departamento.class));
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

    public Departamento findDepartamento(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Departamento.class, id);
        } finally {
            em.close();
        }
    }

    public int getDepartamentoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Departamento> rt = cq.from(Departamento.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
