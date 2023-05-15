package coop.jstp.tp.serviceimpl;

import coop.jstp.tp.dao.MemberDAO;
import coop.jstp.tp.service.MemberService;
import coop.jstp.tp.vo.TestDTO;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

@Service
public class MemberServiceImpl implements MemberService {

    @Autowired
    private MemberDAO memberDAO;

    @Override
    public void memberInput(TestDTO dto) {
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
}
