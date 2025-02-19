package com.fish.shareplan.domain.expense.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
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

    @JsonProperty("expenseItemId")
    private String expenseId;

    private String spender;

    private BigDecimal amount;

    private String description;

    private LocalDateTime spendAt;

    private List<ReceiptDto> receiptList;
}