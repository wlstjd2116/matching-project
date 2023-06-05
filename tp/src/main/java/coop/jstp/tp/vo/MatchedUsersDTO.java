package coop.jstp.tp.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class MatchedUsersDTO {
    private int m_num;
    private int user1;
    private int user2;
}
