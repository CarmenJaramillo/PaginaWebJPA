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
@Table(name = "detallesrolespermisos")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Detallesrolespermisos.findAll", query = "SELECT d FROM Detallesrolespermisos d"),
    @NamedQuery(name = "Detallesrolespermisos.findByIdDetalleRolesPermisos", query = "SELECT d FROM Detallesrolespermisos d WHERE d.idDetalleRolesPermisos = :idDetalleRolesPermisos")})
public class Detallesrolespermisos implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "idDetalleRolesPermisos")
    private Integer idDetalleRolesPermisos;
    @JoinColumn(name = "idPermisos", referencedColumnName = "idPermisos")
    @ManyToOne(optional = false)
    private Permisos idPermisos;
    @JoinColumn(name = "idRol", referencedColumnName = "idRol")
    @ManyToOne(optional = false)
    private Rol idRol;

    public Detallesrolespermisos() {
    }

    public Detallesrolespermisos(Integer idDetalleRolesPermisos) {
        this.idDetalleRolesPermisos = idDetalleRolesPermisos;
    }

    public Integer getIdDetalleRolesPermisos() {
        return idDetalleRolesPermisos;
    }

    public void setIdDetalleRolesPermisos(Integer idDetalleRolesPermisos) {
        this.idDetalleRolesPermisos = idDetalleRolesPermisos;
    }

    public Permisos getIdPermisos() {
        return idPermisos;
    }

    public void setIdPermisos(Permisos idPermisos) {
        this.idPermisos = idPermisos;
    }

    public Rol getIdRol() {
        return idRol;
    }

    public void setIdRol(Rol idRol) {
        this.idRol = idRol;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idDetalleRolesPermisos != null ? idDetalleRolesPermisos.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Detallesrolespermisos)) {
            return false;
        }
        Detallesrolespermisos other = (Detallesrolespermisos) object;
        if ((this.idDetalleRolesPermisos == null && other.idDetalleRolesPermisos != null) || (this.idDetalleRolesPermisos != null && !this.idDetalleRolesPermisos.equals(other.idDetalleRolesPermisos))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entities.Detallesrolespermisos[ idDetalleRolesPermisos=" + idDetalleRolesPermisos + " ]";
    }
    
}
