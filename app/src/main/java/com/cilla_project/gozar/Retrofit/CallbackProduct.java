package com.cilla_project.gozar.Retrofit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CallbackProduct {



        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("count")
        @Expose
        private Integer count;
        @SerializedName("count_total")
        @Expose
        private Integer countTotal;
        @SerializedName("pages")
        @Expose
        private Integer pages;
        @SerializedName("products")
        @Expose
        private List<Product> products = null;

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public Integer getCount() {
            return count;
        }

        public void setCount(Integer count) {
            this.count = count;
        }

        public Integer getCountTotal() {
            return countTotal;
        }

        public void setCountTotal(Integer countTotal) {
            this.countTotal = countTotal;
        }

        public Integer getPages() {
            return pages;
        }

        public void setPages(Integer pages) {
            this.pages = pages;
        }

        public List<Product> getProducts() {
            return products;
        }

        public void setProducts(List<Product> products) {
            this.products = products;
        }



    public class Product {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("image")
        @Expose
        private String image;
        @SerializedName("price")
        @Expose
        private Integer price;
        @SerializedName("price_discount")
        @Expose
        private Integer priceDiscount;
        @SerializedName("stock")
        @Expose
        private Integer stock;
        @SerializedName("draft")
        @Expose
        private Integer draft;
        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("created_at")
        @Expose
        private Integer createdAt;
        @SerializedName("last_update")
        @Expose
        private Integer lastUpdate;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public Integer getPrice() {
            return price;
        }

        public void setPrice(Integer price) {
            this.price = price;
        }

        public Integer getPriceDiscount() {
            return priceDiscount;
        }

        public void setPriceDiscount(Integer priceDiscount) {
            this.priceDiscount = priceDiscount;
        }

        public Integer getStock() {
            return stock;
        }

        public void setStock(Integer stock) {
            this.stock = stock;
        }

        public Integer getDraft() {
            return draft;
        }

        public void setDraft(Integer draft) {
            this.draft = draft;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public Integer getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(Integer createdAt) {
            this.createdAt = createdAt;
        }

        public Integer getLastUpdate() {
            return lastUpdate;
        }

        public void setLastUpdate(Integer lastUpdate) {
            this.lastUpdate = lastUpdate;
        }

    }}



