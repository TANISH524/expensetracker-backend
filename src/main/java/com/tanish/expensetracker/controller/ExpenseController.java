package com.tanish.expensetracker.controller;

import com.tanish.expensetracker.entity.Expense;
import com.tanish.expensetracker.service.ExpenseService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {

    @Autowired
    private ExpenseService expenseService;

    @GetMapping
    public ResponseEntity<?> getAllExpenses(Authentication authentication) {
        return ResponseEntity.ok(
            expenseService.getAllExpenses(authentication.getName()));
    }

    @PostMapping
    public ResponseEntity<?> addExpense(@Valid @RequestBody Expense expense,
                                         Authentication authentication) {
        expenseService.addExpense(expense, authentication.getName());
        return ResponseEntity.ok(Map.of("message", "Expense added successfully!"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateExpense(@PathVariable Long id,
                                            @Valid @RequestBody Expense updatedExpense,
                                            Authentication authentication) {
        try {
            expenseService.updateExpense(id, updatedExpense, authentication.getName());
            return ResponseEntity.ok(Map.of("message", "Expense updated successfully!"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(403)
                    .body(Map.of("message", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteExpense(@PathVariable Long id,
                                            Authentication authentication) {
        try {
            expenseService.deleteExpense(id, authentication.getName());
            return ResponseEntity.ok(Map.of("message", "Expense deleted successfully!"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(403)
                    .body(Map.of("message", e.getMessage()));
        }
    }
}