// pages/otterLink/outterLink.js
Page({

  /**
   * 页面的初始数据
   */
  data: {
    content: '',//web-view的链接
    functionTitle: ''
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad(options) {
    // 禁用“分享给朋友”和“分享到朋友圈”
    wx.hideShareMenu({
      menus: ["shareAppMessage", "shareTimeline"]
    });
    console.log('log34:tableName--' + options.BItableName)

    wx.setStorage({
      key: 'BIPageTitle',
      data: options.BIPageTitle
    })


    var that = this
    wx.getStorage({
      key: "jwt_token",
      success(res) {
        wx.request({
          url: 'https://bill.thqh.com.cn/api-wx/reports',
          // url: 'https://service.thqh.com.cn/api-wx/reports',
          header: {
            'content-type': 'application/json',
            'Authorization': 'Bearer ' + res.data
          },
          method: 'POST',
          data: {
            'reportCode': options.tableName
          },
          success: function (res) {
            console.log('log21:获取BI界面成功')
            console.log(res)
            that.setData({
              content: res.data.data.report_url
            })
          }
        })
      }
    })

  },

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady() {

  },

  bindload() {
    wx.getStorage({
      key: 'BIPageTitle',
      success(res) {
        console.log('log55:BIPageTitle--' + res.BIPageTitle)
        wx.setNavigationBarTitle({
          title: res.BIPageTitle,
        })
      }
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

  }
})