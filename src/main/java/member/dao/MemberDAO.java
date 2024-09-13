// projectMyBatis/src/main/java/member/dao/MemberDAO.java
package member.dao;

import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import member.bean.MemberDTO;

public class MemberDAO {
    private static MemberDAO instance = new MemberDAO();
    private SqlSessionFactory sqlSessionFactory;

    public static MemberDAO getInstance() {
    	return instance;
    }
    
    private MemberDAO() {
        try {
            Reader reader = Resources.getResourceAsReader("mybatis-config.xml");
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /** memberWriteForm.jsp */
    // id 중복검사
    public boolean isExistId(String id) {
    	boolean exist = false;
        SqlSession sqlSession = sqlSessionFactory.openSession(); 
		int rows = sqlSession.selectOne("memberSQL.isExistId", id);
        if (rows > 0) exist = true;
        sqlSession.close();
        return exist;
    }

    // 회원가입
    public boolean memberSignUp(MemberDTO memberDTO) {
        boolean result = false;
        SqlSession sqlSession = sqlSessionFactory.openSession();
        int rows = sqlSession.insert("memberSQL.memberSignUp", memberDTO);
        sqlSession.commit();
        if (rows > 0) result = true;
        sqlSession.close();
        return result;
    }

    /** memberLogInForm.jsp */
    // 로그인
    public Map<String, String> memberLogIn(String id, String pwd) {
        Map<String, String> userInfo = new HashMap<>();
        SqlSession sqlSession = sqlSessionFactory.openSession();
        
        // Map을 사용하여 파라미터 전달
        Map<String, String> params = Map.of("id", id, "pwd", pwd); // params 변수를 정의
        MemberDTO memberDTO = sqlSession.selectOne("memberSQL.memberLogIn", params);
        sqlSession.close(); // 세션 닫기
        
        if (memberDTO != null) {
            userInfo.put("name", memberDTO.getName());
            userInfo.put("email1", memberDTO.getEmail1());
            userInfo.put("email2", memberDTO.getEmail2());
        }
        
        return userInfo;
    }

    /** memberUpdateForm.jsp */
	// 회원정보 불러오기
	public MemberDTO getMemberInfo(String id) {
		SqlSession sqlSession = sqlSessionFactory.openSession();
        MemberDTO memberDTO = sqlSession.selectOne("memberSQL.getMemberInfo", id);
        sqlSession.close();
        return memberDTO;
    }

    // 회원정보 수정
    public boolean memberUpdate(MemberDTO memberDTO) {
    	boolean result = false;
    	SqlSession sqlSession = sqlSessionFactory.openSession();
        int rows = sqlSession.update("memberSQL.memberUpdate", memberDTO);
        sqlSession.commit();
        if (rows > 0) result = true;
        sqlSession.close();
        return result;
    }

    /** memberDeleteForm.jsp */
    // 회원탈퇴
    public boolean memberDelete(String id, String pwd) {
        boolean result = false;
        SqlSession sqlSession = sqlSessionFactory.openSession();
        int rows = sqlSession.delete("memberSQL.memberDelete", Map.of("id", id, "pwd", pwd));
        sqlSession.commit();
        if (rows > 0) result = true;
        sqlSession.close();
        return result;
    }
}