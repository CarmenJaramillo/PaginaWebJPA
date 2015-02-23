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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
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
@Table(name = "detallefacturaproductoscio")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Detallefacturaproductoscio.findAll", query = "SELECT d FROM Detallefacturaproductoscio d"),
    @NamedQuery(name = "Detallefacturaproductoscio.findByIdDetalleFacturaProductoScio", query = "SELECT d FROM Detallefacturaproductoscio d WHERE d.idDetalleFacturaProductoScio = :idDetalleFacturaProductoScio"),
    @NamedQuery(name = "Detallefacturaproductoscio.findByCantidad", query = "SELECT d FROM Detallefacturaproductoscio d WHERE d.cantidad = :cantidad"),
    @NamedQuery(name = "Detallefacturaproductoscio.findByFechaVtaProd", query = "SELECT d FROM Detallefacturaproductoscio d WHERE d.fechaVtaProd = :fechaVtaProd"),
    @NamedQuery(name = "Detallefacturaproductoscio.findByValorUnitario", query = "SELECT d FROM Detallefacturaproductoscio d WHERE d.valorUnitario = :valorUnitario")})
public class Detallefacturaproductoscio implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idDetalleFacturaProductoScio")
    private Integer idDetalleFacturaProductoScio;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cantidad")
    private float cantidad;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fechaVtaProd")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaVtaProd;
    @Basic(optional = false)
    @NotNull
    @Column(name = "valorUnitario")
    private double valorUnitario;
    @JoinColumn(name = "idFactura", referencedColumnName = "idFactura")
    @ManyToOne(optional = false)
    private Factura idFactura;
    @JoinColumn(name = "idProductoScio", referencedColumnName = "idProductoScio")
    @ManyToOne(optional = false)
    private ProductoServicio idProductoScio;

    public Detallefacturaproductoscio() {
    }

    public Detallefacturaproductoscio(Integer idDetalleFacturaProductoScio) {
        this.idDetalleFacturaProductoScio = idDetalleFacturaProductoScio;
    }

    public Detallefacturaproductoscio(Integer idDetalleFacturaProductoScio, float cantidad, Date fechaVtaProd, double valorUnitario) {
        this.idDetalleFacturaProductoScio = idDetalleFacturaProductoScio;
        this.cantidad = cantidad;
        this.fechaVtaProd = fechaVtaProd;
        this.valorUnitario = valorUnitario;
    }

    public Integer getIdDetalleFacturaProductoScio() {
        return idDetalleFacturaProductoScio;
    }

    public void setIdDetalleFacturaProductoScio(Integer idDetalleFacturaProductoScio) {
        this.idDetalleFacturaProductoScio = idDetalleFacturaProductoScio;
    }

    public float getCantidad() {
        return cantidad;
    }

    public void setCantidad(float cantidad) {
        this.cantidad = cantidad;
    }

    public Date getFechaVtaProd() {
        return fechaVtaProd;
    }

    public void setFechaVtaProd(Date fechaVtaProd) {
        this.fechaVtaProd = fechaVtaProd;
    }

    public double getValorUnitario() {
        return valorUnitario;
    }

    public void setValorUnitario(double valorUnitario) {
        this.valorUnitario = valorUnitario;
    }

    public Factura getIdFactura() {
        return idFactura;
    }

    public void setIdFactura(Factura idFactura) {
        this.idFactura = idFactura;
    }

    public ProductoServicio getIdProductoScio() {
        return idProductoScio;
    }

    public void setIdProductoScio(ProductoServicio idProductoScio) {
        this.idProductoScio = idProductoScio;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idDetalleFacturaProductoScio != null ? idDetalleFacturaProductoScio.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Detallefacturaproductoscio)) {
            return false;
        }
        Detallefacturaproductoscio other = (Detallefacturaproductoscio) object;
        if ((this.idDetalleFacturaProductoScio == null && other.idDetalleFacturaProductoScio != null) || (this.idDetalleFacturaProductoScio != null && !this.idDetalleFacturaProductoScio.equals(other.idDetalleFacturaProductoScio))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entities.Detallefacturaproductoscio[ idDetalleFacturaProductoScio=" + idDetalleFacturaProductoScio + " ]";
    }
    
}
