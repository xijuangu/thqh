// pages/userPage/userPage.js
const defaultAvatarURL = '/pages/images/profile.png'
Page({

  /**
   * 页面的初始数据
   */
  data: {
    userName: '游客',
    avatarUrl: defaultAvatarURL,

    tradeAccountLoginState: 1,//记得改回1
    tradeAccountLoginStateContent: '未登录',//记得改回未登录
  },

  tradeCountLogin: function () {
    var that = this
    // console.log("click--" + that.data.tradeAccountLoginState)

    if (this.data.tradeAccountLoginStateContent == '未登录') {
      // console.log("click")
      wx.navigateTo({
        url: '/pages/tradeLoginPage/tadeLoginPage',
      })
    } else if (this.data.tradeAccountLoginStateContent == "已登录") {
      let resultCode = 400;
      wx.showModal({
        title: '提示',
        content: '交易账号已登录，是否需要退出登录',
        complete: (res) => {
          if (res.confirm) {
            // console.log("tradeAccount Logout")
            wx.getStorage({
              key: 'jwt_token_expiration_time',
              success(res) {
                // console.log("onReady-成功获取jwt_token_expiration_time：" + res.data)
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
                          // console.log("log33:")
                          // console.log(response)
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
                            wx.showToast({
                              title: '请求成功',
                              icon: 'success',
                              duration: 2000//持续的时间
                            })
                            wx.redirectTo({
                              url: '/pages/userPage/userPage',
                            })
                          } else {
                            wx.hideLoading()
                            wx.showToast({
                              title: '请求失败，请重试！',
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
                } else {
                  wx.clearStorage({
                    success: function () {
                      // console.log("onLoad-已清除缓存，请重新登录小程序")
                      wx.reLaunch({
                        url: '/pages/wxLoginPage/wxLoginPage',
                      });
                    }
                  })
                }
              }
            })
          } else if (res.cancel) {

          }
        },

      })
    }
  },

  logout: function () {
    wx.setStorage({
      key: 'jwt_token',
      data: '',
      success: function () {
        wx.clearStorage({
          success: function () {
            wx.setStorage({
              key: "tradeAccountLoginState",
              data: 1,
            })
            wx.setStorage({
              key: "disclaimer_storage",
              data: 0,
            })
            wx.reLaunch({
              url: '/pages/wxLoginPage/wxLoginPage',
            });
          },
          fail: function (err) {
            console.error('清除缓存失败:', err);
          }
        });
      },
      fail: function (err) {
        console.error('清除缓存失败:', err);
      }
    });
  },
  toPrivacy: function () {
    wx.navigateTo({
      url: '/pages/privacyPage/privacyPage',
    })
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad(options) {
    console.log('userPage----onLoad')
    var that = this
    
    // 禁用“分享给朋友”和“分享到朋友圈”
    wx.hideShareMenu({
      menus: ["shareAppMessage", "shareTimeline"]
    });

    wx.getStorage({
      key: 'usrWXPofile',
      success(res) {
        // console.log('usrWXPofile-------' + res.data)
        that.setData({
          avatarUrl: res.data
        })
      }
    })

    wx.getStorage({
      key: 'usrWXName',
      success(res) {
        that.setData({
          userName: res.data
        })
      }
    })

    wx.getStorage({
      key: 'tradeAccountLoginState',
      success(res) {
        if (res.data == 1) {
          that.setData({
            tradeAccountLoginStateContent: '未登录'
          })
        } else if (res.data == 0)
          that.setData({
            tradeAccountLoginStateContent: '已登录'
          })
      }
    })



  },

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady() {
    console.log('userPage----onReady')
  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow() {
    console.log('userPage----onShow')
  },

  /**
   * 生命周期函数--监听页面隐藏
   */
  onHide() {
    console.log('userPage----onHide')
  },

  /**
   * 生命周期函数--监听页面卸载
   */
  onUnload() {
    console.log('userPage----onUnload')
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

  }
})