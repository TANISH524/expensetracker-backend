package com.tanish.expensetracker.service;

import com.tanish.expensetracker.entity.Expense;
import com.tanish.expensetracker.entity.User;
import com.tanish.expensetracker.repository.ExpenseRepository;
import com.tanish.expensetracker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ExpenseService {

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private UserRepository userRepository;

    private User getUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found!"));
    }

    public List<Expense> getAllExpenses(String email) {
        User user = getUser(email);
        return expenseRepository.findByUser(user);
    }

    public Expense addExpense(Expense expense, String email) {
        User user = getUser(email);
        expense.setUser(user);
        return expenseRepository.save(expense);
    }

    public Expense updateExpense(Long id, Expense updatedExpense, String email) {
        User user = getUser(email);

        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Expense not found!"));

        if (!expense.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Access denied!");
        }

        expense.setTitle(updatedExpense.getTitle());
        expense.setDescription(updatedExpense.getDescription());
        expense.setAmount(updatedExpense.getAmount());
        expense.setDate(updatedExpense.getDate());
        expense.setCategory(updatedExpense.getCategory());

        return expenseRepository.save(expense);
    }

    public void deleteExpense(Long id, String email) {
        User user = getUser(email);

        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Expense not found!"));

        if (!expense.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Access denied!");
        }

        expenseRepository.delete(expense);
    }
}