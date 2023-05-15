package coop.jstp.tp.service;

import coop.jstp.tp.vo.TestDTO;
import org.springframework.http.ResponseEntity;

public interface MemberService {

    // 회원가입
    public void memberInput(TestDTO dto);

    // ID 중복체크
    public ResponseEntity<?> idDupChk(TestDTO dto);

    public TestDTO login(TestDTO dto);

}
