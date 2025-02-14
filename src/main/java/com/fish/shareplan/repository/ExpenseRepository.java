package com.fish.shareplan.repository;

import com.fish.shareplan.domain.checklist.entity.CheckList;
import com.fish.shareplan.domain.expense.entity.Expense;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ExpenseRepository extends JpaRepository<Expense,String>,ExpenseRepositoryCustom {
    Optional<Expense> findByRoomId(String roomId);
}
