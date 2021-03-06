<%@page import="org.apache.commons.io.filefilter.FalseFileFilter"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<!doctype html>
<html lang="zxx">
<head>
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <title>pillloMart</title>
        <script src="${contextPath}/resources/js/jquery-1.12.1.min.js"></script>
       <c:if test="${msg != null}">
               <script type="text/javascript">
               window.alert('${msg}');
               </script>
       </c:if>
       <c:remove var="msg"/>
</head>
<body>
	<jsp:include page="inc/header.jsp" />

	<!-- breadcrumb part start-->
	<section class="breadcrumb_part">
		<div class="container">
			<div class="row">
				<div class="col-lg-12">
					<div class="breadcrumb_iner">
						<h2>장바구니</h2>
					</div>
				</div>
			</div>
		</div>
	</section>
  <section class="cart_area section_padding">
    <div class="container">
      <div class="cart_inner">
        <div class="table-responsive">
          <table class="table">
            <thead>
              <tr>
                <th scope="col">상품</th>
                <th scope="col">가격</th>
                <th scope="col">수량</th>
                <th scope="col">총가격</th>
                <th scope="col">결제</th>
              </tr>
            </thead>
            <tbody>
            <c:forEach var="cart" items="${cartList}" varStatus="cnt">
              <tr>
                <td>
                  <div class="media">
                    <div class="d-flex">
                      <img src="${contextPath}/resources/img_catfood/${cart.image}"/>
                    </div>
                    <div class="media-body">
                      <p>${cart.name}</p>
                    </div>
                  </div>
                </td>
                <td>
                  <h5>
                  <fmt:formatNumber value="${cart.price}" type="currency" currencySymbol="￦"/>
                  </h5>
                </td>
                <td>
                    <p>${cart.qty} 개</p>
                </td>
                <td id="total">
                  <h5>
                  <fmt:formatNumber value="${cart.price * cart.qty}" type="currency" currencySymbol="￦"/>
                  </h5>
                </td>
                <td>
				<form action="${contextPath}/purchase.pur" method="post">
				<input type="hidden" name="total" value="${cart.price * cart.qty}">
				<input type="hidden" name="cart_num1" value="${cart.cart_num}">
				<input type="hidden" value="${id}" name="id">     
				
				<c:choose>
				<c:when test="${cart.qty>cart.inventory}">
				<p><font color="red">수량부족</font></p>
				</c:when>
				<c:otherwise>
				<input class="genric-btn primary-border small" value="payment" type="submit">
				</c:otherwise>
				</c:choose>
				</form>
				<form action="${contextPath}/deletecart.car" method="post">
				<input type="hidden" name="id" value="${id}">
				<input type="hidden" name="cart_num" value="${cart.cart_num}">
				<input type="submit" value="삭제">
				</form>
                </td>
              </tr>
              </c:forEach>
              <tr>
                <td></td>
                <td></td>
                <td></td>
                <td>
                  <h5>총 결제금액 </h5>
                </td>
                <td>
                  <h5>
                  	<fmt:formatNumber value="${total}" type="currency" currencySymbol="￦" />
                  </h5>
                </td>
              </tr>
              <tr class="bottom_button">
                <td></td>
                <td></td>
                <td></td>
                <td></td>
                <td>
                <div class="clear"></div>
                  <div class="cupon_text float-right">
                  <c:if test="${check==0}">
                   <form action="${contextPath}/purchase.pur" method="post" id="totalpayment">
                  <c:forEach items="${cartList}" varStatus="cnt" var="cart">
                	<input type="hidden" value="${cart.cart_num}" name="cart_num${cnt.count}">
                  </c:forEach>
                  	<input type="hidden" value="${total}" name="total">
					<input type="hidden" value="${id}" name="id">     
				<input type="submit" class="genric-btn primary-border circle" value="total payment" onclick="totalpay()">
                  </form> 
                  </c:if>
                  </div>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div> 
  </section>
<jsp:include page="inc/footer.jsp"/>
</body>
</html>