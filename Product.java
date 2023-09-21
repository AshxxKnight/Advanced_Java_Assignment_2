package org.example;
import javax.persistence.*;

@Entity
public class Product {
        @Id
        @Column(name = "id")
        private String id;

        @Column(name = "name")
        private String name;

        @Column(name = "colour")
        private String colour;

        @Column(name = "genderRecommendation")
        private String genderRecommendation;

        @Column(name = "size")
        private String size;

        @Column(name = "price")
        private double price;

        @Column(name = "rating")
        private double rating;

        @Column(name = "availability")
        private String availability;

        //constructor
        public Product() {

        }
        public Product(String id, String name, String colour, String genderRecommendation, String size, double price, double rating, String availability) {
            this.id = id;
            this.name = name;
            this.colour = colour;
            this.genderRecommendation = genderRecommendation;
            this.price = price;
            this.size = size;
            this.rating = rating;
            this.availability = availability;
        }

        //setters
        public void setId(String id) {
            this.id = id;
        }
        public void setName(String name) {
            this.name = name;
        }
        public void setColor(String colour) {
            this.colour = colour;
        }
        public void setGender(String genderRecommendation) {
            this.genderRecommendation = genderRecommendation;
        }
        public void setPrice(double price) {
            this.price = price;
        }
        public void setSize(String size) {
            this.size = size;
        }
        public void setRating(double rating) {
            this.rating = rating;
        }
        public void setAvailability(String availability) {
            this.availability = availability;
        }

        //getters
        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getColor() {
            return colour;
        }

        public String getGender() {
            return genderRecommendation;
        }

        public String getSize() {
            return size;
        }

        public double getPrice() {
            return price;
        }

        public double getRating() {
            return rating;
        }

        public String getAvailability() {
            return availability;
        }
}