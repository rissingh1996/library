package com.rishabh.librarymanagement.dao;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.Hibernate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "Library")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Library implements Serializable {

    @Id
    Long id;

    @Column(name = "name")
    String name;

    @Column(name = "library_code", nullable = false, unique = true)
    String libraryCode;

    @Column(name = "mon")
    Boolean monday;

    @Column(name = "tues")
    Boolean tuesday;

    @Column(name = "wed")
    Boolean wednesday;

    @Column(name = "thurs")
    Boolean Thursday;

    @Column(name = "fri")
    Boolean friday;

    @Column(name = "sat")
    Boolean saturday;

    @Column(name = "sun")
    Boolean sunday;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Library library = (Library) o;

        return Objects.equals(libraryCode, library.libraryCode);
    }

    @Override
    public int hashCode() {
        return 536193405;
    }
}
