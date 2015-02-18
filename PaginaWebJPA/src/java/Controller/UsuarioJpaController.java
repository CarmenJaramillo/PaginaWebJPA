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
import Entities.Ciudad;
import Entities.Departamento;
import Entities.Rol;
import Entities.Detalleusuariocategoria;
import java.util.ArrayList;
import java.util.List;
import Entities.Detalleusuarioreserva;
import Entities.Usuario;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author Carmen
 */
public class UsuarioJpaController implements Serializable {

    public UsuarioJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Usuario usuario) throws RollbackFailureException, Exception {
        if (usuario.getDetalleusuariocategoriaList() == null) {
            usuario.setDetalleusuariocategoriaList(new ArrayList<Detalleusuariocategoria>());
        }
        if (usuario.getDetalleusuarioreservaList() == null) {
            usuario.setDetalleusuarioreservaList(new ArrayList<Detalleusuarioreserva>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Ciudad idCiudad = usuario.getIdCiudad();
            if (idCiudad != null) {
                idCiudad = em.getReference(idCiudad.getClass(), idCiudad.getIdCiudad());
                usuario.setIdCiudad(idCiudad);
            }
            Departamento idDepartamento = usuario.getIdDepartamento();
            if (idDepartamento != null) {
                idDepartamento = em.getReference(idDepartamento.getClass(), idDepartamento.getIdDepartamento());
                usuario.setIdDepartamento(idDepartamento);
            }
            Rol idRol = usuario.getIdRol();
            if (idRol != null) {
                idRol = em.getReference(idRol.getClass(), idRol.getIdRol());
                usuario.setIdRol(idRol);
            }
            List<Detalleusuariocategoria> attachedDetalleusuariocategoriaList = new ArrayList<Detalleusuariocategoria>();
            for (Detalleusuariocategoria detalleusuariocategoriaListDetalleusuariocategoriaToAttach : usuario.getDetalleusuariocategoriaList()) {
                detalleusuariocategoriaListDetalleusuariocategoriaToAttach = em.getReference(detalleusuariocategoriaListDetalleusuariocategoriaToAttach.getClass(), detalleusuariocategoriaListDetalleusuariocategoriaToAttach.getDetalleUsuarioCategoria());
                attachedDetalleusuariocategoriaList.add(detalleusuariocategoriaListDetalleusuariocategoriaToAttach);
            }
            usuario.setDetalleusuariocategoriaList(attachedDetalleusuariocategoriaList);
            List<Detalleusuarioreserva> attachedDetalleusuarioreservaList = new ArrayList<Detalleusuarioreserva>();
            for (Detalleusuarioreserva detalleusuarioreservaListDetalleusuarioreservaToAttach : usuario.getDetalleusuarioreservaList()) {
                detalleusuarioreservaListDetalleusuarioreservaToAttach = em.getReference(detalleusuarioreservaListDetalleusuarioreservaToAttach.getClass(), detalleusuarioreservaListDetalleusuarioreservaToAttach.getIdDetalleUsuarioReserva());
                attachedDetalleusuarioreservaList.add(detalleusuarioreservaListDetalleusuarioreservaToAttach);
            }
            usuario.setDetalleusuarioreservaList(attachedDetalleusuarioreservaList);
            em.persist(usuario);
            if (idCiudad != null) {
                idCiudad.getUsuarioList().add(usuario);
                idCiudad = em.merge(idCiudad);
            }
            if (idDepartamento != null) {
                idDepartamento.getUsuarioList().add(usuario);
                idDepartamento = em.merge(idDepartamento);
            }
            if (idRol != null) {
                idRol.getUsuarioList().add(usuario);
                idRol = em.merge(idRol);
            }
            for (Detalleusuariocategoria detalleusuariocategoriaListDetalleusuariocategoria : usuario.getDetalleusuariocategoriaList()) {
                Usuario oldIdUsuarioOfDetalleusuariocategoriaListDetalleusuariocategoria = detalleusuariocategoriaListDetalleusuariocategoria.getIdUsuario();
                detalleusuariocategoriaListDetalleusuariocategoria.setIdUsuario(usuario);
                detalleusuariocategoriaListDetalleusuariocategoria = em.merge(detalleusuariocategoriaListDetalleusuariocategoria);
                if (oldIdUsuarioOfDetalleusuariocategoriaListDetalleusuariocategoria != null) {
                    oldIdUsuarioOfDetalleusuariocategoriaListDetalleusuariocategoria.getDetalleusuariocategoriaList().remove(detalleusuariocategoriaListDetalleusuariocategoria);
                    oldIdUsuarioOfDetalleusuariocategoriaListDetalleusuariocategoria = em.merge(oldIdUsuarioOfDetalleusuariocategoriaListDetalleusuariocategoria);
                }
            }
            for (Detalleusuarioreserva detalleusuarioreservaListDetalleusuarioreserva : usuario.getDetalleusuarioreservaList()) {
                Usuario oldIdUsuarioOfDetalleusuarioreservaListDetalleusuarioreserva = detalleusuarioreservaListDetalleusuarioreserva.getIdUsuario();
                detalleusuarioreservaListDetalleusuarioreserva.setIdUsuario(usuario);
                detalleusuarioreservaListDetalleusuarioreserva = em.merge(detalleusuarioreservaListDetalleusuarioreserva);
                if (oldIdUsuarioOfDetalleusuarioreservaListDetalleusuarioreserva != null) {
                    oldIdUsuarioOfDetalleusuarioreservaListDetalleusuarioreserva.getDetalleusuarioreservaList().remove(detalleusuarioreservaListDetalleusuarioreserva);
                    oldIdUsuarioOfDetalleusuarioreservaListDetalleusuarioreserva = em.merge(oldIdUsuarioOfDetalleusuarioreservaListDetalleusuarioreserva);
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

    public void edit(Usuario usuario) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Usuario persistentUsuario = em.find(Usuario.class, usuario.getIdUsuario());
            Ciudad idCiudadOld = persistentUsuario.getIdCiudad();
            Ciudad idCiudadNew = usuario.getIdCiudad();
            Departamento idDepartamentoOld = persistentUsuario.getIdDepartamento();
            Departamento idDepartamentoNew = usuario.getIdDepartamento();
            Rol idRolOld = persistentUsuario.getIdRol();
            Rol idRolNew = usuario.getIdRol();
            List<Detalleusuariocategoria> detalleusuariocategoriaListOld = persistentUsuario.getDetalleusuariocategoriaList();
            List<Detalleusuariocategoria> detalleusuariocategoriaListNew = usuario.getDetalleusuariocategoriaList();
            List<Detalleusuarioreserva> detalleusuarioreservaListOld = persistentUsuario.getDetalleusuarioreservaList();
            List<Detalleusuarioreserva> detalleusuarioreservaListNew = usuario.getDetalleusuarioreservaList();
            List<String> illegalOrphanMessages = null;
            for (Detalleusuariocategoria detalleusuariocategoriaListOldDetalleusuariocategoria : detalleusuariocategoriaListOld) {
                if (!detalleusuariocategoriaListNew.contains(detalleusuariocategoriaListOldDetalleusuariocategoria)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Detalleusuariocategoria " + detalleusuariocategoriaListOldDetalleusuariocategoria + " since its idUsuario field is not nullable.");
                }
            }
            for (Detalleusuarioreserva detalleusuarioreservaListOldDetalleusuarioreserva : detalleusuarioreservaListOld) {
                if (!detalleusuarioreservaListNew.contains(detalleusuarioreservaListOldDetalleusuarioreserva)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Detalleusuarioreserva " + detalleusuarioreservaListOldDetalleusuarioreserva + " since its idUsuario field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idCiudadNew != null) {
                idCiudadNew = em.getReference(idCiudadNew.getClass(), idCiudadNew.getIdCiudad());
                usuario.setIdCiudad(idCiudadNew);
            }
            if (idDepartamentoNew != null) {
                idDepartamentoNew = em.getReference(idDepartamentoNew.getClass(), idDepartamentoNew.getIdDepartamento());
                usuario.setIdDepartamento(idDepartamentoNew);
            }
            if (idRolNew != null) {
                idRolNew = em.getReference(idRolNew.getClass(), idRolNew.getIdRol());
                usuario.setIdRol(idRolNew);
            }
            List<Detalleusuariocategoria> attachedDetalleusuariocategoriaListNew = new ArrayList<Detalleusuariocategoria>();
            for (Detalleusuariocategoria detalleusuariocategoriaListNewDetalleusuariocategoriaToAttach : detalleusuariocategoriaListNew) {
                detalleusuariocategoriaListNewDetalleusuariocategoriaToAttach = em.getReference(detalleusuariocategoriaListNewDetalleusuariocategoriaToAttach.getClass(), detalleusuariocategoriaListNewDetalleusuariocategoriaToAttach.getDetalleUsuarioCategoria());
                attachedDetalleusuariocategoriaListNew.add(detalleusuariocategoriaListNewDetalleusuariocategoriaToAttach);
            }
            detalleusuariocategoriaListNew = attachedDetalleusuariocategoriaListNew;
            usuario.setDetalleusuariocategoriaList(detalleusuariocategoriaListNew);
            List<Detalleusuarioreserva> attachedDetalleusuarioreservaListNew = new ArrayList<Detalleusuarioreserva>();
            for (Detalleusuarioreserva detalleusuarioreservaListNewDetalleusuarioreservaToAttach : detalleusuarioreservaListNew) {
                detalleusuarioreservaListNewDetalleusuarioreservaToAttach = em.getReference(detalleusuarioreservaListNewDetalleusuarioreservaToAttach.getClass(), detalleusuarioreservaListNewDetalleusuarioreservaToAttach.getIdDetalleUsuarioReserva());
                attachedDetalleusuarioreservaListNew.add(detalleusuarioreservaListNewDetalleusuarioreservaToAttach);
            }
            detalleusuarioreservaListNew = attachedDetalleusuarioreservaListNew;
            usuario.setDetalleusuarioreservaList(detalleusuarioreservaListNew);
            usuario = em.merge(usuario);
            if (idCiudadOld != null && !idCiudadOld.equals(idCiudadNew)) {
                idCiudadOld.getUsuarioList().remove(usuario);
                idCiudadOld = em.merge(idCiudadOld);
            }
            if (idCiudadNew != null && !idCiudadNew.equals(idCiudadOld)) {
                idCiudadNew.getUsuarioList().add(usuario);
                idCiudadNew = em.merge(idCiudadNew);
            }
            if (idDepartamentoOld != null && !idDepartamentoOld.equals(idDepartamentoNew)) {
                idDepartamentoOld.getUsuarioList().remove(usuario);
                idDepartamentoOld = em.merge(idDepartamentoOld);
            }
            if (idDepartamentoNew != null && !idDepartamentoNew.equals(idDepartamentoOld)) {
                idDepartamentoNew.getUsuarioList().add(usuario);
                idDepartamentoNew = em.merge(idDepartamentoNew);
            }
            if (idRolOld != null && !idRolOld.equals(idRolNew)) {
                idRolOld.getUsuarioList().remove(usuario);
                idRolOld = em.merge(idRolOld);
            }
            if (idRolNew != null && !idRolNew.equals(idRolOld)) {
                idRolNew.getUsuarioList().add(usuario);
                idRolNew = em.merge(idRolNew);
            }
            for (Detalleusuariocategoria detalleusuariocategoriaListNewDetalleusuariocategoria : detalleusuariocategoriaListNew) {
                if (!detalleusuariocategoriaListOld.contains(detalleusuariocategoriaListNewDetalleusuariocategoria)) {
                    Usuario oldIdUsuarioOfDetalleusuariocategoriaListNewDetalleusuariocategoria = detalleusuariocategoriaListNewDetalleusuariocategoria.getIdUsuario();
                    detalleusuariocategoriaListNewDetalleusuariocategoria.setIdUsuario(usuario);
                    detalleusuariocategoriaListNewDetalleusuariocategoria = em.merge(detalleusuariocategoriaListNewDetalleusuariocategoria);
                    if (oldIdUsuarioOfDetalleusuariocategoriaListNewDetalleusuariocategoria != null && !oldIdUsuarioOfDetalleusuariocategoriaListNewDetalleusuariocategoria.equals(usuario)) {
                        oldIdUsuarioOfDetalleusuariocategoriaListNewDetalleusuariocategoria.getDetalleusuariocategoriaList().remove(detalleusuariocategoriaListNewDetalleusuariocategoria);
                        oldIdUsuarioOfDetalleusuariocategoriaListNewDetalleusuariocategoria = em.merge(oldIdUsuarioOfDetalleusuariocategoriaListNewDetalleusuariocategoria);
                    }
                }
            }
            for (Detalleusuarioreserva detalleusuarioreservaListNewDetalleusuarioreserva : detalleusuarioreservaListNew) {
                if (!detalleusuarioreservaListOld.contains(detalleusuarioreservaListNewDetalleusuarioreserva)) {
                    Usuario oldIdUsuarioOfDetalleusuarioreservaListNewDetalleusuarioreserva = detalleusuarioreservaListNewDetalleusuarioreserva.getIdUsuario();
                    detalleusuarioreservaListNewDetalleusuarioreserva.setIdUsuario(usuario);
                    detalleusuarioreservaListNewDetalleusuarioreserva = em.merge(detalleusuarioreservaListNewDetalleusuarioreserva);
                    if (oldIdUsuarioOfDetalleusuarioreservaListNewDetalleusuarioreserva != null && !oldIdUsuarioOfDetalleusuarioreservaListNewDetalleusuarioreserva.equals(usuario)) {
                        oldIdUsuarioOfDetalleusuarioreservaListNewDetalleusuarioreserva.getDetalleusuarioreservaList().remove(detalleusuarioreservaListNewDetalleusuarioreserva);
                        oldIdUsuarioOfDetalleusuarioreservaListNewDetalleusuarioreserva = em.merge(oldIdUsuarioOfDetalleusuarioreservaListNewDetalleusuarioreserva);
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
                Integer id = usuario.getIdUsuario();
                if (findUsuario(id) == null) {
                    throw new NonexistentEntityException("The usuario with id " + id + " no longer exists.");
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
            Usuario usuario;
            try {
                usuario = em.getReference(Usuario.class, id);
                usuario.getIdUsuario();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The usuario with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Detalleusuariocategoria> detalleusuariocategoriaListOrphanCheck = usuario.getDetalleusuariocategoriaList();
            for (Detalleusuariocategoria detalleusuariocategoriaListOrphanCheckDetalleusuariocategoria : detalleusuariocategoriaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Usuario (" + usuario + ") cannot be destroyed since the Detalleusuariocategoria " + detalleusuariocategoriaListOrphanCheckDetalleusuariocategoria + " in its detalleusuariocategoriaList field has a non-nullable idUsuario field.");
            }
            List<Detalleusuarioreserva> detalleusuarioreservaListOrphanCheck = usuario.getDetalleusuarioreservaList();
            for (Detalleusuarioreserva detalleusuarioreservaListOrphanCheckDetalleusuarioreserva : detalleusuarioreservaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Usuario (" + usuario + ") cannot be destroyed since the Detalleusuarioreserva " + detalleusuarioreservaListOrphanCheckDetalleusuarioreserva + " in its detalleusuarioreservaList field has a non-nullable idUsuario field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Ciudad idCiudad = usuario.getIdCiudad();
            if (idCiudad != null) {
                idCiudad.getUsuarioList().remove(usuario);
                idCiudad = em.merge(idCiudad);
            }
            Departamento idDepartamento = usuario.getIdDepartamento();
            if (idDepartamento != null) {
                idDepartamento.getUsuarioList().remove(usuario);
                idDepartamento = em.merge(idDepartamento);
            }
            Rol idRol = usuario.getIdRol();
            if (idRol != null) {
                idRol.getUsuarioList().remove(usuario);
                idRol = em.merge(idRol);
            }
            em.remove(usuario);
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

    public List<Usuario> findUsuarioEntities() {
        return findUsuarioEntities(true, -1, -1);
    }

    public List<Usuario> findUsuarioEntities(int maxResults, int firstResult) {
        return findUsuarioEntities(false, maxResults, firstResult);
    }

    private List<Usuario> findUsuarioEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Usuario.class));
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

    public Usuario findUsuario(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Usuario.class, id);
        } finally {
            em.close();
        }
    }

    public int getUsuarioCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Usuario> rt = cq.from(Usuario.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
