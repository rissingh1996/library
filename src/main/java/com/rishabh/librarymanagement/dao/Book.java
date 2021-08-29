package com.rishabh.librarymanagement.dao;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.Hibernate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "Book")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Book {

    @JsonIgnore
    @Id
    Long id;

    String category;

    @Column(name = "book_no")
    String bookNo;

    @Column(name = "book_name")
    String bookName;

    @Column
    String author;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Book book = (Book) o;

        return Objects.equals(id, book.id);
    }

    @Override
    public int hashCode() {
        return 967762358;
    }
}
