package com.example.springjpa_spec;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserSearchRequest {

    @Size(min = 1, max = 50, message = "1文字以上、50文字以内で指定してください")
    private String name;

    @Min(value = 0, message = "0以上の値を指定してください")
    private Integer age;

    @Size(min = 1, max = 50, message = "1文字以上、50文字以内で指定してください")
    private String email;

    @Size(min = 1, max = 50, message = "1文字以上、50文字以内で指定してください")
    private String keyword;
}
