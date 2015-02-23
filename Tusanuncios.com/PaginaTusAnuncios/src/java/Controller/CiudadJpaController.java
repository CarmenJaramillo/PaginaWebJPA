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
import Entities.Ciudad;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Entities.Cliente;
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
public class CiudadJpaController implements Serializable {

    public CiudadJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Ciudad ciudad) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (ciudad.getClienteList() == null) {
            ciudad.setClienteList(new ArrayList<Cliente>());
        }
        if (ciudad.getEmpleadoList() == null) {
            ciudad.setEmpleadoList(new ArrayList<Empleado>());
        }
        if (ciudad.getUsuarioList() == null) {
            ciudad.setUsuarioList(new ArrayList<Usuario>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            List<Cliente> attachedClienteList = new ArrayList<Cliente>();
            for (Cliente clienteListClienteToAttach : ciudad.getClienteList()) {
                clienteListClienteToAttach = em.getReference(clienteListClienteToAttach.getClass(), clienteListClienteToAttach.getIdCliente());
                attachedClienteList.add(clienteListClienteToAttach);
            }
            ciudad.setClienteList(attachedClienteList);
            List<Empleado> attachedEmpleadoList = new ArrayList<Empleado>();
            for (Empleado empleadoListEmpleadoToAttach : ciudad.getEmpleadoList()) {
                empleadoListEmpleadoToAttach = em.getReference(empleadoListEmpleadoToAttach.getClass(), empleadoListEmpleadoToAttach.getIdEmpleado());
                attachedEmpleadoList.add(empleadoListEmpleadoToAttach);
            }
            ciudad.setEmpleadoList(attachedEmpleadoList);
            List<Usuario> attachedUsuarioList = new ArrayList<Usuario>();
            for (Usuario usuarioListUsuarioToAttach : ciudad.getUsuarioList()) {
                usuarioListUsuarioToAttach = em.getReference(usuarioListUsuarioToAttach.getClass(), usuarioListUsuarioToAttach.getIdusuario());
                attachedUsuarioList.add(usuarioListUsuarioToAttach);
            }
            ciudad.setUsuarioList(attachedUsuarioList);
            em.persist(ciudad);
            for (Cliente clienteListCliente : ciudad.getClienteList()) {
                Ciudad oldIdCiudadOfClienteListCliente = clienteListCliente.getIdCiudad();
                clienteListCliente.setIdCiudad(ciudad);
                clienteListCliente = em.merge(clienteListCliente);
                if (oldIdCiudadOfClienteListCliente != null) {
                    oldIdCiudadOfClienteListCliente.getClienteList().remove(clienteListCliente);
                    oldIdCiudadOfClienteListCliente = em.merge(oldIdCiudadOfClienteListCliente);
                }
            }
            for (Empleado empleadoListEmpleado : ciudad.getEmpleadoList()) {
                Ciudad oldIdCiudadOfEmpleadoListEmpleado = empleadoListEmpleado.getIdCiudad();
                empleadoListEmpleado.setIdCiudad(ciudad);
                empleadoListEmpleado = em.merge(empleadoListEmpleado);
                if (oldIdCiudadOfEmpleadoListEmpleado != null) {
                    oldIdCiudadOfEmpleadoListEmpleado.getEmpleadoList().remove(empleadoListEmpleado);
                    oldIdCiudadOfEmpleadoListEmpleado = em.merge(oldIdCiudadOfEmpleadoListEmpleado);
                }
            }
            for (Usuario usuarioListUsuario : ciudad.getUsuarioList()) {
                Ciudad oldIdCiudadOfUsuarioListUsuario = usuarioListUsuario.getIdCiudad();
                usuarioListUsuario.setIdCiudad(ciudad);
                usuarioListUsuario = em.merge(usuarioListUsuario);
                if (oldIdCiudadOfUsuarioListUsuario != null) {
                    oldIdCiudadOfUsuarioListUsuario.getUsuarioList().remove(usuarioListUsuario);
                    oldIdCiudadOfUsuarioListUsuario = em.merge(oldIdCiudadOfUsuarioListUsuario);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findCiudad(ciudad.getIdCiudad()) != null) {
                throw new PreexistingEntityException("Ciudad " + ciudad + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Ciudad ciudad) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Ciudad persistentCiudad = em.find(Ciudad.class, ciudad.getIdCiudad());
            List<Cliente> clienteListOld = persistentCiudad.getClienteList();
            List<Cliente> clienteListNew = ciudad.getClienteList();
            List<Empleado> empleadoListOld = persistentCiudad.getEmpleadoList();
            List<Empleado> empleadoListNew = ciudad.getEmpleadoList();
            List<Usuario> usuarioListOld = persistentCiudad.getUsuarioList();
            List<Usuario> usuarioListNew = ciudad.getUsuarioList();
            List<String> illegalOrphanMessages = null;
            for (Cliente clienteListOldCliente : clienteListOld) {
                if (!clienteListNew.contains(clienteListOldCliente)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Cliente " + clienteListOldCliente + " since its idCiudad field is not nullable.");
                }
            }
            for (Empleado empleadoListOldEmpleado : empleadoListOld) {
                if (!empleadoListNew.contains(empleadoListOldEmpleado)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Empleado " + empleadoListOldEmpleado + " since its idCiudad field is not nullable.");
                }
            }
            for (Usuario usuarioListOldUsuario : usuarioListOld) {
                if (!usuarioListNew.contains(usuarioListOldUsuario)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Usuario " + usuarioListOldUsuario + " since its idCiudad field is not nullable.");
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
            ciudad.setClienteList(clienteListNew);
            List<Empleado> attachedEmpleadoListNew = new ArrayList<Empleado>();
            for (Empleado empleadoListNewEmpleadoToAttach : empleadoListNew) {
                empleadoListNewEmpleadoToAttach = em.getReference(empleadoListNewEmpleadoToAttach.getClass(), empleadoListNewEmpleadoToAttach.getIdEmpleado());
                attachedEmpleadoListNew.add(empleadoListNewEmpleadoToAttach);
            }
            empleadoListNew = attachedEmpleadoListNew;
            ciudad.setEmpleadoList(empleadoListNew);
            List<Usuario> attachedUsuarioListNew = new ArrayList<Usuario>();
            for (Usuario usuarioListNewUsuarioToAttach : usuarioListNew) {
                usuarioListNewUsuarioToAttach = em.getReference(usuarioListNewUsuarioToAttach.getClass(), usuarioListNewUsuarioToAttach.getIdusuario());
                attachedUsuarioListNew.add(usuarioListNewUsuarioToAttach);
            }
            usuarioListNew = attachedUsuarioListNew;
            ciudad.setUsuarioList(usuarioListNew);
            ciudad = em.merge(ciudad);
            for (Cliente clienteListNewCliente : clienteListNew) {
                if (!clienteListOld.contains(clienteListNewCliente)) {
                    Ciudad oldIdCiudadOfClienteListNewCliente = clienteListNewCliente.getIdCiudad();
                    clienteListNewCliente.setIdCiudad(ciudad);
                    clienteListNewCliente = em.merge(clienteListNewCliente);
                    if (oldIdCiudadOfClienteListNewCliente != null && !oldIdCiudadOfClienteListNewCliente.equals(ciudad)) {
                        oldIdCiudadOfClienteListNewCliente.getClienteList().remove(clienteListNewCliente);
                        oldIdCiudadOfClienteListNewCliente = em.merge(oldIdCiudadOfClienteListNewCliente);
                    }
                }
            }
            for (Empleado empleadoListNewEmpleado : empleadoListNew) {
                if (!empleadoListOld.contains(empleadoListNewEmpleado)) {
                    Ciudad oldIdCiudadOfEmpleadoListNewEmpleado = empleadoListNewEmpleado.getIdCiudad();
                    empleadoListNewEmpleado.setIdCiudad(ciudad);
                    empleadoListNewEmpleado = em.merge(empleadoListNewEmpleado);
                    if (oldIdCiudadOfEmpleadoListNewEmpleado != null && !oldIdCiudadOfEmpleadoListNewEmpleado.equals(ciudad)) {
                        oldIdCiudadOfEmpleadoListNewEmpleado.getEmpleadoList().remove(empleadoListNewEmpleado);
                        oldIdCiudadOfEmpleadoListNewEmpleado = em.merge(oldIdCiudadOfEmpleadoListNewEmpleado);
                    }
                }
            }
            for (Usuario usuarioListNewUsuario : usuarioListNew) {
                if (!usuarioListOld.contains(usuarioListNewUsuario)) {
                    Ciudad oldIdCiudadOfUsuarioListNewUsuario = usuarioListNewUsuario.getIdCiudad();
                    usuarioListNewUsuario.setIdCiudad(ciudad);
                    usuarioListNewUsuario = em.merge(usuarioListNewUsuario);
                    if (oldIdCiudadOfUsuarioListNewUsuario != null && !oldIdCiudadOfUsuarioListNewUsuario.equals(ciudad)) {
                        oldIdCiudadOfUsuarioListNewUsuario.getUsuarioList().remove(usuarioListNewUsuario);
                        oldIdCiudadOfUsuarioListNewUsuario = em.merge(oldIdCiudadOfUsuarioListNewUsuario);
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
                Integer id = ciudad.getIdCiudad();
                if (findCiudad(id) == null) {
                    throw new NonexistentEntityException("The ciudad with id " + id + " no longer exists.");
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
            Ciudad ciudad;
            try {
                ciudad = em.getReference(Ciudad.class, id);
                ciudad.getIdCiudad();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The ciudad with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Cliente> clienteListOrphanCheck = ciudad.getClienteList();
            for (Cliente clienteListOrphanCheckCliente : clienteListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Ciudad (" + ciudad + ") cannot be destroyed since the Cliente " + clienteListOrphanCheckCliente + " in its clienteList field has a non-nullable idCiudad field.");
            }
            List<Empleado> empleadoListOrphanCheck = ciudad.getEmpleadoList();
            for (Empleado empleadoListOrphanCheckEmpleado : empleadoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Ciudad (" + ciudad + ") cannot be destroyed since the Empleado " + empleadoListOrphanCheckEmpleado + " in its empleadoList field has a non-nullable idCiudad field.");
            }
            List<Usuario> usuarioListOrphanCheck = ciudad.getUsuarioList();
            for (Usuario usuarioListOrphanCheckUsuario : usuarioListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Ciudad (" + ciudad + ") cannot be destroyed since the Usuario " + usuarioListOrphanCheckUsuario + " in its usuarioList field has a non-nullable idCiudad field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(ciudad);
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

    public List<Ciudad> findCiudadEntities() {
        return findCiudadEntities(true, -1, -1);
    }

    public List<Ciudad> findCiudadEntities(int maxResults, int firstResult) {
        return findCiudadEntities(false, maxResults, firstResult);
    }

    private List<Ciudad> findCiudadEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Ciudad.class));
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

    public Ciudad findCiudad(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Ciudad.class, id);
        } finally {
            em.close();
        }
    }

    public int getCiudadCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Ciudad> rt = cq.from(Ciudad.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
