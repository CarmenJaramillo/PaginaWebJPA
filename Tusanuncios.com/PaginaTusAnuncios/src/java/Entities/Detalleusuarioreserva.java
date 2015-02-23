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
@Table(name = "detalleusuarioreserva")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Detalleusuarioreserva.findAll", query = "SELECT d FROM Detalleusuarioreserva d"),
    @NamedQuery(name = "Detalleusuarioreserva.findByIdDetalleUsuarioReserva", query = "SELECT d FROM Detalleusuarioreserva d WHERE d.idDetalleUsuarioReserva = :idDetalleUsuarioReserva"),
    @NamedQuery(name = "Detalleusuarioreserva.findByCantidad", query = "SELECT d FROM Detalleusuarioreserva d WHERE d.cantidad = :cantidad"),
    @NamedQuery(name = "Detalleusuarioreserva.findByVrUnitario", query = "SELECT d FROM Detalleusuarioreserva d WHERE d.vrUnitario = :vrUnitario"),
    @NamedQuery(name = "Detalleusuarioreserva.findByFechaReservacion", query = "SELECT d FROM Detalleusuarioreserva d WHERE d.fechaReservacion = :fechaReservacion")})
public class Detalleusuarioreserva implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "idDetalleUsuarioReserva")
    private Integer idDetalleUsuarioReserva;
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
    @Column(name = "fechaReservacion")
    @Temporal(TemporalType.DATE)
    private Date fechaReservacion;
    @JoinColumn(name = "idreserva", referencedColumnName = "idreserva")
    @ManyToOne(optional = false)
    private Reserva idreserva;
    @JoinColumn(name = "idusuario", referencedColumnName = "idusuario")
    @ManyToOne(optional = false)
    private Usuario idusuario;

    public Detalleusuarioreserva() {
    }

    public Detalleusuarioreserva(Integer idDetalleUsuarioReserva) {
        this.idDetalleUsuarioReserva = idDetalleUsuarioReserva;
    }

    public Detalleusuarioreserva(Integer idDetalleUsuarioReserva, float cantidad, double vrUnitario, Date fechaReservacion) {
        this.idDetalleUsuarioReserva = idDetalleUsuarioReserva;
        this.cantidad = cantidad;
        this.vrUnitario = vrUnitario;
        this.fechaReservacion = fechaReservacion;
    }

    public Integer getIdDetalleUsuarioReserva() {
        return idDetalleUsuarioReserva;
    }

    public void setIdDetalleUsuarioReserva(Integer idDetalleUsuarioReserva) {
        this.idDetalleUsuarioReserva = idDetalleUsuarioReserva;
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

    public Date getFechaReservacion() {
        return fechaReservacion;
    }

    public void setFechaReservacion(Date fechaReservacion) {
        this.fechaReservacion = fechaReservacion;
    }

    public Reserva getIdreserva() {
        return idreserva;
    }

    public void setIdreserva(Reserva idreserva) {
        this.idreserva = idreserva;
    }

    public Usuario getIdusuario() {
        return idusuario;
    }

    public void setIdusuario(Usuario idusuario) {
        this.idusuario = idusuario;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idDetalleUsuarioReserva != null ? idDetalleUsuarioReserva.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Detalleusuarioreserva)) {
            return false;
        }
        Detalleusuarioreserva other = (Detalleusuarioreserva) object;
        if ((this.idDetalleUsuarioReserva == null && other.idDetalleUsuarioReserva != null) || (this.idDetalleUsuarioReserva != null && !this.idDetalleUsuarioReserva.equals(other.idDetalleUsuarioReserva))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entities.Detalleusuarioreserva[ idDetalleUsuarioReserva=" + idDetalleUsuarioReserva + " ]";
    }
    
}
