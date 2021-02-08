# forum项目结构说明

    ├── auth -- 认证服务[30002] 
    
    ├── business -- 业务模块

    ├     ├──  blog -- 博客模块[31000]
    
    ├     ├──  credit -- 用户信用模块[31002]（未开发）
        
    ├     ├──  search -- 数据搜索模块[31001]
    
    ├── common -- 公共依赖组件
    
    ├     ├──  datasource -- 数据源组件
    
    ├     ├──  feign -- feign接口组件
    
    ├     ├──  file -- 文件组件（未开发）
    
    ├     ├──  job -- 任务调度组件（未开发）
    
    ├     ├──  model -- 公共实体组件
    
    ├     ├──  mq -- 消息队列组件（未开发）
    
    ├     ├──  mongodb -- mongodb组件
    
    ├     ├──  oauth2-resource -- 资源模块组件
    
    ├     ├──  redis -- redis组件
    
    ├     ├──  util -- 工具类组件
    
    ├── config -- 配置中心[30001]
    
    ├── core -- service核心组件
    
    ├── eureka -- 服务注册模块[8761]
    
    ├── gateway -- 网关模块[30000]
    

#开发进度
##1.用户注册登录
mysql保存用户账号等数据

安全框架oauth2

token缓存redis
##2.搜索功能
###elasticsearch
##3.点赞功能
###redis做缓存，mongodb做持久化，定时持久化数据
##4.博客内容以及评论
###mongodb
##5.




#备注随笔
##docker 命令
logstash : docker run -d --restart=always --privileged=true --name logstash -p 5044:5044 -p 5047:5047 -p 9600:9600 --network host -v /root/logstash/logstash-7.8.0/config/:/usr/share/logstash/config/ -v /root/logstash/logstash-7.8.0/data/:/usr/share/logstash/config/data
进入es
docker exec -it elasticsearch /bin/bash

##swagger访问路径 
http://localhost:30002/swagger-ui/index.html

##oauth2 
header
key:Authorization
value:bearer token

elasticsearch.yml配置文件设置开启x-pack插件
xpack.security.enabled: true 



设置es 密码： elasticsearch-setup-passwords interactive
默认账号：elastic

//修改elk密码
elk设置密码后，配置密码要记得都加双引号
kibana重启要修改配置文件的es账号密码，否则连接不上es

es和kibana 都用docker部署在106.53.237.234，原计划logstash也同样部署，后因为服务器内存原因，logstash普通方式部署到了128.1.49.194

###20210205
elk以及beatfile都配置完成



