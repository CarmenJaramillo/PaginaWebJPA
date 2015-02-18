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
@Table(name = "estrato")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Estrato.findAll", query = "SELECT e FROM Estrato e"),
    @NamedQuery(name = "Estrato.findByIdEstrato", query = "SELECT e FROM Estrato e WHERE e.idEstrato = :idEstrato"),
    @NamedQuery(name = "Estrato.findByDescripcion", query = "SELECT e FROM Estrato e WHERE e.descripcion = :descripcion")})
public class Estrato implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "idEstrato")
    private Integer idEstrato;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 15)
    @Column(name = "descripcion")
    private String descripcion;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idEstrato")
    private List<Empleado> empleadoList;

    public Estrato() {
    }

    public Estrato(Integer idEstrato) {
        this.idEstrato = idEstrato;
    }

    public Estrato(Integer idEstrato, String descripcion) {
        this.idEstrato = idEstrato;
        this.descripcion = descripcion;
    }

    public Integer getIdEstrato() {
        return idEstrato;
    }

    public void setIdEstrato(Integer idEstrato) {
        this.idEstrato = idEstrato;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @XmlTransient
    public List<Empleado> getEmpleadoList() {
        return empleadoList;
    }

    public void setEmpleadoList(List<Empleado> empleadoList) {
        this.empleadoList = empleadoList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idEstrato != null ? idEstrato.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Estrato)) {
            return false;
        }
        Estrato other = (Estrato) object;
        if ((this.idEstrato == null && other.idEstrato != null) || (this.idEstrato != null && !this.idEstrato.equals(other.idEstrato))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entities.Estrato[ idEstrato=" + idEstrato + " ]";
    }
    
}
