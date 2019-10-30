package com.grsu.guideapp.network.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.List;

public class Data implements Serializable {

    @SerializedName("name")
    @Expose
    private Name name;
    @SerializedName("images")
    @Expose
    private List<Image> images = null;
    @SerializedName("about")
    @Expose
    private About about;
    @SerializedName("route")
    @Expose
    private Route route;
    @SerializedName("category")
    @Expose
    private List<Category> category;

    @SerializedName("address")
    @Expose
    private Language address;
    @SerializedName("email")
    @Expose
    private List<String> email;
    @SerializedName("href")
    @Expose
    private List<Href> href;
    @SerializedName("phone")
    @Expose
    private Phone phone;

    private final static long serialVersionUID = -2713204178998794488L;

    /**
     * No args constructor for use in serialization
     */
    public Data() {
    }

    /**
     *
     * @param route
     * @param name
     * @param about
     * @param images
     */
    public Data(Name name, List<Image> images, About about, Route route, List<Category> category,
            Language address, List<String> email, List<Href> href, Phone phone) {
        this.name = name;
        this.images = images;
        this.about = about;
        this.route = route;
        this.category = category;
        this.address = address;
        this.email = email;
        this.href = href;
        this.phone = phone;
    }

    public Name getName() {
        return name;
    }

    public List<Image> getImages() {
        return images;
    }

    public About getAbout() {
        return about;
    }

    public Route getRoute() {
        return route;
    }

    public List<Category> getCategory() {
        return category;
    }

    public String getAddress() {
        if (address!=null){
            return address.getRu() + "|" + address.getEn() + "|" + address.getCn() + "|"
                    + address.getPl() + "|" + address.getLt();
        }
        return "";
    }

    public String getEmail() {
        if (email != null && !email.isEmpty()) {
            if (email.size() > 1) {
                StringBuilder buffer = new StringBuilder();
                for (String s : email) {
                    buffer.append(s).append("\n");
                }
                return buffer.toString();
            } else {
                return email.get(0);
            }
        }
        return "";
    }

    public String getHref() {
        if (href != null && !href.isEmpty()) {
            if (href.size() > 1) {
                StringBuilder buffer = new StringBuilder();
                for (Href href : href) {
                    buffer.append(href.getHref()).append("\n");
                }
                return buffer.toString();
            } else {
                return href.get(0).getHref();
            }
        }
        return "";
    }

    public String getPhone() {
        if (phone != null) {
            List<String> mobiles = phone.getMobile();
            if (mobiles != null && !mobiles.isEmpty()) {
                if (mobiles.size() > 1) {
                    StringBuilder buffer = new StringBuilder();
                    for (String mobile : mobiles) {
                        buffer.append(mobile).append("\n");
                    }
                    return buffer.toString();
                } else {
                    return mobiles.get(0);
                }
            }
        }
        return "";
    }

    public class Href {
        @SerializedName("href")
        @Expose
        private String href;

        public String getHref() {
            return href;
        }

        public void setHref(String href) {
            this.href = href;
        }
    }

    public class Phone {

        @SerializedName("mobile")
        @Expose
        private List<String> mobile = null;
        /*@SerializedName("office")
        @Expose
        private List<String> office = null;*/

        public List<String> getMobile() {
            return mobile;
        }

        public void setMobile(List<String> mobile) {
            this.mobile = mobile;
        }

        /*public List<String> getOffice() {
            return office;
        }

        public void setOffice(List<String> office) {
            this.office = office;
        }*/
    }
}
