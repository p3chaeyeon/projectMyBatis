//projectMyBatis/src/main/java/member.dao/BoardDAO.java
package board.dao;

import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import board.bean.BoardDTO;

public class BoardDAO {
	// 싱글톤 인스턴스 생성
	private static BoardDAO instance = new BoardDAO();
	private SqlSessionFactory sqlSessionFactory;
    
	public static BoardDAO getInstance() {
		return instance;
	}
    
	public BoardDAO() { // Driver Loading
		try {
			Reader reader = Resources.getResourceAsReader("mybatis-config.xml");
			sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
    
    /** boardWriteForm.jsp */
	// 글쓰기
    public Map<String, String> boardWrite(BoardDTO boardDTO) {
        Map<String, String> resultMap = new HashMap<>();
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            int result = sqlSession.insert("boardSQL.boardWrite", boardDTO);
            sqlSession.commit();
            resultMap.put("status", result > 0 ? "success" : "fail");
        } catch (Exception e) {
        	sqlSession.rollback();
            resultMap.put("status", "error");
            resultMap.put("message", e.getMessage());
        } finally {
        	sqlSession.close();
        }
        return resultMap;
    }
    
	/** boardList.jsp */
	// 글 목록
	public List<BoardDTO> boardList(int startNum, int endNum) {
    	Map<String, Integer> map = new HashMap<String, Integer>();
    	map.put("startNum", startNum);
    	map.put("endNum", endNum);
        
    	SqlSession sqlSession = sqlSessionFactory.openSession();
    	List<BoardDTO> list = sqlSession.selectList("boardSQL.boardList", map);
        
    	sqlSession.close();
        
    	return list;
	}
    
	
	// 글 개수
	public int getTotalA() {
		SqlSession sqlSession = sqlSessionFactory.openSession();
		int totalA = 0;      
		totalA = sqlSession.selectOne("boardSQL.getTotalA");        
		sqlSession.close();       
		return totalA;
	}
}
