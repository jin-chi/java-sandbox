package com.example.springjpa_spec;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserSearchRequest {

    @Size(min = 1, max = 50, message = "名前は1文字以上50文字以下で入力してください")
    private String name;

    @Min(value = 0, message = "年齢は0以上で入力してください")
    private Integer age;
}
