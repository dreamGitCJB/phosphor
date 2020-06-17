// pages/ucenter/inviteCode/inviteCode.js

var app = getApp();
var util = require('../../../utils/util.js');
var api = require('../../../config/api.js');



Page({

  /**
   * 页面的初始数据
   */
  data: {
    buttonDisable:'disabled',
    param: {
      inviteUserId:'',
      phone:'',
    },
    
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    if(options.userId != undefined) {

      const requestParam = this.data.param;

      requestParam.inviteUserId = options.userId;

      this.setData({
        param: requestParam,
      });
    }
         
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
    const that = this;
    setTimeout(function () {
      //要延时执行的代码
      //获取用户的登录信息
        if (app.globalData.hasLogin) {
          let userInfo = wx.getStorageSync('userInfo');
          that.setData({
            userInfo: userInfo,
            hasLogin: true
          });
        } else {
          wx.navigateTo({
            url: "/pages/auth/login/login"
          });
        }; 
     }, 2000) 
      
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
      const requestParam = this.data.param;
      requestParam.phone = value;
      this.setData({
        param :requestParam,
        buttonDisable: '',
      })
    } else {
      this.setData({
        buttonDisable:'disabled'
      })
    }
  },

  clickMethod() {

    const param = this.data.param;

    util.request(api.InvitedPhone, param, 'POST').then((res) => {
      if (res.errno === 0) {
        wx.switchTab({
          url: "/pages/index/index",
        });
      } else {
        wx.showToast({
          title: res.errmsg,
          icon: 'none',
          success: () => {
            wx.switchTab({
              url: "/pages/index/index",
            });
          }
        })
      }
    }),
    console.log();
  }

})