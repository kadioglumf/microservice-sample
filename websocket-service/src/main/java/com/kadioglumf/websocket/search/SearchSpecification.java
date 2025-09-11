package com.kadioglumf.websocket.search;

import com.kadioglumf.websocket.payload.request.search.*;
import com.kadioglumf.websocket.constant.ExceptionConstants;
import com.kadioglumf.websocket.core.exception.BusinessException;
import com.kadioglumf.websocket.enums.RoleType;
import com.kadioglumf.websocket.enums.SearchExpressionType;
import com.kadioglumf.websocket.payload.request.search.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.NonNull;
import org.springframework.util.CollectionUtils;

@Slf4j
@AllArgsConstructor
public class SearchSpecification<T> implements Specification<T> {

  private static final long serialVersionUID = -9153865343320750644L;

  private final SearchRequest request;
  private final SearchExpressionType searchExpressionType;
  private final RoleType searchRoleType;

  @Override
  public Predicate toPredicate(
      @NonNull Root<T> root, @NonNull CriteriaQuery<?> query, @NonNull CriteriaBuilder cb) {
    try {
      List<Predicate> filterPredicates = new ArrayList<>();
      for (FilterRequest filter : this.request.getFilters()) {
        Predicate conditionPredicate = null;

        for (ConditionRequest condition : filter.getConditions()) {
          log.info(
              "Filter: {} {} {}",
              filter.getKey(),
              condition.getFilterType().toString(),
              condition.getValue());
          conditionPredicate =
              condition
                  .getFilterType()
                  .build(
                      root,
                      cb,
                      filter,
                      conditionPredicate,
                      condition,
                      searchExpressionType,
                      searchRoleType);
          if (conditionPredicate == null) {
            throw new BusinessException(ExceptionConstants.FILTER_TYPE_NOT_SUITABLE_ERROR);
          }
        }
        filterPredicates.add(conditionPredicate);
      }

      Predicate predicate = null;
      if (!CollectionUtils.isEmpty(filterPredicates)) {
        predicate = filterPredicates.get(0);
        for (int i = 1; i < filterPredicates.size(); i++) {

          predicate =
              Operator.OR.equals(request.getOperator())
                  ? cb.or(filterPredicates.get(i), predicate)
                  : cb.and(filterPredicates.get(i), predicate);
        }
      }

      List<Order> orders = new ArrayList<>();
      for (SortRequest sort : this.request.getSorts()) {
        orders.add(sort.getDirection().build(root, cb, sort, searchExpressionType, searchRoleType));
      }

      query.orderBy(orders);
      return predicate;
    } catch (BusinessException ex) {
      throw ex;
    } catch (Exception ex) {
      throw new BusinessException(ExceptionConstants.SEARCH_UNKNOWN_ERROR, ex.getMessage());
    }
  }

  public static Pageable getPageable(Integer page, Integer size) {
    return PageRequest.of(
        Objects.requireNonNullElse(page, 0), Objects.requireNonNullElse(size, 100));
  }
}
