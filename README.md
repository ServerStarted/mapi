## mapi mobile 接口框架

### 一、功能点
```
1. context 基本参数
2. 验签 认证
3. token 认证
4. 接入监控 Cat
5. 接入日志 bizlog
6. gzip 压缩
```

#### 1.1 context header 传入字段
```
u           设备唯一标识, uuid
os          系统标识, 包含版本号
d           硬件标识  
app         app唯一标识 (iOS 取bundle id, android 取package name)
av          app 版本号
c           下载渠道
t           token
```

#### 1.2 验签认证

```
appid       应用id, 统一分配
time        时间戳, 如1425284944095

API的有效访问URL包括以下三个部分： 
1). 资源访问路径，如/api/v1/store/merchant/login; 
2). 请求参数：即API对应所需的参数名和参数值param=value，多个请求参数间用&连接
   如appid=12&date=14000000000&phone=18625169606； 
3). 签名串，由签名算法生成，sign=MD5值
 
签名算法如下： 
1). 所有请求参数按key值进行字典升序排列； 
2). 将以上排序后的参数表的value值进行字符串连接，如value1value23value3...valueN； 
3). secret作为后缀，对该字符串进行32位md5
4). 32位MD5值即为签名字符串 
```

#### 1.3 token 认证
ASE 加密，服务端控制

#### 1.4 接入监控 Cat

#### 1.5 接入日志 bizlog

#### 1.6. gzip 压缩
自定义一个编码，目标是不让对方简单的获取到请求内容

* 方式1
gzip 之后每个字节加100
* 方式2
gzip 之后在字节数组前加字节头

###  二、用法

### 引入依赖包
```xml
pom.xml中加入
<dependency>
	<groupId>com.loukou</groupId>
	<artifactId>mapi</artifactId>
	<version>{MAPI-VERISON}</version>
</dependency>
```

### 加入gzip压缩
```xml
web.xml 中加入
	<!-- GZip filter -->
	<filter>
		<filter-name>gzipFilter</filter-name>
		<filter-class>com.loukou.mapi.filter.GZIPFilter</filter-class>
	</filter>

	<filter-mapping>
		<filter-name>gzipFilter</filter-name>
		<url-pattern>/*</url-pattern>
	</filter-mapping>
```
