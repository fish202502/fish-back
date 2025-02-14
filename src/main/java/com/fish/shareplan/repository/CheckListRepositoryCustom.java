package com.fish.shareplan.repository;

import com.fish.shareplan.domain.checklist.entity.CheckList;
import com.fish.shareplan.domain.expense.dto.response.ExpenseResponseDto;
import com.fish.shareplan.domain.expense.entity.Expense;

import java.util.List;

public interface CheckListRepositoryCustom {

    List<CheckList> findAllCheckList(String roomId);
}
