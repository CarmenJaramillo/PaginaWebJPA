/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Controller.exceptions.NonexistentEntityException;
import Controller.exceptions.PreexistingEntityException;
import Controller.exceptions.RollbackFailureException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Entities.Departamento;
import Entities.Barrio;
import Entities.Ciudad;
import Entities.Empleado;
import Entities.Rol;
import Entities.Estrato;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author Carmen
 */
public class EmpleadoJpaController implements Serializable {

    public EmpleadoJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Empleado empleado) throws PreexistingEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Departamento idDepartamento = empleado.getIdDepartamento();
            if (idDepartamento != null) {
                idDepartamento = em.getReference(idDepartamento.getClass(), idDepartamento.getIdDepartamento());
                empleado.setIdDepartamento(idDepartamento);
            }
            Barrio idBarrio = empleado.getIdBarrio();
            if (idBarrio != null) {
                idBarrio = em.getReference(idBarrio.getClass(), idBarrio.getIdBarrio());
                empleado.setIdBarrio(idBarrio);
            }
            Ciudad idCiudad = empleado.getIdCiudad();
            if (idCiudad != null) {
                idCiudad = em.getReference(idCiudad.getClass(), idCiudad.getIdCiudad());
                empleado.setIdCiudad(idCiudad);
            }
            Rol idRol = empleado.getIdRol();
            if (idRol != null) {
                idRol = em.getReference(idRol.getClass(), idRol.getIdRol());
                empleado.setIdRol(idRol);
            }
            Estrato idEstrato = empleado.getIdEstrato();
            if (idEstrato != null) {
                idEstrato = em.getReference(idEstrato.getClass(), idEstrato.getIdEstrato());
                empleado.setIdEstrato(idEstrato);
            }
            em.persist(empleado);
            if (idDepartamento != null) {
                idDepartamento.getEmpleadoList().add(empleado);
                idDepartamento = em.merge(idDepartamento);
            }
            if (idBarrio != null) {
                idBarrio.getEmpleadoList().add(empleado);
                idBarrio = em.merge(idBarrio);
            }
            if (idCiudad != null) {
                idCiudad.getEmpleadoList().add(empleado);
                idCiudad = em.merge(idCiudad);
            }
            if (idRol != null) {
                idRol.getEmpleadoList().add(empleado);
                idRol = em.merge(idRol);
            }
            if (idEstrato != null) {
                idEstrato.getEmpleadoList().add(empleado);
                idEstrato = em.merge(idEstrato);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findEmpleado(empleado.getIdEmpleado()) != null) {
                throw new PreexistingEntityException("Empleado " + empleado + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Empleado empleado) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Empleado persistentEmpleado = em.find(Empleado.class, empleado.getIdEmpleado());
            Departamento idDepartamentoOld = persistentEmpleado.getIdDepartamento();
            Departamento idDepartamentoNew = empleado.getIdDepartamento();
            Barrio idBarrioOld = persistentEmpleado.getIdBarrio();
            Barrio idBarrioNew = empleado.getIdBarrio();
            Ciudad idCiudadOld = persistentEmpleado.getIdCiudad();
            Ciudad idCiudadNew = empleado.getIdCiudad();
            Rol idRolOld = persistentEmpleado.getIdRol();
            Rol idRolNew = empleado.getIdRol();
            Estrato idEstratoOld = persistentEmpleado.getIdEstrato();
            Estrato idEstratoNew = empleado.getIdEstrato();
            if (idDepartamentoNew != null) {
                idDepartamentoNew = em.getReference(idDepartamentoNew.getClass(), idDepartamentoNew.getIdDepartamento());
                empleado.setIdDepartamento(idDepartamentoNew);
            }
            if (idBarrioNew != null) {
                idBarrioNew = em.getReference(idBarrioNew.getClass(), idBarrioNew.getIdBarrio());
                empleado.setIdBarrio(idBarrioNew);
            }
            if (idCiudadNew != null) {
                idCiudadNew = em.getReference(idCiudadNew.getClass(), idCiudadNew.getIdCiudad());
                empleado.setIdCiudad(idCiudadNew);
            }
            if (idRolNew != null) {
                idRolNew = em.getReference(idRolNew.getClass(), idRolNew.getIdRol());
                empleado.setIdRol(idRolNew);
            }
            if (idEstratoNew != null) {
                idEstratoNew = em.getReference(idEstratoNew.getClass(), idEstratoNew.getIdEstrato());
                empleado.setIdEstrato(idEstratoNew);
            }
            empleado = em.merge(empleado);
            if (idDepartamentoOld != null && !idDepartamentoOld.equals(idDepartamentoNew)) {
                idDepartamentoOld.getEmpleadoList().remove(empleado);
                idDepartamentoOld = em.merge(idDepartamentoOld);
            }
            if (idDepartamentoNew != null && !idDepartamentoNew.equals(idDepartamentoOld)) {
                idDepartamentoNew.getEmpleadoList().add(empleado);
                idDepartamentoNew = em.merge(idDepartamentoNew);
            }
            if (idBarrioOld != null && !idBarrioOld.equals(idBarrioNew)) {
                idBarrioOld.getEmpleadoList().remove(empleado);
                idBarrioOld = em.merge(idBarrioOld);
            }
            if (idBarrioNew != null && !idBarrioNew.equals(idBarrioOld)) {
                idBarrioNew.getEmpleadoList().add(empleado);
                idBarrioNew = em.merge(idBarrioNew);
            }
            if (idCiudadOld != null && !idCiudadOld.equals(idCiudadNew)) {
                idCiudadOld.getEmpleadoList().remove(empleado);
                idCiudadOld = em.merge(idCiudadOld);
            }
            if (idCiudadNew != null && !idCiudadNew.equals(idCiudadOld)) {
                idCiudadNew.getEmpleadoList().add(empleado);
                idCiudadNew = em.merge(idCiudadNew);
            }
            if (idRolOld != null && !idRolOld.equals(idRolNew)) {
                idRolOld.getEmpleadoList().remove(empleado);
                idRolOld = em.merge(idRolOld);
            }
            if (idRolNew != null && !idRolNew.equals(idRolOld)) {
                idRolNew.getEmpleadoList().add(empleado);
                idRolNew = em.merge(idRolNew);
            }
            if (idEstratoOld != null && !idEstratoOld.equals(idEstratoNew)) {
                idEstratoOld.getEmpleadoList().remove(empleado);
                idEstratoOld = em.merge(idEstratoOld);
            }
            if (idEstratoNew != null && !idEstratoNew.equals(idEstratoOld)) {
                idEstratoNew.getEmpleadoList().add(empleado);
                idEstratoNew = em.merge(idEstratoNew);
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
                Integer id = empleado.getIdEmpleado();
                if (findEmpleado(id) == null) {
                    throw new NonexistentEntityException("The empleado with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Empleado empleado;
            try {
                empleado = em.getReference(Empleado.class, id);
                empleado.getIdEmpleado();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The empleado with id " + id + " no longer exists.", enfe);
            }
            Departamento idDepartamento = empleado.getIdDepartamento();
            if (idDepartamento != null) {
                idDepartamento.getEmpleadoList().remove(empleado);
                idDepartamento = em.merge(idDepartamento);
            }
            Barrio idBarrio = empleado.getIdBarrio();
            if (idBarrio != null) {
                idBarrio.getEmpleadoList().remove(empleado);
                idBarrio = em.merge(idBarrio);
            }
            Ciudad idCiudad = empleado.getIdCiudad();
            if (idCiudad != null) {
                idCiudad.getEmpleadoList().remove(empleado);
                idCiudad = em.merge(idCiudad);
            }
            Rol idRol = empleado.getIdRol();
            if (idRol != null) {
                idRol.getEmpleadoList().remove(empleado);
                idRol = em.merge(idRol);
            }
            Estrato idEstrato = empleado.getIdEstrato();
            if (idEstrato != null) {
                idEstrato.getEmpleadoList().remove(empleado);
                idEstrato = em.merge(idEstrato);
            }
            em.remove(empleado);
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

    public List<Empleado> findEmpleadoEntities() {
        return findEmpleadoEntities(true, -1, -1);
    }

    public List<Empleado> findEmpleadoEntities(int maxResults, int firstResult) {
        return findEmpleadoEntities(false, maxResults, firstResult);
    }

    private List<Empleado> findEmpleadoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Empleado.class));
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

    public Empleado findEmpleado(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Empleado.class, id);
        } finally {
            em.close();
        }
    }

    public int getEmpleadoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Empleado> rt = cq.from(Empleado.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
