package com.fish.shareplan.domain.expense.entity;

import lombok.*;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "tbl_expense_item")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"receiptImages" ,"expense"})
@Builder
public class ExpenseItem {

    @Id
    @Column(name = "id", columnDefinition = "CHAR(36) DEFAULT UUID()")
    private final String id = UUID.randomUUID().toString();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "expense_id", nullable = false)
    private Expense expense;

    @Column(name = "spender", nullable = false, length = 255)
    private String spender;

    @Column(name = "amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @Column(name = "receipt_url", length = 255)
    private String receiptUrl;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "spent_at", nullable = false)
    private LocalDateTime spentAt;

    @Column(name = "created_at", nullable = false, updatable = false)
    private final LocalDateTime createdAt = LocalDateTime.now();

    @OneToMany(mappedBy = "expenseItem",cascade = CascadeType.REMOVE,orphanRemoval = true)
    @Builder.Default
    private List<ReceiptImage> receiptImages = new ArrayList<>();
}
