# 乐动指间
乐动指间是一款安卓平台的音乐游戏。就像他的名字一样，乐动指间的最终目的是能够使玩家感受到音符在指尖流淌的感觉。本游戏的开发背景是作者学校的大一年度项目，由作者带领的小组共同享有成果。

## 技术实现
* 本游戏基于Android开发，服务器采用Java编写，两者通讯通过局域网Socket完成。
* 由于游戏采用主动刷帧的形式所以调用到了OpenGL ES3.0对游戏界面进行绘制，其他工作界面的开发都是采用Activity+XML的形式完成。
* 客户端与服务端实现长连接，通过activity->Binder->service->socket->server->socket->service->broadcast->activity的方法完成信息交互。客户端发送心跳连接确定是否依然连接。
* 游戏界面特效基于OpenglES实现，其他部分的特效基于SurfaceView绘制实现。
* 游戏音源分别来自于FL的Piano Guitar Kick Bell，播放音乐使用SoundPool。
* 程序进行了比较详尽的错误处理，增强了游戏的健壮性。
* 调用实现的有：圆角Image，Loading图标，等待界面弹幕聊天室，Glide加载图片。

## 特效
* 游戏界面的大部分特效基于帧动画的原理，其中有点击红心、数字跳动、三角形烟花绽放、节奏型工具，钢琴按压等；另外还有滑块点击特效原理是通过编写相应的shader完成的。
* 多个Activity的背景被设置为了电流流动的特效，本特效的思路来源于BiliBili的弹幕聊天室，学习途径是一篇博客[这里]("")，在此基础上进行了重新封装，并编写了电流的路径文件。
* 欢迎界面的浮动音符，继承FrameLayout重写onDraw实现，其中不规则浮动用到了随机化与贝塞尔曲线，学习途径是一篇博客[这里]("")，再次基础上拓展了音符可以跟随触摸位置改变。

## 游戏流程

## TODO:
- [ ] 添加游戏帮助
- [ ] 游戏界面添加点击特效
- [ ] 欢迎界面添加按压彩色水纹效果
- [ ] 优化声音播放问题
- [ ] 拓展单人游戏玩法
- [ ] 修改游戏模式MVC->MVP/MVVC

## 开源技术
1) GLide
2) circleimageview
3) DanmakuFlameMaster
4) NavigationTabBar


## 关于作者
hahalidaxin
* [cnblogs]("http://www.cnblogs.com/lidaxin/")

> Since 2018/5/7
