# 网络讲解

## 三次握手及四次挥手

![image](https://github.com/yinbucheng/mypic/blob/master/tcp1.jpeg?raw=true)


![image](https://github.com/yinbucheng/mypic/blob/master/tcp2.jpeg?raw=true)


## 常见问题

```
1.出现大量TIME_WAIT
这种状态只会是客户端上面存在，也就是springcloud项目中http访问别人会出现，或者k8s上面的http，tcp探针，或者编写的爬虫项目会导致也就是主动发起http请求其他服务器

2.出现大量CLOSE_WAIT
这个状态是服务端出现的状态，是服务端接受到了客户端的FIN_1请求，但服务端未主动发送FIN请求也就是服务端没告诉客户端自己可以关闭了

3.TIME_WAIT默认是多久？
大概是1分钟左右
```