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
@Table(name = "detallefacturaproductoscio")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Detallefacturaproductoscio.findAll", query = "SELECT d FROM Detallefacturaproductoscio d"),
    @NamedQuery(name = "Detallefacturaproductoscio.findByIdDetalleFacturaProductoScio", query = "SELECT d FROM Detallefacturaproductoscio d WHERE d.idDetalleFacturaProductoScio = :idDetalleFacturaProductoScio"),
    @NamedQuery(name = "Detallefacturaproductoscio.findByCantidad", query = "SELECT d FROM Detallefacturaproductoscio d WHERE d.cantidad = :cantidad"),
    @NamedQuery(name = "Detallefacturaproductoscio.findByVrUnitario", query = "SELECT d FROM Detallefacturaproductoscio d WHERE d.vrUnitario = :vrUnitario"),
    @NamedQuery(name = "Detallefacturaproductoscio.findByFechaVentaPauta", query = "SELECT d FROM Detallefacturaproductoscio d WHERE d.fechaVentaPauta = :fechaVentaPauta")})
public class Detallefacturaproductoscio implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "idDetalleFacturaProductoScio")
    private Integer idDetalleFacturaProductoScio;
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
    @Column(name = "fechaVentaPauta")
    @Temporal(TemporalType.DATE)
    private Date fechaVentaPauta;
    @JoinColumn(name = "idFactura", referencedColumnName = "idFactura")
    @ManyToOne(optional = false)
    private Factura idFactura;
    @JoinColumn(name = "idProductoScio", referencedColumnName = "idProductoScio")
    @ManyToOne(optional = false)
    private Productoservicio idProductoScio;

    public Detallefacturaproductoscio() {
    }

    public Detallefacturaproductoscio(Integer idDetalleFacturaProductoScio) {
        this.idDetalleFacturaProductoScio = idDetalleFacturaProductoScio;
    }

    public Detallefacturaproductoscio(Integer idDetalleFacturaProductoScio, float cantidad, double vrUnitario, Date fechaVentaPauta) {
        this.idDetalleFacturaProductoScio = idDetalleFacturaProductoScio;
        this.cantidad = cantidad;
        this.vrUnitario = vrUnitario;
        this.fechaVentaPauta = fechaVentaPauta;
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

    public double getVrUnitario() {
        return vrUnitario;
    }

    public void setVrUnitario(double vrUnitario) {
        this.vrUnitario = vrUnitario;
    }

    public Date getFechaVentaPauta() {
        return fechaVentaPauta;
    }

    public void setFechaVentaPauta(Date fechaVentaPauta) {
        this.fechaVentaPauta = fechaVentaPauta;
    }

    public Factura getIdFactura() {
        return idFactura;
    }

    public void setIdFactura(Factura idFactura) {
        this.idFactura = idFactura;
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
