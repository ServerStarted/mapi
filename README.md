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

spring 的配置文件中加入
    <!-- Import Mapi -->
    <import resource="classpath*:appcontext-mapi.xml" />
```

### 加入servlet
```xml
web.xml 中加入
	<!-- Processes application requests -->
	<servlet>
		<servlet-name>appServlet</servlet-name>
		<servlet-class>com.loukou.mapi.servlet.MapiDispatcherServlet</servlet-class>
		<init-param>
			<param-name>contextConfigLocation</param-name>
			<param-value>classpath*:spring-servlet.xml</param-value>
		</init-param>
		<load-on-startup>1</load-on-startup>
	</servlet>

	<!-- 所有动态请求都是/api前缀 -->
	<servlet-mapping>
		<servlet-name>appServlet</servlet-name>
		<url-pattern>/</url-pattern>
	</servlet-mapping>
```

### 加入验签
```xml
spring 的配置文件中加入
	<!-- 验签 -->
	<mvc:interceptors>
		<mvc:interceptor>
			<mvc:mapping path="/api/v1/store/**" /> <!-- 路径 -->
			<bean class="com.loukou.mapi.sign.SignInterceptor">
				<property name="whiteList">
					<set>
						<value>/store-web/api/v1/store/picture</value> <!-- 不需要验签的接口 -->
					</set>
				</property>
			</bean>
		</mvc:interceptor>
	</mvc:interceptors> 
```

### 加入token 验证
```xml
spring 的配置文件中加入
	<!-- token 验证 -->
	<mvc:interceptors>
		<mvc:interceptor>
			<mvc:mapping path="/api/v1/store/**" />  <!-- 路径 -->
			<bean class="com.loukou.mapi.token.TokenInterceptor">
				<property name="whiteList">
					<set>
						<value>/store-web/api/v1/store/merchant/login</value><!-- 不需要token验证的接口 -->
					</set>
				</property>
			</bean>
		</mvc:interceptor>
	</mvc:interceptors> 
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
