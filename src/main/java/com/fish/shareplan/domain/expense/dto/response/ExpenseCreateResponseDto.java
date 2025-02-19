package com.fish.shareplan.domain.expense.dto.response;

import lombok.*;

import java.util.List;
import java.util.Map;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Builder
public class ExpenseCreateResponseDto {

    private String expenseId;

    private String expenseItemId;

    private List<ReceiptDto> receipt;
}
