/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import java.io.Serializable;
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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Carmen
 */
@Entity
@Table(name = "permisos")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Permisos.findAll", query = "SELECT p FROM Permisos p"),
    @NamedQuery(name = "Permisos.findByIdPermisos", query = "SELECT p FROM Permisos p WHERE p.idPermisos = :idPermisos"),
    @NamedQuery(name = "Permisos.findByDescripcion", query = "SELECT p FROM Permisos p WHERE p.descripcion = :descripcion")})
public class Permisos implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "idPermisos")
    private Integer idPermisos;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "descripcion")
    private String descripcion;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idPermisos")
    private List<Detallesrolespermisos> detallesrolespermisosList;

    public Permisos() {
    }

    public Permisos(Integer idPermisos) {
        this.idPermisos = idPermisos;
    }

    public Permisos(Integer idPermisos, String descripcion) {
        this.idPermisos = idPermisos;
        this.descripcion = descripcion;
    }

    public Integer getIdPermisos() {
        return idPermisos;
    }

    public void setIdPermisos(Integer idPermisos) {
        this.idPermisos = idPermisos;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @XmlTransient
    public List<Detallesrolespermisos> getDetallesrolespermisosList() {
        return detallesrolespermisosList;
    }

    public void setDetallesrolespermisosList(List<Detallesrolespermisos> detallesrolespermisosList) {
        this.detallesrolespermisosList = detallesrolespermisosList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idPermisos != null ? idPermisos.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Permisos)) {
            return false;
        }
        Permisos other = (Permisos) object;
        if ((this.idPermisos == null && other.idPermisos != null) || (this.idPermisos != null && !this.idPermisos.equals(other.idPermisos))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entities.Permisos[ idPermisos=" + idPermisos + " ]";
    }
    
}
