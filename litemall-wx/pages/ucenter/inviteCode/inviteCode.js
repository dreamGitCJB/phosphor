// pages/ucenter/inviteCode/inviteCode.js

var app = getApp();

Page({

  /**
   * 页面的初始数据
   */
  data: {
    inviteUserId:'00000',
    buttonDisable:'disabled',
    phone:'',
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    if(options.userId != undefined) {
      this.setData({
        inviteUserId: options.userId,
      });
    }
    

    // //获取用户的登录信息
    // if (app.globalData.hasLogin) {
    //   let userInfo = wx.getStorageSync('userInfo');
    //   this.setData({
    //     userInfo: userInfo,
    //     hasLogin: true
    //   });
    // } else {
    //   wx.navigateTo({
    //     url: "/pages/auth/login/login"
    //   });
    // };    
  },

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function () {
    console.log(1)
  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function () {
    console.log(2)
  },

  /**
   * 生命周期函数--监听页面隐藏
   */
  onHide: function () {
    console.log(3)
  },

  /**
   * 生命周期函数--监听页面卸载
   */
  onUnload: function () {
    console.log(4)
  },

  /**
   * 页面相关事件处理函数--监听用户下拉动作
   */
  onPullDownRefresh: function () {
  },

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom: function () {
  },

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage: function () {

  },
  
  inputChangeMethod(e) {
    let value = e.detail.value;

    if(value.length === 11 && value.charAt(0) == 1) {
      console.log("-------")
      this.setData({
        phone: value,
        buttonDisable: '',
      })
    } else {
      this.setData({
        buttonDisable:'disabled'
      })
    }
  }

})