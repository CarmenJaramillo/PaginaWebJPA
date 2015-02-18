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
@Table(name = "detalleplancliente")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Detalleplancliente.findAll", query = "SELECT d FROM Detalleplancliente d"),
    @NamedQuery(name = "Detalleplancliente.findByIdDetallePlanCliente", query = "SELECT d FROM Detalleplancliente d WHERE d.idDetallePlanCliente = :idDetallePlanCliente"),
    @NamedQuery(name = "Detalleplancliente.findByPrecioPlan", query = "SELECT d FROM Detalleplancliente d WHERE d.precioPlan = :precioPlan"),
    @NamedQuery(name = "Detalleplancliente.findByFechaPlan", query = "SELECT d FROM Detalleplancliente d WHERE d.fechaPlan = :fechaPlan")})
public class Detalleplancliente implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idDetallePlanCliente")
    private Integer idDetallePlanCliente;
    @Basic(optional = false)
    @NotNull
    @Column(name = "precioPlan")
    private double precioPlan;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fechaPlan")
    @Temporal(TemporalType.DATE)
    private Date fechaPlan;
    @JoinColumn(name = "idCliente", referencedColumnName = "idCliente")
    @ManyToOne(optional = false)
    private Cliente idCliente;
    @JoinColumn(name = "idNoPlan", referencedColumnName = "idNoPlan")
    @ManyToOne(optional = false)
    private Plancliente idNoPlan;

    public Detalleplancliente() {
    }

    public Detalleplancliente(Integer idDetallePlanCliente) {
        this.idDetallePlanCliente = idDetallePlanCliente;
    }

    public Detalleplancliente(Integer idDetallePlanCliente, double precioPlan, Date fechaPlan) {
        this.idDetallePlanCliente = idDetallePlanCliente;
        this.precioPlan = precioPlan;
        this.fechaPlan = fechaPlan;
    }

    public Integer getIdDetallePlanCliente() {
        return idDetallePlanCliente;
    }

    public void setIdDetallePlanCliente(Integer idDetallePlanCliente) {
        this.idDetallePlanCliente = idDetallePlanCliente;
    }

    public double getPrecioPlan() {
        return precioPlan;
    }

    public void setPrecioPlan(double precioPlan) {
        this.precioPlan = precioPlan;
    }

    public Date getFechaPlan() {
        return fechaPlan;
    }

    public void setFechaPlan(Date fechaPlan) {
        this.fechaPlan = fechaPlan;
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
        hash += (idDetallePlanCliente != null ? idDetallePlanCliente.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Detalleplancliente)) {
            return false;
        }
        Detalleplancliente other = (Detalleplancliente) object;
        if ((this.idDetallePlanCliente == null && other.idDetallePlanCliente != null) || (this.idDetallePlanCliente != null && !this.idDetallePlanCliente.equals(other.idDetallePlanCliente))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entities.Detalleplancliente[ idDetallePlanCliente=" + idDetallePlanCliente + " ]";
    }
    
}
