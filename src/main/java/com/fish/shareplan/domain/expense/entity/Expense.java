package com.fish.shareplan.domain.expense.entity;

import com.fish.shareplan.domain.room.entity.Room;
import lombok.*;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "tbl_expense")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "expenseItem")
@Builder
public class Expense {

    @Id
    @Column(name = "id", columnDefinition = "CHAR(36) DEFAULT UUID()")
    private final String id = UUID.randomUUID().toString();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "expense",cascade = CascadeType.REMOVE,orphanRemoval = true)
    @Builder.Default
    private List<ExpenseItem> expenseItem = new ArrayList<>();
}