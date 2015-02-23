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
@Table(name = "metodopago")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Metodopago.findAll", query = "SELECT m FROM Metodopago m"),
    @NamedQuery(name = "Metodopago.findByIdMetodoPago", query = "SELECT m FROM Metodopago m WHERE m.idMetodoPago = :idMetodoPago"),
    @NamedQuery(name = "Metodopago.findByDescripcion", query = "SELECT m FROM Metodopago m WHERE m.descripcion = :descripcion")})
public class Metodopago implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "idMetodoPago")
    private Integer idMetodoPago;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "Descripcion")
    private String descripcion;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idMetodoPago")
    private List<Cliente> clienteList;

    public Metodopago() {
    }

    public Metodopago(Integer idMetodoPago) {
        this.idMetodoPago = idMetodoPago;
    }

    public Metodopago(Integer idMetodoPago, String descripcion) {
        this.idMetodoPago = idMetodoPago;
        this.descripcion = descripcion;
    }

    public Integer getIdMetodoPago() {
        return idMetodoPago;
    }

    public void setIdMetodoPago(Integer idMetodoPago) {
        this.idMetodoPago = idMetodoPago;
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
        hash += (idMetodoPago != null ? idMetodoPago.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Metodopago)) {
            return false;
        }
        Metodopago other = (Metodopago) object;
        if ((this.idMetodoPago == null && other.idMetodoPago != null) || (this.idMetodoPago != null && !this.idMetodoPago.equals(other.idMetodoPago))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return getDescripcion();
    }
    
}
