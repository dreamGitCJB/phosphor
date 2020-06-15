// pages/ucenter/invited/invited.js
var util = require('../../../utils/util')
var api = require('../../../config/api')
Page({

  /**
   * 页面的初始数据
   */
  data: {
    invitedCode:"",
    invitedList:[],
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    let that = this;
    util.request(api.InvitedMy).then(res => {
      if(res.errno === 0) {
        that.setData({
          'invitedCode' : res.data.inviteCode,
          'invitedList' : res.data.inviteList,
        })
      }
    })
  },

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function () {

  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function () {

  },

  /**
   * 生命周期函数--监听页面隐藏
   */
  onHide: function () {

  },

  /**
   * 生命周期函数--监听页面卸载
   */
  onUnload: function () {

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
    const userId = "0000001";
    return {
      title: '这里是小程序',
      path: `pages/ucenter/inviteCode/inviteCode?userId=${userId}`,
    }
  }
})