## 基于ssm的电子商务系统
> 有什么问题可以在 [小奇的论坛](http://bbs.littlepanda.top/forum.php) 或者[小奇的个人博客](http://www.littlepanda.top/)交流
>  以后会分享SpringBoot,SpringCloud的学习方案，同时还会发布基于ssm的BOS系统，ERP系统等.敬请关注

系统架构图:
![系统架构](http://upload.ouliu.net/i/20171217213543xl31w.png)

1. soa架构 web服务调用service服务 service调用dao服务
2. 基于easyUI的后台系统的搭建
3. maven搭建工程，便于分布式管理
4. 基于git的版本管理，连接github
5. [dubbo 服务调用管理，实现系统之间的通信](#1)
6. [activeMQ消息队列的使用](#2)
7. nginx+tomcat实现负载均衡
8. fastDFS 图片服务器的使用
9. cms系统的搭建使用
10. redis/redisCluster添加系统缓存
11. solr/solrCloud实现商品搜索功能，solrJ的使用
12. freemarker页面静态化，提高访问速度
13. sso单点登陆系统，避免tomcat集群seesion共享带来的集群数量限制问题
14. 基于redis的购物车系统
15. [项目部署](#15)

-------

<h3 id="1">dubbo服务调用管理，实现系统之间的通信</h3>
服务提供者:

    <dubbo:service interface="cn.e3mall.cart.service.CartService" ref="cartServiceImpl"  timeout="600000"/> 
    
服务消费者:

    <dubbo:reference interface="cn.e3mall.cart.service.CartService" id="cartService" />
<h3 id="2">activeMQ消息队列的使用</h3>
生产者发送信息:

    jmsTemplate.send(topicDestination, new MessageCreator() {/*发送的信息*/});
首先配置好监听器：

    public class ItemAddListener implements MessageListener //监听发送过来的信息
消费者:

    <bean class="org.springframework.jms.listener.DefaultMessageListenerContainer">
		<property name="connectionFactory" ref="connectionFactory" />
		<property name="destination" ref="topicDestination" />
		<property name="messageListener" ref="itemAddListener" />
    </bean>         
    
<h3 id="15">项目部署</h3>
这里我们可以利用tomcat热部署，也可以使用maven tomcat插件的deploy指令来进行部署war文件
            
    <build>
		<plugins>
			<plugin>
				<groupId>org.apache.tomcat.maven</groupId>
				<artifactId>tomcat7-maven-plugin</artifactId>
				<configuration>
					<path>/</path>
					<port>8082</port>
					<url>192.167.25.111</url>
					<username>tomcat</username>
					<password>tomcat</password>
				</configuration>
			</plugin>
		</plugins>
	</build>
            
