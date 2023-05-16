package coop.jstp.tp.serviceimpl;

import coop.jstp.tp.dao.MemberDAO;
import coop.jstp.tp.service.MemberService;
import coop.jstp.tp.vo.LoginDTO;
import coop.jstp.tp.vo.TestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

@Service
public class MemberServiceImpl implements MemberService {

    @Autowired
    private MemberDAO memberDAO;

    @Override
    public void memberInput(LoginDTO dto) {
        memberDAO.memberInput(dto);
    }
    @Override
    public ResponseEntity<?> idDupChk(TestDTO dto) {
        int chk = memberDAO.idDupChk(dto);

        HttpHeaders header = new HttpHeaders();
        header.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));

        return new ResponseEntity<>(chk, header, HttpStatus.OK);
    }

    // 로그인 시도
    @Override
    public TestDTO login(TestDTO dto) {
        TestDTO result = memberDAO.login(dto);
        return result;
    }

    // 회원가입 set parameter
    @Override
    public LoginDTO setInputParam(LoginDTO dto) {
        LoginDTO lDto = new LoginDTO();

        lDto.setId(dto.getId());
        lDto.setPw(dto.getPw());
        lDto.setUserName(dto.getUserName());

        return lDto;
    }
}
