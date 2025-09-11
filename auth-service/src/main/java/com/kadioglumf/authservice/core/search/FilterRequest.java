package com.kadioglumf.authservice.core.search;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class FilterRequest implements Serializable {

  private static final long serialVersionUID = 6293344849078612450L;

  @NotBlank private String key;

  @NotNull private List<ConditionRequest> conditions;

  @NotNull private FieldType fieldType;

  @NotNull private Operator operator;

  public List<ConditionRequest> getConditions() {
    if (Objects.isNull(this.conditions)) return new ArrayList<>();
    return this.conditions;
  }
}
