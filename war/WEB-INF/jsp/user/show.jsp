<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Tag-It</title>
<link rel="stylesheet" type="text/css" href="/css/style.css" />
</head>
<body>
	<jsp:include page="/WEB-INF/jsp/includes/menubar.jsp" />
	<jsp:include page="/WEB-INF/jsp/includes/msg_error.jsp" />
	<jsp:include page="/WEB-INF/jsp/includes/msg_flash.jsp" />
	<div id="user_show">
		<h1>Perfil do avaliador</h1>
			ID: 				${user.id} <br/>
		 	Nome do avaliador: 	${user.name} <br/>
			E-mail:				${user.email} <br/>
			Procurando mais informações para colocar aqui!....
		 <br/> <br/>
		 <c:if test="${not empty isEdited}"><a href="/profile/edit">Editar</a></c:if>
	</div>	
	<jsp:include page="/WEB-INF/jsp/includes/footer.jsp" />
</body>
</html>
