// pages/ucenter/integralRecords/integralRecords.js
var util = require("../../../utils/util")
var api = require('../../../config/api')

Page({

  /**
   * 页面的初始数据
   */
  data: {
    'integralTotal':'',
    'showType':0,
    'integralList':[],
    'limit':1,
    'size': 20,
    'totalPages':1,
  },

  listIntegral() {
    util.request(api.IntegralList,{
      limit : this.data.limit,
      size: this.data.size,
      showType: this.data.showType,
    }).then((res) => {
      console.log(res)
      if(res.errno === 0) {
        this.setData({
          integralList: [...this.data.integralList, ...res.data.list],
          totalPages: res.data.pages
        });
      } else {
        wx.showToast({
          title: res.errmsg,
          icon: 'none',
        })
      }
      
    })
  },


  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
  
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
    util.request(api.IntegralTotal).then((res) => {
      if(res.errno === 0) {
         this.setData({
           'integralTotal':res.data.integral,
         })
      }
    });
    this.listIntegral();
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
    
    wx.showNavigationBarLoading() //在标题栏中显示加载
    this.setData({
      'integralList':[],
      'limit':1,
    })
    this.listIntegral();
    wx.hideNavigationBarLoading() //完成停止加载
    wx.stopPullDownRefresh() //停止下拉刷新
  },

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom: function () {
    if (this.data.totalPages > this.data.page) {
      this.setData({
        page: this.data.page + 1
      });
      this.listIntegral();
    } else {
      wx.showToast({
        title: '没有更多数据了',
        icon: 'none',
        duration: 2000
      });
      return false;
    }
  },

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage: function () {

  },

  switchTab: function(event) {
    console.log(111);
    let showType = event.currentTarget.dataset.index;
    this.setData({
      integralList: [],
      showType: showType,
      size: 20,
      limit: 1,
      totalPages: 1
    });

    this.listIntegral();
  },
})