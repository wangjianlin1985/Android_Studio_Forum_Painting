# Android_Studio_Forum_Painting
安卓Android二次元社区论坛bbs绘画app可导入Studio毕业源码案例设计
## 开发环境: Myclipse/Eclipse/Idea都可以(服务器端) + Eclipse(手机客户端) + mysql数据库
## 系统客户端和服务器端架构技术: 界面层，业务逻辑层，数据层3层分离技术，MVC设计思想！
## 服务器和客户端数据通信格式： XML格式(用于传输查询的记录集)和json格式(用于传输单个的对象信息)
内容是  做一个 能上传图片文本 ，有新闻（只读）模块和 论坛模块的手机APP
除了bbs和新闻
还有图片上传 和文章上传
热门就是 点击量最多的放在一个地方
## 实体ER属性：
用户: 用户名,登录密码,姓名,性别,出生日期,用户照片,联系电话,邮箱,家庭地址,注册时间

新闻: 新闻id,标题,新闻内容,浏览量,发布时间

帖子: 帖子id,帖子标题,帖子内容,浏览量,发帖人,发帖时间

帖子回复: 回复id,被回帖子,回复内容,回复人,回复时间

绘画: 作品id,作品名称,绘画分类,作品图片,作品描述,作品文件,点击率,发布用户,发布时间

文章: 文章id,标题,文章分类,内容,点击率,发布用户,发布时间

绘画分类: 绘画分类id,绘画分类名称

文章分类: 文章分类id,文章分类名称