/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
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
    @NamedQuery(name = "Detalleusuarioreserva.findByVrunitario", query = "SELECT d FROM Detalleusuarioreserva d WHERE d.vrunitario = :vrunitario")})
public class Detalleusuarioreserva implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "idDetalleUsuarioReserva")
    private Integer idDetalleUsuarioReserva;
    @Basic(optional = false)
    @NotNull
    @Column(name = "vrunitario")
    private double vrunitario;
    @JoinColumn(name = "idReserva", referencedColumnName = "idReserva")
    @ManyToOne(optional = false)
    private Reserva idReserva;
    @JoinColumn(name = "idUsuario", referencedColumnName = "idUsuario")
    @ManyToOne(optional = false)
    private Usuario idUsuario;

    public Detalleusuarioreserva() {
    }

    public Detalleusuarioreserva(Integer idDetalleUsuarioReserva) {
        this.idDetalleUsuarioReserva = idDetalleUsuarioReserva;
    }

    public Detalleusuarioreserva(Integer idDetalleUsuarioReserva, double vrunitario) {
        this.idDetalleUsuarioReserva = idDetalleUsuarioReserva;
        this.vrunitario = vrunitario;
    }

    public Integer getIdDetalleUsuarioReserva() {
        return idDetalleUsuarioReserva;
    }

    public void setIdDetalleUsuarioReserva(Integer idDetalleUsuarioReserva) {
        this.idDetalleUsuarioReserva = idDetalleUsuarioReserva;
    }

    public double getVrunitario() {
        return vrunitario;
    }

    public void setVrunitario(double vrunitario) {
        this.vrunitario = vrunitario;
    }

    public Reserva getIdReserva() {
        return idReserva;
    }

    public void setIdReserva(Reserva idReserva) {
        this.idReserva = idReserva;
    }

    public Usuario getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Usuario idUsuario) {
        this.idUsuario = idUsuario;
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
