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
import java.util.ArrayList;
import java.util.List;
import Entities.Empleado;
import Entities.Rol;
import Entities.Usuario;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author Carmen
 */
public class RolJpaController implements Serializable {

    public RolJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Rol rol) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (rol.getDetallesrolespermisosList() == null) {
            rol.setDetallesrolespermisosList(new ArrayList<Detallesrolespermisos>());
        }
        if (rol.getEmpleadoList() == null) {
            rol.setEmpleadoList(new ArrayList<Empleado>());
        }
        if (rol.getUsuarioList() == null) {
            rol.setUsuarioList(new ArrayList<Usuario>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            List<Detallesrolespermisos> attachedDetallesrolespermisosList = new ArrayList<Detallesrolespermisos>();
            for (Detallesrolespermisos detallesrolespermisosListDetallesrolespermisosToAttach : rol.getDetallesrolespermisosList()) {
                detallesrolespermisosListDetallesrolespermisosToAttach = em.getReference(detallesrolespermisosListDetallesrolespermisosToAttach.getClass(), detallesrolespermisosListDetallesrolespermisosToAttach.getIdDetalleRolesPermisos());
                attachedDetallesrolespermisosList.add(detallesrolespermisosListDetallesrolespermisosToAttach);
            }
            rol.setDetallesrolespermisosList(attachedDetallesrolespermisosList);
            List<Empleado> attachedEmpleadoList = new ArrayList<Empleado>();
            for (Empleado empleadoListEmpleadoToAttach : rol.getEmpleadoList()) {
                empleadoListEmpleadoToAttach = em.getReference(empleadoListEmpleadoToAttach.getClass(), empleadoListEmpleadoToAttach.getIdEmpleado());
                attachedEmpleadoList.add(empleadoListEmpleadoToAttach);
            }
            rol.setEmpleadoList(attachedEmpleadoList);
            List<Usuario> attachedUsuarioList = new ArrayList<Usuario>();
            for (Usuario usuarioListUsuarioToAttach : rol.getUsuarioList()) {
                usuarioListUsuarioToAttach = em.getReference(usuarioListUsuarioToAttach.getClass(), usuarioListUsuarioToAttach.getIdusuario());
                attachedUsuarioList.add(usuarioListUsuarioToAttach);
            }
            rol.setUsuarioList(attachedUsuarioList);
            em.persist(rol);
            for (Detallesrolespermisos detallesrolespermisosListDetallesrolespermisos : rol.getDetallesrolespermisosList()) {
                Rol oldIdRolOfDetallesrolespermisosListDetallesrolespermisos = detallesrolespermisosListDetallesrolespermisos.getIdRol();
                detallesrolespermisosListDetallesrolespermisos.setIdRol(rol);
                detallesrolespermisosListDetallesrolespermisos = em.merge(detallesrolespermisosListDetallesrolespermisos);
                if (oldIdRolOfDetallesrolespermisosListDetallesrolespermisos != null) {
                    oldIdRolOfDetallesrolespermisosListDetallesrolespermisos.getDetallesrolespermisosList().remove(detallesrolespermisosListDetallesrolespermisos);
                    oldIdRolOfDetallesrolespermisosListDetallesrolespermisos = em.merge(oldIdRolOfDetallesrolespermisosListDetallesrolespermisos);
                }
            }
            for (Empleado empleadoListEmpleado : rol.getEmpleadoList()) {
                Rol oldIdRolOfEmpleadoListEmpleado = empleadoListEmpleado.getIdRol();
                empleadoListEmpleado.setIdRol(rol);
                empleadoListEmpleado = em.merge(empleadoListEmpleado);
                if (oldIdRolOfEmpleadoListEmpleado != null) {
                    oldIdRolOfEmpleadoListEmpleado.getEmpleadoList().remove(empleadoListEmpleado);
                    oldIdRolOfEmpleadoListEmpleado = em.merge(oldIdRolOfEmpleadoListEmpleado);
                }
            }
            for (Usuario usuarioListUsuario : rol.getUsuarioList()) {
                Rol oldIdRolOfUsuarioListUsuario = usuarioListUsuario.getIdRol();
                usuarioListUsuario.setIdRol(rol);
                usuarioListUsuario = em.merge(usuarioListUsuario);
                if (oldIdRolOfUsuarioListUsuario != null) {
                    oldIdRolOfUsuarioListUsuario.getUsuarioList().remove(usuarioListUsuario);
                    oldIdRolOfUsuarioListUsuario = em.merge(oldIdRolOfUsuarioListUsuario);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findRol(rol.getIdRol()) != null) {
                throw new PreexistingEntityException("Rol " + rol + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Rol rol) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Rol persistentRol = em.find(Rol.class, rol.getIdRol());
            List<Detallesrolespermisos> detallesrolespermisosListOld = persistentRol.getDetallesrolespermisosList();
            List<Detallesrolespermisos> detallesrolespermisosListNew = rol.getDetallesrolespermisosList();
            List<Empleado> empleadoListOld = persistentRol.getEmpleadoList();
            List<Empleado> empleadoListNew = rol.getEmpleadoList();
            List<Usuario> usuarioListOld = persistentRol.getUsuarioList();
            List<Usuario> usuarioListNew = rol.getUsuarioList();
            List<String> illegalOrphanMessages = null;
            for (Detallesrolespermisos detallesrolespermisosListOldDetallesrolespermisos : detallesrolespermisosListOld) {
                if (!detallesrolespermisosListNew.contains(detallesrolespermisosListOldDetallesrolespermisos)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Detallesrolespermisos " + detallesrolespermisosListOldDetallesrolespermisos + " since its idRol field is not nullable.");
                }
            }
            for (Empleado empleadoListOldEmpleado : empleadoListOld) {
                if (!empleadoListNew.contains(empleadoListOldEmpleado)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Empleado " + empleadoListOldEmpleado + " since its idRol field is not nullable.");
                }
            }
            for (Usuario usuarioListOldUsuario : usuarioListOld) {
                if (!usuarioListNew.contains(usuarioListOldUsuario)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Usuario " + usuarioListOldUsuario + " since its idRol field is not nullable.");
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
            rol.setDetallesrolespermisosList(detallesrolespermisosListNew);
            List<Empleado> attachedEmpleadoListNew = new ArrayList<Empleado>();
            for (Empleado empleadoListNewEmpleadoToAttach : empleadoListNew) {
                empleadoListNewEmpleadoToAttach = em.getReference(empleadoListNewEmpleadoToAttach.getClass(), empleadoListNewEmpleadoToAttach.getIdEmpleado());
                attachedEmpleadoListNew.add(empleadoListNewEmpleadoToAttach);
            }
            empleadoListNew = attachedEmpleadoListNew;
            rol.setEmpleadoList(empleadoListNew);
            List<Usuario> attachedUsuarioListNew = new ArrayList<Usuario>();
            for (Usuario usuarioListNewUsuarioToAttach : usuarioListNew) {
                usuarioListNewUsuarioToAttach = em.getReference(usuarioListNewUsuarioToAttach.getClass(), usuarioListNewUsuarioToAttach.getIdusuario());
                attachedUsuarioListNew.add(usuarioListNewUsuarioToAttach);
            }
            usuarioListNew = attachedUsuarioListNew;
            rol.setUsuarioList(usuarioListNew);
            rol = em.merge(rol);
            for (Detallesrolespermisos detallesrolespermisosListNewDetallesrolespermisos : detallesrolespermisosListNew) {
                if (!detallesrolespermisosListOld.contains(detallesrolespermisosListNewDetallesrolespermisos)) {
                    Rol oldIdRolOfDetallesrolespermisosListNewDetallesrolespermisos = detallesrolespermisosListNewDetallesrolespermisos.getIdRol();
                    detallesrolespermisosListNewDetallesrolespermisos.setIdRol(rol);
                    detallesrolespermisosListNewDetallesrolespermisos = em.merge(detallesrolespermisosListNewDetallesrolespermisos);
                    if (oldIdRolOfDetallesrolespermisosListNewDetallesrolespermisos != null && !oldIdRolOfDetallesrolespermisosListNewDetallesrolespermisos.equals(rol)) {
                        oldIdRolOfDetallesrolespermisosListNewDetallesrolespermisos.getDetallesrolespermisosList().remove(detallesrolespermisosListNewDetallesrolespermisos);
                        oldIdRolOfDetallesrolespermisosListNewDetallesrolespermisos = em.merge(oldIdRolOfDetallesrolespermisosListNewDetallesrolespermisos);
                    }
                }
            }
            for (Empleado empleadoListNewEmpleado : empleadoListNew) {
                if (!empleadoListOld.contains(empleadoListNewEmpleado)) {
                    Rol oldIdRolOfEmpleadoListNewEmpleado = empleadoListNewEmpleado.getIdRol();
                    empleadoListNewEmpleado.setIdRol(rol);
                    empleadoListNewEmpleado = em.merge(empleadoListNewEmpleado);
                    if (oldIdRolOfEmpleadoListNewEmpleado != null && !oldIdRolOfEmpleadoListNewEmpleado.equals(rol)) {
                        oldIdRolOfEmpleadoListNewEmpleado.getEmpleadoList().remove(empleadoListNewEmpleado);
                        oldIdRolOfEmpleadoListNewEmpleado = em.merge(oldIdRolOfEmpleadoListNewEmpleado);
                    }
                }
            }
            for (Usuario usuarioListNewUsuario : usuarioListNew) {
                if (!usuarioListOld.contains(usuarioListNewUsuario)) {
                    Rol oldIdRolOfUsuarioListNewUsuario = usuarioListNewUsuario.getIdRol();
                    usuarioListNewUsuario.setIdRol(rol);
                    usuarioListNewUsuario = em.merge(usuarioListNewUsuario);
                    if (oldIdRolOfUsuarioListNewUsuario != null && !oldIdRolOfUsuarioListNewUsuario.equals(rol)) {
                        oldIdRolOfUsuarioListNewUsuario.getUsuarioList().remove(usuarioListNewUsuario);
                        oldIdRolOfUsuarioListNewUsuario = em.merge(oldIdRolOfUsuarioListNewUsuario);
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
                Integer id = rol.getIdRol();
                if (findRol(id) == null) {
                    throw new NonexistentEntityException("The rol with id " + id + " no longer exists.");
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
            Rol rol;
            try {
                rol = em.getReference(Rol.class, id);
                rol.getIdRol();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The rol with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Detallesrolespermisos> detallesrolespermisosListOrphanCheck = rol.getDetallesrolespermisosList();
            for (Detallesrolespermisos detallesrolespermisosListOrphanCheckDetallesrolespermisos : detallesrolespermisosListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Rol (" + rol + ") cannot be destroyed since the Detallesrolespermisos " + detallesrolespermisosListOrphanCheckDetallesrolespermisos + " in its detallesrolespermisosList field has a non-nullable idRol field.");
            }
            List<Empleado> empleadoListOrphanCheck = rol.getEmpleadoList();
            for (Empleado empleadoListOrphanCheckEmpleado : empleadoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Rol (" + rol + ") cannot be destroyed since the Empleado " + empleadoListOrphanCheckEmpleado + " in its empleadoList field has a non-nullable idRol field.");
            }
            List<Usuario> usuarioListOrphanCheck = rol.getUsuarioList();
            for (Usuario usuarioListOrphanCheckUsuario : usuarioListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Rol (" + rol + ") cannot be destroyed since the Usuario " + usuarioListOrphanCheckUsuario + " in its usuarioList field has a non-nullable idRol field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(rol);
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

    public List<Rol> findRolEntities() {
        return findRolEntities(true, -1, -1);
    }

    public List<Rol> findRolEntities(int maxResults, int firstResult) {
        return findRolEntities(false, maxResults, firstResult);
    }

    private List<Rol> findRolEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Rol.class));
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

    public Rol findRol(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Rol.class, id);
        } finally {
            em.close();
        }
    }

    public int getRolCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Rol> rt = cq.from(Rol.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
