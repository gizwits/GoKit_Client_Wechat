# GoKit_Client_Wechat

* 这是一款接入微信公众号的开源代码示例 APP，可以帮助开发者快速入手，使用公众号来控制机智云的物联网设备。

* 该例子针对的是微信公众号控制机智云的设备。

## 功能介绍

 Gkit Client Wechat 主要展示如何使用 OpenApi 和 Websocket ，开发微信公众号控制机智云设备。项目中用到了大部分主要 OpenApi 接口，供使用 Web 的开发者参考。主要功能如下：
> 1. 微信宠物屋的控制设备功能
> 2. 微信宠物屋的显示温度、湿度功能
> 3. 微信宠物屋的接入微信流程
	

* 如果开发者希望开发的设备与以上功能类似，可参考或直接使用该 J2EE 进行修改进行快速开发自己的智能公众号。

* 另外，因为该项目并没有相对应的实体硬件设备供开发者使用，因此还提供了扫描虚拟设备功能，通过扫描机智云实验室内相对应的虚拟设备，可进行设备的绑定和控制等功能。同时可免费申请 GoKit 进行设备的配置入网和绑定等流程。

## 项目依赖和安装

1. 需要 Jdk8 版本，需要 tomcat8 运行.
2. 使用 Msql 数据库.
3. 使用的是 servlet + jsp 的框架来编写该J2EE.

## 硬件依赖

Gizwits Light 项目调试，需要有调试设备的支持，您可以使用虚拟设备或者实体设备搭建调试环境。

* 虚拟设备

>机智云官网提供 GoKit 虚拟设备的支持，链接地址：[http://site.gizwits.com/zh-cn/developer/product/631/detail](http://site.gizwits.com/zh-cn/developer/product/631/detail)

* 实体设备

>GoKit 开发板。您可以在机智云官方网站上免费预约申请（限量10000台），申请地址：
[http://gizwits.com/zh-cn/gokit](http://gizwits.com/zh-cn/gokit)
        
>GoKit 开发板提供 MCU 开源代码供智能硬件设计者参考，请去此处下载：
[https://github.com/gizwits/gokit-mcu](https://github.com/gizwits/gokit-mcu)

## 问题反馈

您可以给机智云的技术支持人员发送邮件，反馈您在使用过程中遇到的任何问题。

邮箱：[janel@gizwits.com](mailto:janel@gizwits.com?subject=Gokit微信端技术问题)
