package com.rishabh.librarymanagement.dao;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.Hibernate;

import javax.annotation.PostConstruct;
import javax.persistence.*;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "Userr")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User implements Serializable {

    @Id
    String loginId;

    @ManyToOne
    @JoinColumn(name = "library_code", referencedColumnName = "library_code", nullable = false)
    Library library;

    @Column(name = "role", nullable = false)
    String role;

    @Column(name = "name")
    String name;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        User user = (User) o;

        return Objects.equals(loginId, user.loginId);
    }

    @Override
    public int hashCode() {
        return 562048007;
    }
}
