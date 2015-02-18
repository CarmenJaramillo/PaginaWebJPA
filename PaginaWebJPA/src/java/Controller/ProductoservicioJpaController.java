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
import Entities.Categoria;
import Entities.Detallefacturaproductoscio;
import java.util.ArrayList;
import java.util.List;
import Entities.Detalleclienteproducto;
import Entities.Productoservicio;
import Entities.Reserva;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author Carmen
 */
public class ProductoservicioJpaController implements Serializable {

    public ProductoservicioJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Productoservicio productoservicio) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (productoservicio.getDetallefacturaproductoscioList() == null) {
            productoservicio.setDetallefacturaproductoscioList(new ArrayList<Detallefacturaproductoscio>());
        }
        if (productoservicio.getDetalleclienteproductoList() == null) {
            productoservicio.setDetalleclienteproductoList(new ArrayList<Detalleclienteproducto>());
        }
        if (productoservicio.getReservaList() == null) {
            productoservicio.setReservaList(new ArrayList<Reserva>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Categoria idCategoria = productoservicio.getIdCategoria();
            if (idCategoria != null) {
                idCategoria = em.getReference(idCategoria.getClass(), idCategoria.getIdCategoria());
                productoservicio.setIdCategoria(idCategoria);
            }
            List<Detallefacturaproductoscio> attachedDetallefacturaproductoscioList = new ArrayList<Detallefacturaproductoscio>();
            for (Detallefacturaproductoscio detallefacturaproductoscioListDetallefacturaproductoscioToAttach : productoservicio.getDetallefacturaproductoscioList()) {
                detallefacturaproductoscioListDetallefacturaproductoscioToAttach = em.getReference(detallefacturaproductoscioListDetallefacturaproductoscioToAttach.getClass(), detallefacturaproductoscioListDetallefacturaproductoscioToAttach.getIdDetalleFacturaProductoScio());
                attachedDetallefacturaproductoscioList.add(detallefacturaproductoscioListDetallefacturaproductoscioToAttach);
            }
            productoservicio.setDetallefacturaproductoscioList(attachedDetallefacturaproductoscioList);
            List<Detalleclienteproducto> attachedDetalleclienteproductoList = new ArrayList<Detalleclienteproducto>();
            for (Detalleclienteproducto detalleclienteproductoListDetalleclienteproductoToAttach : productoservicio.getDetalleclienteproductoList()) {
                detalleclienteproductoListDetalleclienteproductoToAttach = em.getReference(detalleclienteproductoListDetalleclienteproductoToAttach.getClass(), detalleclienteproductoListDetalleclienteproductoToAttach.getIdDetalleClienteProducto());
                attachedDetalleclienteproductoList.add(detalleclienteproductoListDetalleclienteproductoToAttach);
            }
            productoservicio.setDetalleclienteproductoList(attachedDetalleclienteproductoList);
            List<Reserva> attachedReservaList = new ArrayList<Reserva>();
            for (Reserva reservaListReservaToAttach : productoservicio.getReservaList()) {
                reservaListReservaToAttach = em.getReference(reservaListReservaToAttach.getClass(), reservaListReservaToAttach.getIdReserva());
                attachedReservaList.add(reservaListReservaToAttach);
            }
            productoservicio.setReservaList(attachedReservaList);
            em.persist(productoservicio);
            if (idCategoria != null) {
                idCategoria.getProductoservicioList().add(productoservicio);
                idCategoria = em.merge(idCategoria);
            }
            for (Detallefacturaproductoscio detallefacturaproductoscioListDetallefacturaproductoscio : productoservicio.getDetallefacturaproductoscioList()) {
                Productoservicio oldIdProductoScioOfDetallefacturaproductoscioListDetallefacturaproductoscio = detallefacturaproductoscioListDetallefacturaproductoscio.getIdProductoScio();
                detallefacturaproductoscioListDetallefacturaproductoscio.setIdProductoScio(productoservicio);
                detallefacturaproductoscioListDetallefacturaproductoscio = em.merge(detallefacturaproductoscioListDetallefacturaproductoscio);
                if (oldIdProductoScioOfDetallefacturaproductoscioListDetallefacturaproductoscio != null) {
                    oldIdProductoScioOfDetallefacturaproductoscioListDetallefacturaproductoscio.getDetallefacturaproductoscioList().remove(detallefacturaproductoscioListDetallefacturaproductoscio);
                    oldIdProductoScioOfDetallefacturaproductoscioListDetallefacturaproductoscio = em.merge(oldIdProductoScioOfDetallefacturaproductoscioListDetallefacturaproductoscio);
                }
            }
            for (Detalleclienteproducto detalleclienteproductoListDetalleclienteproducto : productoservicio.getDetalleclienteproductoList()) {
                Productoservicio oldIdProductoScioOfDetalleclienteproductoListDetalleclienteproducto = detalleclienteproductoListDetalleclienteproducto.getIdProductoScio();
                detalleclienteproductoListDetalleclienteproducto.setIdProductoScio(productoservicio);
                detalleclienteproductoListDetalleclienteproducto = em.merge(detalleclienteproductoListDetalleclienteproducto);
                if (oldIdProductoScioOfDetalleclienteproductoListDetalleclienteproducto != null) {
                    oldIdProductoScioOfDetalleclienteproductoListDetalleclienteproducto.getDetalleclienteproductoList().remove(detalleclienteproductoListDetalleclienteproducto);
                    oldIdProductoScioOfDetalleclienteproductoListDetalleclienteproducto = em.merge(oldIdProductoScioOfDetalleclienteproductoListDetalleclienteproducto);
                }
            }
            for (Reserva reservaListReserva : productoservicio.getReservaList()) {
                Productoservicio oldIdProductoScioOfReservaListReserva = reservaListReserva.getIdProductoScio();
                reservaListReserva.setIdProductoScio(productoservicio);
                reservaListReserva = em.merge(reservaListReserva);
                if (oldIdProductoScioOfReservaListReserva != null) {
                    oldIdProductoScioOfReservaListReserva.getReservaList().remove(reservaListReserva);
                    oldIdProductoScioOfReservaListReserva = em.merge(oldIdProductoScioOfReservaListReserva);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findProductoservicio(productoservicio.getIdProductoScio()) != null) {
                throw new PreexistingEntityException("Productoservicio " + productoservicio + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Productoservicio productoservicio) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Productoservicio persistentProductoservicio = em.find(Productoservicio.class, productoservicio.getIdProductoScio());
            Categoria idCategoriaOld = persistentProductoservicio.getIdCategoria();
            Categoria idCategoriaNew = productoservicio.getIdCategoria();
            List<Detallefacturaproductoscio> detallefacturaproductoscioListOld = persistentProductoservicio.getDetallefacturaproductoscioList();
            List<Detallefacturaproductoscio> detallefacturaproductoscioListNew = productoservicio.getDetallefacturaproductoscioList();
            List<Detalleclienteproducto> detalleclienteproductoListOld = persistentProductoservicio.getDetalleclienteproductoList();
            List<Detalleclienteproducto> detalleclienteproductoListNew = productoservicio.getDetalleclienteproductoList();
            List<Reserva> reservaListOld = persistentProductoservicio.getReservaList();
            List<Reserva> reservaListNew = productoservicio.getReservaList();
            List<String> illegalOrphanMessages = null;
            for (Detallefacturaproductoscio detallefacturaproductoscioListOldDetallefacturaproductoscio : detallefacturaproductoscioListOld) {
                if (!detallefacturaproductoscioListNew.contains(detallefacturaproductoscioListOldDetallefacturaproductoscio)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Detallefacturaproductoscio " + detallefacturaproductoscioListOldDetallefacturaproductoscio + " since its idProductoScio field is not nullable.");
                }
            }
            for (Detalleclienteproducto detalleclienteproductoListOldDetalleclienteproducto : detalleclienteproductoListOld) {
                if (!detalleclienteproductoListNew.contains(detalleclienteproductoListOldDetalleclienteproducto)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Detalleclienteproducto " + detalleclienteproductoListOldDetalleclienteproducto + " since its idProductoScio field is not nullable.");
                }
            }
            for (Reserva reservaListOldReserva : reservaListOld) {
                if (!reservaListNew.contains(reservaListOldReserva)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Reserva " + reservaListOldReserva + " since its idProductoScio field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idCategoriaNew != null) {
                idCategoriaNew = em.getReference(idCategoriaNew.getClass(), idCategoriaNew.getIdCategoria());
                productoservicio.setIdCategoria(idCategoriaNew);
            }
            List<Detallefacturaproductoscio> attachedDetallefacturaproductoscioListNew = new ArrayList<Detallefacturaproductoscio>();
            for (Detallefacturaproductoscio detallefacturaproductoscioListNewDetallefacturaproductoscioToAttach : detallefacturaproductoscioListNew) {
                detallefacturaproductoscioListNewDetallefacturaproductoscioToAttach = em.getReference(detallefacturaproductoscioListNewDetallefacturaproductoscioToAttach.getClass(), detallefacturaproductoscioListNewDetallefacturaproductoscioToAttach.getIdDetalleFacturaProductoScio());
                attachedDetallefacturaproductoscioListNew.add(detallefacturaproductoscioListNewDetallefacturaproductoscioToAttach);
            }
            detallefacturaproductoscioListNew = attachedDetallefacturaproductoscioListNew;
            productoservicio.setDetallefacturaproductoscioList(detallefacturaproductoscioListNew);
            List<Detalleclienteproducto> attachedDetalleclienteproductoListNew = new ArrayList<Detalleclienteproducto>();
            for (Detalleclienteproducto detalleclienteproductoListNewDetalleclienteproductoToAttach : detalleclienteproductoListNew) {
                detalleclienteproductoListNewDetalleclienteproductoToAttach = em.getReference(detalleclienteproductoListNewDetalleclienteproductoToAttach.getClass(), detalleclienteproductoListNewDetalleclienteproductoToAttach.getIdDetalleClienteProducto());
                attachedDetalleclienteproductoListNew.add(detalleclienteproductoListNewDetalleclienteproductoToAttach);
            }
            detalleclienteproductoListNew = attachedDetalleclienteproductoListNew;
            productoservicio.setDetalleclienteproductoList(detalleclienteproductoListNew);
            List<Reserva> attachedReservaListNew = new ArrayList<Reserva>();
            for (Reserva reservaListNewReservaToAttach : reservaListNew) {
                reservaListNewReservaToAttach = em.getReference(reservaListNewReservaToAttach.getClass(), reservaListNewReservaToAttach.getIdReserva());
                attachedReservaListNew.add(reservaListNewReservaToAttach);
            }
            reservaListNew = attachedReservaListNew;
            productoservicio.setReservaList(reservaListNew);
            productoservicio = em.merge(productoservicio);
            if (idCategoriaOld != null && !idCategoriaOld.equals(idCategoriaNew)) {
                idCategoriaOld.getProductoservicioList().remove(productoservicio);
                idCategoriaOld = em.merge(idCategoriaOld);
            }
            if (idCategoriaNew != null && !idCategoriaNew.equals(idCategoriaOld)) {
                idCategoriaNew.getProductoservicioList().add(productoservicio);
                idCategoriaNew = em.merge(idCategoriaNew);
            }
            for (Detallefacturaproductoscio detallefacturaproductoscioListNewDetallefacturaproductoscio : detallefacturaproductoscioListNew) {
                if (!detallefacturaproductoscioListOld.contains(detallefacturaproductoscioListNewDetallefacturaproductoscio)) {
                    Productoservicio oldIdProductoScioOfDetallefacturaproductoscioListNewDetallefacturaproductoscio = detallefacturaproductoscioListNewDetallefacturaproductoscio.getIdProductoScio();
                    detallefacturaproductoscioListNewDetallefacturaproductoscio.setIdProductoScio(productoservicio);
                    detallefacturaproductoscioListNewDetallefacturaproductoscio = em.merge(detallefacturaproductoscioListNewDetallefacturaproductoscio);
                    if (oldIdProductoScioOfDetallefacturaproductoscioListNewDetallefacturaproductoscio != null && !oldIdProductoScioOfDetallefacturaproductoscioListNewDetallefacturaproductoscio.equals(productoservicio)) {
                        oldIdProductoScioOfDetallefacturaproductoscioListNewDetallefacturaproductoscio.getDetallefacturaproductoscioList().remove(detallefacturaproductoscioListNewDetallefacturaproductoscio);
                        oldIdProductoScioOfDetallefacturaproductoscioListNewDetallefacturaproductoscio = em.merge(oldIdProductoScioOfDetallefacturaproductoscioListNewDetallefacturaproductoscio);
                    }
                }
            }
            for (Detalleclienteproducto detalleclienteproductoListNewDetalleclienteproducto : detalleclienteproductoListNew) {
                if (!detalleclienteproductoListOld.contains(detalleclienteproductoListNewDetalleclienteproducto)) {
                    Productoservicio oldIdProductoScioOfDetalleclienteproductoListNewDetalleclienteproducto = detalleclienteproductoListNewDetalleclienteproducto.getIdProductoScio();
                    detalleclienteproductoListNewDetalleclienteproducto.setIdProductoScio(productoservicio);
                    detalleclienteproductoListNewDetalleclienteproducto = em.merge(detalleclienteproductoListNewDetalleclienteproducto);
                    if (oldIdProductoScioOfDetalleclienteproductoListNewDetalleclienteproducto != null && !oldIdProductoScioOfDetalleclienteproductoListNewDetalleclienteproducto.equals(productoservicio)) {
                        oldIdProductoScioOfDetalleclienteproductoListNewDetalleclienteproducto.getDetalleclienteproductoList().remove(detalleclienteproductoListNewDetalleclienteproducto);
                        oldIdProductoScioOfDetalleclienteproductoListNewDetalleclienteproducto = em.merge(oldIdProductoScioOfDetalleclienteproductoListNewDetalleclienteproducto);
                    }
                }
            }
            for (Reserva reservaListNewReserva : reservaListNew) {
                if (!reservaListOld.contains(reservaListNewReserva)) {
                    Productoservicio oldIdProductoScioOfReservaListNewReserva = reservaListNewReserva.getIdProductoScio();
                    reservaListNewReserva.setIdProductoScio(productoservicio);
                    reservaListNewReserva = em.merge(reservaListNewReserva);
                    if (oldIdProductoScioOfReservaListNewReserva != null && !oldIdProductoScioOfReservaListNewReserva.equals(productoservicio)) {
                        oldIdProductoScioOfReservaListNewReserva.getReservaList().remove(reservaListNewReserva);
                        oldIdProductoScioOfReservaListNewReserva = em.merge(oldIdProductoScioOfReservaListNewReserva);
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
                Integer id = productoservicio.getIdProductoScio();
                if (findProductoservicio(id) == null) {
                    throw new NonexistentEntityException("The productoservicio with id " + id + " no longer exists.");
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
            Productoservicio productoservicio;
            try {
                productoservicio = em.getReference(Productoservicio.class, id);
                productoservicio.getIdProductoScio();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The productoservicio with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Detallefacturaproductoscio> detallefacturaproductoscioListOrphanCheck = productoservicio.getDetallefacturaproductoscioList();
            for (Detallefacturaproductoscio detallefacturaproductoscioListOrphanCheckDetallefacturaproductoscio : detallefacturaproductoscioListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Productoservicio (" + productoservicio + ") cannot be destroyed since the Detallefacturaproductoscio " + detallefacturaproductoscioListOrphanCheckDetallefacturaproductoscio + " in its detallefacturaproductoscioList field has a non-nullable idProductoScio field.");
            }
            List<Detalleclienteproducto> detalleclienteproductoListOrphanCheck = productoservicio.getDetalleclienteproductoList();
            for (Detalleclienteproducto detalleclienteproductoListOrphanCheckDetalleclienteproducto : detalleclienteproductoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Productoservicio (" + productoservicio + ") cannot be destroyed since the Detalleclienteproducto " + detalleclienteproductoListOrphanCheckDetalleclienteproducto + " in its detalleclienteproductoList field has a non-nullable idProductoScio field.");
            }
            List<Reserva> reservaListOrphanCheck = productoservicio.getReservaList();
            for (Reserva reservaListOrphanCheckReserva : reservaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Productoservicio (" + productoservicio + ") cannot be destroyed since the Reserva " + reservaListOrphanCheckReserva + " in its reservaList field has a non-nullable idProductoScio field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Categoria idCategoria = productoservicio.getIdCategoria();
            if (idCategoria != null) {
                idCategoria.getProductoservicioList().remove(productoservicio);
                idCategoria = em.merge(idCategoria);
            }
            em.remove(productoservicio);
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

    public List<Productoservicio> findProductoservicioEntities() {
        return findProductoservicioEntities(true, -1, -1);
    }

    public List<Productoservicio> findProductoservicioEntities(int maxResults, int firstResult) {
        return findProductoservicioEntities(false, maxResults, firstResult);
    }

    private List<Productoservicio> findProductoservicioEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Productoservicio.class));
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

    public Productoservicio findProductoservicio(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Productoservicio.class, id);
        } finally {
            em.close();
        }
    }

    public int getProductoservicioCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Productoservicio> rt = cq.from(Productoservicio.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
