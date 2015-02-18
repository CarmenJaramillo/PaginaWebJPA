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
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Carmen
 */
@Entity
@Table(name = "plancliente")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Plancliente.findAll", query = "SELECT p FROM Plancliente p"),
    @NamedQuery(name = "Plancliente.findByIdNoPlan", query = "SELECT p FROM Plancliente p WHERE p.idNoPlan = :idNoPlan"),
    @NamedQuery(name = "Plancliente.findByDescripcion", query = "SELECT p FROM Plancliente p WHERE p.descripcion = :descripcion"),
    @NamedQuery(name = "Plancliente.findByFechaPlan", query = "SELECT p FROM Plancliente p WHERE p.fechaPlan = :fechaPlan"),
    @NamedQuery(name = "Plancliente.findByFechaVcto", query = "SELECT p FROM Plancliente p WHERE p.fechaVcto = :fechaVcto"),
    @NamedQuery(name = "Plancliente.findByPrecioPlan", query = "SELECT p FROM Plancliente p WHERE p.precioPlan = :precioPlan")})
public class Plancliente implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "idNoPlan")
    private Integer idNoPlan;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 250)
    @Column(name = "descripcion")
    private String descripcion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fechaPlan")
    @Temporal(TemporalType.DATE)
    private Date fechaPlan;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fechaVcto")
    @Temporal(TemporalType.DATE)
    private Date fechaVcto;
    @Basic(optional = false)
    @NotNull
    @Column(name = "PrecioPlan")
    private double precioPlan;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idNoPlan")
    private List<Detalleplancliente> detalleplanclienteList;

    public Plancliente() {
    }

    public Plancliente(Integer idNoPlan) {
        this.idNoPlan = idNoPlan;
    }

    public Plancliente(Integer idNoPlan, String descripcion, Date fechaPlan, Date fechaVcto, double precioPlan) {
        this.idNoPlan = idNoPlan;
        this.descripcion = descripcion;
        this.fechaPlan = fechaPlan;
        this.fechaVcto = fechaVcto;
        this.precioPlan = precioPlan;
    }

    public Integer getIdNoPlan() {
        return idNoPlan;
    }

    public void setIdNoPlan(Integer idNoPlan) {
        this.idNoPlan = idNoPlan;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Date getFechaPlan() {
        return fechaPlan;
    }

    public void setFechaPlan(Date fechaPlan) {
        this.fechaPlan = fechaPlan;
    }

    public Date getFechaVcto() {
        return fechaVcto;
    }

    public void setFechaVcto(Date fechaVcto) {
        this.fechaVcto = fechaVcto;
    }

    public double getPrecioPlan() {
        return precioPlan;
    }

    public void setPrecioPlan(double precioPlan) {
        this.precioPlan = precioPlan;
    }

    @XmlTransient
    public List<Detalleplancliente> getDetalleplanclienteList() {
        return detalleplanclienteList;
    }

    public void setDetalleplanclienteList(List<Detalleplancliente> detalleplanclienteList) {
        this.detalleplanclienteList = detalleplanclienteList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idNoPlan != null ? idNoPlan.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Plancliente)) {
            return false;
        }
        Plancliente other = (Plancliente) object;
        if ((this.idNoPlan == null && other.idNoPlan != null) || (this.idNoPlan != null && !this.idNoPlan.equals(other.idNoPlan))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entities.Plancliente[ idNoPlan=" + idNoPlan + " ]";
    }
    
}
