package com.fish.shareplan.repository;

import com.fish.shareplan.domain.expense.dto.response.ExpenseResponseDto;

import java.util.List;

public interface ExpenseRepositoryCustom {

    List<ExpenseResponseDto> findAllExpense(String roomId);
}
