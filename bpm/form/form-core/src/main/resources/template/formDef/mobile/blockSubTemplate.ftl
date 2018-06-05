	<div type="subGroup" tableName="${table.name}" path="${table.path}" <#if table.relation=='onetoone'>initdata="true"</#if> ng-if="!permission.table.${table.name}.hidden">
		<div class="content-block-title">
			${formDesc}
<#if table.relation!='onetoone'><a href="###" class="button btn-sm pull-right" ng-click="add('${table.path}')" ng-if="permission.table.${table.name}.add">添加</a></#if>
		</div>
		
		<div class="list-block" ng-repeat="item in data.${table.path} track by $index">
<#if table.relation!='onetoone'><a class="fa fa-trash-o fa-lg pull-right" ng-click="remove('${table.path}',$index)" ng-if="permission.table.${table.name}.del"></a></#if>
			<ul>
<#list fieldList as field>
				<li ng-if="permission.fields.${table.name}.${field.name}!='n'">
					<div class="item-content">
			          <div class="item-inner">
			            <div class="item-title label">${field.desc}</div>
			            <div class="item-input"><@input field=field isSub=isSub/></div>
			          </div>
			        </div>
				</li>
</#list>
			</ul>
		</div>
		<p ng-if="!data.${table.path}.length &&permission.table.${table.name}.add && !permission.table.${table.name}.required">
			<a href="#" class="button button-fill" ng-click="add('${table.path}')">请添加 ${table.desc}数据</a>
		</p>
		<p ng-if="permission.table.${table.name}.required && !data.${table.path}.length" ng-model="data.${table.path}" ht-validate="{required:true}">
			<a href="#" class="button button-fill button-danger" ng-click="add('${table.path}')">请至少需要添加一行${table.desc}数据！</a>
		</p>
	</div>