<%-- projectMyBatis/src/main/webapp/member/memberLogIn.jsp --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="member.dao.MemberDAO" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.HashMap" %>

<%
    // 요청에서 전달된 아이디와 비밀번호 가져오기
    String userId = request.getParameter("id");
    String userPwd = request.getParameter("pwd");

    // DAO 인스턴스 가져오기
    MemberDAO memberDAO = MemberDAO.getInstance();
    
    Map<String, String> resultMap = new HashMap<>();  // 결과를 저장할 Map

    try {
        if (userId != null && userPwd != null) {
            // DAO에서 사용자 정보 확인
            Map<String, String> userInfo = memberDAO.memberLogIn(userId, userPwd);
            
            if (!userInfo.isEmpty()) {  // userInfo가 비어있지 않으면 성공
                resultMap.put("status", "success");
                resultMap.putAll(userInfo);  // 사용자 정보를 resultMap에 추가
            } else {
                resultMap.put("status", "fail");
            }
        }
    } catch (Exception e) {
        e.printStackTrace(); // 오류 발생 시 콘솔에 로그 출력
        resultMap.put("status", "error");
        resultMap.put("message", e.getMessage());
    }

    // Response
    response.setContentType("text/plain; charset=UTF-8");
    out.print(resultMap);
    out.flush();
%>