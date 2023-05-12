package coop.jstp.tp.dao;

import coop.jstp.tp.vo.TestDTO;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MemberDAOImpl implements MemberDAO{

    @Autowired
    private SqlSession sqlSession;

    @Override
    public void memberInput(TestDTO testDTO) {
        sqlSession.insert("matchMapper.memberInput", testDTO);
    }

    @Override
    public int idDupChk(TestDTO testDTO) {
        return sqlSession.selectOne("memberMapper.idDupChk", testDTO);
    }

    @Override
    public TestDTO login(TestDTO testDTO) {
        return sqlSession.selectOne("memberMapper.login", testDTO);
    }
}
