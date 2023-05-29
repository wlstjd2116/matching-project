package coop.jstp.tp.service;

import coop.jstp.tp.vo.LoginDTO;
import coop.jstp.tp.vo.TestDTO;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.HashMap;

public interface MemberService {

    // 회원가입
    public void memberInput(LoginDTO dto);

    // ID 중복체크
    public ResponseEntity<?> idDupChk(TestDTO dto);

    public TestDTO login(TestDTO dto);

    public LoginDTO setInputParam(LoginDTO dto);

    public ResponseEntity<HashMap> getUserId(String summonerName);

    public ResponseEntity<ArrayList> getUserTier(String accountId);

}
