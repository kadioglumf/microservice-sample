package com.kadioglumf.websocket.enums;

import com.kadioglumf.websocket.payload.request.search.SortRequest;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Root;

public enum SortDirection {
  ASC {
    public <T> Order build(
        Root<T> root,
        CriteriaBuilder cb,
        SortRequest request,
        SearchExpressionType searchExpressionType,
        RoleType searchRoleType) {
      return cb.asc(searchExpressionType.getExpression(root, request.getKey(), searchRoleType));
    }
  },
  DESC {
    public <T> Order build(
        Root<T> root,
        CriteriaBuilder cb,
        SortRequest request,
        SearchExpressionType searchExpressionType,
        RoleType searchRoleType) {
      return cb.desc(searchExpressionType.getExpression(root, request.getKey(), searchRoleType));
    }
  };

  public abstract <T> Order build(
      Root<T> root,
      CriteriaBuilder cb,
      SortRequest request,
      SearchExpressionType searchExpressionType,
      RoleType searchRoleType);
}
