var app = angular.module("app", [ 'base', 'baseDirective' ]);
app.controller('ctrl', [ '$scope', 'baseService', 'ArrayToolService', '$filter', function($scope, baseService, ArrayToolService, $filter) {
	var filter = $filter('filter');
	$scope.ArrayTool = ArrayToolService;

	$scope.init = function() {
		// 初始化数据
		$scope.data = {};
		// uedtor的配置
		$scope.editorConfig = {
			toolbars : [ [ 'source', 'undo', 'redo', 'bold', 'italic', 'removeformat', 'formatmatch', 'autotypeset', 'blockquote', 'pasteplain', '|', 'forecolor', 'backcolor', 'insertorderedlist', 'insertunorderedlist', '|', 'selectTemplate' ] ],
			initialFrameHeight : window.innerHeight - 260,
			enableAutoSave : false,
			autoHeightEnabled:false,
			allHtmlEnabled:true,
			focus : true,
			iframeCssUrl : '../../assets/js/plugins/ueditor/themes/pcframe.css',// 加入css
		};
	};

	$scope.$on("afterLoadEvent", function(event, data) {
	});

	/**
	 * 预览
	 */
	$scope.preview = function() {
		var conf = {
			height : 0,
			title : "预览",
			url : "formDefPreview.html?key=" + $scope.data.key,
			passData : {
				html : $scope.data.html
			}
		};
		$.Dialog.open(conf);
	};

	$scope.$watch("data.boKey", function(newValue, oldValue) {
		if (!newValue || newValue === oldValue) {
			return;
		}
		// 加载boTree
		// 请求参数
		var params = {
			boKey : newValue
		};
		var callBack = function(event, treeId, treeNode) {

		};

		var url = __ctx + "/form/formDef/boTreeData";
		var ztreeCreator = new ZtreeCreator("boTree", url);
		ztreeCreator.setCallback({
			onClick : callBack
		});

		ztreeCreator.initZtree(params);
	});

	/**
	 * 获取获取备份表单信息
	 */
	$scope.getBackupHtml = function() {
		var url = __ctx + "/form/formDef/getBackupHtml";
		var defer = baseService.postForm(url, {
			id : $scope.data.id
		});
		$.getResultData(defer, function(data) {
			$.Toast.success("同步成功");
			$scope.data.html = data;
		});
	};
} ]);
