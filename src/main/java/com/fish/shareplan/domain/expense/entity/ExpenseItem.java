package com.fish.shareplan.domain.expense.entity;

import com.fish.shareplan.domain.expense.dto.request.ExpenseRequestDto;
import com.fish.shareplan.domain.expense.dto.response.ExpenseItemDto;
import com.fish.shareplan.domain.expense.dto.response.ReceiptDto;
import lombok.*;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

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

    @OneToOne
    @JoinColumn(name = "expense_id", referencedColumnName = "id", nullable = false)
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
    @CreationTimestamp
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "expenseItem",cascade = CascadeType.REMOVE,orphanRemoval = true)
    @Builder.Default
    private List<ReceiptImage> receiptImages = new ArrayList<>();

    public void update(ExpenseRequestDto dto){
        this.amount = dto.getAmount();
        this.spender = dto.getSpender();
        this.description = dto.getDescription();
        this.spentAt =  dto.getSpendAt();
    }

    public static ExpenseItemDto toDto(ExpenseItem expenseItem,List<ReceiptImage> receiptImages){
        return ExpenseItemDto.builder()
                .expenseId(expenseItem.expense.getId())
                .spender(expenseItem.spender)
                .amount(expenseItem.amount)
                .description(expenseItem.description)
                .spendAt(expenseItem.spentAt)
                .receiptList(receiptImages.stream().map(ReceiptImage::toDto).toList())
                .build();
    }

    public static ExpenseItemDto toDto(ExpenseItem expenseItem){
        return ExpenseItemDto.builder()
                .expenseId(expenseItem.expense.getId())
                .spender(expenseItem.spender)
                .amount(expenseItem.amount)
                .description(expenseItem.description)
                .spendAt(expenseItem.spentAt)
                .build();
    }
}
