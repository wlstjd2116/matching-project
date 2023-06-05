package coop.jstp.tp.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class MatchStartDTO {
    private int userNum;
    private int matchingTier;
}
