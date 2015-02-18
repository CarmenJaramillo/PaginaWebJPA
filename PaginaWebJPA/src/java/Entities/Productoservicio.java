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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Carmen
 */
@Entity
@Table(name = "productoservicio")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Productoservicio.findAll", query = "SELECT p FROM Productoservicio p"),
    @NamedQuery(name = "Productoservicio.findByIdProductoScio", query = "SELECT p FROM Productoservicio p WHERE p.idProductoScio = :idProductoScio"),
    @NamedQuery(name = "Productoservicio.findByNombreProductoServicio", query = "SELECT p FROM Productoservicio p WHERE p.nombreProductoServicio = :nombreProductoServicio"),
    @NamedQuery(name = "Productoservicio.findByVrUnitario", query = "SELECT p FROM Productoservicio p WHERE p.vrUnitario = :vrUnitario"),
    @NamedQuery(name = "Productoservicio.findByCantidad", query = "SELECT p FROM Productoservicio p WHERE p.cantidad = :cantidad")})
public class Productoservicio implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "idProductoScio")
    private Integer idProductoScio;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 250)
    @Column(name = "nombreProductoServicio")
    private String nombreProductoServicio;
    @Basic(optional = false)
    @NotNull
    @Column(name = "vrUnitario")
    private double vrUnitario;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cantidad")
    private float cantidad;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idProductoScio")
    private List<Detallefacturaproductoscio> detallefacturaproductoscioList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idProductoScio")
    private List<Detalleclienteproducto> detalleclienteproductoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idProductoScio")
    private List<Reserva> reservaList;
    @JoinColumn(name = "idCategoria", referencedColumnName = "idCategoria")
    @ManyToOne(optional = false)
    private Categoria idCategoria;

    public Productoservicio() {
    }

    public Productoservicio(Integer idProductoScio) {
        this.idProductoScio = idProductoScio;
    }

    public Productoservicio(Integer idProductoScio, String nombreProductoServicio, double vrUnitario, float cantidad) {
        this.idProductoScio = idProductoScio;
        this.nombreProductoServicio = nombreProductoServicio;
        this.vrUnitario = vrUnitario;
        this.cantidad = cantidad;
    }

    public Integer getIdProductoScio() {
        return idProductoScio;
    }

    public void setIdProductoScio(Integer idProductoScio) {
        this.idProductoScio = idProductoScio;
    }

    public String getNombreProductoServicio() {
        return nombreProductoServicio;
    }

    public void setNombreProductoServicio(String nombreProductoServicio) {
        this.nombreProductoServicio = nombreProductoServicio;
    }

    public double getVrUnitario() {
        return vrUnitario;
    }

    public void setVrUnitario(double vrUnitario) {
        this.vrUnitario = vrUnitario;
    }

    public float getCantidad() {
        return cantidad;
    }

    public void setCantidad(float cantidad) {
        this.cantidad = cantidad;
    }

    @XmlTransient
    public List<Detallefacturaproductoscio> getDetallefacturaproductoscioList() {
        return detallefacturaproductoscioList;
    }

    public void setDetallefacturaproductoscioList(List<Detallefacturaproductoscio> detallefacturaproductoscioList) {
        this.detallefacturaproductoscioList = detallefacturaproductoscioList;
    }

    @XmlTransient
    public List<Detalleclienteproducto> getDetalleclienteproductoList() {
        return detalleclienteproductoList;
    }

    public void setDetalleclienteproductoList(List<Detalleclienteproducto> detalleclienteproductoList) {
        this.detalleclienteproductoList = detalleclienteproductoList;
    }

    @XmlTransient
    public List<Reserva> getReservaList() {
        return reservaList;
    }

    public void setReservaList(List<Reserva> reservaList) {
        this.reservaList = reservaList;
    }

    public Categoria getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(Categoria idCategoria) {
        this.idCategoria = idCategoria;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idProductoScio != null ? idProductoScio.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Productoservicio)) {
            return false;
        }
        Productoservicio other = (Productoservicio) object;
        if ((this.idProductoScio == null && other.idProductoScio != null) || (this.idProductoScio != null && !this.idProductoScio.equals(other.idProductoScio))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entities.Productoservicio[ idProductoScio=" + idProductoScio + " ]";
    }
    
}
