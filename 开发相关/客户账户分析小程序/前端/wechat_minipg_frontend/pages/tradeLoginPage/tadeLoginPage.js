// pages/tradeLoginPage/tadeLoginPage.js
Page({

  /**
   * 页面的初始数据
   */
  data: {
    hidePW: false,
    showPW: true,
    passWordType: true,
    warnTextShow: 0,
    warnText1: true,
    warnText2: true,
    warnText3: true,
    warnText4: true,
    animation_warnText: {},
    mask: 'off',
    // 存储从后端生成的公钥
    rsaPublicKeyBase64: 'MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA7I0T5DQK66ZidzR25ZkiICR90/5aqo2lzJOtj+gQCwqhKvr55ltL5+C6eu319jIWea9gl40vTKNqsfhWmyOUEo3CwcHYqUJ0Y5cZmhD61T7ADSLo56JySJ3HChcaCxRt4j2dsurLb2h8SvN/zsEgwXEFFXTXB+WaXsMikRjeFUUC4BYLEBejERZNZlSltEZMih97QdgJVKNCMW1mcixg8OeZNz+Gx/vQq8w92mwU/i1HbuDa/awJYJMs+svDa7m/wz288QN6eZMkCC31k1WQIAU0894t3pxp4ZXxnbw3RNcr2AjGb0xdhACkV5W/a3toeUsryKP8tEErDBP9fIPfYQIDAQAB',
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad(options) {
    // 禁用“分享给朋友”和“分享到朋友圈”
    wx.hideShareMenu({
      menus: ["shareAppMessage", "shareTimeline"]
    });
  },

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady() {
    this.animation_warnText = wx.createAnimation({
      duration: 1500,
      timingFunction: 'ease',
    })
  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow() {

  },

  /**
   * 生命周期函数--监听页面隐藏
   */
  onHide() {

  },

  /**
   * 生命周期函数--监听页面卸载
   */
  onUnload() {

  },

  /**
   * 页面相关事件处理函数--监听用户下拉动作
   */
  onPullDownRefresh() {

  },

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom() {

  },

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage() {

  },

  showPW: function () {
    this.setData({
      passWordType: !this.data.passWordType,
      hidePW: !this.data.hidePW,
      showPW: !this.data.showPW,
    })
  },

  hidePW: function () {
    this.setData({
      passWordType: !this.data.passWordType,
      hidePW: !this.data.hidePW,
      showPW: !this.data.showPW,
    })
  },

  tradeAcoountLogin: function (data) {
    // 获取表单输入的值
    const accountnumber = data.detail.value.accountnumber;
    const accountpassword = data.detail.value.accountpassword;
    

    var that = this
    // 检查账号是否为空
    if (!accountnumber) {
      this.setData({
        warnTextShow: 1,
        warnText1: false,//交易账号为空，显示
        warnText2: true,
        warnText3: true,
        warnText4: true,
      })
      this.animation_warnText.opacity(0.5).step()
      this.animation_warnText.opacity(0).step()
      setTimeout(function () {
        that.setData({
          animation_warnText: that.animation_warnText.export(),
        })
      }, 100)
      return;
    } else if (!accountpassword) {//密码为空
      this.setData({
        warnTextShow: 1,
        warnText1: true,
        warnText2: false,
        warnText3: true,
        warnText4: true,
      })
      this.animation_warnText.opacity(0.5).step()
      this.animation_warnText.opacity(0).step()
      setTimeout(function () {
        that.setData({
          animation_warnText: that.animation_warnText.export(),
        })
      }, 100)
      return;
    }

    const encryptor = require('../../utils/rsaEncrypt'); //获取工具类中的rsaEncrypt.js

    wx.getStorage({
      key: 'jwt_token',
      success(res) {
        wx.request({
          url: 'https://bill.thqh.com.cn/api-wx/users/tradeaccount/login',
          // url: 'https://service.thqh.com.cn/api-wx/users/tradeaccount/login',
          data: {
            trade_account: accountnumber,
            password: encryptor.encryptWithRsa(accountpassword,that.data.rsaPublicKeyBase64),
            client_ip: "188.2.2.2"
          },
          method: 'POST',
          header: {
            'content-type': 'application/json',
            'Authorization': 'Bearer ' + res.data
          },
          timeout: 6000,
          success: function (res) {
            // console.log("log18:交易账号认证接口调用成功")
            // console.log(res)
            if (res.data.code == 0) {
              wx.setStorage({
                key: "tradeAccountLoginState",
                data: res.data.code,
              })
              wx.setStorage({
                key: "disclaimer_storage",
                data: 0,
              })
              wx.hideLoading()
              wx.showToast({
                title: '登录成功，即将跳转',
                duration: 1000,
                icon: "none"
              })
              setTimeout(function () {
                wx.reLaunch({
                  url: '/pages/functionPage/demo',
                })
              }, 900)
            } else if (res.data.code == 2004) {
              wx.hideLoading()
              wx.setStorage({
                key: "tradeAccountLoginState",
                data: 1,//记得改回1
              })
              that.setData({
                mask: 'off'
              })
              that.setData({
                warnTextShow: 1,
                warnText1: true,
                warnText2: true,
                warnText3: false,
                warnText4: true,
              })
              that.animation_warnText.opacity(0.5).step()
              that.animation_warnText.opacity(0).step()
              setTimeout(function () {
                that.setData({
                  animation_warnText: that.animation_warnText.export(),
                })
              }, 100)
            } else {
              wx.hideLoading()
              wx.setStorage({
                key: "tradeAccountLoginState",
                data: 1,//记得改回1
              })
              that.setData({
                mask: 'off'
              })
              that.setData({
                warnTextShow: 1,
                warnText1: true,
                warnText2: true,
                warnText3: true,
                warnText4: false,
              })
              that.animation_warnText.opacity(0.5).step()
              that.animation_warnText.opacity(0).step()
              setTimeout(function () {
                that.setData({
                  animation_warnText: that.animation_warnText.export(),
                })
              }, 100)
            }
          },
          fail: function () {
            wx.hideLoading()
            that.setData({
              mask: 'off'
            })
            wx.showToast({
              title: '请求超时',
              icon: 'error',
              duration: 1000,
            })
          }
        })
        wx.showLoading({
          title: '加载中',
        })
        that.setData({
          mask: 'on'
        })
      }
    })

  }
})