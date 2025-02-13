package com.fish.shareplan.domain.expense.dto.request;

import com.fish.shareplan.domain.expense.entity.ReceiptImage;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Builder
public class ExpenseRequestDto {

    private String spender;

    private BigDecimal amount;

    private List<MultipartFile> images;

    private String description;

    private LocalDateTime spendAt;

    private LocalDateTime createdAt;
}
