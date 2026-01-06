// pages/demo/demo.js
const query = wx.createSelectorQuery();
const tradeAccountStateMark = query.select('.tradeAccountStateMark');
const defaultAvatarURL = '/pages/images/profile.png'
Page({

  /**
   * 页面的初始数据
   */
  data: {
    tradeAccountStateMark: "tradeAccountStateMark",//功能界面的交易账号登录状态变更
    tradeAccountStateMarkContent: "交易账号未登录",

    show: "",//控制遮罩与弹窗所在主控件的层级关系，通过wxss设置z-index进行控制
    menu_show: "",//控制趋势分析菜单是否显示
    disclaimer_show: "",

    animation_popWindow_obj: null, // 新增：保存原始动画对象
    animation_screenMaskON_obj: null,
    animation_screenMaskON: {},
    animation_popWindow: {},

    content: "",
    userPageTitle: "",
    UserName: "",
    avatarUrl: defaultAvatarURL,
    displayUserName: "暂未设置昵称", // 新增用于展示处理后用户名的字段

    flag: false //用于避免onLoad和onReady同时调用disclaimer_popup
  },


  attached: function () {
    console.log("attached");
    this.setData({
      UserName: "admin",
    })
  },
  /**
* 生命周期函数--监听页面显示
*/
  onLoad() {
    console.log("onLoad")
    // 禁用“分享给朋友”和“分享到朋友圈”
    wx.hideShareMenu({
      menus: ["shareAppMessage", "shareTimeline"]
    });
    var that = this

    wx.getStorage({
      key: 'jwt_token_expiration_time',
      success(res) {
        console.log("onLoad-成功获取jwt_token_expiration_time：" + res.data)
        const jwt_currentTime = new Date();
        const jwt_expiration = new Date(res.data);
        if (jwt_currentTime < jwt_expiration) {
          console.log("onLoad-判断currentTime < jwt_expiration")
          wx.getStorage({
            key: 'jwt_token',
            success(res) {
              console.log("onLoad-成功获取jwt_token")
              wx.request({
                url: 'https://bill.thqh.com.cn/api-wx/users/tradeaccount',
                // url: 'https://service.thqh.com.cn/api-wx/users/tradeaccount',
                method: 'GET',
                header: {
                  'Authorization': 'Bearer ' + res.data
                },
                success: function (response) {
                  console.log(response)
                  //判断获取到的expiration_time，
                  const currentTime = new Date();

                  const expiration = new Date(response.data.data.expiration_time);

                  const trade_accountBinding = response.data.data.trade_account

                  if (trade_accountBinding == null) {
                    console.log("onLoad-判断交易账号未绑定")
                    wx.setStorage({
                      key: 'tradeAccountLoginState',
                      data: 1
                    })

                    that.setData({
                      tradeAccountStateMark: "tradeAccountStateMark",
                      tradeAccountStateMarkContent: "交易账号未登录"
                    })
                  } else {
                    if (currentTime < expiration) {
                      console.log("onLoad-判断交易登录状态未过期")
                      wx.setStorage({
                        key: 'tradeAccountLoginState',
                        data: 0 //已登录
                      })
                      that.setData({
                        tradeAccountStateMark: "tradeAccountStateMarkActive",
                        tradeAccountStateMarkContent: "交易账号已登录"
                      })



                      wx.getStorage({
                        key: 'disclaimer_storage',
                        success(res) {
                          console.log('onLoad-成功获取Storage：disclaimer_storage')
                          if (res.data == 0) {
                            console.log('onLoad-判断disclaimer_storage=,展示重要声明')
                            that.setData({
                              flag: true
                            })
                            that.disclaimer_popup()
                          }
                        }
                      })

                    } else if (currentTime >= expiration) {
                      console.log("onLoad-判断交易登录状态已过期")
                      wx.setStorage({
                        key: 'tradeAccountLoginState',
                        data: 1 //未登录
                      })
                      that.setData({
                        tradeAccountStateMark: "tradeAccountStateMark",
                        tradeAccountStateMarkContent: "交易账号未登录"
                      })
                    }
                  }
                }
              })
            }
          })
        } else if (jwt_currentTime >= jwt_expiration) {
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


  },

  onReady: function () {
    console.log("onReady")
    //用户名称和用户头像
    var that = this
    wx.getStorage({
      key: 'jwt_token_expiration_time',
      success(res) {
        console.log("onReady-成功获取jwt_token_expiration_time：" + res.data)
        const jwt_currentTime = new Date();
        const jwt_expiration = new Date(res.data);
        if (jwt_currentTime < jwt_expiration) {
          console.log("onReady-判断currentTime < jwt_expiration")
          //设置用户头像
          wx.getStorage({
            key: 'usrWXPofile',
            success(res) {
              console.log('onReady-成功获取Storage用户头像')
              that.setData({
                avatarUrl: res.data
              })
            }
          }),

            //设置显示的用户名称
            wx.getStorage({
              key: 'usrWXName',
              success(res) {
                if (res.data.length >= 11) {
                  console.log('onReady-成功获取Storage用户名')
                  const front = res.data.slice(0, 3)
                  const end = res.data.slice(-4)
                  const displayUserName_ = front + '****' + end
                  that.setData({
                    displayUserName: displayUserName_
                  })
                  wx.setStorage({
                    key: 'usrWXName',
                    data: displayUserName_
                  })
                } else {
                  that.setData({
                    displayUserName: res.data
                  })
                }
              }
            })

          //设置重要提示
          wx.getStorage({
            key: 'tradeAccountLoginState',
            success(res) {
              console.log('onReady-成功获取Storage：tradeAccountLoginState')
              if (res.data == 0) {
                wx.getStorage({
                  key: 'disclaimer_storage',
                  success(res) {
                    console.log('onReady-成功获取Storage：disclaimer_storage')
                    if (res.data == 0 && (!that.data.flag)) {
                      console.log('onReady-判断disclaimer_storage=0且onLoad未调用popup,展示重要声明')
                      that.disclaimer_popup()
                    }
                  }
                })
              }
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




  },


  onShow: function () {
    console.log("onShow")
    //设置动画
    console.log("onShow-配置动画")
    // 创建并保存原始动画对象
    const animation_popWindow_obj = wx.createAnimation({
      duration: 200,
      timingFunction: 'linear',
    })
    const animation_screenMaskON_obj = wx.createAnimation({
      duration: 200,
      timingFunction: 'linear',
    })

    this.setData({
      animation_popWindow_obj: animation_popWindow_obj,
      animation_screenMaskON_obj: animation_screenMaskON_obj
    })

    var that = this
    wx.getStorage({
      key: 'jwt_token_expiration_time',
      success(res) {
        console.log("onReady-成功获取jwt_token_expiration_time：" + res.data)
        const jwt_currentTime = new Date();
        const jwt_expiration = new Date(res.data);
        if (jwt_currentTime < jwt_expiration) {
          wx.getStorage({
            key: 'tradeAccountLoginState',
            success(res) {
              console.log('onShow-成功获取Storage：tradeAccountLoginState')
              if (res.data == 0) {
                console.log('onShow-判断成功获取Storage：tradeAccountLoginState=0,设置tradeAccountStateMark=交易账号已登录')
                that.setData({
                  tradeAccountStateMark: "tradeAccountStateMarkActive",
                  tradeAccountStateMarkContent: "交易账号已登录"
                })
              } else if (res.data == 1) {
                console.log('onShow-判断成功获取Storage：tradeAccountLoginState=1,设置tradeAccountStateMark=交易账号未登录')
                that.setData({
                  tradeAccountStateMark: "tradeAccountStateMark",
                  tradeAccountStateMarkContent: "交易账号未登录"
                })
              }
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



  },


  toUserPage: function () {
    console.log('toUserPage')
    wx.navigateTo({
      url: '/pages/userPage/userPage',
    })
  },

  onClick: function () {
    this.setData({
      tradeAccountStateMark: "tradeAccountStateMarkActive",
    })

  },

  functionOnClickFirstCheck: function (BItableName, BIPageTitle) {
    console.log("functionOnClickFirstCheck", BItableName)
    wx.getStorage({
      key: 'jwt_token_expiration_time',
      success(res) {
        console.log("onReady-成功获取jwt_token_expiration_time：" + res.data)
        const jwt_currentTime = new Date();
        const jwt_expiration = new Date(res.data);
        if (jwt_currentTime < jwt_expiration) {
          wx.getStorage({
            key: 'tradeAccountLoginState',
            success(res) {
              if (res.data == 1) {
                wx.showModal({
                  title: '交易账号未登录',
                  content: '请先登录交易账号再进行后续操作',
                  complete: (res) => {
                    if (res.cancel) {

                    }
                    if (res.confirm) {
                      wx.navigateTo({
                        url: '/pages/tradeLoginPage/tadeLoginPage',
                      })
                    }
                  }
                })
              } else if (res.data == 0) {
                wx.navigateTo({
                  url: '/pages/otterLink/outterLink?tableName=' + BItableName + '&BIPageTitle=' + BIPageTitle,
                })
              }
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

  },

  function1OnClick: function () {
    this.functionOnClickFirstCheck('REPORT_ACCOUNT_FUND', '账户资金分析')
  },

  function2OnClick: function () {
    this.functionOnClickFirstCheck('REPORT_VARIETY_ANALYSIS', '品种交易偏好分析')
  },

  function3OnClick: function () {
    this.functionOnClickFirstCheck('REPORT_RISK_ANALYSIS', '风险度分析')
  },

  function4OnClick: function () {
    console.log("function4OnClick")

    const { animation_popWindow_obj, animation_screenMaskON_obj } = this.data
    animation_popWindow_obj.translateY(0).step()
    animation_screenMaskON_obj.opacity(0.5).step()

    this.setData({
      show: "show",
      menu_show: "show",
      disclaimer_show: "",
      animation_popWindow: animation_popWindow_obj.export(),
      animation_screenMaskON: animation_screenMaskON_obj.export(),
    })
  },


  cancleOnClick: function () {
    console.log("cancleOnClick")
    const { animation_popWindow_obj, animation_screenMaskON_obj } = this.data

    animation_popWindow_obj.translateY('570rpx').step() // 注意单位
    animation_screenMaskON_obj.opacity(0).step()

    this.setData({
      animation_popWindow: animation_popWindow_obj.export(),
      animation_screenMaskON: animation_screenMaskON_obj.export(),
    })

    setTimeout(() => {
      this.setData({
        show: "",
        menu_show: "",
        disclaimer_show: ""
      })
    }, 200)
  },

  SubFunction1OnClick: function () {
    this.functionOnClickFirstCheck('REPORT_MARKETVALUE_ANALYSIS', '市值权益曲线')
  },

  SubFunction2OnClick: function () {
    this.functionOnClickFirstCheck('REPORT_YIELD_ANALYSIS', '累计收益率曲线')
  },

  SubFunction3OnClick: function () {
    this.functionOnClickFirstCheck('REPORT_RETRACEMENT_ANALYSIS', '回撤率曲线')
  },

  disclaimer_popup: function () {
    console.log("disclaimer_popup")
    const { animation_popWindow_obj, animation_screenMaskON_obj } = this.data
    animation_popWindow_obj.translateY(0).step()
    animation_screenMaskON_obj.opacity(0.5).step()

    this.setData({
      show: "show",
      menu_show: "",
      disclaimer_show: "show",
      animation_popWindow: animation_popWindow_obj.export(),
      animation_screenMaskON: animation_screenMaskON_obj.export(),
    })
  },

  disclaimer_cancleOnClick: function () {
    console.log("disclaimer_cancleOnClick")
    const { animation_popWindow_obj, animation_screenMaskON_obj } = this.data

    animation_popWindow_obj.translateY('570rpx').step() // 注意单位
    animation_screenMaskON_obj.opacity(0).step()

    this.setData({
      animation_popWindow: animation_popWindow_obj.export(),
      animation_screenMaskON: animation_screenMaskON_obj.export(),
    })

    setTimeout(() => {
      this.setData({
        show: "",
        menu_show: "",
        disclaimer_show: ""
      })
    }, 200)

    wx.setStorage({
      key: "disclaimer_storage",
      data: 1,
    })
  }
})
