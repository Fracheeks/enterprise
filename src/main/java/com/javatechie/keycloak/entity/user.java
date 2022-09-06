package com.javatechie.keycloak.entity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "user_info")
@Data
public abstract class user {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "roleName")
    public String roleName;

    @Column(name = "username")
    public String username;

    @Column(name = "userIdByToke")
    @JsonIgnore
    public String userIdByToken; //referenced to keycloak

}
