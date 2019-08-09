package com.cpr.ads.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author Created by Pear on 8/2/2019.
 * Item từng phần data lấy được từ API
 */
public class ItemData {
    private String cover;
    @SerializedName("text_intall_color")
    @Expose
    private String textIntallColor;
    @SerializedName("btn_clicked_color")
    @Expose
    private String btnClickedColor;
    @SerializedName("package")
    @Expose
    private String jsonMemberPackage;
    @SerializedName("btn_unclicked_color")
    @Expose
    private String btnUnclickedColor;
    @SerializedName("bg_start_color")
    @Expose
    private String bgStartColor;

    private String name;
    private String logo;
    private int id;
    @SerializedName("bg_end_color")
    @Expose
    private String bgEndColor;
    @SerializedName("text_title_color")
    @Expose
    private String textTitleColor;

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getTextIntallColor() {
        return textIntallColor;
    }

    public void setTextIntallColor(String textIntallColor) {
        this.textIntallColor = textIntallColor;
    }

    public String getBtnClickedColor() {
        return btnClickedColor;
    }

    public void setBtnClickedColor(String btnClickedColor) {
        this.btnClickedColor = btnClickedColor;
    }

    public String getJsonMemberPackage() {
        return jsonMemberPackage;
    }

    public void setJsonMemberPackage(String jsonMemberPackage) {
        this.jsonMemberPackage = jsonMemberPackage;
    }

    public String getBtnUnclickedColor() {
        return btnUnclickedColor;
    }

    public void setBtnUnclickedColor(String btnUnclickedColor) {
        this.btnUnclickedColor = btnUnclickedColor;
    }

    public String getBgStartColor() {
        return bgStartColor;
    }

    public void setBgStartColor(String bgStartColor) {
        this.bgStartColor = bgStartColor;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBgEndColor() {
        return bgEndColor;
    }

    public void setBgEndColor(String bgEndColor) {
        this.bgEndColor = bgEndColor;
    }

    public String getTextTitleColor() {
        return textTitleColor;
    }

    public void setTextTitleColor(String textTitleColor) {
        this.textTitleColor = textTitleColor;
    }

    @Override
    public String toString() {
        return
                "DataItem{" +
                        "cover = '" + cover + '\'' +
                        ",text_intall_color = '" + textIntallColor + '\'' +
                        ",btn_clicked_color = '" + btnClickedColor + '\'' +
                        ",package = '" + jsonMemberPackage + '\'' +
                        ",btn_unclicked_color = '" + btnUnclickedColor + '\'' +
                        ",bg_start_color = '" + bgStartColor + '\'' +
                        ",name = '" + name + '\'' +
                        ",logo = '" + logo + '\'' +
                        ",id = '" + id + '\'' +
                        ",bg_end_color = '" + bgEndColor + '\'' +
                        ",text_title_color = '" + textTitleColor + '\'' +
                        "}";
    }
}
