package com.fish.shareplan.repository;

import com.fish.shareplan.domain.expense.entity.ExpenseItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExpenseItemRepository extends JpaRepository<ExpenseItem,String> {
}
