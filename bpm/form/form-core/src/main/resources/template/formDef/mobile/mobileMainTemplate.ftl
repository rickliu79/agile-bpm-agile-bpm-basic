<#setting number_format="0">
<div class="mainForm">
	<div class="formTitle">${formDesc}</div>
	<#list fieldList as field>
          <div class="formItem" ng-if="permission.fields.${table.name}.${field.name}!='n'">
            <div class="itemTitle">${field.desc}</div>
            <div class="iteminput">
              <@input field=field isSub=false/>
            </div>
          </div>
		<#--分组-->
		<#if field.isSeparator && field.separatorTitle!="">
			<div class="group-title">${field.separatorTitle}</div>
		</#if>
	</#list>
</div>

