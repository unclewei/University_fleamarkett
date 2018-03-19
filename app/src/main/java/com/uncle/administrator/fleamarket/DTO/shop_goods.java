package com.uncle.administrator.fleamarket.DTO;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobObject;

public class shop_goods extends BmobObject {
    private String image1;
    private String image2;
    private String image3;
    private String image4;
    private String image5;
    private String image6;
    private String image7;
    private String image8;
    private String image9;

    private String title;
    private String text;
    private String price;
    private String variety;
    private String time;

    private int zan_nub;
    private int pictureNub;

    private String owner;
    private String college;
    private String organization;
    private String head_portrait;
    private String name;

    public shop_goods() {
    }

    public shop_goods(String image1, String image2, String image3,
                      String image4, String image5, String image6,
                      String image7, String image8, String image9,
                      String title, String text, String price, String variety,
                      int zan_nub,
                      int pictureNub, String owner, String college,
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

    public String getTime() {
        return getCreatedAt();
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

    public String getImage7() {
        return image7;
    }

    public void setImage7(String image7) {
        this.image7 = image7;
    }

    public String getImage8() {
        return image8;
    }

    public void setImage8(String image8) {
        this.image8 = image8;
    }

    public String getImage9() {
        return image9;
    }

    public void setImage9(String image9) {
        this.image9 = image9;
    }

    public List<String> ImageList() {
        List<String> list = new ArrayList<>();
        int nub = getPictureNub();
        list.add(image1);
        list.add(image2);
        list.add(image3);
        switch (nub) {
            case 9:
                list.add(image9);
            case 8:
                list.add(image8);
            case 7:
                list.add(image7);
            case 6:
                list.add(image6);
            case 5:
                list.add(image5);
            case 4:
                list.add(image4);
                break;
            case 3:
                break;
            default:
                break;
        }
        return list;
    }
}
