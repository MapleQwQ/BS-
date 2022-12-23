package com.example.server.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Entity
@Data
@NoArgsConstructor
@Getter
@Setter
public class AppUser {
    @Id
    @GeneratedValue
    private Long id;

    @NotBlank(message = "Name is mandatory.")
    private String userName;

    @NotBlank(message = "Email is mandatory")
    private String email;

    //   小坑 不能用NotBlank 要用NotNull
    @NotNull(message = "Type is mandatory")
    private int isAdmin;

    @NotBlank(message = "Password is mandatory")
    private String password;

    public AppUser(String userName, String email, int isAdmin, String password) {
        this.userName = userName;
        this.email = email;
        this.isAdmin = isAdmin;
        this.password = password;
    }


    public Long getId(){return id;}
    public String getUserName(){return userName;}
    public String getEmail(){return email;}
    public String getPassword(){return password;}
    public void setPassword(String password){this.password = password;}
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) {
//            return true;
//        }
//        if (!(o instanceof AppUser another)) {
//            return false;
//        }
//        return Objects.equals(this.id, another.id);
//    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AppUser)) return false;
        AppUser appUser = (AppUser) o;
        return Objects.equals(id, appUser.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "User{" + "id=" + this.id + ", userName='" + this.userName + '\'' + ", isAdmin=" + this.isAdmin + ", email='" + this.email + '\'' + '}';
    }
}