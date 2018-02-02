package com.uncle.bomb;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

public class ShopGoods extends BmobObject {
    private String image1;
    private String image2;
    private String image3;
    private String image4;
    private String image5;
    private String image6;

    private String title;
    private String text;
    private String price;
    private String variety;
    private int zan_nub;
    private int pictureNub;

    private String owner;
    private String college;
    private String organization;
    private String head_portrait;
    private String name;

    public ShopGoods() {
    }

    public ShopGoods(String image1, String image2, String image3,
                     String image4, String image5, String image6,
                     String title, String text, String price, String variety,
                     int zan_nub, int pictureNub, String owner, String college,
                     String organization, String head_portrait, String name) {

        this.image1 = image1;
        this.image2 = image2;
        this.image3 = image3;
        this.image4 = image4;
        this.image5 = image5;
        this.image6 = image6;
        this.title = title;
        this.text = text;
        this.price = price;
        this.variety = variety;
        this.zan_nub = zan_nub;
        this.pictureNub = pictureNub;
        this.owner = owner;
        this.college = college;
        this.organization = organization;
        this.head_portrait = head_portrait;
        this.name = name;
    }

    public int getPictureNub() {
        return pictureNub;
    }

    public void setPictureNub(int pictureNub) {
        this.pictureNub = pictureNub;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHead_portrait() {
        return head_portrait;
    }

    public void setHead_portrait(String head_portrait) {
        this.head_portrait = head_portrait;
    }

    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public int getZan_nub() {
        return zan_nub;
    }

    public void setZan_nub(int zan_nub) {
        this.zan_nub = zan_nub;
    }

    public String getImage1() {
        return image1;
    }

    public void setImage1(String image1) {
        this.image1 = image1;
    }

    public String getImage2() {
        return image2;
    }

    public void setImage2(String image2) {
        this.image2 = image2;
    }

    public String getImage3() {
        return image3;
    }

    public void setImage3(String image3) {
        this.image3 = image3;
    }

    public String getImage4() {
        return image4;
    }

    public void setImage4(String image4) {
        this.image4 = image4;
    }

    public String getImage5() {
        return image5;
    }

    public void setImage5(String image5) {
        this.image5 = image5;
    }

    public String getImage6() {
        return image6;
    }

    public void setImage6(String image6) {
        this.image6 = image6;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getVariety() {
        return variety;
    }

    public void setVariety(String variety) {
        this.variety = variety;
    }
}
