// components/second.js
Component({

  /**
   * 组件的属性列表
   */
  properties: {
    title: {
      type: String,
      value: ''
    },
    barOperation: {
      type: Number,
      value: 0
    },

    //隐私协议授权栏信息
    privacyAuthInfo: {
      type: String,
      value: ''
    },

    privacyAuthShow: {
      type: String,
      value: ''
    },

    //控制状态栏与导航栏是否有背景色
    show_background_color: {
      type: String,
      value: '' // 默认无背景
    }

  },

  /**
   * 组件的初始数据
   */
  data: {
    statusBarHeight: 0,
    capsuleTop: 0,
    capsuleHeight: 0,

    animation_PopWindow_obj: null,//保存原始 动画对象
    animation_ScreenMaskOn_obj: null,//保存原始 动画对象
    // animation_PopWindow: {},
    animation_ScreenMaskOn: {},

    // animation_PopWindow_show: "",
    animation_ScreenMaskOn_show: "",

  },

  // 声明周期方法
  onLoad() {
    console.log("topNev onLoad trigger")
  },

  onReady() {
    console.log("topNev onReady trigger")
  },

  onshow() {
    console.log("topNev onshow trigger")
  },

  /**
   * 组件的方法列表
   */
  methods: {
    barOperation1: function () {
      console.log('topNev barOperation1 method call')
      console.log(this.properties.barOperation)
      if (this.properties.barOperation === 1) {
        wx.navigateBack({
          url: '/pages/functionPage/demo',
        })
      } else if (this.properties.barOperation === 2) {
        wx.navigateBack({
          url: '/pages/userPage/userPage',
        })
      } else {
        wx.navigateBack()
      }

    },

    cancelPrivacyAuth: function () {
      console.log("topNev cancelPrivacyAuth method onClick")

      const animation_ScreenMaskOn_obj = wx.createAnimation({
        duration: 200,
        timingFunction: 'linear',
      })

      animation_ScreenMaskOn_obj.opacity(0.5).step()

      this.setData({
        // animation_PopWindow: animation_PopWindow_obj.export(),
        animation_ScreenMaskOn: animation_ScreenMaskOn_obj.export(),

        // animation_PopWindow_obj : animation_PopWindow_obj,
        animation_ScreenMaskOn_obj: animation_ScreenMaskOn_obj,

        // animation_PopWindow_show: "show",
        animation_ScreenMaskOn_show: "show",

      })

      var that = this

      wx.showModal({
        title: '温馨提示',
        content: '尊敬的用户，撤回隐私协议的授权，将导致小程序退出登录并且无法使用账户分析功能，请您知晓',
        cancelText: '下次再说',
        confirmText: '撤回授权',
        success: function (res) {
          if (res.confirm) {//这里是点击了确定以后
            const { animation_ScreenMaskOn_obj } = that.data
            animation_ScreenMaskOn_obj.opacity(0).step()

            that.setData({
              animation_screenMaskON: animation_ScreenMaskOn_obj.export(),
            })

            setTimeout(() => {
              that.setData({
                animation_screenMaskON_show: "",
              }),
                wx.getStorage({
                  key: 'tradeAccountLoginState',
                  success(res) {
                    if (res.data == 0 || res.data == 1) {
                      console.log("cancelPrivacyAuth get tradeAccountLoginState:", res.data)
                      wx.getStorage({
                        key: 'jwt_token_expiration_time',
                        success(res) {
                          console.log("onReady-成功获取jwt_token_expiration_time：" + res.data)
                          const jwt_currentTime = new Date();
                          const jwt_expiration = new Date(res.data);
                          if (jwt_currentTime < jwt_expiration) {
                            wx.getStorage({
                              key: 'jwt_token',
                              success(res) {
                                wx.request({
                                  url: 'https://bill.thqh.com.cn/api-wx/users/tradeaccount',
                                  // url: 'https://service.thqh.com.cn/api-wx/users/tradeaccount',
                                  header: {
                                    'Authorization': 'Bearer ' + res.data,
                                    'content-type': 'application/json'
                                  },
                                  method: 'DELETE',
                                  timeout: 6000,
                                  success: function (response) {
                                    console.log("log33:")
                                    console.log(response)
                                    if (response.data.code == 0) {
                                      wx.hideLoading()
                                      wx.setStorage({
                                        key: 'tradeAccountLoginState',
                                        data: 1//1-交易未登录
                                      })
                                      wx.setStorage({
                                        key: "disclaimer_storage",
                                        data: 0,
                                      })
                                    } else {
                                      wx.hideLoading()
                                      wx.showToast({
                                        title: '退出交易登录请求失败，请重试！',
                                        icon: 'none',
                                        duration: 2000//持续的时间
                                      })
                                    }
                                  },

                                })
                              }
                            })
                            wx.showLoading({
                              title: '加载中', // 提示的内容
                              mask: true // 是否显示透明蒙层，防止触摸穿透
                            });
                            wx.clearStorage({
                              success: function () {
                                console.log("onLoad-已清除缓存，请重新登录小程序")
                                wx.reLaunch({
                                  url: '/pages/wxLoginPage/wxLoginPage',
                                });
                              }
                            })
                          } else {
                            wx.clearStorage({
                              success: function () {
                                console.log("onLoad-已清除缓存，请重新登录小程序")
                                wx.reLaunch({
                                  url: '/pages/wxLoginPage/wxLoginPage',
                                });
                              }
                            })
                          }
                        }
                      })
                    }
                  }
                })
            }, 400)

          } else {//这里是点击了取消以后
            console.log('用户点击取消')
          }
        }
      })

    },
  },

  lifetimes: {
    attached: function () {
      this.setData({
        statusBarHeight: wx.getWindowInfo().statusBarHeight, //状态栏的高度 
        capsuleTop: wx.getMenuButtonBoundingClientRect().top, //胶囊顶部到导航栏顶部的距离差
        capsuleHeight: wx.getMenuButtonBoundingClientRect().height, //胶囊的高度
      });
    }
  }
})