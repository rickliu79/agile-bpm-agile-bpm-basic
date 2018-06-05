<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
<title>通用资源授权配置</title>
<%@include file="/commons/include/list.jsp"%>
</head>
<body>
	<div class="easyui-layout" fit="true" scroll="no">
		<div data-options="region:'center',border:false" style="text-align: center;">
			<div id="gridSearch" class="toolbar-search ">
				<div class="toolbar-box border">
					<div class="toolbar-head">
						<!-- 顶部按钮 -->
						<div class="buttons">
							<a class="btn btn-sm btn-primary fa-search" href="#">
								<span>搜索</span>
							</a>
							<a class="btn btn-sm btn-primary fa-add" href="#" onclick="openDetail('','add')">
								<span>添加</span>
							</a>
							<a class="btn btn-sm btn-primary fa-remove" href="#" action="/sys/persistence/sysAuthorization/remove">
								<span>删除</span>
							</a>
						</div>
						<div class="tools">
							<a href="javascript:;" class="collapse">
								<i class="bigger-190 fa  fa-angle-double-up"></i>
							</a>
						</div>
					</div>
					<div class="toolbar-body">
						<form id="searchForm" class="search-form">
							<ul style="margin-left: -26px">
								<li><span>授权目标:</span><input class="inputText" type="text" name="Q^right_target_^SL"></li>
								<li><span>权限类型:</span><input class="inputText" type="text" name="Q^right_type_^SL"></li>
								<li><span>授权标识:</span><input class="inputText" type="text" name="Q^right_identity_^SL"></li>
								<li><span>标识名字:</span><input class="inputText" type="text" name="Q^right_identity_name_^SL"></li>
								<li><span>授权code=identity+type:</span><input class="inputText" type="text" name="Q^right_permission_code_^SL"></li>
								<li>
									<span>创建时间 从</span>:<input  name="Q^right_create_time_^DL"  class="inputText date" />
								</li>
								<li>
									<span>至: </span><input  name="Q^right_create_time_^DG" class="inputText date" />
								</li>
								<li><span>创建人:</span><input class="inputText" type="text" name="Q^right_create_by_^SL"></li>
							</ul>
						</form>
					</div>
				</div>
			</div>
			<div id="grid" class="my-easyui-datagrid"></div>
		</div>
	</div>
</body>
</html>
<script>
	$(function() {
		loadGrid();
	});
		
	function openDetail(id, action) {
		var title = action == "edit" ? "编辑通用资源授权配置" : action == "add" ? "添加通用资源授权配置" : "查看通用资源授权配置";
		action =action == "edit" ? "Edit" : action == "add" ? "Edit" : "Get";
		var url="sysAuthorization" + action;
		if(!$.isEmpty(id)){
			url+='?id=' + id;
		}
		HT.window.openEdit(url, title, action, 'grid', 500, 500, null, null, id, true);
	}
	
	
	
	function loadGrid() {
		$('#grid').datagrid($.extend($defaultOptions,{
			url :  "listJson",
			idField : "rightId",
			sortName : 'right_id_',
			sortOrder : 'desc',
			striped : true,
			columns : [ [
			{field : 'rightTarget',sortName : "right_target_",title : '授权目标',width : 250,align : 'center',sortable : 'true'
			
			
			}, 
			{field : 'rightType',sortName : "right_type_",title : '权限类型',width : 250,align : 'center',sortable : 'true'
			
			
			}, 
			{field : 'rightIdentity',sortName : "right_identity_",title : '授权标识',width : 250,align : 'center',sortable : 'true'
			
			
			}, 
			{field : 'rightIdentityName',sortName : "right_identity_name_",title : '标识名字',width : 250,align : 'center',sortable : 'true'
			
			
			}, 
			{field : 'rightPermissionCode',sortName : "right_permission_code_",title : '授权code=identity+type',width : 250,align : 'center',sortable : 'true'
			
			
			}, 
			{field : 'rightCreateTime',sortName : "right_create_time_",title : '创建时间',width : 250,align : 'center',sortable : 'true'
			,formatter:dateTimeFormatter
			
			}, 
			{field : 'rightCreateBy',sortName : "right_create_by_",title : '创建人',width : 250,align : 'center',sortable : 'true'
			
			
			}, 
			{
				field : 'colManage',
				title : '操作',
				width : 80,
				align : 'center',
				formatter : function(value, row, index) {
					var result = "<a class='btn btn-default fa fa-edit' onClick='openDetail(" + row.id + ",\"edit\")' herf='#'>编辑</a>";
					result += "<a class='btn btn-default fa fa-remove' onClick='openDetail(" + row.id + ",\"get\")' herf='#'>明细</a>";
					result += "<a class='rowInLine btn btn-default fa fa-remove' action='/sys/persistence/sysAuthorization/remove?rightId=" + row.id + "' herf='#'>删除</a>";
					return result;
				}
			} ] ],
			onLoadSuccess : function() {
				handGridLoadSuccess();
			}
		}));
	}
</script>
