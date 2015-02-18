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
@Table(name = "detalleusuariocategoria")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Detalleusuariocategoria.findAll", query = "SELECT d FROM Detalleusuariocategoria d"),
    @NamedQuery(name = "Detalleusuariocategoria.findByDetalleUsuarioCategoria", query = "SELECT d FROM Detalleusuariocategoria d WHERE d.detalleUsuarioCategoria = :detalleUsuarioCategoria")})
public class Detalleusuariocategoria implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "detalleUsuarioCategoria")
    private Integer detalleUsuarioCategoria;
    @JoinColumn(name = "idUsuario", referencedColumnName = "idUsuario")
    @ManyToOne(optional = false)
    private Usuario idUsuario;
    @JoinColumn(name = "idCategoria", referencedColumnName = "idCategoria")
    @ManyToOne(optional = false)
    private Categoria idCategoria;

    public Detalleusuariocategoria() {
    }

    public Detalleusuariocategoria(Integer detalleUsuarioCategoria) {
        this.detalleUsuarioCategoria = detalleUsuarioCategoria;
    }

    public Integer getDetalleUsuarioCategoria() {
        return detalleUsuarioCategoria;
    }

    public void setDetalleUsuarioCategoria(Integer detalleUsuarioCategoria) {
        this.detalleUsuarioCategoria = detalleUsuarioCategoria;
    }

    public Usuario getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Usuario idUsuario) {
        this.idUsuario = idUsuario;
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
        hash += (detalleUsuarioCategoria != null ? detalleUsuarioCategoria.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Detalleusuariocategoria)) {
            return false;
        }
        Detalleusuariocategoria other = (Detalleusuariocategoria) object;
        if ((this.detalleUsuarioCategoria == null && other.detalleUsuarioCategoria != null) || (this.detalleUsuarioCategoria != null && !this.detalleUsuarioCategoria.equals(other.detalleUsuarioCategoria))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entities.Detalleusuariocategoria[ detalleUsuarioCategoria=" + detalleUsuarioCategoria + " ]";
    }
    
}
