<table class="form-table">
	<tr>
		<th colspan="2">${relation.table.comment}</th>
	</tr>
	<#list relation.table.columnsWithoutPk as column>
	<tr>
		<th>${column.comment}</th>
		<td>${generator.getColumn(column,relation)}</td>
	</tr>
	</#list>
</table>