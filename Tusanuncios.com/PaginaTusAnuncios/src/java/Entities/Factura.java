/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Carmen
 */
@Entity
@Table(name = "factura")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Factura.findAll", query = "SELECT f FROM Factura f"),
    @NamedQuery(name = "Factura.findByIdFactura", query = "SELECT f FROM Factura f WHERE f.idFactura = :idFactura"),
    @NamedQuery(name = "Factura.findByFechaFactura", query = "SELECT f FROM Factura f WHERE f.fechaFactura = :fechaFactura"),
    @NamedQuery(name = "Factura.findByFechaVcto", query = "SELECT f FROM Factura f WHERE f.fechaVcto = :fechaVcto")})
public class Factura implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idFactura")
    private Integer idFactura;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fechaFactura")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaFactura;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fechaVcto")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaVcto;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idFactura")
    private List<Detallefacturaproductoscio> detallefacturaproductoscioList;
    @JoinColumn(name = "idCliente", referencedColumnName = "idCliente")
    @ManyToOne(optional = false)
    private Cliente idCliente;
    @JoinColumn(name = "idNoPlan", referencedColumnName = "idNoPlan")
    @ManyToOne(optional = false)
    private Plancliente idNoPlan;

    public Factura() {
    }

    public Factura(Integer idFactura) {
        this.idFactura = idFactura;
    }

    public Factura(Integer idFactura, Date fechaFactura, Date fechaVcto) {
        this.idFactura = idFactura;
        this.fechaFactura = fechaFactura;
        this.fechaVcto = fechaVcto;
    }

    public Integer getIdFactura() {
        return idFactura;
    }

    public void setIdFactura(Integer idFactura) {
        this.idFactura = idFactura;
    }

    public Date getFechaFactura() {
        return fechaFactura;
    }

    public void setFechaFactura(Date fechaFactura) {
        this.fechaFactura = fechaFactura;
    }

    public Date getFechaVcto() {
        return fechaVcto;
    }

    public void setFechaVcto(Date fechaVcto) {
        this.fechaVcto = fechaVcto;
    }

    @XmlTransient
    public List<Detallefacturaproductoscio> getDetallefacturaproductoscioList() {
        return detallefacturaproductoscioList;
    }

    public void setDetallefacturaproductoscioList(List<Detallefacturaproductoscio> detallefacturaproductoscioList) {
        this.detallefacturaproductoscioList = detallefacturaproductoscioList;
    }

    public Cliente getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Cliente idCliente) {
        this.idCliente = idCliente;
    }

    public Plancliente getIdNoPlan() {
        return idNoPlan;
    }

    public void setIdNoPlan(Plancliente idNoPlan) {
        this.idNoPlan = idNoPlan;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idFactura != null ? idFactura.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Factura)) {
            return false;
        }
        Factura other = (Factura) object;
        if ((this.idFactura == null && other.idFactura != null) || (this.idFactura != null && !this.idFactura.equals(other.idFactura))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entities.Factura[ idFactura=" + idFactura + " ]";
    }
    
}
