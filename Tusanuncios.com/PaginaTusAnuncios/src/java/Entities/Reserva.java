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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
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
    @NamedQuery(name = "Reserva.findByIdreserva", query = "SELECT r FROM Reserva r WHERE r.idreserva = :idreserva"),
    @NamedQuery(name = "Reserva.findByCantidad", query = "SELECT r FROM Reserva r WHERE r.cantidad = :cantidad"),
    @NamedQuery(name = "Reserva.findByValorUnitario", query = "SELECT r FROM Reserva r WHERE r.valorUnitario = :valorUnitario")})
public class Reserva implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "idreserva")
    private Integer idreserva;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Cantidad")
    private float cantidad;
    @Basic(optional = false)
    @NotNull
    @Column(name = "valorUnitario")
    private double valorUnitario;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idreserva")
    private List<Detalleusuarioreserva> detalleusuarioreservaList;
    @JoinColumn(name = "idProducto", referencedColumnName = "idProductoScio")
    @ManyToOne(optional = false)
    private ProductoServicio idProducto;

    public Reserva() {
    }

    public Reserva(Integer idreserva) {
        this.idreserva = idreserva;
    }

    public Reserva(Integer idreserva, float cantidad, double valorUnitario) {
        this.idreserva = idreserva;
        this.cantidad = cantidad;
        this.valorUnitario = valorUnitario;
    }

    public Integer getIdreserva() {
        return idreserva;
    }

    public void setIdreserva(Integer idreserva) {
        this.idreserva = idreserva;
    }

    public float getCantidad() {
        return cantidad;
    }

    public void setCantidad(float cantidad) {
        this.cantidad = cantidad;
    }

    public double getValorUnitario() {
        return valorUnitario;
    }

    public void setValorUnitario(double valorUnitario) {
        this.valorUnitario = valorUnitario;
    }

    @XmlTransient
    public List<Detalleusuarioreserva> getDetalleusuarioreservaList() {
        return detalleusuarioreservaList;
    }

    public void setDetalleusuarioreservaList(List<Detalleusuarioreserva> detalleusuarioreservaList) {
        this.detalleusuarioreservaList = detalleusuarioreservaList;
    }

    public ProductoServicio getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(ProductoServicio idProducto) {
        this.idProducto = idProducto;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idreserva != null ? idreserva.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Reserva)) {
            return false;
        }
        Reserva other = (Reserva) object;
        if ((this.idreserva == null && other.idreserva != null) || (this.idreserva != null && !this.idreserva.equals(other.idreserva))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entities.Reserva[ idreserva=" + idreserva + " ]";
    }
    
}
