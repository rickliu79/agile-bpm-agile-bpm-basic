# agile-bpm-basic

#### 项目介绍
敏捷工作流基础版

文档 http://doc.agilebpm.cn/
部署文档 http://agile-bpm.gitee.io/docs/bpm/bootstrap.html

#### 工作流解决方案
我们通过业务对象、表单、流程引擎共同协作来解决业务流难实施的痛点。

业务对象用来承载、持久化业务数据；

表单则是业务数据的展示层；

流程则用来驱动业务数据流转。

三者协作完成流程实施。

 > 
- **业务对象:** 由实体（表）组成，居然支持任意数据结构（关联关系），并且可以跨库来组织业务对象。而且难以置信的支持N层。
- **表单:** 表单完美的支撑了业务对象的展示，并支持丰富的前端组件。
- **流程引擎:** 高效、解耦、强大、灵活。流程引擎一切功能皆插件。

#### 软件架构
软件架构说明
- 每个模块分 API core rest 三个组件组成
- 前端 angular bootstrap-table bootstrap 等 js组件。 前后端分离，使用webpack 模块化打包
- 后端 spring4.3.17 mybatis  activiti5.22 redis groovy quartz mq atomikos spring security等


![如图所示](https://gitee.com/uploads/images/2018/0525/230104_41370bb1_1861740.png "屏幕截图.png")


#### 流程引擎

具备超快流程实施，业务迭代开发的能力。

#####定义业务对象
![输入图片说明](https://gitee.com/uploads/images/2018/0606/011446_bf2fbd84_1861740.png "屏幕截图.png")
##### 表单
![输入图片说明](https://gitee.com/uploads/images/2018/0606/011930_df63251f_1861740.png "屏幕截图.png")
#####流程设计器
![输入图片说明](https://gitee.com/uploads/images/2018/0525/231459_95401bef_1861740.png "屏幕截图.png")

## 目前基础版源码正在整理中...
qq 交流群 :four:  :seven:  :seven:  :seven:  :eight:  :one:  :eight:  :five:  :seven: 