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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
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
@Table(name = "cliente")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Cliente.findAll", query = "SELECT c FROM Cliente c"),
    @NamedQuery(name = "Cliente.findByIdCliente", query = "SELECT c FROM Cliente c WHERE c.idCliente = :idCliente"),
    @NamedQuery(name = "Cliente.findByNit", query = "SELECT c FROM Cliente c WHERE c.nit = :nit"),
    @NamedQuery(name = "Cliente.findByRazonSocial", query = "SELECT c FROM Cliente c WHERE c.razonSocial = :razonSocial"),
    @NamedQuery(name = "Cliente.findByDireccion", query = "SELECT c FROM Cliente c WHERE c.direccion = :direccion"),
    @NamedQuery(name = "Cliente.findByTelefono", query = "SELECT c FROM Cliente c WHERE c.telefono = :telefono"),
    @NamedQuery(name = "Cliente.findByEmail", query = "SELECT c FROM Cliente c WHERE c.email = :email"),
    @NamedQuery(name = "Cliente.findByContrasenia", query = "SELECT c FROM Cliente c WHERE c.contrasenia = :contrasenia")})
public class Cliente implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idCliente")
    private Integer idCliente;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 15)
    @Column(name = "nit")
    private String nit;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "razonSocial")
    private String razonSocial;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "direccion")
    private String direccion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 15)
    @Column(name = "telefono")
    private String telefono;
    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "email")
    private String email;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "contrasenia")
    private String contrasenia;
    @JoinColumn(name = "IdActEcon", referencedColumnName = "IdActEcon")
    @ManyToOne(optional = false)
    private Actividadeconomica idActEcon;
    @JoinColumn(name = "idBarrio", referencedColumnName = "idBarrio")
    @ManyToOne(optional = false)
    private Barrio idBarrio;
    @JoinColumn(name = "idCiudad", referencedColumnName = "idCiudad")
    @ManyToOne(optional = false)
    private Ciudad idCiudad;
    @JoinColumn(name = "idDepartamento", referencedColumnName = "idDepartamento")
    @ManyToOne(optional = false)
    private Departamento idDepartamento;
    @JoinColumn(name = "idMetodoPago", referencedColumnName = "idMetodoPago")
    @ManyToOne(optional = false)
    private Metodopago idMetodoPago;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idCliente")
    private List<Detalleclienteproducto> detalleclienteproductoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idCliente")
    private List<Factura> facturaList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idCliente")
    private List<Detalleplancliente> detalleplanclienteList;

    public Cliente() {
    }

    public Cliente(Integer idCliente) {
        this.idCliente = idCliente;
    }

    public Cliente(Integer idCliente, String nit, String razonSocial, String direccion, String telefono, String email, String contrasenia) {
        this.idCliente = idCliente;
        this.nit = nit;
        this.razonSocial = razonSocial;
        this.direccion = direccion;
        this.telefono = telefono;
        this.email = email;
        this.contrasenia = contrasenia;
    }

    public Integer getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Integer idCliente) {
        this.idCliente = idCliente;
    }

    public String getNit() {
        return nit;
    }

    public void setNit(String nit) {
        this.nit = nit;
    }

    public String getRazonSocial() {
        return razonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContrasenia() {
        return contrasenia;
    }

    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }

    public Actividadeconomica getIdActEcon() {
        return idActEcon;
    }

    public void setIdActEcon(Actividadeconomica idActEcon) {
        this.idActEcon = idActEcon;
    }

    public Barrio getIdBarrio() {
        return idBarrio;
    }

    public void setIdBarrio(Barrio idBarrio) {
        this.idBarrio = idBarrio;
    }

    public Ciudad getIdCiudad() {
        return idCiudad;
    }

    public void setIdCiudad(Ciudad idCiudad) {
        this.idCiudad = idCiudad;
    }

    public Departamento getIdDepartamento() {
        return idDepartamento;
    }

    public void setIdDepartamento(Departamento idDepartamento) {
        this.idDepartamento = idDepartamento;
    }

    public Metodopago getIdMetodoPago() {
        return idMetodoPago;
    }

    public void setIdMetodoPago(Metodopago idMetodoPago) {
        this.idMetodoPago = idMetodoPago;
    }

    @XmlTransient
    public List<Detalleclienteproducto> getDetalleclienteproductoList() {
        return detalleclienteproductoList;
    }

    public void setDetalleclienteproductoList(List<Detalleclienteproducto> detalleclienteproductoList) {
        this.detalleclienteproductoList = detalleclienteproductoList;
    }

    @XmlTransient
    public List<Factura> getFacturaList() {
        return facturaList;
    }

    public void setFacturaList(List<Factura> facturaList) {
        this.facturaList = facturaList;
    }

    @XmlTransient
    public List<Detalleplancliente> getDetalleplanclienteList() {
        return detalleplanclienteList;
    }

    public void setDetalleplanclienteList(List<Detalleplancliente> detalleplanclienteList) {
        this.detalleplanclienteList = detalleplanclienteList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idCliente != null ? idCliente.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Cliente)) {
            return false;
        }
        Cliente other = (Cliente) object;
        if ((this.idCliente == null && other.idCliente != null) || (this.idCliente != null && !this.idCliente.equals(other.idCliente))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return getRazonSocial();
    }
    
}
