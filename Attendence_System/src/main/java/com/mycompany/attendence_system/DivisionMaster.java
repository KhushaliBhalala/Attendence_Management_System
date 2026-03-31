/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.attendence_system;

import jakarta.json.bind.annotation.JsonbTransient;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

/**
 *
 * @author HP
 */
@Entity
@Table(name = "division_master")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DivisionMaster.findAll", query = "SELECT d FROM DivisionMaster d"),
    @NamedQuery(name = "DivisionMaster.findById", query = "SELECT d FROM DivisionMaster d WHERE d.id = :id"),
    @NamedQuery(name = "DivisionMaster.findByDivisionName", query = "SELECT d FROM DivisionMaster d WHERE d.divisionName = :divisionName"),
    @NamedQuery(name = "DivisionMaster.findByCreatedBy", query = "SELECT d FROM DivisionMaster d WHERE d.createdBy = :createdBy"),
    @NamedQuery(name = "DivisionMaster.findByCreatedDate", query = "SELECT d FROM DivisionMaster d WHERE d.createdDate = :createdDate"),
    @NamedQuery(name = "DivisionMaster.findByModifiedBy", query = "SELECT d FROM DivisionMaster d WHERE d.modifiedBy = :modifiedBy"),
    @NamedQuery(name = "DivisionMaster.findByModifiedDate", query = "SELECT d FROM DivisionMaster d WHERE d.modifiedDate = :modifiedDate")})
public class DivisionMaster implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "division_name")
    private String divisionName;
    @Basic(optional = false)
    @NotNull
    @Column(name = "created_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;
    @Basic(optional = false)
    @NotNull
    @Column(name = "modified_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifiedDate;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "created_by")
    private Integer createdBy;
    @Column(name = "modified_by")
    private Integer modifiedBy;
    @OneToMany(mappedBy = "divisionId")
    @JsonbTransient
    private Collection<StudentMaster> studentMasterCollection;
    @OneToMany(mappedBy = "divisionId")
    @JsonbTransient
    private Collection<ClassMaster> classMasterCollection;

    public DivisionMaster() {
    }

    public DivisionMaster(Integer id) {
        this.id = id;
    }

    public DivisionMaster(Integer id, String divisionName, Date createdDate, Date modifiedDate) {
        this.id = id;
        this.divisionName = divisionName;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDivisionName() {
        return divisionName;
    }

    public void setDivisionName(String divisionName) {
        this.divisionName = divisionName;
    }

    public Integer getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Integer getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(Integer modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public Date getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }
@JsonbTransient
    @XmlTransient
    public Collection<StudentMaster> getStudentMasterCollection() {
        return studentMasterCollection;
    }

    public void setStudentMasterCollection(Collection<StudentMaster> studentMasterCollection) {
        this.studentMasterCollection = studentMasterCollection;
    }

    @XmlTransient
    public Collection<ClassMaster> getClassMasterCollection() {
        return classMasterCollection;
    }

    public void setClassMasterCollection(Collection<ClassMaster> classMasterCollection) {
        this.classMasterCollection = classMasterCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DivisionMaster)) {
            return false;
        }
        DivisionMaster other = (DivisionMaster) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.attendence_system.DivisionMaster[ id=" + id + " ]";
    }   
}
