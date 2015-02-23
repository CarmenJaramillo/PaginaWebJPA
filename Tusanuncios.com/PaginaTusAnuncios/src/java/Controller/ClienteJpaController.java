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
import Entities.Actividadeconomica;
import Entities.Barrio;
import Entities.Ciudad;
import Entities.Cliente;
import Entities.Departamento;
import Entities.Metodopago;
import Entities.Detalleclienteproducto;
import java.util.ArrayList;
import java.util.List;
import Entities.Factura;
import Entities.Detalleplancliente;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author Carmen
 */
public class ClienteJpaController implements Serializable {

    public ClienteJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Cliente cliente) throws RollbackFailureException, Exception {
        if (cliente.getDetalleclienteproductoList() == null) {
            cliente.setDetalleclienteproductoList(new ArrayList<Detalleclienteproducto>());
        }
        if (cliente.getFacturaList() == null) {
            cliente.setFacturaList(new ArrayList<Factura>());
        }
        if (cliente.getDetalleplanclienteList() == null) {
            cliente.setDetalleplanclienteList(new ArrayList<Detalleplancliente>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Actividadeconomica idActEcon = cliente.getIdActEcon();
            if (idActEcon != null) {
                idActEcon = em.getReference(idActEcon.getClass(), idActEcon.getIdActEcon());
                cliente.setIdActEcon(idActEcon);
            }
            Barrio idBarrio = cliente.getIdBarrio();
            if (idBarrio != null) {
                idBarrio = em.getReference(idBarrio.getClass(), idBarrio.getIdBarrio());
                cliente.setIdBarrio(idBarrio);
            }
            Ciudad idCiudad = cliente.getIdCiudad();
            if (idCiudad != null) {
                idCiudad = em.getReference(idCiudad.getClass(), idCiudad.getIdCiudad());
                cliente.setIdCiudad(idCiudad);
            }
            Departamento idDepartamento = cliente.getIdDepartamento();
            if (idDepartamento != null) {
                idDepartamento = em.getReference(idDepartamento.getClass(), idDepartamento.getIdDepartamento());
                cliente.setIdDepartamento(idDepartamento);
            }
            Metodopago idMetodoPago = cliente.getIdMetodoPago();
            if (idMetodoPago != null) {
                idMetodoPago = em.getReference(idMetodoPago.getClass(), idMetodoPago.getIdMetodoPago());
                cliente.setIdMetodoPago(idMetodoPago);
            }
            List<Detalleclienteproducto> attachedDetalleclienteproductoList = new ArrayList<Detalleclienteproducto>();
            for (Detalleclienteproducto detalleclienteproductoListDetalleclienteproductoToAttach : cliente.getDetalleclienteproductoList()) {
                detalleclienteproductoListDetalleclienteproductoToAttach = em.getReference(detalleclienteproductoListDetalleclienteproductoToAttach.getClass(), detalleclienteproductoListDetalleclienteproductoToAttach.getIdDetalleClienteProducto());
                attachedDetalleclienteproductoList.add(detalleclienteproductoListDetalleclienteproductoToAttach);
            }
            cliente.setDetalleclienteproductoList(attachedDetalleclienteproductoList);
            List<Factura> attachedFacturaList = new ArrayList<Factura>();
            for (Factura facturaListFacturaToAttach : cliente.getFacturaList()) {
                facturaListFacturaToAttach = em.getReference(facturaListFacturaToAttach.getClass(), facturaListFacturaToAttach.getIdFactura());
                attachedFacturaList.add(facturaListFacturaToAttach);
            }
            cliente.setFacturaList(attachedFacturaList);
            List<Detalleplancliente> attachedDetalleplanclienteList = new ArrayList<Detalleplancliente>();
            for (Detalleplancliente detalleplanclienteListDetalleplanclienteToAttach : cliente.getDetalleplanclienteList()) {
                detalleplanclienteListDetalleplanclienteToAttach = em.getReference(detalleplanclienteListDetalleplanclienteToAttach.getClass(), detalleplanclienteListDetalleplanclienteToAttach.getIdDetallePlanCliente());
                attachedDetalleplanclienteList.add(detalleplanclienteListDetalleplanclienteToAttach);
            }
            cliente.setDetalleplanclienteList(attachedDetalleplanclienteList);
            em.persist(cliente);
            if (idActEcon != null) {
                idActEcon.getClienteList().add(cliente);
                idActEcon = em.merge(idActEcon);
            }
            if (idBarrio != null) {
                idBarrio.getClienteList().add(cliente);
                idBarrio = em.merge(idBarrio);
            }
            if (idCiudad != null) {
                idCiudad.getClienteList().add(cliente);
                idCiudad = em.merge(idCiudad);
            }
            if (idDepartamento != null) {
                idDepartamento.getClienteList().add(cliente);
                idDepartamento = em.merge(idDepartamento);
            }
            if (idMetodoPago != null) {
                idMetodoPago.getClienteList().add(cliente);
                idMetodoPago = em.merge(idMetodoPago);
            }
            for (Detalleclienteproducto detalleclienteproductoListDetalleclienteproducto : cliente.getDetalleclienteproductoList()) {
                Cliente oldIdClienteOfDetalleclienteproductoListDetalleclienteproducto = detalleclienteproductoListDetalleclienteproducto.getIdCliente();
                detalleclienteproductoListDetalleclienteproducto.setIdCliente(cliente);
                detalleclienteproductoListDetalleclienteproducto = em.merge(detalleclienteproductoListDetalleclienteproducto);
                if (oldIdClienteOfDetalleclienteproductoListDetalleclienteproducto != null) {
                    oldIdClienteOfDetalleclienteproductoListDetalleclienteproducto.getDetalleclienteproductoList().remove(detalleclienteproductoListDetalleclienteproducto);
                    oldIdClienteOfDetalleclienteproductoListDetalleclienteproducto = em.merge(oldIdClienteOfDetalleclienteproductoListDetalleclienteproducto);
                }
            }
            for (Factura facturaListFactura : cliente.getFacturaList()) {
                Cliente oldIdClienteOfFacturaListFactura = facturaListFactura.getIdCliente();
                facturaListFactura.setIdCliente(cliente);
                facturaListFactura = em.merge(facturaListFactura);
                if (oldIdClienteOfFacturaListFactura != null) {
                    oldIdClienteOfFacturaListFactura.getFacturaList().remove(facturaListFactura);
                    oldIdClienteOfFacturaListFactura = em.merge(oldIdClienteOfFacturaListFactura);
                }
            }
            for (Detalleplancliente detalleplanclienteListDetalleplancliente : cliente.getDetalleplanclienteList()) {
                Cliente oldIdClienteOfDetalleplanclienteListDetalleplancliente = detalleplanclienteListDetalleplancliente.getIdCliente();
                detalleplanclienteListDetalleplancliente.setIdCliente(cliente);
                detalleplanclienteListDetalleplancliente = em.merge(detalleplanclienteListDetalleplancliente);
                if (oldIdClienteOfDetalleplanclienteListDetalleplancliente != null) {
                    oldIdClienteOfDetalleplanclienteListDetalleplancliente.getDetalleplanclienteList().remove(detalleplanclienteListDetalleplancliente);
                    oldIdClienteOfDetalleplanclienteListDetalleplancliente = em.merge(oldIdClienteOfDetalleplanclienteListDetalleplancliente);
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

    public void edit(Cliente cliente) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Cliente persistentCliente = em.find(Cliente.class, cliente.getIdCliente());
            Actividadeconomica idActEconOld = persistentCliente.getIdActEcon();
            Actividadeconomica idActEconNew = cliente.getIdActEcon();
            Barrio idBarrioOld = persistentCliente.getIdBarrio();
            Barrio idBarrioNew = cliente.getIdBarrio();
            Ciudad idCiudadOld = persistentCliente.getIdCiudad();
            Ciudad idCiudadNew = cliente.getIdCiudad();
            Departamento idDepartamentoOld = persistentCliente.getIdDepartamento();
            Departamento idDepartamentoNew = cliente.getIdDepartamento();
            Metodopago idMetodoPagoOld = persistentCliente.getIdMetodoPago();
            Metodopago idMetodoPagoNew = cliente.getIdMetodoPago();
            List<Detalleclienteproducto> detalleclienteproductoListOld = persistentCliente.getDetalleclienteproductoList();
            List<Detalleclienteproducto> detalleclienteproductoListNew = cliente.getDetalleclienteproductoList();
            List<Factura> facturaListOld = persistentCliente.getFacturaList();
            List<Factura> facturaListNew = cliente.getFacturaList();
            List<Detalleplancliente> detalleplanclienteListOld = persistentCliente.getDetalleplanclienteList();
            List<Detalleplancliente> detalleplanclienteListNew = cliente.getDetalleplanclienteList();
            List<String> illegalOrphanMessages = null;
            for (Detalleclienteproducto detalleclienteproductoListOldDetalleclienteproducto : detalleclienteproductoListOld) {
                if (!detalleclienteproductoListNew.contains(detalleclienteproductoListOldDetalleclienteproducto)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Detalleclienteproducto " + detalleclienteproductoListOldDetalleclienteproducto + " since its idCliente field is not nullable.");
                }
            }
            for (Factura facturaListOldFactura : facturaListOld) {
                if (!facturaListNew.contains(facturaListOldFactura)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Factura " + facturaListOldFactura + " since its idCliente field is not nullable.");
                }
            }
            for (Detalleplancliente detalleplanclienteListOldDetalleplancliente : detalleplanclienteListOld) {
                if (!detalleplanclienteListNew.contains(detalleplanclienteListOldDetalleplancliente)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Detalleplancliente " + detalleplanclienteListOldDetalleplancliente + " since its idCliente field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idActEconNew != null) {
                idActEconNew = em.getReference(idActEconNew.getClass(), idActEconNew.getIdActEcon());
                cliente.setIdActEcon(idActEconNew);
            }
            if (idBarrioNew != null) {
                idBarrioNew = em.getReference(idBarrioNew.getClass(), idBarrioNew.getIdBarrio());
                cliente.setIdBarrio(idBarrioNew);
            }
            if (idCiudadNew != null) {
                idCiudadNew = em.getReference(idCiudadNew.getClass(), idCiudadNew.getIdCiudad());
                cliente.setIdCiudad(idCiudadNew);
            }
            if (idDepartamentoNew != null) {
                idDepartamentoNew = em.getReference(idDepartamentoNew.getClass(), idDepartamentoNew.getIdDepartamento());
                cliente.setIdDepartamento(idDepartamentoNew);
            }
            if (idMetodoPagoNew != null) {
                idMetodoPagoNew = em.getReference(idMetodoPagoNew.getClass(), idMetodoPagoNew.getIdMetodoPago());
                cliente.setIdMetodoPago(idMetodoPagoNew);
            }
            List<Detalleclienteproducto> attachedDetalleclienteproductoListNew = new ArrayList<Detalleclienteproducto>();
            for (Detalleclienteproducto detalleclienteproductoListNewDetalleclienteproductoToAttach : detalleclienteproductoListNew) {
                detalleclienteproductoListNewDetalleclienteproductoToAttach = em.getReference(detalleclienteproductoListNewDetalleclienteproductoToAttach.getClass(), detalleclienteproductoListNewDetalleclienteproductoToAttach.getIdDetalleClienteProducto());
                attachedDetalleclienteproductoListNew.add(detalleclienteproductoListNewDetalleclienteproductoToAttach);
            }
            detalleclienteproductoListNew = attachedDetalleclienteproductoListNew;
            cliente.setDetalleclienteproductoList(detalleclienteproductoListNew);
            List<Factura> attachedFacturaListNew = new ArrayList<Factura>();
            for (Factura facturaListNewFacturaToAttach : facturaListNew) {
                facturaListNewFacturaToAttach = em.getReference(facturaListNewFacturaToAttach.getClass(), facturaListNewFacturaToAttach.getIdFactura());
                attachedFacturaListNew.add(facturaListNewFacturaToAttach);
            }
            facturaListNew = attachedFacturaListNew;
            cliente.setFacturaList(facturaListNew);
            List<Detalleplancliente> attachedDetalleplanclienteListNew = new ArrayList<Detalleplancliente>();
            for (Detalleplancliente detalleplanclienteListNewDetalleplanclienteToAttach : detalleplanclienteListNew) {
                detalleplanclienteListNewDetalleplanclienteToAttach = em.getReference(detalleplanclienteListNewDetalleplanclienteToAttach.getClass(), detalleplanclienteListNewDetalleplanclienteToAttach.getIdDetallePlanCliente());
                attachedDetalleplanclienteListNew.add(detalleplanclienteListNewDetalleplanclienteToAttach);
            }
            detalleplanclienteListNew = attachedDetalleplanclienteListNew;
            cliente.setDetalleplanclienteList(detalleplanclienteListNew);
            cliente = em.merge(cliente);
            if (idActEconOld != null && !idActEconOld.equals(idActEconNew)) {
                idActEconOld.getClienteList().remove(cliente);
                idActEconOld = em.merge(idActEconOld);
            }
            if (idActEconNew != null && !idActEconNew.equals(idActEconOld)) {
                idActEconNew.getClienteList().add(cliente);
                idActEconNew = em.merge(idActEconNew);
            }
            if (idBarrioOld != null && !idBarrioOld.equals(idBarrioNew)) {
                idBarrioOld.getClienteList().remove(cliente);
                idBarrioOld = em.merge(idBarrioOld);
            }
            if (idBarrioNew != null && !idBarrioNew.equals(idBarrioOld)) {
                idBarrioNew.getClienteList().add(cliente);
                idBarrioNew = em.merge(idBarrioNew);
            }
            if (idCiudadOld != null && !idCiudadOld.equals(idCiudadNew)) {
                idCiudadOld.getClienteList().remove(cliente);
                idCiudadOld = em.merge(idCiudadOld);
            }
            if (idCiudadNew != null && !idCiudadNew.equals(idCiudadOld)) {
                idCiudadNew.getClienteList().add(cliente);
                idCiudadNew = em.merge(idCiudadNew);
            }
            if (idDepartamentoOld != null && !idDepartamentoOld.equals(idDepartamentoNew)) {
                idDepartamentoOld.getClienteList().remove(cliente);
                idDepartamentoOld = em.merge(idDepartamentoOld);
            }
            if (idDepartamentoNew != null && !idDepartamentoNew.equals(idDepartamentoOld)) {
                idDepartamentoNew.getClienteList().add(cliente);
                idDepartamentoNew = em.merge(idDepartamentoNew);
            }
            if (idMetodoPagoOld != null && !idMetodoPagoOld.equals(idMetodoPagoNew)) {
                idMetodoPagoOld.getClienteList().remove(cliente);
                idMetodoPagoOld = em.merge(idMetodoPagoOld);
            }
            if (idMetodoPagoNew != null && !idMetodoPagoNew.equals(idMetodoPagoOld)) {
                idMetodoPagoNew.getClienteList().add(cliente);
                idMetodoPagoNew = em.merge(idMetodoPagoNew);
            }
            for (Detalleclienteproducto detalleclienteproductoListNewDetalleclienteproducto : detalleclienteproductoListNew) {
                if (!detalleclienteproductoListOld.contains(detalleclienteproductoListNewDetalleclienteproducto)) {
                    Cliente oldIdClienteOfDetalleclienteproductoListNewDetalleclienteproducto = detalleclienteproductoListNewDetalleclienteproducto.getIdCliente();
                    detalleclienteproductoListNewDetalleclienteproducto.setIdCliente(cliente);
                    detalleclienteproductoListNewDetalleclienteproducto = em.merge(detalleclienteproductoListNewDetalleclienteproducto);
                    if (oldIdClienteOfDetalleclienteproductoListNewDetalleclienteproducto != null && !oldIdClienteOfDetalleclienteproductoListNewDetalleclienteproducto.equals(cliente)) {
                        oldIdClienteOfDetalleclienteproductoListNewDetalleclienteproducto.getDetalleclienteproductoList().remove(detalleclienteproductoListNewDetalleclienteproducto);
                        oldIdClienteOfDetalleclienteproductoListNewDetalleclienteproducto = em.merge(oldIdClienteOfDetalleclienteproductoListNewDetalleclienteproducto);
                    }
                }
            }
            for (Factura facturaListNewFactura : facturaListNew) {
                if (!facturaListOld.contains(facturaListNewFactura)) {
                    Cliente oldIdClienteOfFacturaListNewFactura = facturaListNewFactura.getIdCliente();
                    facturaListNewFactura.setIdCliente(cliente);
                    facturaListNewFactura = em.merge(facturaListNewFactura);
                    if (oldIdClienteOfFacturaListNewFactura != null && !oldIdClienteOfFacturaListNewFactura.equals(cliente)) {
                        oldIdClienteOfFacturaListNewFactura.getFacturaList().remove(facturaListNewFactura);
                        oldIdClienteOfFacturaListNewFactura = em.merge(oldIdClienteOfFacturaListNewFactura);
                    }
                }
            }
            for (Detalleplancliente detalleplanclienteListNewDetalleplancliente : detalleplanclienteListNew) {
                if (!detalleplanclienteListOld.contains(detalleplanclienteListNewDetalleplancliente)) {
                    Cliente oldIdClienteOfDetalleplanclienteListNewDetalleplancliente = detalleplanclienteListNewDetalleplancliente.getIdCliente();
                    detalleplanclienteListNewDetalleplancliente.setIdCliente(cliente);
                    detalleplanclienteListNewDetalleplancliente = em.merge(detalleplanclienteListNewDetalleplancliente);
                    if (oldIdClienteOfDetalleplanclienteListNewDetalleplancliente != null && !oldIdClienteOfDetalleplanclienteListNewDetalleplancliente.equals(cliente)) {
                        oldIdClienteOfDetalleplanclienteListNewDetalleplancliente.getDetalleplanclienteList().remove(detalleplanclienteListNewDetalleplancliente);
                        oldIdClienteOfDetalleplanclienteListNewDetalleplancliente = em.merge(oldIdClienteOfDetalleplanclienteListNewDetalleplancliente);
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
                Integer id = cliente.getIdCliente();
                if (findCliente(id) == null) {
                    throw new NonexistentEntityException("The cliente with id " + id + " no longer exists.");
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
            Cliente cliente;
            try {
                cliente = em.getReference(Cliente.class, id);
                cliente.getIdCliente();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The cliente with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Detalleclienteproducto> detalleclienteproductoListOrphanCheck = cliente.getDetalleclienteproductoList();
            for (Detalleclienteproducto detalleclienteproductoListOrphanCheckDetalleclienteproducto : detalleclienteproductoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Cliente (" + cliente + ") cannot be destroyed since the Detalleclienteproducto " + detalleclienteproductoListOrphanCheckDetalleclienteproducto + " in its detalleclienteproductoList field has a non-nullable idCliente field.");
            }
            List<Factura> facturaListOrphanCheck = cliente.getFacturaList();
            for (Factura facturaListOrphanCheckFactura : facturaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Cliente (" + cliente + ") cannot be destroyed since the Factura " + facturaListOrphanCheckFactura + " in its facturaList field has a non-nullable idCliente field.");
            }
            List<Detalleplancliente> detalleplanclienteListOrphanCheck = cliente.getDetalleplanclienteList();
            for (Detalleplancliente detalleplanclienteListOrphanCheckDetalleplancliente : detalleplanclienteListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Cliente (" + cliente + ") cannot be destroyed since the Detalleplancliente " + detalleplanclienteListOrphanCheckDetalleplancliente + " in its detalleplanclienteList field has a non-nullable idCliente field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Actividadeconomica idActEcon = cliente.getIdActEcon();
            if (idActEcon != null) {
                idActEcon.getClienteList().remove(cliente);
                idActEcon = em.merge(idActEcon);
            }
            Barrio idBarrio = cliente.getIdBarrio();
            if (idBarrio != null) {
                idBarrio.getClienteList().remove(cliente);
                idBarrio = em.merge(idBarrio);
            }
            Ciudad idCiudad = cliente.getIdCiudad();
            if (idCiudad != null) {
                idCiudad.getClienteList().remove(cliente);
                idCiudad = em.merge(idCiudad);
            }
            Departamento idDepartamento = cliente.getIdDepartamento();
            if (idDepartamento != null) {
                idDepartamento.getClienteList().remove(cliente);
                idDepartamento = em.merge(idDepartamento);
            }
            Metodopago idMetodoPago = cliente.getIdMetodoPago();
            if (idMetodoPago != null) {
                idMetodoPago.getClienteList().remove(cliente);
                idMetodoPago = em.merge(idMetodoPago);
            }
            em.remove(cliente);
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

    public List<Cliente> findClienteEntities() {
        return findClienteEntities(true, -1, -1);
    }

    public List<Cliente> findClienteEntities(int maxResults, int firstResult) {
        return findClienteEntities(false, maxResults, firstResult);
    }

    private List<Cliente> findClienteEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Cliente.class));
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

    public Cliente findCliente(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Cliente.class, id);
        } finally {
            em.close();
        }
    }

    public int getClienteCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Cliente> rt = cq.from(Cliente.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
