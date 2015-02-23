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
import Entities.ProductoServicio;
import Entities.Reserva;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author Carmen
 */
public class ProductoServicioJpaController implements Serializable {

    public ProductoServicioJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(ProductoServicio productoServicio) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (productoServicio.getDetallefacturaproductoscioList() == null) {
            productoServicio.setDetallefacturaproductoscioList(new ArrayList<Detallefacturaproductoscio>());
        }
        if (productoServicio.getDetalleclienteproductoList() == null) {
            productoServicio.setDetalleclienteproductoList(new ArrayList<Detalleclienteproducto>());
        }
        if (productoServicio.getReservaList() == null) {
            productoServicio.setReservaList(new ArrayList<Reserva>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Categoria idCategoria = productoServicio.getIdCategoria();
            if (idCategoria != null) {
                idCategoria = em.getReference(idCategoria.getClass(), idCategoria.getIdCategoria());
                productoServicio.setIdCategoria(idCategoria);
            }
            List<Detallefacturaproductoscio> attachedDetallefacturaproductoscioList = new ArrayList<Detallefacturaproductoscio>();
            for (Detallefacturaproductoscio detallefacturaproductoscioListDetallefacturaproductoscioToAttach : productoServicio.getDetallefacturaproductoscioList()) {
                detallefacturaproductoscioListDetallefacturaproductoscioToAttach = em.getReference(detallefacturaproductoscioListDetallefacturaproductoscioToAttach.getClass(), detallefacturaproductoscioListDetallefacturaproductoscioToAttach.getIdDetalleFacturaProductoScio());
                attachedDetallefacturaproductoscioList.add(detallefacturaproductoscioListDetallefacturaproductoscioToAttach);
            }
            productoServicio.setDetallefacturaproductoscioList(attachedDetallefacturaproductoscioList);
            List<Detalleclienteproducto> attachedDetalleclienteproductoList = new ArrayList<Detalleclienteproducto>();
            for (Detalleclienteproducto detalleclienteproductoListDetalleclienteproductoToAttach : productoServicio.getDetalleclienteproductoList()) {
                detalleclienteproductoListDetalleclienteproductoToAttach = em.getReference(detalleclienteproductoListDetalleclienteproductoToAttach.getClass(), detalleclienteproductoListDetalleclienteproductoToAttach.getIdDetalleClienteProducto());
                attachedDetalleclienteproductoList.add(detalleclienteproductoListDetalleclienteproductoToAttach);
            }
            productoServicio.setDetalleclienteproductoList(attachedDetalleclienteproductoList);
            List<Reserva> attachedReservaList = new ArrayList<Reserva>();
            for (Reserva reservaListReservaToAttach : productoServicio.getReservaList()) {
                reservaListReservaToAttach = em.getReference(reservaListReservaToAttach.getClass(), reservaListReservaToAttach.getIdreserva());
                attachedReservaList.add(reservaListReservaToAttach);
            }
            productoServicio.setReservaList(attachedReservaList);
            em.persist(productoServicio);
            if (idCategoria != null) {
                idCategoria.getProductoServicioList().add(productoServicio);
                idCategoria = em.merge(idCategoria);
            }
            for (Detallefacturaproductoscio detallefacturaproductoscioListDetallefacturaproductoscio : productoServicio.getDetallefacturaproductoscioList()) {
                ProductoServicio oldIdProductoScioOfDetallefacturaproductoscioListDetallefacturaproductoscio = detallefacturaproductoscioListDetallefacturaproductoscio.getIdProductoScio();
                detallefacturaproductoscioListDetallefacturaproductoscio.setIdProductoScio(productoServicio);
                detallefacturaproductoscioListDetallefacturaproductoscio = em.merge(detallefacturaproductoscioListDetallefacturaproductoscio);
                if (oldIdProductoScioOfDetallefacturaproductoscioListDetallefacturaproductoscio != null) {
                    oldIdProductoScioOfDetallefacturaproductoscioListDetallefacturaproductoscio.getDetallefacturaproductoscioList().remove(detallefacturaproductoscioListDetallefacturaproductoscio);
                    oldIdProductoScioOfDetallefacturaproductoscioListDetallefacturaproductoscio = em.merge(oldIdProductoScioOfDetallefacturaproductoscioListDetallefacturaproductoscio);
                }
            }
            for (Detalleclienteproducto detalleclienteproductoListDetalleclienteproducto : productoServicio.getDetalleclienteproductoList()) {
                ProductoServicio oldIdProductoScioOfDetalleclienteproductoListDetalleclienteproducto = detalleclienteproductoListDetalleclienteproducto.getIdProductoScio();
                detalleclienteproductoListDetalleclienteproducto.setIdProductoScio(productoServicio);
                detalleclienteproductoListDetalleclienteproducto = em.merge(detalleclienteproductoListDetalleclienteproducto);
                if (oldIdProductoScioOfDetalleclienteproductoListDetalleclienteproducto != null) {
                    oldIdProductoScioOfDetalleclienteproductoListDetalleclienteproducto.getDetalleclienteproductoList().remove(detalleclienteproductoListDetalleclienteproducto);
                    oldIdProductoScioOfDetalleclienteproductoListDetalleclienteproducto = em.merge(oldIdProductoScioOfDetalleclienteproductoListDetalleclienteproducto);
                }
            }
            for (Reserva reservaListReserva : productoServicio.getReservaList()) {
                ProductoServicio oldIdProductoOfReservaListReserva = reservaListReserva.getIdProducto();
                reservaListReserva.setIdProducto(productoServicio);
                reservaListReserva = em.merge(reservaListReserva);
                if (oldIdProductoOfReservaListReserva != null) {
                    oldIdProductoOfReservaListReserva.getReservaList().remove(reservaListReserva);
                    oldIdProductoOfReservaListReserva = em.merge(oldIdProductoOfReservaListReserva);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findProductoServicio(productoServicio.getIdProductoScio()) != null) {
                throw new PreexistingEntityException("ProductoServicio " + productoServicio + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(ProductoServicio productoServicio) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            ProductoServicio persistentProductoServicio = em.find(ProductoServicio.class, productoServicio.getIdProductoScio());
            Categoria idCategoriaOld = persistentProductoServicio.getIdCategoria();
            Categoria idCategoriaNew = productoServicio.getIdCategoria();
            List<Detallefacturaproductoscio> detallefacturaproductoscioListOld = persistentProductoServicio.getDetallefacturaproductoscioList();
            List<Detallefacturaproductoscio> detallefacturaproductoscioListNew = productoServicio.getDetallefacturaproductoscioList();
            List<Detalleclienteproducto> detalleclienteproductoListOld = persistentProductoServicio.getDetalleclienteproductoList();
            List<Detalleclienteproducto> detalleclienteproductoListNew = productoServicio.getDetalleclienteproductoList();
            List<Reserva> reservaListOld = persistentProductoServicio.getReservaList();
            List<Reserva> reservaListNew = productoServicio.getReservaList();
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
                    illegalOrphanMessages.add("You must retain Reserva " + reservaListOldReserva + " since its idProducto field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idCategoriaNew != null) {
                idCategoriaNew = em.getReference(idCategoriaNew.getClass(), idCategoriaNew.getIdCategoria());
                productoServicio.setIdCategoria(idCategoriaNew);
            }
            List<Detallefacturaproductoscio> attachedDetallefacturaproductoscioListNew = new ArrayList<Detallefacturaproductoscio>();
            for (Detallefacturaproductoscio detallefacturaproductoscioListNewDetallefacturaproductoscioToAttach : detallefacturaproductoscioListNew) {
                detallefacturaproductoscioListNewDetallefacturaproductoscioToAttach = em.getReference(detallefacturaproductoscioListNewDetallefacturaproductoscioToAttach.getClass(), detallefacturaproductoscioListNewDetallefacturaproductoscioToAttach.getIdDetalleFacturaProductoScio());
                attachedDetallefacturaproductoscioListNew.add(detallefacturaproductoscioListNewDetallefacturaproductoscioToAttach);
            }
            detallefacturaproductoscioListNew = attachedDetallefacturaproductoscioListNew;
            productoServicio.setDetallefacturaproductoscioList(detallefacturaproductoscioListNew);
            List<Detalleclienteproducto> attachedDetalleclienteproductoListNew = new ArrayList<Detalleclienteproducto>();
            for (Detalleclienteproducto detalleclienteproductoListNewDetalleclienteproductoToAttach : detalleclienteproductoListNew) {
                detalleclienteproductoListNewDetalleclienteproductoToAttach = em.getReference(detalleclienteproductoListNewDetalleclienteproductoToAttach.getClass(), detalleclienteproductoListNewDetalleclienteproductoToAttach.getIdDetalleClienteProducto());
                attachedDetalleclienteproductoListNew.add(detalleclienteproductoListNewDetalleclienteproductoToAttach);
            }
            detalleclienteproductoListNew = attachedDetalleclienteproductoListNew;
            productoServicio.setDetalleclienteproductoList(detalleclienteproductoListNew);
            List<Reserva> attachedReservaListNew = new ArrayList<Reserva>();
            for (Reserva reservaListNewReservaToAttach : reservaListNew) {
                reservaListNewReservaToAttach = em.getReference(reservaListNewReservaToAttach.getClass(), reservaListNewReservaToAttach.getIdreserva());
                attachedReservaListNew.add(reservaListNewReservaToAttach);
            }
            reservaListNew = attachedReservaListNew;
            productoServicio.setReservaList(reservaListNew);
            productoServicio = em.merge(productoServicio);
            if (idCategoriaOld != null && !idCategoriaOld.equals(idCategoriaNew)) {
                idCategoriaOld.getProductoServicioList().remove(productoServicio);
                idCategoriaOld = em.merge(idCategoriaOld);
            }
            if (idCategoriaNew != null && !idCategoriaNew.equals(idCategoriaOld)) {
                idCategoriaNew.getProductoServicioList().add(productoServicio);
                idCategoriaNew = em.merge(idCategoriaNew);
            }
            for (Detallefacturaproductoscio detallefacturaproductoscioListNewDetallefacturaproductoscio : detallefacturaproductoscioListNew) {
                if (!detallefacturaproductoscioListOld.contains(detallefacturaproductoscioListNewDetallefacturaproductoscio)) {
                    ProductoServicio oldIdProductoScioOfDetallefacturaproductoscioListNewDetallefacturaproductoscio = detallefacturaproductoscioListNewDetallefacturaproductoscio.getIdProductoScio();
                    detallefacturaproductoscioListNewDetallefacturaproductoscio.setIdProductoScio(productoServicio);
                    detallefacturaproductoscioListNewDetallefacturaproductoscio = em.merge(detallefacturaproductoscioListNewDetallefacturaproductoscio);
                    if (oldIdProductoScioOfDetallefacturaproductoscioListNewDetallefacturaproductoscio != null && !oldIdProductoScioOfDetallefacturaproductoscioListNewDetallefacturaproductoscio.equals(productoServicio)) {
                        oldIdProductoScioOfDetallefacturaproductoscioListNewDetallefacturaproductoscio.getDetallefacturaproductoscioList().remove(detallefacturaproductoscioListNewDetallefacturaproductoscio);
                        oldIdProductoScioOfDetallefacturaproductoscioListNewDetallefacturaproductoscio = em.merge(oldIdProductoScioOfDetallefacturaproductoscioListNewDetallefacturaproductoscio);
                    }
                }
            }
            for (Detalleclienteproducto detalleclienteproductoListNewDetalleclienteproducto : detalleclienteproductoListNew) {
                if (!detalleclienteproductoListOld.contains(detalleclienteproductoListNewDetalleclienteproducto)) {
                    ProductoServicio oldIdProductoScioOfDetalleclienteproductoListNewDetalleclienteproducto = detalleclienteproductoListNewDetalleclienteproducto.getIdProductoScio();
                    detalleclienteproductoListNewDetalleclienteproducto.setIdProductoScio(productoServicio);
                    detalleclienteproductoListNewDetalleclienteproducto = em.merge(detalleclienteproductoListNewDetalleclienteproducto);
                    if (oldIdProductoScioOfDetalleclienteproductoListNewDetalleclienteproducto != null && !oldIdProductoScioOfDetalleclienteproductoListNewDetalleclienteproducto.equals(productoServicio)) {
                        oldIdProductoScioOfDetalleclienteproductoListNewDetalleclienteproducto.getDetalleclienteproductoList().remove(detalleclienteproductoListNewDetalleclienteproducto);
                        oldIdProductoScioOfDetalleclienteproductoListNewDetalleclienteproducto = em.merge(oldIdProductoScioOfDetalleclienteproductoListNewDetalleclienteproducto);
                    }
                }
            }
            for (Reserva reservaListNewReserva : reservaListNew) {
                if (!reservaListOld.contains(reservaListNewReserva)) {
                    ProductoServicio oldIdProductoOfReservaListNewReserva = reservaListNewReserva.getIdProducto();
                    reservaListNewReserva.setIdProducto(productoServicio);
                    reservaListNewReserva = em.merge(reservaListNewReserva);
                    if (oldIdProductoOfReservaListNewReserva != null && !oldIdProductoOfReservaListNewReserva.equals(productoServicio)) {
                        oldIdProductoOfReservaListNewReserva.getReservaList().remove(reservaListNewReserva);
                        oldIdProductoOfReservaListNewReserva = em.merge(oldIdProductoOfReservaListNewReserva);
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
                Integer id = productoServicio.getIdProductoScio();
                if (findProductoServicio(id) == null) {
                    throw new NonexistentEntityException("The productoServicio with id " + id + " no longer exists.");
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
            ProductoServicio productoServicio;
            try {
                productoServicio = em.getReference(ProductoServicio.class, id);
                productoServicio.getIdProductoScio();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The productoServicio with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Detallefacturaproductoscio> detallefacturaproductoscioListOrphanCheck = productoServicio.getDetallefacturaproductoscioList();
            for (Detallefacturaproductoscio detallefacturaproductoscioListOrphanCheckDetallefacturaproductoscio : detallefacturaproductoscioListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This ProductoServicio (" + productoServicio + ") cannot be destroyed since the Detallefacturaproductoscio " + detallefacturaproductoscioListOrphanCheckDetallefacturaproductoscio + " in its detallefacturaproductoscioList field has a non-nullable idProductoScio field.");
            }
            List<Detalleclienteproducto> detalleclienteproductoListOrphanCheck = productoServicio.getDetalleclienteproductoList();
            for (Detalleclienteproducto detalleclienteproductoListOrphanCheckDetalleclienteproducto : detalleclienteproductoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This ProductoServicio (" + productoServicio + ") cannot be destroyed since the Detalleclienteproducto " + detalleclienteproductoListOrphanCheckDetalleclienteproducto + " in its detalleclienteproductoList field has a non-nullable idProductoScio field.");
            }
            List<Reserva> reservaListOrphanCheck = productoServicio.getReservaList();
            for (Reserva reservaListOrphanCheckReserva : reservaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This ProductoServicio (" + productoServicio + ") cannot be destroyed since the Reserva " + reservaListOrphanCheckReserva + " in its reservaList field has a non-nullable idProducto field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Categoria idCategoria = productoServicio.getIdCategoria();
            if (idCategoria != null) {
                idCategoria.getProductoServicioList().remove(productoServicio);
                idCategoria = em.merge(idCategoria);
            }
            em.remove(productoServicio);
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

    public List<ProductoServicio> findProductoServicioEntities() {
        return findProductoServicioEntities(true, -1, -1);
    }

    public List<ProductoServicio> findProductoServicioEntities(int maxResults, int firstResult) {
        return findProductoServicioEntities(false, maxResults, firstResult);
    }

    private List<ProductoServicio> findProductoServicioEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(ProductoServicio.class));
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

    public ProductoServicio findProductoServicio(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(ProductoServicio.class, id);
        } finally {
            em.close();
        }
    }

    public int getProductoServicioCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<ProductoServicio> rt = cq.from(ProductoServicio.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
