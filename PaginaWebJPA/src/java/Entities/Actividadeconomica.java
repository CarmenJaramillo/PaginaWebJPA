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
@Table(name = "actividadeconomica")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Actividadeconomica.findAll", query = "SELECT a FROM Actividadeconomica a"),
    @NamedQuery(name = "Actividadeconomica.findByIdActEcon", query = "SELECT a FROM Actividadeconomica a WHERE a.idActEcon = :idActEcon"),
    @NamedQuery(name = "Actividadeconomica.findByDescripcion", query = "SELECT a FROM Actividadeconomica a WHERE a.descripcion = :descripcion")})
public class Actividadeconomica implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "idActEcon")
    private Integer idActEcon;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "Descripcion")
    private String descripcion;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idActEcon")
    private List<Cliente> clienteList;

    public Actividadeconomica() {
    }

    public Actividadeconomica(Integer idActEcon) {
        this.idActEcon = idActEcon;
    }

    public Actividadeconomica(Integer idActEcon, String descripcion) {
        this.idActEcon = idActEcon;
        this.descripcion = descripcion;
    }

    public Integer getIdActEcon() {
        return idActEcon;
    }

    public void setIdActEcon(Integer idActEcon) {
        this.idActEcon = idActEcon;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @XmlTransient
    public List<Cliente> getClienteList() {
        return clienteList;
    }

    public void setClienteList(List<Cliente> clienteList) {
        this.clienteList = clienteList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idActEcon != null ? idActEcon.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Actividadeconomica)) {
            return false;
        }
        Actividadeconomica other = (Actividadeconomica) object;
        if ((this.idActEcon == null && other.idActEcon != null) || (this.idActEcon != null && !this.idActEcon.equals(other.idActEcon))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entities.Actividadeconomica[ idActEcon=" + idActEcon + " ]";
    }
    
}
