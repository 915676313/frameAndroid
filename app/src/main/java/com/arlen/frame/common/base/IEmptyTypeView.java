package com.arlen.frame.common.base;

/**
 * Created by Arlen on 2017/5/3 10:23.
 */

public interface IEmptyTypeView {

    EmptyType getEmptyType();

    enum EmptyType {

        //默认
        NONE("", 0);

//        //补货首页，库存预警
//        NO_STOCK_WARNING("暂无库存预警信息~", R.mipmap.ic_empty_warning),
//
//        //查看评论，暂无评论
//        NO_ORDER_COMMENT("您还没有评价过批发商~", R.mipmap.ic_empty_comment),
//
//        //暂无订单统计
//        NO_BUSINESS_STATISTIC("暂无订单数据统计~", R.mipmap.ic_empty_statistic),
//
//        //公共设计，内容加载失败
//        NO_CONTENT_NORMAL("内容加载失败，请重新加载~", R.mipmap.ic_empty_normal),
//
//        //您还没有积分
//        NO_MEMBER_SCORE("您还没有积分~", R.mipmap.ic_empty_member),
//
//        //没有店员
//        NO_SETTING_CLERK("您还没有店员~", R.mipmap.ic_empty_clerk),
//
//        //没有搜索到商品
//        NO_SEARCH_DEAL("没有搜到该商品~", R.mipmap.ic_empty_search_deal),
//
//        //暂无支付记录
//        NO_PAY_RECORD("暂无支付记录~", R.mipmap.ic_empty_day_detail),
//
//        //您还没有拆分商品
//        NO_DEAL_SPLIT("您还没有拆分商品~", R.mipmap.ic_empty_split),
//
//        //您还没有满减的商品
//        NO_DEAL_REDUCE("您还没有满减的商品~", R.mipmap.ic_empty_reduce),
//
//        //您还没有特价的商品
//        NO_DEAL_SPECIAL("您还没有特价的商品~", R.mipmap.ic_empty_reduce),
//
//        //您还没有配置收银机
//        NO_SETTING_POS("您还没有配置收银机~", R.mipmap.ic_empty_pos),
//
//        //您还没有商品套餐
//        NO_DEAL_MEAL("您还没有商品套餐~", R.mipmap.ic_empty_meal),
//
//        //您还没有做法
//        NO_DEAL_DO("您还没有做法~", R.mipmap.ic_empty_meal),
//
//        //您还没有会员
//        NO_MEMBER("您还没有会员~", R.mipmap.ic_empty_member_1),
//
//        //没有搜索到该会员
//        NO_SEARCH_MEMBER("没有搜索到该会员~", R.mipmap.ic_empty_search_member),
//
//        //无优惠券
//        NO_DEAL_COUPON("您还没有配置优惠券~", R.mipmap.ic_empty_coupon),
//
//        //无出库记录
//        NO_OUT_STOCK("您还没有出库记录~", R.mipmap.ic_empty_comment),
//
//        //无出库记录
//        NO_IN_STOCK("您还没有入库记录~", R.mipmap.ic_empty_comment),
//
//        //购物车为空
//        NO_BUY_CAR("您还没有订货单~", R.mipmap.ic_empty_statistic),
//
//        //没有组装商品
//        NO_DEAL_COMBINE("您还没有组装商品~", R.mipmap.ic_empty_meal),
//
//        //没有退仓商品
//        NO_RETURN_STORE("您还没有退仓记录~", R.mipmap.ic_empty_comment),
//
//        //没有销售订单
//        NO_SALE_ORDER("您还没有销售订单~", R.mipmap.ic_empty_statistic);


        private String contentRes;
        private int imgResId;

        EmptyType(String contentRes, int imgResId) {
            this.contentRes = contentRes;
            this.imgResId = imgResId;
        }

        public String getContentRes() {
            return contentRes;
        }

        public int getImgResId() {
            return imgResId;
        }

    }

}
