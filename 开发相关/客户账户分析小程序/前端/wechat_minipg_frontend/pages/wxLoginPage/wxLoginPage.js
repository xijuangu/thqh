// pages/wxLoginPage/wxLoginPage.js
const defaultAvatarUrl = 'https://mmbiz.qpic.cn/mmbiz/icTdbqWNOwNRna42FI242Lcia07jQodd2FJGIYQfG0LAJGFxM4FbnQP6yfMxBgJ0F3YRqJCJ1aPAK2dQagdusBZg/0'

Page({

  /**
   * 页面的初始数据
   */
  data: {
    loginStatus: 0, //登录状态，默认为0

    //用户信息
    avatarUrl: defaultAvatarUrl,
    usrWXName: "",

    //提交按钮
    disable: "disable",

    //动画
    Show: 0,
    bottomPopAnimation: {},
    maskAnimation: {},

    //隐私政策确认
    privacyAuth: "non-active", //用于控制隐私政策确认按钮勾选状态下的背景颜色
    privacyAuth_isChecked: false, //绑定checkbox的checked属性来定义勾选框的默认勾选状态
  },

  wxlogin: function () {
    var that = this

    if (that.data.privacyAuth_isChecked) {
      that.setData({
        Show: 1
      })
      that.maskAnimation.opacity(0.5).step()
      that.bottomPopAnimation.translateY(0).step()

      setTimeout(function () {
        that.setData({
          maskAnimation: that.maskAnimation.export(),
          bottomPopAnimation: that.bottomPopAnimation.export(),
        })
      }, 200)
    }
    else {
      wx.showToast({
        title: '请阅读并同意用户隐私政策',
        icon: 'none',
        duration: 1300//持续的时间
      })
    }
  },

  getphonenumber: function (e) {
    console.log("动态令牌" + e.detail.code)
    console.log("回调信息（成功失败都会返回）" + e.detail.errMsg)


    wx.request({
      url: 'https://bill.thqh.com.cn/api-wx/users/auth',
      // url: 'https://service.thqh.com.cn/api-wx/users/auth',
      method: 'POST',
      data: {
        'code': e.detail.code,
        'type': 'phone_number'
      },
      header: {
        'content-type': 'application/json'
      },
      enableProfile: true,
      success: function (res) {
        console.log("log10:手机号授权登录接口调用成功，数据返回：")
        console.log(res)
        //存储接口调用凭证jwt_token
        wx.setStorage({
          key: "jwt_token",
          data: res.data.data.jwt_token
        })

        //存储用户头像，由于是手机号登录，使用默认头像，进入主界面后调用获取用户信息接口返回的数据没有头像信息
        wx.setStorage({
          key: "usrWXPofile",
          data: defaultAvatarUrl
        })

        //存储用户手机号，用于前台用户名称显示
        wx.setStorage({
          key: "phone_number",
          data: res.data.data.phone_number
        })

        wx.setStorage({
          key: "usrWXName",
          data: res.data.data.phone_number
        })

        wx.setStorage({
          key: "jwt_token_expiration_time",
          data: res.data.data.JWT_expiration_time
        })

        wx.setStorage({
          key: "disclaimer_storage",
          data: 0,
        })

        wx.redirectTo({
          url: '/pages/functionPage/demo',
        })


      },
      fail: function (error) {
        console.log(error)
        wx.showToast({
          title: '未知错误X',
          icon: 'error',
          duration: 2000
        })
      }

    })


  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad(options) {
    wx.showShareMenu({
      withShareTicket: true,
      menus: ["shareAppMessage", "shareTimeline"]
    })
    let that = this;
    wx.getStorage({
      key: "jwt_token",
      success(res) {
        if (res.data != null) {
          console.log("满足条件跳转")
          wx.redirectTo({
            url: '/pages/functionPage/demo',
          })
        } else {
          console.log("没有jwt_token")
        }
      },
      fail() {
        // wx.redirectTo({
        //   url: '/pages/wxLoginPage/wxLoginPage',
        // })
      }
    });
    // console.log(this.data.loginStatus)
  },

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady() {
    // console.log("onReady--动画设置")
    this.maskAnimation = wx.createAnimation({
      duration: 200,
      timingFunction: 'linear',
    }),
      this.bottomPopAnimation = wx.createAnimation({
        duration: 200,
        timingFunction: 'linear',
      })
  },

  onChooseAvatar(e) {
    const { avatarUrl } = e.detail
    this.setData({
      avatarUrl,
    })

    var that = this
    if (this.data.usrWXName == "") {
      console.log("name is empty")
    } else {
      console.log("name is not empty")
      if (that.data.avatarUrl == defaultAvatarUrl) {
        console.log("profile is empty")
        return
      } else {
        console.log("profile is not empty")
        that.setData({
          disable: ""
        })
      }
    }

  },

  getInputValue(e) {
    // console.log(e.detail.value)
    var that = this
    this.setData({
      usrWXName: e.detail.value
    })
    if (this.data.avatarUrl == defaultAvatarUrl) {
      console.log("profile is empty")
    } else {
      console.log("profile is not empty")
      if (that.data.usrWXName == "") {
        console.log("name is empty")
        that.setData({
          disable: "disable",
          usrWXName: e.detail.value
        })
        return;
      } else {
        console.log("name is not empty")
        that.setData({
          disable: ""
        })
      }

    }
  },

  closeBottomPop: function () {
    console.log("closeBottomPopFunction")
    this.maskAnimation.opacity(0).step()
    this.bottomPopAnimation.translateY(650 + 'rpx').step()
    this.setData({
      maskAnimation: this.maskAnimation.export(),
      bottomPopAnimation: this.bottomPopAnimation.export(),
    })

    var that = this
    setTimeout(function () {
      that.setData({
        Show: 0,
      })
      wx.setStorage({
        key: "usrWXPofile",
        data: defaultAvatarUrl,
      })
      wx.setStorage({
        key: "usrWXName",
        data: "",
      })
    }, 200)

  },

  test: function () {
    console.log("testFunction")
    this.setData({
      Show: 1
    })
    this.maskAnimation.opacity(0.5).step()
    this.bottomPopAnimation.translateY(0).step()
    var that = this
    setTimeout(function () {
      that.setData({
        maskAnimation: that.maskAnimation.export(),
        bottomPopAnimation: that.bottomPopAnimation.export(),
      })
    }, 200)
  },

  submit: function () {
    var that = this
    if (this.data.disable == "") {
      wx.setStorage({
        key: "usrWXPofile",
        data: that.data.avatarUrl,
      })
      wx.setStorage({
        key: "usrWXName",
        data: that.data.usrWXName,
        success() {
          wx.login({
            success: (res) => {
              console.log(res.code)
              console.log(res)
              wx.request({
                url: 'https://bill.thqh.com.cn/api-wx/users/auth',
                // url: 'https://service.thqh.com.cn/api-wx/users/auth',
                method: 'POST',
                data: {
                  'code': res.code,
                  'type': 'openid'
                },
                header: {
                  'content-type': 'application/json'
                },
                success: function (res) {
                  console.log("log10:手机号授权登录接口调用成功，数据返回：")
                  console.log(res)
                  if (res.data.code == 0) {
                    //存储接口调用凭证jwt_token
                    wx.setStorage({
                      key: "jwt_token",
                      data: res.data.data.jwt_token
                    })
                    wx.setStorage({
                      key: 'usrWXName',
                      data: that.data.usrWXName
                    })
                    wx.setStorage({
                      key: 'avatarUrl',
                      data: that.data.avatarUrl
                    })

                    wx.setStorage({
                      key: "jwt_token_expiration_time",
                      data: res.data.data.JWT_expiration_time
                    })

                    wx.setStorage({
                      key: "disclaimer_storage",
                      data: 0,
                    })

                    wx.redirectTo({
                      url: '/pages/functionPage/demo',
                    })
                  } else {
                    wx.showToast({
                      title: '请求失败',
                      icon: 'error',
                      duration: 2000
                    })
                  }
                },
                fail: function (error) {
                  console.log(error)
                  wx.showToast({
                    title: '未知错误X',
                    icon: 'error',
                    duration: 2000
                  })
                }
              })
            },
          })
        }
      })

    }

  },


  HandelPrivacyCheck: function (e) {
    console.log('wxLoginPage HandelPrivacyCheck call')

    var that = this

    if (!that.data.privacyAuth_isChecked) {
      that.setData({
        privacyAuth: 'active'
      })
      that.setData({
        privacyAuth_isChecked: true
      })
    } else {
      that.setData({
        privacyAuth: 'non-active'
      })
      that.setData({
        privacyAuth_isChecked: false
      })
    }
  },

  phoneNumberLogin_onbClick: function () {
    console.log('wxLoginPage phoneNumberLogin_onbClick call')
    var that = this
    if (that.data.privacyAuth_isChecked) {

    } else {
      wx.showToast({
        title: '请阅读并同意用户隐私政策',
        icon: 'none',
        duration: 1300//持续的时间
      })
    }

  },

  toPrivacyPage: function (e) {
    console.log('wxLoginPage toPrivacyPage call')
    console.log(e.currentTarget.dataset.type)
    var privacyType = e.currentTarget.dataset.type
    var privacyAuthShow = 'non-show'
    var barOperation = '3'
    if (privacyType == 1) {
      wx.navigateTo({
        url: '/pages/appPrivacyPolicyPage/appPrivacyPolicyPage?privacyAuthShow=' + privacyAuthShow + '&barOperation=' + barOperation,
      })
    } else if (privacyType == 2) {
      wx.navigateTo({
        url: '/pages/UserInfoColletListPage/UserInfoColletListPage?privacyAuthShow=' + privacyAuthShow + '&barOperation=' + barOperation,
      })
    } else if (privacyType == 3) {
      wx.navigateTo({
        url: '/pages/userInfoThirdPartyPolicyPage/userInfoThirdPartyPolicyPage?privacyAuthShow=' + privacyAuthShow + '&barOperation=' + barOperation,
      })
    } else if (privacyType == 4) {
      wx.navigateTo({
        url: '/pages/loginPolicyPage/loginPolicyPage?privacyAuthShow=' + privacyAuthShow + '&barOperation=' + barOperation,
      })
    }
  }



})