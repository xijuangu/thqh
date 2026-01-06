// pages/userInfoThirdPartyPolicyPage/userInfoThirdPartyPolicyPage.js
Page({
  data: {
    privacyAuthShow: '',
    barOperation: ''
  },
  onLoad(options) {
    // 禁用“分享给朋友”和“分享到朋友圈”
    wx.hideShareMenu({
      menus: ["shareAppMessage", "shareTimeline"]
    });
    var that = this;
    console.log('userInfoThirdPartyPolicyPage onload trigger')
    console.log(options.privacyAuthShow)
    that.setData({
      privacyAuthShow: options.privacyAuthShow,
      barOperation: options.barOperation
    })
  },
  // 点击处理函数
  copylink: function (url) {
    const url_ = url
    // 复制链接到剪贴板
    wx.setClipboardData({
      data: url_,
      success: () => {
        // 复制成功，显示提示
        wx.showToast({
          title: '链接已复制到剪贴板，请通过第三方浏览器打开查看',
          icon: 'none',
          duration: 2700
        });
      },
      fail: () => {
        // 复制失败，显示错误提示
        wx.showToast({
          title: '复制失败，请手动复制',
          icon: 'none',
          duration: 2000
        });
      }
    });
  },

  link0: function () {
    const url = "https://kf.qq.com/";
    // console.log('tab')
    this.copylink(url);
  },

  link1: function () {
    const url = "https://kingstartech.com/#/sdk/privacy";
    // console.log('tab')
    this.copylink(url);
  },


});