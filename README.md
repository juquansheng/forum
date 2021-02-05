# forum

##docker 命令
logstash : docker run -d --restart=always --privileged=true --name logstash -p 5044:5044 -p 5047:5047 -p 9600:9600 --network host -v /root/logstash/logstash-7.8.0/config/:/usr/share/logstash/config/ -v /root/logstash/logstash-7.8.0/data/:/usr/share/logstash/config/data
进入es
docker exec -it elasticsearch /bin/bash



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

