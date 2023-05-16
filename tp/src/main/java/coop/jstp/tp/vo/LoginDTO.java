package coop.jstp.tp.vo;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import javax.validation.*;
import javax.validation.constraints.NotBlank;

@Data
public class LoginDTO {

    @ApiModelProperty(notes="id", position = 1, dataType = "String")
    @NotBlank(message = "아이디가 빈 값입니다.")
    private String id;

    @ApiModelProperty(notes="password", position = 2, dataType = "String")
    @NotBlank(message = "비밀번호가 빈 값입니다.")
    private String pw;

    @ApiModelProperty(notes = "userName", position = 3, dataType = "String")
    @NotBlank(message="소환사 이름이 빈 값입니다.")
    private String userName;

    private Integer u_Num;
    private String u_Reg;
}
