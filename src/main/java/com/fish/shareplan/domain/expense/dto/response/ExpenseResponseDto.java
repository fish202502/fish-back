package com.fish.shareplan.domain.expense.dto.response;

import lombok.*;

import java.util.List;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Builder
public class ExpenseResponseDto {
    private String expenseId;
    private List<ExpenseItemDto> expenseItemList;
}
