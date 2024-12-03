package com.github.ajharry69.kcbtechnicalinterview.task;

import com.github.ajharry69.kcbtechnicalinterview.task.models.Task;
import com.github.ajharry69.kcbtechnicalinterview.task.models.TaskStatus;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
public class TaskSpecification implements Specification<Task> {
    private final UUID projectId;
    private final TaskStatus status;
    private final LocalDate dueDate;

    @Override
    public Predicate toPredicate(Root<Task> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(criteriaBuilder.equal(root.get("projectId"), projectId));
        if (status != null) {
            predicates.add(criteriaBuilder.equal(root.get("status"), status));
        }
        if (dueDate != null) {
            predicates.add(criteriaBuilder.equal(root.get("dueDate"), dueDate));
        }

        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }
}
