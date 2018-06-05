var formDirective = angular.module("custFormDirective", ["base","formServiceModule"]);
/**
 * 功能说明：
 * 选择器
 * <input type="text"   permission="" ht-selector="main.name" 
 *   selectorconfig="{type:\'UserDialog\',display:\'fullName\', single:true,
					  bind:{userId:\''+dataName+'id\',fullName:\''+dataName+'\'}}" />
 permission：w 可选择，r 显示选择的数据。
 ht-selector : scope 数据表达式。
 	主表：data.main.name
 	子表：item.name
 
 selectorconfig:对话框配置
 single:true 或 false 是否单选
 type：对话框类型
 display ：对话框显示的字段。
 bind：key 为对话框返回字段
 	  val 为scope数据表达式
 */
formDirective.directive('htSelector',['$rootScope', function($rootScope){
    return {
    	restrict : 'AE',
    	template:getDirectiveTplHtml('htSelector'),
    	scope: {
    		htSelector:'='
    	},
        link: function($scope, element, attrs) {
        	element.removeClass();
        	$scope.permission=getPermission(attrs.permission,$scope);
        	//绑定配置
        	var selector=eval("("+ attrs.selectorconfig+")");
        	//绑定配置
        	var bind=selector.bind;
        	//显示字段。
        	var display=selector.display;
        	// 除去掉无效的bind 
        	for(var key in bind){
        		if(!bind[key].split(".")[1]) delete bind[key] //如果当前bind 不是 data.table.xx /item.field 则移除
        		if(bind[key] == attrs.htSelector)display = key; //display 展示字段等于当前控件的填充值
        	}
        	
        	//是否单选
        	
        	$scope.data={};
        	
        	//将控件数据初始化到指定本地域中。
        	$scope.initData=function(){
        		for(var key in bind){
            		var val=eval("($scope.$parent." + bind[key] +")");
            		console.info("val:" + val );
            		var aryVal=[];
            		if(val){
            			aryVal=val.split(","); 
            		}
            		//数据存放local变量data中。
            		$scope.data[key]=aryVal;
        		}
        	}
        	//对值进行双向绑定
        	$scope.$watch('htSelector',  function(newValue, oldValue) {
        		if(newValue!=oldValue){
        			$scope.initData();
        			$scope.render();
        		}
        	});
        	
        	//数据展示
        	$scope.render=function(){
        		$scope.names=$scope.data[display];
        	}
        	/**展示对话框*/
        	$scope.showDialog = function(){
        		var initData=[];
        		for(var i=0;i<$scope.data[display].length;i++){
        			var obj={};
        			for(var key in bind){
        				obj[key]=$scope.data[key][i];
        			}
        			initData.push(obj);
        		}
        		//打开对话框，scope.dialogOk则为处理同意数据
        		var conf = $.extend(selector,{callBack:$scope.dialogOk,initData:initData}); 
        		eval(selector.type+'(conf)');
        	}
        	//删除数据信息
        	$scope.remove = function(index){
				for(var key in $scope.data){
					var ary=$scope.data[key];
					ary.splice(index,1);
				}
				$scope.updScope();
			}// 选中数据点击事件
        	$scope.selectedOnClick = function(index){
        		for(var key in bind){
        			if(key !=selector.display){
        				var id=$scope.data[key][index];
        			}
        		}
        		//不同对话框选择的数据进行展示个体弹框，这个将来可以拓展到 各自对话框的自身来实现
        		
			}
        	
        	//更新值到父容器
        	$scope.updScope =function(){
        		for(var key in bind){
    				var dataStr = $scope.data[key]?$scope.data[key].join(","):"";//以逗号分隔数据
        			var tmp='$scope.$parent.'+bind[key]+'="'+dataStr +'"';
        			eval(tmp);
        		}
        		$scope.render();
        		!$rootScope.$$phase && $rootScope.$digest();
        	}
        	
        	//选择完成
        	$scope.dialogOk = function(returnData){
        		//将选择的值更新到data{[],[],}中
        		//先初始化防止报空
        		if(!returnData)returnData=[];
        		$scope.data = {};
        		for(var key in bind) $scope.data[key]=[];
        		
        		for(var i=0,object;object=returnData[i++];){
        			for(var key in bind){
            			$scope.data[key].push(object[key])
            		}
        		}
        		$scope.updScope();
        	};
        	//初始化数据
        	$scope.initData();
        	//显示数据。
        	$scope.render();	
        }
    }
}])

/*
 * 上传指令。
 * 使用方法:
 * 	<input ht-upload="data.main.name" permission='w' />
 * 	ht-input对应的属性值为scope对应的数据值路径。
 * 	permission:
 * 		取值有两种：
 * 			r:只读
 *  		w:可输入
 */
.directive('htUpload',["$rootScope",function($rootScope) {
	return {
    	restrict : 'A',
    	template : getDirectiveTplHtml('htUpload'),
    	scope: {
    		htUpload:"="
    	},
		link: function (scope,element,attrs,ctrl){
			element.removeClass();
			scope.permission = getPermission(attrs.permission,scope);
			var max = attrs.issingle ? 1:20;
			
        	var jsonStr = scope.htUpload?scope.htUpload.replace(/￥@@￥/g,"\""):"[]";
        	scope.files = eval("(" +jsonStr +")"); 
        	
        	scope.dialogOk = function(data){
        		scope.files = data;
        		scope.htUpload=JSON.stringify(data);
        		!$rootScope.$$phase&&$rootScope.$digest();
        	}
        	
    		scope.showDialog = function(){
    			var conf ={callback:scope.dialogOk,max:max}; 
    			UploadDialog(conf);
    		}
    		scope.onClick = function(id){
    			window.location.href=__ctx+"/system/file/download?id="+id;
    		}
    		
    		scope.$watch('htUpload',  function(newValue, oldValue) {
    			if(newValue==oldValue) return ;
    			
    			if(!newValue) scope.files=[];
    			else scope.files = eval("(" +newValue +")");
    			
    			!$rootScope.$$phase&&$rootScope.$digest();
        	});
    		scope.remove = function(index){
				scope.files.splice(index,1);
				//更新字段值
				if(!scope.files ||scope.files.length==0) scope.htUpload="";
				else scope.htUpload=JSON.stringify(scope.files);
			};
			
		}
	}}])
	/*
 * 功能输入框指令。
 * 使用方法:
 * <div >
 * 	<input ht-input="data.main.name" permission='w' />
 * </div>
 * 	ht-input对应的属性值为scope对应的数据值路径。
 * 	permission:
 * 		取值有两种：
 * 			r:只读
 *  		w:可输入
 */
.directive('htInput', function() {
	return {
    	restrict : 'AE',
    	link: function(scope, element, attrs,ctrl){
    		var permission = getPermission(attrs.permission,scope);
    		
    		if(permission=='r')	{
    			element.after(eval("scope."+attrs.ngModel));
    			element.remove();
    		}
    		if(permission=='n')	{ $(element).remove(); }
    		
    	}
}})
/*
 * 多行文本框指令。
 * 使用方法:
 * 	<textarea ht-textarea="data.main.name" permission='w' />
 * 	ht-textarea 对应的属性值为scope对应的数据值路径。
 * 	permission:
 * 		取值有两种：
 * 			r:只读
 *  		w:可输入
 */
.directive('htTextarea', function() {
	return {
    	restrict : 'AE',
    	scope: {
    		htTextarea:"=",
    		placeholder:'@'
    	},
    	link: function(scope, element, attrs,ctrl){
    		var permission = getPermission(attrs.permission,scope);
    		
    		if(permission=='r')	{
    			element.hide();
    			element.after(scope.htTextarea);
    		}
    		else if(permission=='n') { $(element).remove(); }
    	}
}})
/**
 * 表单使用 htCheckboxs：
 * 
 * 属性说明：
 * ht-checkboxs:对应scope中的数据。
 * permission：权限 r,w
 * values:选项数据为一个json
 * 使用示例:
 * <div ht-checkboxs="data.users" permission="w" defualtvalue="">
 *   <lable><input type="checkbox" value="1"/>红</lable>
 * </div>
 */
.directive('htCheckboxs',['commonService','$rootScope', function(commonService,$rootScope) {
	/*checkBox 选中事件**/
	handChange__=function(event){
		var scope=event.data.scope;
		var aryChecked=event.data.aryChecked;
		var isArray=event.data.isArray;
		commonService.operatorAry(this.value,this.checked,aryChecked);
		scope.htCheckboxs=isArray?aryChecked:aryChecked.join(",");
		$rootScope.$digest();
	}

    return {
    	restrict : 'A',
    	scope:{
    		htCheckboxs:"="
    	},
    	
    	link: function (scope, element, attrs){
    		/*权限 begin */
    		var permission=getPermission(attrs.permission,scope);
    		if(permission == 'n') {
    			element.remove();
    			return;
    		}
    		if(permission == 'r'){
    			if(scope.htCheckboxs){
    				var values = scope.htCheckboxs.split(",");
    				var text = "";
    				for(var i=0,val;val=values[i++];){
    					if(!val) continue;
    					var checkBox = $("input[value='"+val+"']",element);
    					if(checkBox.length>0){
    						text =text+ checkBox.parent().text();
    						if(i != values.length)text=text+",";
    					}
    				} 
    				element.after(text);
    			}
    			element.hide();
    			return;
    		}/*权限 end */
    		
    		var name=attrs.htCheckboxs;
        	//选中的值
        	var aryChecked=[];
        	var isArray=true;
        	
    		var val=scope.htCheckboxs||"";
    		if(typeof val == "string"){
    			//初始化
    			if(val!=""){ aryChecked=val.split(","); }
    			isArray=false;
    		}
    		var chkObjs=$("input",element);
    		//遍历相同名称的checkbox，初始化选中和绑定change事件。
    		for(var i=0;i<chkObjs.length;i++){
    			var obj=$(chkObjs[i]);
    			commonService.isChecked(obj.val(),aryChecked) && obj.attr("checked",true);
    			obj.bind("change",{scope:scope,isArray:isArray,aryChecked:aryChecked} ,handChange__)
    		}
        }
    }
}])

/**
 * 下拉选择框。
 * 属性说明：
 * 	ht-select：指令 属性值为scope的数据路径。
 *  permission：权限，值为r,w
 *  options:可选值
 * <select ht-select="data.main.hobbys" permission="w" ng-model=""></select>
 */
.directive('htSelect',['commonService', function(commonService) {
	 return {
		restrict : 'AE',
		require: "ngModel",
		scope:{
			htSelect:"="
		},
		link: function(scope, element, attrs,ctrl){
			var permission = getPermission(attrs.permission,scope);
			var aryOptions=eval(attrs.options);
			var isMultiple =attrs.multiple!=undefined;
			
			if(permission == 'n'){
				element.hide();
			}else if(permission=='r'){
				var value = scope.htSelect;
				if(!value){
					element.hide();
					return;
				}
				
    			if(isMultiple) {
    				value=value.split(",");
    			}else{
    				value=new Array(value+""); 
    			}
    			
    			var text=[];
				for (var int = 0,val;val=value[int++];) {
					text .push( $("option[value='"+val+"']",element).text());
				}
				element.after(text.join("，")).hide();
    		}
			
			if(!isMultiple) return;
			ctrl.$formatters.push(function(value){
				 if(value) return value.split(",")
				 return []
			});
	        ctrl.$parsers.push(function(value){
	        	 if(value&&value.length>0) return value.join(",")
	        	 return "";
	        });
		}
			       
}}])

//
/**
 * 自定义查询 联动下拉框。
 * {'alias':'searchUser','valueBind':'userId','labelBind':'account','bind':{'account_':'data.spxsxxb.spmc'}}
 * <ht-select-query="{上面的json}" options={}>
 */
.directive('htSelectQuery',['baseService', function(baseService) {
	 return {
		restrict : 'AE',
		require: "ngModel",
		scope:{
			ngModel:"="
		},
		link: function(scope, element, attrs,ctrl){
			var permission = getPermission(attrs.permission,scope);
			var isMultiple =attrs.multiple!=undefined;
			if(permission == 'n'){
				element.hide();
			}
			
			var htSelectQuery=parseToJson(attrs.htSelectQuery);
			scope.labelKey = htSelectQuery.labelBind;
			scope.valueKey =htSelectQuery.valueBind;
			scope.option =[];
			// 初始化param
			var param ={};
			for(key in htSelectQuery.bind){
				param[key] =eval("scope.$parent."+htSelectQuery.bind[key]);
			}
			
			//联动查询参数绑定
			function watchParam(){
				if(!htSelectQuery.bind)return;
				for(key in htSelectQuery.bind){
					scope.$parent.$watch(htSelectQuery.bind[key],
						function(newValue,oldValue,scope){
							if(newValue!=oldValue){
								loadOptions(this.exp,newValue); 
							}
						}
					)
				}
			}
			function loadOptions(exp,value){
				if(exp)
				for(key in htSelectQuery.bind){
					if(htSelectQuery.bind[key]==exp){
						param[key] =value;
					}
				}
				var queryParam = {alias:htSelectQuery.alias};
				queryParam.querydata = param;
				DoQuery(queryParam,function(data){
					scope.$apply(function(){
						scope.options=[];
						$.each(data,function(i,item){
							var option ={}; option.val=item[scope.valueKey]; option.text =item[scope.labelKey];
							scope.options.push(option);
						})
					})
					scope.handleReadPermission(); 
				});
			}
			loadOptions();
			watchParam();
			
			scope.isSelect = function(value){
				if(!ctrl.$modelValue) return false;
				if(!isMultiple)return ctrl.$modelValue==value;
				else return ctrl.$modelValue.indexOf(value)!=-1;
				
			}
			scope.handleReadPermission = function(){
				//只读处理
				if(permission=='r'){
					var value = scope.ngModel;
					if(!value){
						element.hide(); return;
					}
					
	    			if(isMultiple) value=value.split(",");
	    			else value=new Array(value+""); 
	    			
	    			var text=[];
					for (var int = 0,val;val=value[int++];) {
						text .push( $("option[value='"+val+"']",element).text());
					}
					element.after(text.join("，")).hide();
					return;
	    		}
			}
			
			//多选处理
			if(!isMultiple) return;
			ctrl.$formatters.push(function(value){
				 if(value) return value.split(",")
				 return []
			});
	        ctrl.$parsers.push(function(value){
	        	 if(value&&value.length>0) return value.join(",")
	        	 return "";
	        });
		},
		template:"<option value=''>请选择</option><option ng-repeat='option in options' value='{{option.val}}' ng-selected='isSelect(option.val)'>{{option.text}}<option>"
			       
}}])


/**
 * 功能说明：
 *  单选按钮指令
 * 	ht-radios：指令名称，值为数据路径
 *  permission：权限 w,可写,r只读。
 *  values：单选框对应的值
 * <div ht-radios="data.color" permission="w">
 * 		<label class="radio-inline">
 * 			<input type="radio" value="1" ng-model="">啊
 * 		</label>
 * </div>
 * <div 
 */
.directive('htRadios',['commonService', function(commonService) {
    return {
    	restrict : 'A',
    	scope:{
    		htRadios:'='
    	},
    	link: function(scope,element,attrs){
    		var permission=getPermission(attrs.permission,scope);
    		
    		if(permission =='n'){
    			element.remove();
    		}
    		else if(permission =='r'){
    			var val=scope.htRadios;
    			if(val){
    				var radio = $("input[value='"+val+"']",element);
    				radio&&element.html(radio.parent().text());
    			}
    			else element.hide(); 
    		}
        }
    }
}])
/**
 *  日期控件 ht-date
 * <input ht-date="yyyy-MM-dd HH:mm:ss" permission="w"/>
 * **/
.directive('htDate', function() {
	return {
		restrict : 'A',
		require : "ngModel",
		link : function(scope, element, attrs, $ctrl) {
			var permission=getPermission(attrs.permission,scope);
			if(permission=="w" || permission=="b"){
				//pc情况
				if(window.FORM_TYPE_!='mobile'){
					element.addClass("dateformat");
					return;
				}
				var preset = "date";
				if(attrs.htDate =='yyyy-MM-dd HH:mm:ss') preset = 'datetime';
    			if(attrs.htDate =='HH:mm:ss')preset = 'time';
				
    			var dateConfig = DTPickerOpt[preset]; 
    			element.mobiscroll(dateConfig);
				return;
			}
			if(permission=="r"){
				element.after(eval("scope."+attrs.ngModel));
			}
			element.hide();
		}
	};
})
/**
 * 数据字典指令。
 * dictype：数据字典别名。
 */
.directive('htDic', function($injector){
	return {
		restrict: 'A',
		scope:{
			htDic:'='
    	},
    	template:getDirectiveTplHtml("htDic"),
		link: function(scope, element, attrs) { 
			element.removeClass();
			//scope.validate = attrs.validate;
			scope.permission =getPermission(attrs.permission,scope);
			var dicKey =attrs.dickey; //数据字典的类型
			var url=attrs.url|| __ctx +"/system/dataDict/getByTypeKeyForComBo?typeKey="+dicKey;
			var keyName =attrs.keyName||"key";
			var valName=attrs.valName||"text";
			
			scope.treeId = parseInt(Math.random()*1000)
			scope.dicData={};
        	scope.treeClick = function(event,treeId,treeNode){
        		scope.dicData.key =treeNode[keyName];
        		scope.dicData.value =treeNode[valName];
        		scope.htDic =scope.dicData.key;
        		!scope.$parent.$$phase&&scope.$parent.$digest();
        		
        		// 树隐藏
        		if(window.FORM_TYPE_!='mobile'){
        			$('#dropBody'+treeId).dropdown('close');
        		}else{
        			$.closeModal($('#popover'+treeId));
        		}
        	}
        	
        	scope.loadOrgTree = function(){
        		new ZtreeCreator(scope.treeId,url)
        		.setDataKey({idKey:keyName,name:valName})
 				.setCallback({onClick:scope.treeClick})
 				.setChildKey()
 				.setOutLookStyle()
 				.initZtree({},1,function(treeObj,treeId){
 					
 					//树列表的dropBody初始化
 					if(window.FORM_TYPE_!='mobile'){
 						$('#dropBody'+treeId).dropdown({justify: '#dropDown'+treeId});
 					}else{
 						$(".page").after($('#popover'+treeId));
 					}
 					
 					// 通过key 回显Value
 					if(scope.htDic){
 						//获取key 的那个Value
 						var node = treeObj.getNodesByFilter( function(node){ if(node[keyName]==scope.htDic) return true; else return false; },true);
 						if(node){
 							scope.dicData.key =node[keyName];
 			        		scope.dicData.value =node[valName];
 			        		!scope.$parent.$$phase&&scope.$parent.$digest();
						}
 					}
				});
        	}
        	scope.loadOrgTree();
		}
	};
})

/**
 * 富文本框指令：
 * <span ht-editor="{}" height="" width="" ng-model="content"></span>
 * ng-model
 * editorConfig{hieght}
 */
.directive('htEditor', function() {
	return {
		restrict : 'AE',
		replace:true,
		require : '?ngModel',
		scope : {},
		link : function(scope, element, attrs, ngModel) {
			element.removeClass("form-control");
			var defaultConf = { focus : true,
					toolbars : [ [ 'source', 'undo', 'redo', 'bold', 'italic', 'removeformat', 'formatmatch', 'autotypeset', 'blockquote', 'pasteplain', '|', 'forecolor', 'backcolor', 'insertorderedlist', 'insertunorderedlist' ] ],
					initialFrameHeight : 150, initialFrameWidth : 600
				};
			
			var editorConfig = angular.fromJson(attrs.htEditor);
			editorConfig = $.extend(editorConfig,defaultConf);
			
			var editor = new UE.ui.Editor(editorConfig);
			editor.render(element[0]);
			if (ngModel) {
				// Model数据更新时，更新百度UEditor
				ngModel.$render = function() {
					try {
						editor.setContent(ngModel.$viewValue);
					} catch (e) {
					}
				};
				// 设置内容。
				editor.ready(function() {
					editor.setContent(ngModel.$viewValue);
				});
				// 百度UEditor数据更新时，更新Model
				editor.addListener('contentChange', function() {
					setTimeout(function() {
						scope.$apply(function() {
							ngModel.$setViewValue(editor.getContent());
						})
					}, 0);
				});
			}

		}
	}
})


/**
 * 校验入口.  {require:true,dateRangeEnd:{targetVal:scope.data.sss}}  //不能大于结束日期
 * 校验指令，指定必须有ngModel属性。
 */
.directive('htValidate', [function () {
      return {
          require: "ngModel",
          link: function (scope, element, attr, ctrl) {
        	  var validate = attr.htValidate;
        	  var permission=getPermission(attr.permission,scope);
        	  //如果不必填且没有其他校验返回
        	  if(permission!=="b" && validate=="{}")return;
    		  var validateJson = eval('(' + validate + ')');
        	  
        	  if(permission=="b")validateJson.required = true;
        	  
              var customValidator = function (value) {
            	  if(!validate) return true;
            	  handlTargetValue(validateJson);
                  var validity =  $.fn.validRules(value,validateJson,element);
                  ctrl.$setValidity("customValidate", validity);
                  return validity ? value : undefined;
              };
              
              ctrl.$formatters.push(customValidator);
              ctrl.$parsers.push(customValidator);
              

              //获取比较目标字段的值。   所有比较的都包含target对象eg:{rule:{target:data.mian.name}}
              var handlTargetValue = function(validateJson){
            	  for(key in validateJson){
            		  if(validateJson[key].target){
            			  validateJson[key].targetVal =eval("scope."+validateJson[key].target);
            		  }
            	  }
              }
              
          }
      };
  }])
  /**
   * 格式化数字 ht-number
   * {'coinValue':'￥','isShowComdify':1,'decimalValue':2}
   * 
   */
  .directive('htNumber', ['formService',function (formService) {
      return {
          require: "ngModel",
          link: function (scope, element, attr, inputCtrl) {
        	  var permission=getPermission(attr.permission,scope);
        	  var formater = attr.htNumber;
        	  if(!formater) return;
        	  
    		  var formaterJson = eval('(' + formater + ')');
        	  var input = element;
        	  if(permission=="n"){
        		  element.remove();
        		  return;
        	  } else if(permission=="r"){
        		  var val = eval("scope."+attr.ngModel)
        		  if(!val){
        			  element.hide();return;
        		  }
        		  element.before(formService.numberFormat(val,formaterJson)+" ");
        		  setCapital(val);
        		  element.remove();
        	  }
        	  
        	 //光标移出展示格式化数据
        	 input.blur(function(a){
        		 inputCtrl.$setViewValue(formService.numberFormat(inputCtrl.$modelValue,formaterJson));
        		 inputCtrl.$render();
        		 // 如果跟随人民币
        		 setCapital();
        	 });
        	
             //  model to view
        	 inputCtrl.$formatters.push(function(value){
        		 setCapital();
        		 return formService.numberFormat(value,formaterJson); 
              }); 
        	 inputCtrl.$parsers.push(function(value){
        		 return $.fn.toNumber(value);
              }); 
        	 
        	 function setCapital(val){
        		 if (formaterJson.capital) {
        			 var capital =element.next("capital");
        			 if(capital.length==0)capital=$("<capital></capital>");
        			 capital.text("("+formService.convertCurrency(val?val:inputCtrl.$modelValue)+")");
         			element.after(capital); 
      			 }
        	 }
          }
      };
  }])
  
  /**
   * 字段函数计算
   */
.directive('htFuncexp',['formService',function(formService){
	var link = function($scope, element, attrs, $ctrl) {
		var modelName = attrs.ngModel;
		var watchArr = [];
		var funcexp=attrs.htFuncexp,
			watchField=getWatchField(funcexp);
		if(watchField.length>0){
			for(var i=0,f;f=watchField[i++];){
				//子表字段的监控
				var subMsg = f.split(".");
				if(subMsg.length>3){
					var fieldName = subMsg[3];
					var subTableSrc =f.replace("."+fieldName,"");
					
					/* $scope.$watch(function($scope){
						return eval("$scope."+subTableSrc+".map(function(obj){return obj."+fieldName+"})");
					},function(newValue,oldValue,scope){
						if(newValue!= oldValue){
							formService.doMath(scope,modelName,funcexp);
						}
					});*/
					try{
						formService.doMath($scope,modelName,funcexp);
					}catch(e){
						
					}
					// 监控已存在的
					var length = eval("$scope."+subTableSrc+".length || 0");
					for (var int = 0; int < length; int++) {
						watch(subTableSrc+"["+(int)+"]."+fieldName);
					}
					//监控新添加的
					var watched = $scope.$watch(subTableSrc+".length",function(newValue,oldValue,scope) {
		        		if(newValue>oldValue){
		        			watch(subTableSrc+"["+(newValue-1)+"]."+fieldName);
		        		}else if(newValue<oldValue){
		        			watchArr.pop()(); 
		        			formService.doMath($scope,modelName,funcexp);
		        		}
		        	});
				}
				else{
					//主表和子表单条运算
					watch(f);
				} 
				
			}
			
		}
		// 监控
		function watch(path){
			var watch = $scope.$watch(path,function(newValue,oldValue,scope) {
        		if(newValue!=oldValue){
        			formService.doMath(scope,modelName,funcexp);
        		}
        	});
			watchArr.push(watch);
		}
		function getWatchField(statFun){
			var myregexp = /\(([data.main|data.sub|item].*?)\)/g;
			var match = myregexp.exec(statFun);
			var arrs=[];
			while (match != null) {
				var str =match[1];
				var has=false;
				for(var i=0,v;v=arrs[i++];){
					if(v == str)has =true;
				}
				if(!has)arrs.push(str);
				match = myregexp.exec(statFun);
			}
			return arrs;
		}
	};
	return {
		restrict : 'A',
		require : "ngModel",
		compile : function() {
			return link;
		}
	};
}])
/**
 *  计算日期间隔
 */ 
.directive('htDatecalc', ['formService','$filter',function (formService,$filter) {
      return {
          require: "ngModel",
          link: function ($scope, element, attr, ctrl) {
        	  if(!attr.htDatecalc) return;
        	  var ngModel =attr.ngModel;
    		  var dateCalc = eval('(' + attr.htDatecalc + ')');
    		  var startSrc =dateCalc.start;
    		  var endSrc = dateCalc.end;
    		  var diffType =dateCalc.diffType;
    		  if(startSrc.split(".").length>2){ //子表的
    			  startSrc ="item."+startSrc.split(".")[2];
    			  endSrc ="item."+endSrc.split(".")[2];
    		  }else{
    			  endSrc = "data."+endSrc;
    			  startSrc ="data."+startSrc;
    		  }
			  $scope.$watch(startSrc,function(newValue, oldValue) {
	        	   if(newValue!=oldValue){
	        		   var endDate =eval("$scope."+endSrc);//scope.data.main.field , item.field
	        		   var int =formService.doDateCalc(newValue,endDate,diffType);
	        			   ctrl.$setViewValue(int);
	        			   ctrl.$render();
	        	   }
    		   });
			  $scope.$watch(endSrc,function(newValue, oldValue) {
	        	   if(newValue!=oldValue){
	        		   var start =eval("$scope."+startSrc);//scope.data.main.field , item.field
	        		   var int = formService.doDateCalc(start,newValue,diffType);
	        			   ctrl.$setViewValue(int);
	        			   ctrl.$render();
	        	   }
    		   });
    			  
          }
      };
  }])
/**
 * 自定义对话框 ht-custdialog
 */
formDirective.directive('htCustdialog',['$rootScope', function($rootScope) {
    return {
    	restrict : 'AE',
    	scope:true,
        link: function($scope, element, attrs) {
        	var permission=getPermission(attrs.permission,$scope);
        	// 没有权限
        	if(permission && permission !='w' && permission != 'b'){
        		element.hide();return;
        	}
        	$scope.confJson=eval("("+ attrs.htCustdialog+")");
        	
        	//是否位于子表。位于子表的填充方式为当前行覆盖
        	var isInSub = $scope.confJson.isInSub;//(element.parents("[type='subtable']").length >0);
        	
        	//绑定配置
        	var dialogMappingConf=$scope.confJson.custDialog.mappingConf;
        	var isCombine = $scope.confJson.custDialog.type =='combiDialog';
        	//对话框初始化监听
        	$(element).click(function(){
        		$scope.showDialog();
        	});
        	
        	/**展示对话框*/
        	$scope.showDialog = function(){
        		var param =$scope.getQueryParam($scope.confJson.custDialog.conditions);
        		
        		var conf = {param:param};//initData
        		CustomDialog.openCustomDialog($scope.confJson.custDialog.alias,$scope.dialogOk,conf);
        	}
        	
        	$scope.dialogOk = function(returnData,dialog){
        		if(dialog)dialog.dialog('close');
        		$scope.pushDataToForm(returnData,dialogMappingConf);
        	}
        	
        	$scope.custQueryOk = function(returnData,mappingConf){
        		$scope.pushDataToForm(returnData,mappingConf);
        	}
        	//自定义查询，自定义对话框
        	$scope.initCustQuery = function(){
        		if($scope.confJson.custQueryList.length==0) return ;
        		
        		for(var i = 0,custQuery;custQuery=$scope.confJson.custQueryList[i++];){
        			if(custQuery.type != 'aliasScript' && custQuery.type != 'custQuery') continue;
        			//通过触发字段进行初始化
        			var gangedTarget =custQuery.gangedTarget;
        			
        			// 回车联动
        			if(custQuery.ganged == 'inter'){
        				if(!isInSub){
                			var target =$("[ng-model='data."+gangedTarget+"']");
            			}else{
            				var gangedArray = gangedTarget.split(".");
            				var target = $("[ng-model='"+gangedArray[gangedArray.length-1]+"']",$(element).closest("tr"));
            			}
        				
        				var autoQueryData = $(target).data("autoQueryData");
        				if(!autoQueryData){
        					autoQueryData=[];
        					$(target).bind("keydown", function(event){ 
            					if(event.keyCode != 13) return;
            					var bindCustQuerys =$(this).data("autoQueryData");
            					for(var j = 0,q;q=bindCustQuerys[j++];){
            						$scope.executeQuery(q);
            					}
            				});
        				}
        				autoQueryData.push(custQuery);
        				$(target).data("autoQueryData",autoQueryData);
        			}else{
        				
        			//值改变联动
        				if(!$scope.autoQueryData)$scope.autoQueryData ={};
        				//多个自定义查询绑定到同一个字段上，仅仅执行一次
        				var gangedTargetStr = "data."+gangedTarget; //需要watch 的字符串
        				if(!$scope.autoQueryData[gangedTargetStr]){
        					$scope.autoQueryData[gangedTargetStr]=[];
        					
        					$scope.$watch(gangedTargetStr,function(newValue, oldValue) {
            	        		if(newValue!=oldValue && newValue){ // 如果为空 则不查了
            	        			var queryList = $scope.autoQueryData[this.exp];
            	        			for(var k=0,q;q=queryList[k++];){
            	        				$scope.executeQuery(q);
            	        			}
            	        		}
            	        	});
        				}
        				$scope.autoQueryData[gangedTargetStr].push(custQuery)
        				
        			}
    				
        		}
        	}
        	/** 执行别名脚本查询，自定义查询*/
        	$scope.executeQuery = function(custQuery){
        		var param={alias:custQuery.alias};
				param.querydata = $scope.getQueryParam(custQuery.conditions);
				
				if(custQuery.type == 'custQuery'){
					DoQuery(param,function(data){
						$scope.pushDataToForm(data,custQuery.mappingConf);
					})
				}
				else if(custQuery.type == 'aliasScript'){
					var data = RunAliasScript(param)
					$scope.pushDataToForm(data,custQuery.mappingConf);
				}
        	}
        
    		//通过参数条件配置获取参数 条件
        	$scope.getQueryParam = function(conditions){
        		var param ={};
        		for (var i = 0,condition;condition=conditions[i++];) {
        			var value = condition.defaultValue
					if(condition.bind){
						value =eval("$scope.data."+condition.bind)||value;
					}
        			if(value)param[condition.field] = value;
				}
        		return param;
        	}
        	window.setTimeout($scope.initCustQuery,10);//有时候trigger目标字段还没出现。
        	
        	//填充数据到数据库
        	$scope.pushDataToForm=function(returnData,mappingConf){
        		//将选择的值更新到data{[],[],}中
        		//先初始化防止报空
        		if(!returnData)returnData=[];
        		if(!$.isArray(returnData)){
        			returnData=new Array(returnData);
        		}
        		// 循环所有的返回值
        		for (var i = 0; i < returnData.length; i++) {
        			var hasSub = false,subDatas={};
        			// 循环所有mapping，将返回值，插入指定字段
					for (var int = 0,mapping;mapping= mappingConf[int++];){ 
						if(!mapping.from) continue; 
						var value =returnData[i][mapping.from] || returnData[i][mapping.from.toLowerCase()];
						var targets =mapping.target;
						// 返回值单个字段可以映射表单多个字段
						for (var j = 0,target;target= targets[j++];){
							/**target格式 主表 【表明.字段名】子表 【主表明.子表名.字段名】 **/
							target =target.toLowerCase();
							var targetArray =target.split(".");
							//如果是主表【表明.字段名】
							if(targetArray.length==2){
								$scope.data[targetArray[0]][targetArray[1]]=value; 
							}
							//如果是位于子表
							else if(isInSub){
								$scope.item[targetArray[2]]=value;
							}//位于主表填充子表，添加行的形式
							else{
								hasSub = true;
								if(!subDatas[targetArray[1]])subDatas[targetArray[1]]={}
								subDatas[targetArray[1]][targetArray[2]] = value;
								subDatas[targetArray[1]].mian_table_name_ =targetArray[0]
							}
							
						}
					}
					// 如果有子表
					if(hasSub){
						for(tableName in subDatas){
							//var tempData =$.extend({},$scope.dataInit[mianTableName][tableName].row);
							var data = $.extend({},subDatas[tableName]);  
							var mainTableName =data.mian_table_name_
							delete data.mian_table_name_;
							$scope.data[mainTableName]["sub_"+tableName].push(data);
						}
					}
				}
        		!$rootScope.$$phase&&$rootScope.$digest(); 
        	}
        	
        	
        	
        }
    }
}])
/**
 * 表单意见 ht-bpm-opinion
 */
.directive('htBpmOpinion', [function(){
	return {
		restrict:"A",
		scope:{
			htBpmOpinion:"=",
			opinionHistory:"="
		},
		link:function(scope, element, attrs){
			scope.style = attrs.style;
			element.removeClass(); 
			element.removeAttr("style"); // replace 不起效why?
			scope.permission = eval("scope.$parent."+attrs.permission)||'w';
			if(scope.permission=='n')element.remove();
		},
		template:'<div>\
					<textarea taskopinion  ng-model="htBpmOpinion" ht-validate="{required:{{permission==\'b\'}} }" style="{{style}}" ng-show="permission==\'w\'|| permission==\'b\'"></textarea>\
					<blockquote ng-repeat="opinion in opinionHistory" ng-if="opinionHistory.length>0">\
		                <div>\
							<span >{{opinion.auditorName}}</span>&nbsp;<span class="label label-default"> {{opinion.status | taskstatus}} </span>&nbsp;{{opinion.createTime |date:"yyyy-MM-dd HH:mm:ss"}} &nbsp; \
			            </div>\
						<div style="padding-top:10px">{{opinion.opinion}}</div> \
			        </blockquote>\
				  </div>',
		replace:true
	};
}])

/**
 * 表单中的流程图
 */
.directive('htBpmFlowImage', ['bpmService', function(bpm){
	return {
		restrict:"EA",
		link:function(scope, element, attrs){
			var url ;
			if(!bpm.isInstance){
				url =__ctx + '/flow/task/taskImage?taskId=' + bpm.getTaskId();
				if(bpm.isCreateInstance()) url =__ctx+ '/bpm/bpmImage?defId='+bpm.getDefId();
			}else{
				url =__ctx +"/flow/instance/instanceFlowImage?id="+bpm.getProInstId();
			}
			//如果在页面中
			if(attrs.htBpmFlowImage=="inHtml") {
				element.html("<iframe src='"+url+"' style='width:100%;height:100%;border:0' frameborder='0'></iframe>");
				return;
			}
			element.bind('click',function(){
    			var def = {title : '流程图',width : 950,height : 600,modal : true,resizable : false,iconCls : 'fa fa-table'};
    			$.topCall.dialog({src:url,base : def});
			});
		}
	};
}])
.directive('htBpmApprovalHistory', ['bpmService', function(bpm){
	return {
		restrict:"EA",
		link:function(scope, element, attrs){
			var urlParam ="instId="+ bpm.getProInstId();
			if(!bpm.isInstance){
				urlParam = "taskId=" + bpm.getTaskId();
			}
			if(attrs.htBpmApprovalHistory=="inHtml"){
				if(bpm.isCreateInstance()){
					element.hide();
					return ;
				}
				element.html("<iframe src='"+__ctx + '/flow/instance/instanceFlowOpinions?' + urlParam +"' style='width:100%;height:100%;border:0' frameborder='0'></iframe>");
				return;
			}
			
			element.bind('click',function(){
    			var def = {title : '审批历史',width : 950,height : 500,modal : true,resizable : false,iconCls : 'fa fa-table'};
    			        //启动流程
				if(bpm.isCreateInstance()){
				    $.topCall.alert("流程未启动");
				}
				//完成任务
				else{
	    			$.topCall.dialog({src:__ctx + '/flow/instance/instanceFlowOpinions?taskId=' + bpm.getTaskId(),base : def});
				}
			});
		}
	};
}])
/**
 * 意见历史中过滤器审批状态过滤器。
 */
.filter("taskstatus",function(){
    return function(input){
    	var rtn="";
        switch(input){
        	case "agree": 				rtn="同意"; 			break;
        	case "start": 				rtn="提交"; 			break;
        	case "end": 				rtn="结束"; 			break;
        	case "awaiting_check": 		rtn="待审批"; 		break;
        	case "oppose": 				rtn="反对"; 			break;
        	case "abandon":  			rtn="弃权"; 			break;
        	case "reject": 				rtn="驳回"; 			break;
        	case "backToStart": 		rtn="驳回到发起人"; 	break;
        	case "reSubmit": 			rtn="重新提交";		break;
        	case "revoker": 			rtn="撤回"; 			break;
        	case "revoker_to_start":	rtn="撤回到发起人"; break;
        	case "signPass": 			rtn="会签通过"; 		break;
        	case "signNotPass": 		rtn="会签不通过"; 	break;
        	case "signBackCancel":		rtn="驳回取消"; 		break;
        	case "signRecoverCancel":	rtn="撤销取消"; 		break;
        	case "passCancel": 			rtn="通过取消"; 		break;
        	case "notPassCancel": 		rtn="不通过取消"; 	break;
        	case "transforming": 		rtn="流转中"; 		break;
        	case "transAgree": 			rtn="同意"; 			break;
        	case "transOppose":  		rtn="反对"; 			break;
        	case "skip": 				rtn="跳过执行"; 		break;
        	case "manual_end": 			rtn="人工终止"; 		break;
        }
        return rtn;
    }
});

formDirective.service('custFormFnService', [function() {
    var service = {
		initScopeFn:function(scope){
			if(scope.add){
				console.warn("scope 中已经存在 add方法,则不进行自定义表单内置方法的初始化！")
				return ;
			}
			//添加子表记录
			scope.add = function(path){
				var arr = path.split(".");
				if(arr.length<2){
					alert("subtable path is error!")
				}
				var subTableName = arr[1].replace("sub_","");
				var tempData = scope.data[arr[0]].initData[subTableName];
				
				if(!tempData)tempData={};
				var ary = eval("(scope.data." + path + ")"); 
				var newItem = angular.copy(tempData);
				if(ary){
					ary.push(newItem);
				}
				else{
					var rows=[];
					rows.push(newItem);
					eval("(scope.data." + path + "=rows)")
				}
			};

			scope.delete_flag_=0;
			//删除子表记录
			scope.remove = function(path,index){
				if(delete_flag_>0) return ;
				var ary = eval("(scope.data." + path + ")");
				if(ary&&ary.length>0){
					ary.splice(index,1);
				}
				delete_flag_=1;
				window.setTimeout(function(){delete_flag_=0;},50);
			};
			
			scope.$on("html:update",function(event,data){
				scope.form = data.form;
				//如果是内部表单的情况才处理数据。
				if(data.form.type==="INNER"){
					scope.data =data.data;
					scope.permission = parseToJson(data.permission);
					// 初始化表单
					window.setTimeout(scope.initSubTableData,100);
				}
			});

			//初始化子表数据
			scope.initSubTableData = function(data,permission){
				var permission = scope.permission;
				if(!permission){return;}
				
				var initSubTable = [];
				for(var subTable in permission.table){
					if(permission.table[subTable].required){
						initSubTable.push(subTable);
					}
				}
				
				$("[type='subGroup'][initdata]").each(function(i,item){
					initSubTable.push($(item).attr("tablename"));
				});
				var data = scope.data;
				for(var i=0,subTable;subTable=initSubTable[i++];){
					for(var boCode in data){
						var initData =data[boCode].initData[subTable];
						if(initData &&(!data[boCode]["sub_"+subTable]||data[boCode]["sub_"+subTable].length==0)){
							data[boCode]["sub_"+subTable] = [];
							data[boCode]["sub_"+subTable].push($.extend({},initData));
						}
					}
				}
				!scope.$$phase&&scope.$digest(); 
			}
    	}
    };
    return service;
}]);



//data.permission   {tableName:{table{},field{}}} 
function getPermission(permissionPath,scope){
	var permission = scope.permission||scope.$parent.permission||scope.$parent.$parent.permission;
	if(!permission||!permissionPath) return "w";
	try {
		var p = eval(permissionPath); 
	} catch (e) {
		console.info("获取权限出现了异常 permissionPath:"+permissionPath)
		console.info(permission);
		console.info(e);
	}
	return p ||'w'; 
}
//表单中需要初始化表单后执行的初始化动作
//eg：office 控件ht-office-plugin 已经有了统一初始化所有office指令的动作。故此我们不在写指令单个单个的处理
function ngFormReady(scope){
	//初始化office控件
	if(window.OfficePlugin) OfficePlugin.init(scope);
}


/**
 * 调用别名脚本。
 * @param param	传入参数，参数格式 {alias:"别名必须传入",name:"abc","age":20}
 * @returns 返回数据格式如下：
 * {"result":"4","isSuccess":0,"msg":"别名脚本执行成功！"}
 * result:脚本返回的结果
 * isSuccess：0表示成果能够，1，失败
 * msg ：具体的出错信息
 */
function RunAliasScript(param){
	if(!param) param={};
	var url=__ctx + '/platform/system/aliasScript/executeAliasScript.ht';
	var rtn="";
	$.ajaxSetup({async:false});  //同步
	$.post(url,param,function(data){
		rtn=eval("("+ data +")");
	});
	$.ajaxSetup({async:true}); //异步
	return rtn;

}
