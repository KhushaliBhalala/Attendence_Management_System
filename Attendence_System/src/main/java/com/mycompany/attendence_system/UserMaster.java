/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.attendence_system;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Date;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import java.util.Collection;

/**
 *
 * @author HP
 */
@Entity
@Table(name = "user_master")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UserMaster.findAll", query = "SELECT u FROM UserMaster u"),
    @NamedQuery(name = "UserMaster.findById", query = "SELECT u FROM UserMaster u WHERE u.id = :id"),
    @NamedQuery(name = "UserMaster.findByUsername", query = "SELECT u FROM UserMaster u WHERE u.username = :username"),
    @NamedQuery(name = "UserMaster.findByPassword", query = "SELECT u FROM UserMaster u WHERE u.password = :password"),
    @NamedQuery(name = "UserMaster.findByCreatedBy", query = "SELECT u FROM UserMaster u WHERE u.createdBy = :createdBy"),
    @NamedQuery(name = "UserMaster.findByCreatedAt", query = "SELECT u FROM UserMaster u WHERE u.createdAt = :createdAt"),
    @NamedQuery(name = "UserMaster.findByModifiedBy", query = "SELECT u FROM UserMaster u WHERE u.modifiedBy = :modifiedBy"),
    @NamedQuery(name = "UserMaster.findByModifiedAt", query = "SELECT u FROM UserMaster u WHERE u.modifiedAt = :modifiedAt"),
  @NamedQuery(name = "UserMaster.validate", 
    query = "SELECT u FROM UserMaster u WHERE u.username = :uname AND u.password = :pwd AND u.roleId.id = :rid")

})

public class UserMaster implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Size(max = 100)
    @Column(name = "username")
    private String username;
    @Size(max = 100)
    @Column(name = "password")
    private String password;
    @Column(name = "created_by")
    private Integer createdBy;
    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    @Column(name = "modified_by")
    private Integer modifiedBy;
    @Column(name = "modified_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifiedAt;
    @JoinColumn(name = "role_id", referencedColumnName = "id")
    @ManyToOne
    private RoleMaster roleId;
    
    @OneToMany(mappedBy = "userId")
private Collection<FacultyMaster> facultyMasterCollection;
    
    public Integer getFacultyId() {
    if (facultyMasterCollection != null && !facultyMasterCollection.isEmpty()) {
        // Get the first faculty record associated with this user
        return facultyMasterCollection.iterator().next().getId();
    }
    return null;
}

    @PrePersist
    protected void onCreate() {
        this.createdAt = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        this.modifiedAt = new Date();
    }

    public UserMaster() {
    }

    public UserMaster(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(Integer modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public Date getModifiedAt() {
        return modifiedAt;
    }

    public void setModifiedAt(Date modifiedAt) {
        this.modifiedAt = modifiedAt;
    }

    public RoleMaster getRoleId() {
        return roleId;
    }

    public void setRoleId(RoleMaster roleId) {
        this.roleId = roleId;
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
        if (!(object instanceof UserMaster)) {
            return false;
        }
        UserMaster other = (UserMaster) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.attendence_system.UserMaster[ id=" + id + " ]";
    }

}
