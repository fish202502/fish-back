package com.fish.shareplan.domain.expense.dto.response;

import com.fish.shareplan.domain.expense.entity.QExpenseItem;
import com.querydsl.core.Tuple;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Builder
public class ExpenseItemDto {

    private String expenseItemId;

    private String spender;

    private BigDecimal amount;

    private String description;

    private LocalDateTime spendAt;

    private List<ReceiptDto> receiptList;

    public static ExpenseItemDto from(Tuple tuple) {
        return ExpenseItemDto.builder()
                .expenseItemId(tuple.get(QExpenseItem.expenseItem.id))
                .spender(tuple.get(QExpenseItem.expenseItem.spender))
                .amount(tuple.get(QExpenseItem.expenseItem.amount))
                .description(tuple.get(QExpenseItem.expenseItem.description))
                .spendAt(tuple.get(QExpenseItem.expenseItem.spentAt))
                .build();
    }
}