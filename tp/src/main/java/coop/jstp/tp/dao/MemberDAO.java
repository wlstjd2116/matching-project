package coop.jstp.tp.dao;

import coop.jstp.tp.vo.LoginDTO;
import coop.jstp.tp.vo.TestDTO;

public interface MemberDAO {
    // 회원 가입
    public void memberInput(LoginDTO dto);

    public int idDupChk(TestDTO testDTO);

    public TestDTO login(TestDTO testDTO);
}
