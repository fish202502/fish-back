package com.fish.shareplan.repository;

import com.fish.shareplan.domain.expense.entity.Expense;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExpenseRepository extends JpaRepository<Expense,String>,ExpenseRepositoryCustom {

}
