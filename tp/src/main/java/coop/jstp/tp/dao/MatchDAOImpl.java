package coop.jstp.tp.dao;

import coop.jstp.tp.vo.*;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Repository
public class MatchDAOImpl implements MatchDAO{

    @Autowired
    private SqlSession sqlSession;
    @Override
    public void matchingStart(MatchStartDTO dto) {
        sqlSession.insert("matchMapper.matchingStart", dto);
    }

    @Override
    public void matchingEnd(int userNum) {
        sqlSession.delete("matchMapper.matchingEnd", userNum);
    }

    @Override
    public int matchingOther() {
        return sqlSession.selectOne("matchMapper.matchingOther");
    }

    @Override
    public List<MatchingDTO> matchedUser() {
        return sqlSession.selectList("matchMapper.getMatchedUser");
    }

    @Override
    public TestDTO getSummonerName(int uNum) {
        return sqlSession.selectOne("matchMapper.getSummonerName", uNum);
    }

    @Override
    public List<HashMap<?, ?>> getMatchingUsers(){
        return sqlSession.selectList("matchMapper.getMatchingUsers");
    }

    @Override
    public void saveUserList(MatchedUsersDTO dto) {
        sqlSession.insert("matchMapper.saveUserList", dto);
    }

    @Override
    public List<MatchedUsersDTO> callSavedUserList() {
        return sqlSession.selectList("matchMapper.callSavedUserList");
    }

    @Override
    public void matchingComplete(int matchingNumber) {
        sqlSession.delete("matchMapper.matchingComplete", matchingNumber);
    }
}
