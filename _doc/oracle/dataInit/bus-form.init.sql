-- FORM 模块

-- 表单管理菜单启用
UPDATE sys_resource SET ENABLE_MENU_='1' WHERE (ID_='23');

-- 表单初始化模板

INSERT INTO "FORM_TEMPLATE" VALUES ('10000000000103', '单列模板', 'pc', 'main', '<table class="form-table">
	<#list relation.table.columnsWithOutHidden as column>
	<tr>								
		<th>${column.comment}</th>
		<td>${generator.getColumn(column,relation)}</td>								
	</tr>
	</#list>
</table>
${getOne2OneChild(relation)}




<#function getOne2OneChild relation> 
	<#assign relationList = relation.getChildren(''oneToOne'')>
	<#assign rtn>
		<#list relationList as relation>
			<div ${generator.getSubAttrs(relation)} >
				<div class="block-title"> <span class="title">${relation.tableComment} </span>
					${getOne2ManyChild(relation)}
				</div>
				<table class="form-table">
					<#list relation.table.columnsWithOutHidden as column>
						<tr>
							<th>${column.comment}</th>
							<td>${generator.getColumn(column,relation)} </td>
						</tr>
					</#list>
				</table>
				${getOne2OneChild(relation)}
			</div>
		</#list>
	</#assign>
	<#return rtn>
</#function>

<#function getOne2ManyChild relation> 
	<#assign relationList = relation.getChildren(''oneToMany'')>
	<#assign rtn>
		 <#if relationList?? && (relationList?size > 0) >
		<div class="pull-left"><#list relationList as relation><a href="#" class="btn btn-link btn-sm fa fa-detail" ng-model="${relation.parent.tableKey}" ab-sub-detail="${relation.getBusObj().getKey()}-${relation.tableKey}" ab-show-permission="tablePermission.${relation.busObj.key}.${relation.tableKey}">${relation.tableComment}</a>
		</#list>
		</div>
		</#if>
	</#assign>
	<#return rtn>
</#function>', '单列模板', '0', 'mainOneColumn');
INSERT INTO "FORM_TEMPLATE" VALUES ('10000000000104', '两列模板', 'pc', 'main', '<table class="form-table">
	<#assign index=1>
	<#list relation.table.columnsWithOutHidden as column>
	<#if index==1>
	<tr>
	</#if>								
		<th>${column.comment}</th>
		<td ${getColspan(index,column_has_next)}>${generator.getColumn(column,relation)}</td>								
	<#if field.isSeparator==true || !column_has_next || index==2>
	</tr>
	<#assign index=0>
	</#if>
	<#assign index=index+1>
	</#list>
</table>
${getOne2OneChild(relation)}

<#function getOne2OneChild relation> 
	<#assign relationList = relation.getChildren(''oneToOne'')>
	<#assign rtn>
		<#list relationList as relation>
			<div ${generator.getSubAttrs(relation)} >
				<div class="block-title"> <span class="title">${relation.tableComment} </span>
					${getOne2ManyChild(relation)}
				</div>
				<table class="form-table">
					<#assign index=1>
					<#list relation.table.columnsWithOutHidden as column>
						<#if index==1>
						<tr>
						</#if>
							<th>${column.comment}</th>
							<td ${getColspan(index,column_has_next)}>${generator.getColumn(column,relation)}</td>
						<#if !column_has_next || index==2>
						</tr>
						<#assign index=0>
						</#if> 
						<#assign index=index+1>
					</#list>
				</table>
				${getOne2OneChild(relation)}
			</div>
		</#list>
	</#assign>
	<#return rtn>
</#function>

<#function getOne2ManyChild relation> 
	<#assign relationList = relation.getChildren(''oneToMany'')>
	<#assign rtn>
		 <#if relationList?? && (relationList?size > 0) >
		<div class="pull-left"><#list relationList as relation><a href="#" class="btn btn-link btn-sm fa fa-detail" ng-model="${relation.parent.tableKey}" ab-sub-detail="${relation.getBusObj().getKey()}-${relation.tableKey}" ab-show-permission="tablePermission.${relation.busObj.key}.${relation.tableKey}">${relation.tableComment}</a>
		</#list>
		</div>
		</#if>
	</#assign>
	<#return rtn>
</#function>


<#function getColspan index,hasNext>
	<#assign rtn="">
	
	 <#if !hasNext && index !=2>
		<#assign rtn="colspan=''"+((2-index)*2+1)+"''"> 
	</#if>
	
	<#return rtn>
</#function>', '两列模板', '0', 'mainTwoColumn');
INSERT INTO "FORM_TEMPLATE" VALUES ('10000000000105', '三列模板', 'pc', 'main', '<table class="form-table">
	<#assign index=1>
	<#list relation.table.columnsWithOutHidden as column>
		<#if index==1>
		<tr>
		</#if>
			<th>${column.comment}</th>
			<td ${getColspan(index,column_has_next)}> ${generator.getColumn(column,relation)} </td>
		<#if !column_has_next || index==3>
		</tr>
		<#assign index=0>
		</#if> 
		<#assign index=index+1>
	</#list>
</table>
 ${getOne2OneChild(relation)}
 
 
<#function getOne2OneChild relation> 
	<#assign relationList = relation.getChildren(''oneToOne'')>
	<#assign rtn>
		<#list relationList as relation>
			<div ${generator.getSubAttrs(relation)} >
				<div class="block-title"> <span class="title">${relation.tableComment} </span>
					${getOne2ManyChild(relation)}
				</div>
				<table class="form-table">
					<#assign index=1>
					<#list relation.table.columnsWithOutHidden as column>
						<#if index==1>
						<tr>
						</#if>
							<th>${column.comment}</th>
							<td ${getColspan(index,column_has_next)}>${generator.getColumn(column,relation)}</td>
						<#if !column_has_next || index==3>
						</tr>
						<#assign index=0>
						</#if> 
						<#assign index=index+1>
					</#list>
				</table>
				${getOne2OneChild(relation)}
			</div>
		</#list>
	</#assign>
	<#return rtn>
</#function>

<#function getOne2ManyChild relation> 
	<#assign relationList = relation.getChildren(''oneToMany'')>
	<#assign rtn>
		 <#if relationList?? && (relationList?size > 0) >
		<div class="pull-left"><#list relationList as relation><a href="#" class="btn btn-link btn-sm fa fa-detail" ng-model="${relation.parent.tableKey}" ab-sub-detail="${relation.getBusObj().getKey()}-${relation.tableKey}" ab-show-permission="tablePermission.${relation.busObj.key}.${relation.tableKey}">${relation.tableComment}</a>
		</#list>
		</div>
		</#if>
	</#assign>
	<#return rtn>
</#function>

<#function getColspan index,hasNext>
	<#assign rtn="">
	
	 <#if !hasNext && index !=3>
		<#assign rtn="colspan=''"+((3-index)*2+1)+"''"> 
	</#if>
	
	<#return rtn>
</#function>', '三列模板', '0', 'mainThreeColumn');
INSERT INTO "FORM_TEMPLATE" VALUES ('10000000000106', '四列模板', 'pc', 'main', '<table class="form-table">
	<#assign index=1>
	<#list relation.table.columnsWithOutHidden as column>
		<#if index==1>
		<tr>
		</#if>
			<th>${column.comment}</th>
			<td ${getColspan(index,column_has_next)}> ${generator.getColumn(column,relation)} </td>
		<#if !column_has_next || index==4>
		</tr>
		<#assign index=0>
		</#if> 
		<#assign index=index+1>
	</#list>
</table>

<#function getOne2OneChild relation> 
	<#assign relationList = relation.getChildren(''oneToOne'')>
	<#assign rtn>
		<#list relationList as relation>
			<div ${generator.getSubAttrs(relation)} >
				<div class="block-title"> <span class="title">${relation.tableComment} </span>
					${getOne2ManyChild(relation)}
				</div>
				<table class="form-table">
					<#assign index=1>
					<#list relation.table.columnsWithOutHidden as column>
						<#if index==1>
						<tr>
						</#if>
							<th>${column.comment}</th>
							<td ${getColspan(index,column_has_next)}>${generator.getColumn(column,relation)}</td>
						<#if !column_has_next || index==4>
						</tr>
						<#assign index=0>
						</#if> 
						<#assign index=index+1>
					</#list>
				</table>
				${getOne2OneChild(relation)}
			</div>
		</#list>
	</#assign>
	<#return rtn>
</#function>

<#function getOne2ManyChild relation> 
	<#assign relationList = relation.getChildren(''oneToMany'')>
	<#assign rtn>
		 <#if relationList?? && (relationList?size > 0) >
		<div class="pull-left"><#list relationList as relation><a href="#" class="btn btn-link btn-sm fa fa-detail" ng-model="${relation.parent.tableKey}" ab-sub-detail="${relation.getBusObj().getKey()}-${relation.tableKey}" ab-show-permission="tablePermission.${relation.busObj.key}.${relation.tableKey}">${relation.tableComment}</a>
		</#list>
		</div>
		</#if>
	</#assign>
	<#return rtn>
</#function>

<#function getColspan index,hasNext>
	<#assign rtn="">
	
	 <#if (!hasNext || isSeparator==true) && index !=4>
		<#assign rtn="colspan=''"+((4-index)*2+1)+"''"> 
	</#if>
	
	<#return rtn>
</#function>', '四列模板', '0', 'mainFourColumn');
INSERT INTO "FORM_TEMPLATE" VALUES ('10000000000107', '单列模板', 'pc', 'subTable', '<div ${generator.getSubAttrs(relation)} ab-show-permission="tablePermission.${relation.busObj.key}.${relation.tableKey}" >
	<div class="ibox-title"><span class="title">${relation.tableComment}</span>
		<a href="javascript:void(0)" class="btn btn-primary btn-sm fa fa-plus" ng-model="${generator.getScopePath(relation)}" ab-sub-add="initData.${relation.busObj.key}.${relation.tableKey}" ab-edit-permission="tablePermission.${relation.busObj.key}.${relation.tableKey}">添加</a>
	</div>
	<div class="ibox-content" ng-repeat="${relation.tableKey} in ${generator.getScopePath(relation)} track by $index"> ${getOne2ManyChild(relation)}<a class="btn btn-danger btn-xs fa fa-delete pull-right" ng-click="ArrayTool.del($index,${generator.getScopePath(relation)})" ab-edit-permission="tablePermission.${relation.busObj.key}.${relation.tableKey}"> 移除</a>
		<table class="form-table">
		<#list relation.table.columnsWithOutHidden as column>
			<tr>
				<th>${column.comment}</th>
				<td>${generator.getColumn(column,relation)} </td>
			</tr>
		</#list>
		</table>
		 ${getOne2OneChild(relation)}
	</div>
</div>

<#function getOne2OneChild relation> 
	<#assign relationList = relation.getChildren(''oneToOne'')>
	<#assign rtn>
		<#list relationList as relation>
			<div ${generator.getSubAttrs(relation)} >
				<div class="block-title"> <span class="title">${relation.tableComment} </span>
					${getOne2ManyChild(relation)}
				</div>
				<table class="form-table">
					<#list relation.table.columnsWithOutHidden as column>
						<tr>
							<th>${column.comment}</th>
							<td>${generator.getColumn(column,relation)} </td>
						</tr>
					</#list>
				</table>
				${getOne2OneChild(relation)}
			</div>
		</#list>
	</#assign>
	<#return rtn>
</#function>

<#function getOne2ManyChild relation> 
	<#assign relationList = relation.getChildren(''oneToMany'')>
	<#assign rtn>
		 <#if relationList?? && (relationList?size > 0) >
		<div class="pull-left"><#list relationList as relation><a href="javascript:void(0)" class="btn btn-link btn-sm fa fa-detail" ng-model="${relation.parent.tableKey}" ab-sub-detail="${relation.getBusObj().getKey()}-${relation.tableKey}" ab-show-permission="tablePermission.${relation.busObj.key}.${relation.tableKey}" >${relation.tableComment}</a>
		</#list>
		</div>
		</#if>
	</#assign>
	<#return rtn>
</#function>', '单列模板', '0', 'subOneColumn');
INSERT INTO "FORM_TEMPLATE" VALUES ('10000000000108', '两列模板', 'pc', 'subTable', '<div ${generator.getSubAttrs(relation)} ab-show-permission="tablePermission.${relation.busObj.key}.${relation.tableKey}" >
	<div class="ibox-title"><span class="title">${relation.tableComment}</span>
		<a href="###" class="btn btn-primary btn-sm fa fa-plus" ng-model="${generator.getScopePath(relation)}" ab-sub-add="initData.${relation.busObj.key}.${relation.tableKey}" ab-edit-permission="tablePermission.${relation.busObj.key}.${relation.tableKey}" >添加</a>
	</div>
	<div class="ibox-content" ng-repeat="${relation.tableKey} in ${generator.getScopePath(relation)} track by $index"> ${getOne2ManyChild(relation)}<a class="btn btn-danger btn-xs fa fa-delete pull-right" ng-click="ArrayTool.del($index,${generator.getScopePath(relation)})" ab-edit-permission="tablePermission.${relation.busObj.key}.${relation.tableKey}"> 移除</a>
		<table class="form-table">
		<#assign index=1>
		<#list relation.table.columnsWithOutHidden as column>
			<#if index==1>
			<tr>
			</#if>
				<th>${column.comment}</th>
				<td ${getColspan(index,column_has_next)}>${generator.getColumn(column,relation)}</td>
			<#if !column_has_next || index==2>
			</tr>
			<#assign index=0>
			</#if> 
			<#assign index=index+1>
		</#list>
		</table>
		 ${getOne2OneChild(relation)}
	</div>
</div>

<#function getOne2OneChild relation> 
	<#assign relationList = relation.getChildren(''oneToOne'')>
	<#assign rtn>
		<#list relationList as relation>
			<div ${generator.getSubAttrs(relation)} >
				<div class="block-title"> <span class="title">${relation.tableComment} </span>
					${getOne2ManyChild(relation)}
				</div>
				<table class="form-table">
					<#assign index=1>
					<#list relation.table.columnsWithOutHidden as column>
						<#if index==1>
						<tr>
						</#if>
							<th>${column.comment}</th>
							<td ${getColspan(index,column_has_next)}>${generator.getColumn(column,relation)}</td>
						<#if !column_has_next || index==2>
						</tr>
						<#assign index=0>
						</#if> 
						<#assign index=index+1>
					</#list>
				</table>
				${getOne2OneChild(relation)}
			</div>
		</#list>
	</#assign>
	<#return rtn>
</#function>

<#function getOne2ManyChild relation> 
	<#assign relationList = relation.getChildren(''oneToMany'')>
	<#assign rtn>
		 <#if relationList?? && (relationList?size > 0) >
		<div class="pull-left"><#list relationList as relation><a href="#" class="btn btn-link btn-sm fa fa-detail" ng-model="${relation.parent.tableKey}" ab-sub-detail="${relation.getBusObj().getKey()}-${relation.tableKey}" ab-show-permission="tablePermission.${relation.busObj.key}.${relation.tableKey}">${relation.tableComment}</a>
		</#list>
		</div>
		</#if>
	</#assign>
	<#return rtn>
</#function>

<#function getColspan index,hasNext>
	<#assign rtn="">
		 <#if !hasNext && index !=2>
			<#assign rtn="colspan=''"+((2-index)*2+1)+"''"> 
		</#if>
<#return rtn>
</#function> ', '两列模板', '0', 'subTwoColumn');
INSERT INTO "FORM_TEMPLATE" VALUES ('10000000000109', '三列模板', 'pc', 'subTable', '<div ${generator.getSubAttrs(relation)} ab-show-permission="tablePermission.${relation.busObj.key}.${relation.tableKey}" >
	<div class="ibox-title"><span class="title">${relation.tableComment}</span>
		<a href="###" class="btn btn-primary btn-sm fa fa-plus" ng-model="${generator.getScopePath(relation)}" ab-sub-add="initData.${relation.busObj.key}.${relation.tableKey}" ab-edit-permission="tablePermission.${relation.busObj.key}.${relation.tableKey}">添加</a>
	</div>
	<div class="ibox-content" ng-repeat="${relation.tableKey} in ${generator.getScopePath(relation)} track by $index"> ${getOne2ManyChild(relation)}<a class="btn btn-danger btn-xs fa fa-delete pull-right" ng-click="ArrayTool.del($index,${generator.getScopePath(relation)})" ab-edit-permission="tablePermission.${relation.busObj.key}.${relation.tableKey}"> 移除</a>
		<table class="form-table">
		<#assign index=1>
		<#list relation.table.columnsWithOutHidden as column>
			<#if index==1>
			<tr>
			</#if>
				<th>${column.comment}</th>
				<td ${getColspan(index,column_has_next)}>${generator.getColumn(column,relation)}</td>
			<#if !column_has_next || index==3>
			</tr>
			<#assign index=0>
			</#if> 
			<#assign index=index+1>
		</#list>
		</table>
		 ${getOne2OneChild(relation)}
	</div>
</div>

<#function getOne2OneChild relation> 
	<#assign relationList = relation.getChildren(''oneToOne'')>
	<#assign rtn>
		<#list relationList as relation>
			<div ${generator.getSubAttrs(relation)} >
				<div class="block-title"> <span class="title">${relation.tableComment} </span>
					${getOne2ManyChild(relation)}
				</div>
				<table class="form-table">
					<#assign index=1>
					<#list relation.table.columnsWithOutHidden as column>
						<#if index==1>
						<tr>
						</#if>
							<th>${column.comment}</th>
							<td ${getColspan(index,column_has_next)}>${generator.getColumn(column,relation)}</td>
						<#if !column_has_next || index==3>
						</tr>
						<#assign index=0>
						</#if> 
						<#assign index=index+1>
					</#list>
				</table>
				${getOne2OneChild(relation)}
			</div>
		</#list>
	</#assign>
	<#return rtn>
</#function>

<#function getOne2ManyChild relation> 
	<#assign relationList = relation.getChildren(''oneToMany'')>
	<#assign rtn>
		 <#if relationList?? && (relationList?size > 0) >
		<div class="pull-left"><#list relationList as relation><a href="#" class="btn btn-link btn-sm fa fa-detail" ng-model="${relation.parent.tableKey}" ab-sub-detail="${relation.getBusObj().getKey()}-${relation.tableKey}" ab-show-permission="tablePermission.${relation.busObj.key}.${relation.tableKey}" >${relation.tableComment}</a>
		</#list>
		</div>
		</#if>
	</#assign>
	<#return rtn>
</#function>

<#function getColspan index,hasNext>
	<#assign rtn="">
		 <#if (!hasNext || isSeparator==true) && index !=3>
			<#assign rtn="colspan=''"+((3-index)*2+1)+"''"> 
		</#if>
<#return rtn>
</#function>', '三列模板', '0', 'subThreeColumn');
INSERT INTO "FORM_TEMPLATE" VALUES ('10000000000110', 'vux主表模板', 'mobile', 'main', '<script>
	<!--脚本将会混入表单自定义表单控件-->
	window.custFormComponentMixin ={
			data: function () {
		    	return {"test":"helloWorld"};
		  	},
			created:function(){
				console.log("混入对象的钩子被调用");
			},methods:{
				testaaa:function(){alert(1)}
			}
	}
</script>
<div class="weui-cells weui-cells_form">
<#list relation.table.columnsWithOutHidden as column>
	<div class="weui-cell" v-ab-permission:show="${vuxGenerator.getPermissionPath(column,relation)}">
        <div class="weui-cell__hd"><label class="weui-label">${column.comment}</label></div>
        <div class="weui-cell__bd">${vuxGenerator.getColumn(column,relation)}</div>
	</div>
</#list>
</div>
${getOne2OneChild(relation)}

<#function getOne2OneChild relation> 
	<#assign relationList = relation.getChildren(''oneToOne'')>
	<#assign rtn>
		<#list relationList as relation>
			<div ${vuxGenerator.getSubAttrs(relation)} >
				<div class="weui-cells__title"> ${relation.tableComment}
					${getOne2ManyChild(relation)}
				</div>
				
				<div class="weui-cells weui-cells_form">
					<#list relation.table.columnsWithOutHidden as column>
						<div class="weui-cell" v-ab-permission:show="${vuxGenerator.getPermissionPath(column,relation)}">
					        <div class="weui-cell__hd"><label class="weui-label">${column.comment}</label></div>
					        <div class="weui-cell__bd">${vuxGenerator.getColumn(column,relation)}</div>
					    </div>
					</#list>
				</div>
				
				${getOne2OneChild(relation)}
			</div>
		</#list>
	</#assign>
	<#return rtn>
</#function>

<#function getOne2ManyChild relation> 
	<#assign relationList = relation.getChildren(''oneToMany'')>
	<#assign rtn>
		 <#if relationList?? && (relationList?size > 0) >
		<div class="pull-left"><#list relationList as relation><a href="#"  v-on:click="showSubTable(${relation.parent.tableKey},''${relation.tableKey}'')" class="fa fa-list-alt weui-btn weui-btn_mini weui-btn_primary"  v-ab-permission:show="tablePermission.${relation.busObj.key}.${relation.tableKey}">${relation.tableComment}详情</a>
		</#list>
		</div>
		</#if>
	</#assign>
	<#return rtn>
</#function>', 'vux主表模板', '0', 'vuxMainTemplate');
INSERT INTO "FORM_TEMPLATE" VALUES ('10000000000111', 'vux子表模板', 'mobile', 'subTable', '<div ${vuxGenerator.getSubAttrs(relation)} v-ab-permission:show="tablePermission.${relation.busObj.key}.${relation.tableKey}" >
	<#if vuxGenerator.isThreeChildren(relation)><popup v-model="subTableDialog.${relation.tableKey}" position="bottom" height="80%"> </#if>
	<div class="weui-cells__title" ><span class="title">${relation.tableComment}</span>
		<a href="javascript:;" v-sub-add="[${vuxGenerator.getScopePath(relation)},initData.${relation.busObj.key}.${relation.tableKey}]" class="fa fa-plus weui-btn weui-btn_mini weui-btn_primary" v-ab-permission:edit="tablePermission.${relation.busObj.key}.${relation.tableKey}"></a> 
	</div>
	<div class="weui-cells weui-cells_form" v-for="(${relation.tableKey},index) in ${vuxGenerator.getScopePath(relation)}">
		<div class="weui-cells__title">
		 	${getOne2ManyChild(relation)}
		 	<a href="javascript:;" v-sub-del="[${vuxGenerator.getScopePath(relation)},index]" class="fa fa-trash weui-btn weui-btn_mini weui-btn_warn pull-right" v-ab-permission:edit="tablePermission.${relation.busObj.key}.${relation.tableKey}"></a> 
		 </div>
		<#list relation.table.columnsWithOutHidden as column>
			<div class="weui-cell" v-ab-permission:show="${vuxGenerator.getPermissionPath(column,relation)}">
		        <div class="weui-cell__hd"><label class="weui-label">${column.comment}</label></div>
				<div class="weui-cell__bd">${vuxGenerator.getColumn(column,relation)}</div>
	    	</div>
		</#list>
		 ${getOne2OneChild(relation)}
	</div>
	<#if vuxGenerator.isThreeChildren(relation) ></popup></#if>
</div>

<#function getOne2OneChild relation> 
	<#assign relationList = relation.getChildren(''oneToOne'')>
	<#assign rtn>
		<#list relationList as relation>
			<div ${vuxGenerator.getSubAttrs(relation)} >
				<div class="weui-cells__title"> ${relation.tableComment}
					${getOne2ManyChild(relation)}
				</div>
				
				<div class="weui-cells weui-cells_form">
					<#list relation.table.columnsWithOutHidden as column>
						<div class="weui-cell" v-ab-permission:show="${vuxGenerator.getPermissionPath(column,relation)}">
					        <div class="weui-cell__hd"><label class="weui-label">${column.comment}</label></div>
					        <div class="weui-cell__bd">${vuxGenerator.getColumn(column,relation)}</div>
					    </div>
					</#list>
				</div>
				
				${getOne2OneChild(relation)}
			</div>
		</#list>
	</#assign>
	<#return rtn>
</#function>

<#function getOne2ManyChild relation> 
	<#assign relationList = relation.getChildren(''oneToMany'')>
	<#assign rtn>
		 <#if relationList?? && (relationList?size > 0) >
			<div class="pull-left"><#list relationList as relation><a href="#"  v-on:click="showSubTable(${relation.parent.tableKey},''${relation.tableKey}'')" class="fa fa-list-alt weui-btn weui-btn_mini weui-btn_primary"  v-ab-permission:show="tablePermission.${relation.busObj.key}.${relation.tableKey}">${relation.tableComment}详情</a>		</#list>
		</div>
		</#if>
	</#assign>
	<#return rtn>
</#function>', 'vux子表模板', '0', 'vuxSubTemplate');


-- 系统对话框
INSERT INTO "FORM_CUST_DIALOG" VALUES ('20000004660004', 'getMyUsablePanels', '获取可用面板', '面板布局获取可用的面板', 'list', 'dataSourceDefault', '本地数据源', 'table', 'workbenchPanelManager.getMyUsablePanels', '1', '10', '800', '600', '1', '1', '{"pidInitValScript":false}', '[{"columnName":"name","showName":"名字"},{"columnName":"desc","showName":"描述"}]', '[{"columnName":"layoutKey","condition":"EQ","dbType":"varchar","showName":"layoutKey","value":{},"valueSource":"param"}]', '[{"columnName":"type","returnName":"type"},{"columnName":"alias","returnName":"alias"},{"columnName":"name","returnName":"name"},{"columnName":"type","returnName":"type"},{"columnName":"desc","returnName":"desc"},{"columnName":"dataType","returnName":"dataType"},{"columnName":"dataSource","returnName":"dataSource"},{"columnName":"autoRefresh","returnName":"autoRefresh"},{"columnName":"width","returnName":"width"},{"columnName":"height","returnName":"height"},{"columnName":"displayContent","returnName":"displayContent"},{"columnName":"moreUrl","returnName":"moreUrl"},{"columnName":"id","returnName":"id"},{"columnName":"height","returnName":"custHeight"},{"columnName":"width","returnName":"custWidth"}]', '[]', 'interface');
INSERT INTO "FORM_CUST_DIALOG" VALUES ('20000005180002', 'scriptSelector', '常用脚本选择框', '选择常用脚本', 'list', 'dataSourceDefault', '本地数据源', 'table', 'sys_script', '1', '10', '800', '600', '1', '0', '{"pidInitValScript":false}', '[{"columnName":"name_","showName":"名称"},{"columnName":"category_","showName":"分类"},{"columnName":"memo_","showName":"备注"}]', '[{"columnName":"name_","condition":"LK","dbType":"varchar","showName":"脚本名称","value":{"ctrlType":"inputText"},"valueSource":"param"}]', '[{"columnName":"script_","returnName":"script"}]', '[]', null);
INSERT INTO "FORM_CUST_DIALOG" VALUES ('20000003460003', 'formSelector', '表单选择框', null, 'list', 'dataSourceDefault', '本地数据源', 'table', 'form_def', '1', '10', '800', '600', '1', '0', '{"pidInitValScript":false}', '[{"columnName":"name_","showName":"名字"},{"columnName":"key_","showName":"key"},{"columnName":"desc_","showName":"描述"},{"columnName":"bo_name_","showName":"业务对象"},{"columnName":"bo_key_","showName":"业务对象key"}]', '[{"columnName":"bo_key_","condition":"IN","dbType":"varchar","showName":"别名","value":{"ctrlType":""},"valueSource":"param"},{"columnName":"type_","condition":"EQ","dbType":"varchar","showName":"分类","value":{},"valueSource":"param"}]', '[{"columnName":"id_","returnName":"id"},{"columnName":"key_","returnName":"key"},{"columnName":"name_","returnName":"name"},{"columnName":"desc_","returnName":"desc"},{"columnName":"group_id_","returnName":"groupId"},{"columnName":"bo_key_","returnName":"boKey"},{"columnName":"bo_name_","returnName":"boName"},{"columnName":"type_","returnName":"type_"}]', '[]', null);
INSERT INTO "FORM_CUST_DIALOG" VALUES ('20000008820001', 'IdentitySeletor', '流水号选择', '流水号选择', 'list', 'dataSourceDefault', '本地数据源', 'table', 'sys_serialno', '1', '10', '800', '600', '1', '0', '{"pidInitValScript":false}', '[{"columnName":"name_","showName":"名称"},{"columnName":"alias_","showName":"别名"},{"columnName":"regulation_","showName":"规则"},{"columnName":"step_","showName":"步长"}]', '[{"columnName":"name_","condition":"LK","dbType":"varchar","showName":"名称","value":{"ctrlType":"inputText"},"valueSource":"param"},{"columnName":"alias_","condition":"LK","dbType":"varchar","showName":"别名","value":{"ctrlType":"inputText"},"valueSource":"param"}]', '[{"columnName":"id_","returnName":"id"},{"columnName":"name_","returnName":"name"},{"columnName":"alias_","returnName":"alias"},{"columnName":"regulation_","returnName":"regulation"}]', '[]', null);
INSERT INTO "FORM_CUST_DIALOG" VALUES ('20000013260001', 'bpmDefSelector', '流程定义选择', '流程定义主版本', 'list', 'dataSourceDefault', '本地数据源', 'table', 'bpm_definition', '1', '10', '800', '600', '1', '0', '{"pidInitValScript":false}', '[{"columnName":"name_","showName":"流程名称"},{"columnName":"key_","showName":"流程业务主键"},{"columnName":"desc_","showName":"流程描述"}]', '[{"columnName":"name_","condition":"LK","dbType":"varchar","showName":"流程名称","value":{"ctrlType":"inputText"},"valueSource":"param"},{"columnName":"is_main_","condition":"EQ","dbType":"varchar","showName":"版本 - 是否主版本","value":{"text":"Y"},"valueSource":"fixedValue"}]', '[{"columnName":"name_","returnName":"name"},{"columnName":"key_","returnName":"key"},{"columnName":"desc_","returnName":"desc"},{"columnName":"type_id_","returnName":"typeId"},{"columnName":"is_main_","returnName":"isMain"}]', '[]', null);
INSERT INTO "FORM_CUST_DIALOG" VALUES ('10000005470001', 'ywblb', '业务表列表', '业务表列表', 'list', 'dataSourceDefault', '本地数据源', 'table', 'bus_table', '1', '10', '1300', '600', '1', '1', '{"pidInitValScript":false}', '[{"columnName":"comment_","showName":"描述"},{"columnName":"key_","showName":"业务表key"},{"columnName":"name_","showName":"表名"}]', '[{"columnName":"key_","condition":"LK","dbType":"varchar","showName":"业务表key","value":{"ctrlType":"inputText"},"valueSource":"param"},{"columnName":"name_","condition":"LK","dbType":"varchar","showName":"表名","value":{"ctrlType":"inputText"},"valueSource":"param"}]', '[{"columnName":"id_","returnName":"id"},{"columnName":"key_","returnName":"key"},{"columnName":"name_","returnName":"name"},{"columnName":"ds_key_","returnName":"dsKey"},{"columnName":"comment_","returnName":"comment"}]', '[]', null);
INSERT INTO "FORM_CUST_DIALOG" VALUES ('20000002250001', 'busObjectSelect', '业务对象选择', null, 'list', 'dataSourceDefault', '本地数据源', 'table', 'bus_object', '1', '10', '800', '600', '1', '1', '{"pidInitValScript":false}', '[{"columnName":"key_","showName":"别名"},{"columnName":"name_","showName":"名字"},{"columnName":"desc_","showName":"描述"}]', '[{"columnName":"key_","condition":"LK","dbType":"varchar","showName":"key","value":{"ctrlType":"inputText"},"valueSource":"param"},{"columnName":"name_","condition":"LK","dbType":"varchar","showName":"名字","value":{"ctrlType":"inputText"},"valueSource":"param"}]', '[{"columnName":"id_","returnName":"id"},{"columnName":"key_","returnName":"key"},{"columnName":"name_","returnName":"name"},{"columnName":"desc_","returnName":"desc"}]', '[]', null);
INSERT INTO "FORM_CUST_DIALOG" VALUES ('10000005290006', 'zdydhklb', '自定义对话框列表', '自定义对话框列表', 'list', 'dataSourceDefault', '本地数据源', 'table', 'form_cust_dialog', '1', '10', '1300', '800', '1', '1', '{"pidInitValScript":false}', '[{"columnName":"name_","formatter":"","showName":"名字"},{"columnName":"key_","showName":"别名"},{"columnName":"style_","formatter":"var span = ''<span class=\"label label-primary\">表</span>'';\n\t\tif (value == ''view'')\n\t\t\tspan = ''<span class=\"label label-warning\">视图</span>'';\n\t\treturn span;","showName":"显示类型"}]', '[{"columnName":"key_","condition":"EQ","dbType":"varchar","showName":"别名","value":{"ctrlType":"inputText"},"valueSource":"param"},{"columnName":"name_","condition":"EQ","dbType":"varchar","showName":"名字","value":{"ctrlType":"inputText"},"valueSource":"param"}]', '[{"columnName":"key_","returnName":"key"},{"columnName":"name_","returnName":"name"}]', '[{"columnName":"id_","sortType":"desc"}]', null);
INSERT INTO "FORM_CUST_DIALOG" VALUES ('20000003050001', 'postSelector', '岗位选择框', null, 'list', 'dataSourceDefault', '本地数据源', 'table', 'org_group_rel', '1', '10', '800', '600', '1', '1', '{"pidInitValScript":false}', '[{"columnName":"rel_name_","showName":"岗位名称"},{"columnName":"rel_code_","showName":"岗位编码"},{"columnName":"rel_def_name_","showName":"职务名称"}]', '[]', '[{"columnName":"id_","returnName":"id"},{"columnName":"group_id_","returnName":"orgId"},{"columnName":"rel_def_id_","returnName":"jobId"},{"columnName":"rel_name_","returnName":"name"},{"columnName":"rel_code_","returnName":"key"},{"columnName":"rel_def_name_","returnName":"jobName"}]', '[]', null);
INSERT INTO "FORM_CUST_DIALOG" VALUES ('20000003160001', 'orgSelector', '组织选择框', null, 'tree', 'dataSourceDefault', '本地数据源', 'table', 'org_group', '1', '10', '800', '600', '1', '1', '{"id":"id_","pid":"parent_id_","pidInitVal":"0","pidInitValScript":false,"showColumn":"name_"}', '[]', '[]', '[{"columnName":"id_","returnName":"id"},{"columnName":"name_","returnName":"name"},{"columnName":"parent_id_","returnName":"parentId"},{"columnName":"code_","returnName":"key"},{"columnName":"grade_","returnName":"grade"}]', '[]', null);
INSERT INTO "FORM_CUST_DIALOG" VALUES ('20000003160002', 'roleSelector', '角色对话框', null, 'list', 'dataSourceDefault', '本地数据源', 'table', 'org_role', '1', '10', '800', '600', '1', '1', '{"pidInitValScript":false}', '[{"columnName":"name_","showName":"角色名称"},{"columnName":"alias_","showName":"别名"}]', '[{"columnName":"name_","condition":"LK","dbType":"varchar","showName":"名称","value":{"ctrlType":"inputText"},"valueSource":"param"},{"columnName":"alias_","condition":"LK","dbType":"varchar","showName":"别名","value":{"ctrlType":"inputText"},"valueSource":"param"}]', '[{"columnName":"id_","returnName":"id"},{"columnName":"name_","returnName":"name"},{"columnName":"alias_","returnName":"key"},{"columnName":"enabled_","returnName":"enabled"}]', '[]', null);
INSERT INTO "FORM_CUST_DIALOG" VALUES ('20000003130001', 'userSelector', '用户查询', null, 'list', 'dataSourceDefault', '本地数据源', 'table', 'org_user', '1', '10', '930', '660', '1', '1', '{"pidInitValScript":false}', '[{"columnName":"fullname_","showName":"姓名"},{"columnName":"account_","showName":"账号"},{"columnName":"mobile_","showName":"手机号码"}]', '[{"columnName":"fullname_","condition":"LK","dbType":"varchar","showName":"姓名","value":{"ctrlType":"inputText"},"valueSource":"param"},{"columnName":"account_","condition":"LK","dbType":"varchar","showName":"账号","value":{"ctrlType":"inputText"},"valueSource":"param"},{"columnName":"status_","condition":"EQ","dbType":"number","showName":"0:禁用，1正常","value":{"text":"1"},"valueSource":"fixedValue"}]', '[{"columnName":"id_","returnName":"id"},{"columnName":"fullname_","returnName":"name"},{"columnName":"account_","returnName":"account"},{"columnName":"email_","returnName":"email"},{"columnName":"mobile_","returnName":"mobile"},{"columnName":"weixin_","returnName":"weixin"},{"columnName":"address_","returnName":"address"},{"columnName":"sex_","returnName":"sex"},{"columnName":"status_","returnName":"status"}]', '[]', null);
INSERT INTO "FORM_CUST_DIALOG" VALUES ('20000003410001', 'jobSelector', '职务选择', '职务选择框', 'list', 'dataSourceDefault', '本地数据源', 'table', 'org_group_reldef', '1', '10', '800', '600', '1', '1', '{"pidInitValScript":false}', '[{"columnName":"name_","showName":"名称"},{"columnName":"code_","showName":"编码"},{"columnName":"description_","showName":"描述"}]', '[{"columnName":"name_","condition":"LK","dbType":"varchar","showName":"名称","value":{"ctrlType":"inputText"},"valueSource":"param"}]', '[{"columnName":"id_","returnName":"id"},{"columnName":"name_","returnName":"name"},{"columnName":"code_","returnName":"key"},{"columnName":"post_level_","returnName":"postLevel"},{"columnName":"description_","returnName":"description"}]', '[]', null);
