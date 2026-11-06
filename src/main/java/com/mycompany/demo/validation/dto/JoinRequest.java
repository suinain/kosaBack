package com.mycompany.demo.validation.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data 
@AllArgsConstructor 
@NoArgsConstructor 
public class JoinRequest {
    @NotBlank(message = "mid는 필수 입력 정보입니다.")
    @Size(min = 5, max = 20, message = "mid는 5자 이상 20자 이하여야 합니다.")
    @Pattern(
        regexp = "(?=.*[A-Z])(?=.*[!_])[a-zA-Z][a-zA-Z0-9!_]*",
        message = "mid는 영문자로 시작하고, 대문자와 특수기호(! 또는 _)를 각각 1자 이상 포함하며, 영문자, 숫자, !, _만 사용할 수 있습니다."
    )
    private String mid;

    @NotBlank(message = "이름은 필수 입력 정보입니다.")
    @Size(min = 2, max = 20, message = "이름은 2자 이상 50자 이하여야 합니다.")
    private String mname;

    @NotBlank(message = "이메일은 필수 입력 정보입니다.")
    @Email(message = "올바른 이메일 형식으로 입력해야 합니다.")
    private String memail;

    @NotBlank(message = "비밀번호는 필수 입력 정보입니다.")
    @Size(min = 8, max = 64, message = "비밀번호는 8자 이상 64자 이하여야 합니다.")
    @Pattern(regexp = "(?U)\\S+", message = "비밀번호에는 공백이나 줄바꿈을 사용할 수 없습니다.")
    private String mpassword;

    @NotNull(message = "나이는 필수 입력 정보입니다.")
    @Min(value = 0, message = "나이는 0세 이상이어야 합니다.")
    @Max(value = 150, message = "나이는 150세 이하여야 합니다.")
    private Integer mage;
}
