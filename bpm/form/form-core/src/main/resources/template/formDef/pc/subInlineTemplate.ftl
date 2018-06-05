<#setting number_format="0">
<br>
	
<table class="w100" tableName="${table.name}" type="subGroup" path="${table.path}" <#if table.relation=='onetoone'>initdata="true"</#if> ng-if="!permission.table.${table.name}.hidden">
	<tr>
		<td colspan="${fieldList?size}">
			<span class="title">${table.desc} </span>
			<#if table.relation!='onetoone'><a href="javascript:void(0)" class="group-title-add-btn" ng-click="add('${table.path}')" ng-if="permission.table.${table.name}.add">添加</a></#if>
		</td>
	</tr>
	<tr class="firstRow">
		<#list fieldList as field>
			<th ng-if="permission.fields.${table.name}.${field.name}!='n'">${field.desc}</th>
		</#list>
	</tr>
	<tr class="owner-div"  type="subGroupTr" ng-repeat="item in data.${table.path} track by $index">
		<#list fieldList as field>
			<td ng-if="permission.fields.${table.name}.${field.name}!='n'">
				<@input field=field isSub=true/>
				<#if !field_has_next && table.relation!='onetoone'>
					<a class="subFieldList floatTools" ng-click="remove('${table.path}',$index)" ng-if="permission.table.${table.name}.del" ><span class="fa fa-delete actionBtn" title="移除"></span></a>
				</#if>
			</td>
		</#list>
	</tr>
	<tr ng-if="!data.${table.path}.length &&permission.table.${table.name}.add && !permission.table.${table.name}.required">
		<td colspan="${fieldList?size}"  class="alert alert-info subdatamessage">
			请添加 ${table.desc}数据
		</td>
	</tr>
	<tr ng-if="permission.table.${table.name}.required && !data.${table.path}.length">	
		<td colspan="${fieldList?size}"  ng-model="data.${table.path}" ht-validate="{required:true}" class="alert alert-danger subdatamessage">
			请至少需要添加一行${table.desc}数据！
		</td>
	</tr>
	
</table>
		

