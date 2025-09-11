package com.kadioglumf.dataservice.payload.request.search;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.kadioglumf.dataservice.enums.FieldType;
import com.kadioglumf.dataservice.enums.Operator;
import jakarta.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class SearchRequest implements Serializable {

  @Serial private static final long serialVersionUID = 8514625832019794838L;

  private Operator operator;

  private List<FilterRequest> filters;

  private List<SortRequest> sorts;

  @NotNull private Integer page;

  @NotNull private Integer size;

  public List<FilterRequest> getFilters() {
    if (Objects.isNull(this.filters)) return new ArrayList<>();
    return this.filters;
  }

  public List<SortRequest> getSorts() {
    if (Objects.isNull(this.sorts)) return new ArrayList<>();
    return this.sorts;
  }

  public void addFilterByParameter(
      @NonNull String key,
      @NonNull List<ConditionRequest> conditionRequestList,
      @NonNull FieldType fieldType,
      @Nullable Operator operator) {
    FilterRequest filter = new FilterRequest();

    filter.setKey(key);
    filter.setConditions(conditionRequestList);
    filter.setFieldType(fieldType);
    filter.setOperator(operator);

    if (Objects.isNull(this.filters)) {
      this.filters = new ArrayList<>();
    }
    filters.add(filter);
  }
}
