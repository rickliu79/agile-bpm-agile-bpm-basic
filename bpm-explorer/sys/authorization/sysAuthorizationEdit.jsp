<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!doctype html>
<html ng-app="app">
	<head>
		<%@include file="/commons/include/ngEdit.jsp" %>
		<script type="text/javascript" src="${ctx}/js/sys/sysAuthorization/sysAuthorizationEditController.js"></script>
	</head>
	<body ng-controller="ctrl">
		
			<!-- 顶部按钮 -->
			<div class="toolbar-panel">
				<div class="buttons">
					<a class="btn btn-primary fa-save" ng-model="data" ht-save="save"><span>保存</span></a>
					<a class="btn btn-primary fa-back" onClick="HT.window.closeEdit();"><span>返回</span></a>
				</div>
			</div>
			<form name="form" method="post" ht-load="getJson?id=${param.id}"  ng-model="data">
				<table class="table-form"  cellspacing="0" >
							<tr>								
								<th>授权目标:<span class="required">*</span></th>
								<td>
									<input class="inputText" type="text" ng-model="data.rightTarget"   ht-validate="{required:true,maxlength:192}"  />
								</td>								
							</tr>
							<tr>								
								<th>权限类型:<span class="required">*</span></th>
								<td>
									<input class="inputText" type="text" ng-model="data.rightType"   ht-validate="{required:true,maxlength:192}"  />
								</td>								
							</tr>
							<tr>								
								<th>授权标识:<span class="required">*</span></th>
								<td>
									<input class="inputText" type="text" ng-model="data.rightIdentity"   ht-validate="{required:true,maxlength:192}"  />
								</td>								
							</tr>
							<tr>								
								<th>标识名字:<span class="required">*</span></th>
								<td>
									<input class="inputText" type="text" ng-model="data.rightIdentityName"   ht-validate="{required:true,maxlength:765}"  />
								</td>								
							</tr>
							<tr>								
								<th>授权code=identity+type:<span class="required">*</span></th>
								<td>
									<input class="inputText" type="text" ng-model="data.rightPermissionCode"   ht-validate="{required:true,maxlength:375}"  />
								</td>								
							</tr>
							<tr>								
								<th>创建时间:<span class="required">*</span></th>
								<td>
									<input class="inputText" type="text" ng-model="data.rightCreateTime" ht-date  ht-validate="{required:true}"  />
								</td>								
							</tr>
							<tr>								
								<th>创建人:<span class="required">*</span></th>
								<td>
									<input class="inputText" type="text" ng-model="data.rightCreateBy"   ht-validate="{required:true,maxlength:192}"  />
								</td>								
							</tr>
				</table>
				
				
			</form>
		
	</body>
</html>