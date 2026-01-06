// pages/privacyPage/privacyPage.js
Page({

  /**
   * 页面的初始数据
   */
  data: {

  },

  toPrivacy1: function () {
    console.log('tab')
    var privacyAuthShow = 'show'
    var barOperation = '2'
    wx.navigateTo({
      url: '/pages/appPrivacyPolicyPage/appPrivacyPolicyPage?privacyAuthShow=' + privacyAuthShow + '&barOperation=' + barOperation,
    })
  },

  toPrivacy2: function () {
    console.log('tab')
    var privacyAuthShow = 'non-show'
    var barOperation = '2'
    wx.navigateTo({
      url: '/pages/UserInfoColletListPage/UserInfoColletListPage?privacyAuthShow=' + privacyAuthShow + '&barOperation=' + barOperation,

    })
  },

  toPrivacy3: function () {
    console.log('tab')
    var privacyAuthShow = 'non-show'
    var barOperation = '2'
    wx.navigateTo({
      url: '/pages/userInfoThirdPartyPolicyPage/userInfoThirdPartyPolicyPage?privacyAuthShow=' + privacyAuthShow + '&barOperation=' + barOperation,
    })
  },

  toPrivacy4: function () {
    console.log('tab')
    var privacyAuthShow = 'non-show'
    var barOperation = '2'
    wx.navigateTo({
      url: '/pages/loginPolicyPage/loginPolicyPage?privacyAuthShow=' + privacyAuthShow + '&barOperation=' + barOperation,
    })
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