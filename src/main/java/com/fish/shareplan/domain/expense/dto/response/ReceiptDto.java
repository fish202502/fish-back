package com.fish.shareplan.domain.expense.dto.response;

import lombok.*;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Builder
public class ReceiptDto {
    private String receiptId;

    private String url;
}
