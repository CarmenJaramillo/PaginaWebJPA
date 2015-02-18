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
@Table(name = "reserva")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Reserva.findAll", query = "SELECT r FROM Reserva r"),
    @NamedQuery(name = "Reserva.findByIdReserva", query = "SELECT r FROM Reserva r WHERE r.idReserva = :idReserva"),
    @NamedQuery(name = "Reserva.findByFechaReserva", query = "SELECT r FROM Reserva r WHERE r.fechaReserva = :fechaReserva"),
    @NamedQuery(name = "Reserva.findByFechaVcto", query = "SELECT r FROM Reserva r WHERE r.fechaVcto = :fechaVcto"),
    @NamedQuery(name = "Reserva.findByVrUnitario", query = "SELECT r FROM Reserva r WHERE r.vrUnitario = :vrUnitario")})
public class Reserva implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "idReserva")
    private Integer idReserva;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fechaReserva")
    @Temporal(TemporalType.DATE)
    private Date fechaReserva;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fechaVcto")
    @Temporal(TemporalType.DATE)
    private Date fechaVcto;
    @Basic(optional = false)
    @NotNull
    @Column(name = "vrUnitario")
    private double vrUnitario;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idReserva")
    private List<Detalleusuarioreserva> detalleusuarioreservaList;
    @JoinColumn(name = "idProductoScio", referencedColumnName = "idProductoScio")
    @ManyToOne(optional = false)
    private Productoservicio idProductoScio;

    public Reserva() {
    }

    public Reserva(Integer idReserva) {
        this.idReserva = idReserva;
    }

    public Reserva(Integer idReserva, Date fechaReserva, Date fechaVcto, double vrUnitario) {
        this.idReserva = idReserva;
        this.fechaReserva = fechaReserva;
        this.fechaVcto = fechaVcto;
        this.vrUnitario = vrUnitario;
    }

    public Integer getIdReserva() {
        return idReserva;
    }

    public void setIdReserva(Integer idReserva) {
        this.idReserva = idReserva;
    }

    public Date getFechaReserva() {
        return fechaReserva;
    }

    public void setFechaReserva(Date fechaReserva) {
        this.fechaReserva = fechaReserva;
    }

    public Date getFechaVcto() {
        return fechaVcto;
    }

    public void setFechaVcto(Date fechaVcto) {
        this.fechaVcto = fechaVcto;
    }

    public double getVrUnitario() {
        return vrUnitario;
    }

    public void setVrUnitario(double vrUnitario) {
        this.vrUnitario = vrUnitario;
    }

    @XmlTransient
    public List<Detalleusuarioreserva> getDetalleusuarioreservaList() {
        return detalleusuarioreservaList;
    }

    public void setDetalleusuarioreservaList(List<Detalleusuarioreserva> detalleusuarioreservaList) {
        this.detalleusuarioreservaList = detalleusuarioreservaList;
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
        hash += (idReserva != null ? idReserva.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Reserva)) {
            return false;
        }
        Reserva other = (Reserva) object;
        if ((this.idReserva == null && other.idReserva != null) || (this.idReserva != null && !this.idReserva.equals(other.idReserva))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entities.Reserva[ idReserva=" + idReserva + " ]";
    }
    
}
