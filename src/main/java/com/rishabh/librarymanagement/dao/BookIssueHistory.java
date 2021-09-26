package com.rishabh.librarymanagement.dao;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
//@Table(name = "Book_Issue_History", uniqueConstraints = { @UniqueConstraint(columnNames = { "user", "book" }) })
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookIssueHistory implements Serializable {

    @Id
    @GeneratedValue
    Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    User user;

    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    Book book;

    @Column(name = "issued_date", nullable = false)
    Date issuedDate;

    @Column(name = "due_date", nullable = false)
    Date dueDate;

    @Column(name = "returned_date")
    Date returnedDate;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        BookIssueHistory that = (BookIssueHistory) o;

        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return 607390392;
    }
}
