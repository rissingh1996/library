package com.rishabh.librarymanagement.dao;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "Book_Inventory")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookInventory {

    @Id
    @GeneratedValue
    Long id;

    @Column(name = "library_code", nullable = false)
    String libraryCode;

    @ManyToOne
    @JoinColumn(name = "book_id")
    Book book;

    @Column(name = "book_count")
    Integer bookCount = 0;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        BookInventory that = (BookInventory) o;

        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return 567009187;
    }
}
