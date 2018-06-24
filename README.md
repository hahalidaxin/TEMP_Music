# 乐动指间
乐动指间是一款安卓平台的音乐游戏。就像他的名字一样，乐动指间的最终目的是能够使玩家感受到音符在指尖流淌的感觉。本游戏的开发背景是作者学校的大一年度项目，由作者带领的小组共同享有成果。

## 技术实现
* 本游戏基于Android开发，服务器采用Java编写，两者通讯通过局域网Socket完成。
* 由于游戏采用主动刷帧的形式所以调用到了OpenGL ES3.0对游戏界面进行绘制，其他工作界面的开发都是采用Activity+XML的形式完成。
* 客户端与服务端实现长连接，通过activity->Binder->service->socket->server->socket->service->broadcast->activity的方法完成信息交互。客户端发送心跳连接确定是否依然连接。
* 游戏界面特效基于OpenglES实现，其他部分的特效基于SurfaceView绘制实现。
* 游戏音源分别来自于FL的Piano Guitar Kick Bell，播放音乐使用SoundPool。
* 程序进行了比较详尽的错误处理，增强了游戏的健壮性。
* 调用实现的有：圆角Image，Loading图标，等待界面弹幕聊天室，Glide加载图片、NavigationTabBar主选择界面。

## 特效
* 游戏界面的大部分特效基于帧动画的原理，其中有点击红心、数字跳动、三角形烟花绽放、节奏型工具，钢琴按压等；另外还有滑块点击特效原理是通过编写相应的shader完成的。
* 多个Activity的背景被设置为了电流流动的特效，本特效的思路来源于BiliBili的弹幕聊天室，学习途径是一篇博客[这里]("")，在此基础上进行了重新封装，并编写了电流的路径文件。
* 欢迎界面的浮动音符，继承FrameLayout重写onDraw实现，其中不规则浮动用到了随机化与贝塞尔曲线，学习途径是一篇博客[这里]("")，再次基础上拓展了音符可以跟随触摸位置改变。
* 其他诸如控件的透明度变化、控件的位移、EditText的抖动、手机屏幕翻转提示等都是基于XML实现的补间动画。

## 游戏流程

## 界面展示

<table>
  <tr>
    <td>
      <a><img           src="https://github.com/hahalidaxin/TEMP_Music/blob/master/IMGforREADME/%E5%8D%95%E4%BA%BA%E6%B8%B8%E6%88%8F%E6%9B%B2%E7%9B%AE%E9%80%89%E6%8B%A9%E7%95%8C%E9%9D%A2.jpg" width = "270" height = "480" alt="图片名称" style="display:inline-block" /></a>
    </td>
    <td>  
      <a><img src="https://github.com/hahalidaxin/TEMP_Music/blob/master/IMGforREADME/%E6%B8%B8%E6%88%8F%E8%BF%9B%E8%A1%8C%E7%95%8C%E9%9D%A2.jpg" width = "270" height = "480" alt="图片名称" style="display:inline-block"  /></a>
    </td>
    <td>  
      <a><img src="https://github.com/hahalidaxin/TEMP_Music/blob/master/IMGforREADME/%E5%8D%95%E4%BA%BA%E6%B8%B8%E6%88%8F%E7%BB%93%E6%9D%9F%E6%8E%A5%E7%95%8C%E9%9D%A2.jpg" width = "270" height = "480" alt="图片名称"  /></a>
    </td>
  </tr>
  <tr>
    <td>
      <a><img src="https://github.com/hahalidaxin/TEMP_Music/blob/master/IMGforREADME/%E6%8E%92%E8%A1%8C.jpg" width = "270" height = "480" alt="图片名称"  /> </a>
    </td>
    <td>
      <a><img src="https://github.com/hahalidaxin/TEMP_Music/blob/master/IMGforREADME/%E5%A4%9A%E4%BA%BA.jpg" width = "270" height = "480" alt="图片名称"  /> </a>
    </td>
    <td>
      <a><img src="https://github.com/hahalidaxin/TEMP_Music/blob/master/IMGforREADME/%E5%88%9B%E5%BB%BA%E6%88%BF%E9%97%B4.jpg" width = "270" height = "480" alt="图片名称"  /> </a>
    </td>
  </tr>
  <tr>
    <td>
      <a><img src="https://github.com/hahalidaxin/TEMP_Music/blob/master/IMGforREADME/%E5%A4%9A%E4%BA%BA%E6%BC%94%E5%A5%8F%E9%80%89%E6%8B%A9%E6%9B%B2%E7%9B%AE.jpg" width = "270" height = "480" alt="图片名称"  /> </a>
    </td>
    <td>
      <a><img src="https://github.com/hahalidaxin/TEMP_Music/blob/master/IMGforREADME/%E5%BC%B9%E5%B9%95%E8%81%8A%E5%A4%A9%E7%AD%89%E5%BE%85%E5%AE%A4.jpg" width = "270" height = "480" alt="图片名称"  /> </a>
    </td>
    <td>
      <a><img src="https://github.com/hahalidaxin/TEMP_Music/blob/master/IMGforREADME/%E8%B0%B1%E6%9B%B2%E7%95%8C%E9%9D%A2.jpg" width = "270" height = "480" alt="图片名称"  /> </a>
    </td>
  </tr>
  
  <tr>
    <td>
      <a><img src="https://github.com/hahalidaxin/TEMP_Music/blob/master/IMGforREADME/%E8%B0%B1%E6%9B%B2%E4%B8%8A%E4%BC%A0%E7%95%8C%E9%9D%A2.jpg" width = "270" height = "480" alt="图片名称"  /> </a>
    </td>
    <td>
      <a><img src="https://github.com/hahalidaxin/TEMP_Music/blob/master/IMGforREADME/%E6%B8%B8%E6%88%8F%E5%A3%B0%E6%98%8E.jpg" width = "270" height = "480" alt="图片名称"  /></a>
    </td>
    <td>
      <a><img src="https://github.com/hahalidaxin/TEMP_Music/blob/master/IMGforREADME/%E8%AE%BE%E7%BD%AE%E7%95%8C%E9%9D%A2.jpg" width = "270" height = "480" alt="图片名称"  /></a>
    </td>
  </tr>
</table>

## TODO:
- [ ] 添加游戏帮助
- [ ] 增加可以使用的音调
- [ ] 游戏界面添加点击特效
- [ ] 欢迎界面添加按压彩色水纹效果
- [ ] 优化声音播放问题
- [ ] 拓展单人游戏玩法
- [ ] 修改游戏模式MVC->MVP/MVVC

## 开源技术
1) GLide
2) Circleimageview
3) DanmakuFlameMaster
4) NavigationTabBar
5) Phoenix Pull-to-Refresh
6) CircleButton

## 免责声明
游戏纯属个人产品，不用于任何商业用途
<br>本游戏的开发灵感来自于音乐游戏《钢琴键》，采用了游戏中音乐列表的侧边图片，如有侵权，将立即删除。

## 关于作者
hahalidaxin
* [cnblogs]("http://www.cnblogs.com/lidaxin/")

> Since 2018/5/7
