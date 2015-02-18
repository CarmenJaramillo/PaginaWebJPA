/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Carmen
 */
@Entity
@Table(name = "detalleclienteproducto")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Detalleclienteproducto.findAll", query = "SELECT d FROM Detalleclienteproducto d"),
    @NamedQuery(name = "Detalleclienteproducto.findByIdDetalleClienteProducto", query = "SELECT d FROM Detalleclienteproducto d WHERE d.idDetalleClienteProducto = :idDetalleClienteProducto"),
    @NamedQuery(name = "Detalleclienteproducto.findByCantidad", query = "SELECT d FROM Detalleclienteproducto d WHERE d.cantidad = :cantidad"),
    @NamedQuery(name = "Detalleclienteproducto.findByVrUnitario", query = "SELECT d FROM Detalleclienteproducto d WHERE d.vrUnitario = :vrUnitario"),
    @NamedQuery(name = "Detalleclienteproducto.findByFechaReserva", query = "SELECT d FROM Detalleclienteproducto d WHERE d.fechaReserva = :fechaReserva")})
public class Detalleclienteproducto implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "idDetalleClienteProducto")
    private Integer idDetalleClienteProducto;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cantidad")
    private float cantidad;
    @Basic(optional = false)
    @NotNull
    @Column(name = "vrUnitario")
    private double vrUnitario;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fechaReserva")
    @Temporal(TemporalType.DATE)
    private Date fechaReserva;
    @JoinColumn(name = "idCliente", referencedColumnName = "idCliente")
    @ManyToOne(optional = false)
    private Cliente idCliente;
    @JoinColumn(name = "idProductoScio", referencedColumnName = "idProductoScio")
    @ManyToOne(optional = false)
    private Productoservicio idProductoScio;

    public Detalleclienteproducto() {
    }

    public Detalleclienteproducto(Integer idDetalleClienteProducto) {
        this.idDetalleClienteProducto = idDetalleClienteProducto;
    }

    public Detalleclienteproducto(Integer idDetalleClienteProducto, float cantidad, double vrUnitario, Date fechaReserva) {
        this.idDetalleClienteProducto = idDetalleClienteProducto;
        this.cantidad = cantidad;
        this.vrUnitario = vrUnitario;
        this.fechaReserva = fechaReserva;
    }

    public Integer getIdDetalleClienteProducto() {
        return idDetalleClienteProducto;
    }

    public void setIdDetalleClienteProducto(Integer idDetalleClienteProducto) {
        this.idDetalleClienteProducto = idDetalleClienteProducto;
    }

    public float getCantidad() {
        return cantidad;
    }

    public void setCantidad(float cantidad) {
        this.cantidad = cantidad;
    }

    public double getVrUnitario() {
        return vrUnitario;
    }

    public void setVrUnitario(double vrUnitario) {
        this.vrUnitario = vrUnitario;
    }

    public Date getFechaReserva() {
        return fechaReserva;
    }

    public void setFechaReserva(Date fechaReserva) {
        this.fechaReserva = fechaReserva;
    }

    public Cliente getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Cliente idCliente) {
        this.idCliente = idCliente;
    }

    public Productoservicio getIdProductoScio() {
        return idProductoScio;
    }

    public void setIdProductoScio(Productoservicio idProductoScio) {
        this.idProductoScio = idProductoScio;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idDetalleClienteProducto != null ? idDetalleClienteProducto.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Detalleclienteproducto)) {
            return false;
        }
        Detalleclienteproducto other = (Detalleclienteproducto) object;
        if ((this.idDetalleClienteProducto == null && other.idDetalleClienteProducto != null) || (this.idDetalleClienteProducto != null && !this.idDetalleClienteProducto.equals(other.idDetalleClienteProducto))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entities.Detalleclienteproducto[ idDetalleClienteProducto=" + idDetalleClienteProducto + " ]";
    }
    
}
