package com.example.employee_search;

import java.util.List;

import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmployeeRequestDto {

    @Size(min = 1, max = 100, message = "1文字以上100文字以下で指定してください")
    String name;

    @Size(max = 10, message = "10件まで指定可能です")
    List<String> departments;

    @Min(value = 0, message = "0以上の値を指定してください")
    Integer salaryFrom;

    @Min(value = 0, message = "0以上の値を指定してください")
    Integer salaryTo;

    Boolean topLevelOnly;

    public boolean isEmpty() {
        return !StringUtils.hasText(name)
                && CollectionUtils.isEmpty(departments)
                && salaryFrom == null
                && salaryTo == null
                && topLevelOnly == null;
    }
}
