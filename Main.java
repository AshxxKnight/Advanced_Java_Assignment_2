package org.example;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;


import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;


public class Main {

    private static final String CSV_DIRECTORY = "C:\\Users\\anshusaxena\\Desktop\\Advance Java\\Assigment Links";
    private static SessionFactory factory;
    static boolean fileLoaded = false;

    public static void main(String[] args) {

        factory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();


        // Read input parameters
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter T-shirt color: ");
        String colour = scanner.nextLine();

        System.out.print("Enter T-shirt size: ");
        String size = scanner.nextLine();

        System.out.print("Enter gender (M/F/U): ");
        String gender = scanner.nextLine();

        System.out.print("Enter output preference (Price/Rating/Both): ");
        String outputPreference = scanner.nextLine();

        scanner.close();

        // Load CSV files in a separate thread to check for new files at runtime
        Thread fileLoaderThread = new Thread(() -> {
            while (true) {
                try {
                    if (!fileLoaded) {
                        loadCSV();
                        fileLoaded = true;
                    }
                    TimeUnit.SECONDS.sleep(30);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        fileLoaderThread.start();

        // Wait for file loading to complete
        while (!fileLoaded) {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        //matched Result
        List<Product> matchedResult = searchProduct(colour, size, gender,outputPreference);

        //display output Product
        displayOutput(matchedResult);

        //SessionFactory Close
        factory.close();

    }


    public static void loadCSV() {
        // Load CSV files
        File[] csvFiles = new File(CSV_DIRECTORY).listFiles(file -> file.isFile() && file.getName().endsWith(".csv"));
        for (File filename : csvFiles) {
            try (CSVReader reader = new CSVReader(new FileReader(filename))) {
                List<String[]> rows = reader.readAll();
                boolean firstLine = true;
                for (String[] row : rows) {
                    if(firstLine) {
                        firstLine = false;
                        continue;
                    }

                    Product product = new Product();

                    product.setId(row[0]);
                    product.setName(row[1]);
                    product.setColor(row[2]);
                    product.setGender(row[3]);
                    product.setSize(row[4]);
                    product.setPrice(Double.parseDouble(row[5]));
                    product.setRating(Double.parseDouble(row[6]));
                    product.setAvailability(row[7]);

                    Session session = factory.openSession();
                    session.beginTransaction();
                    session.saveOrUpdate(product);
                    session.getTransaction().commit();
                    session.close();


                }
            } catch (IOException | CsvException e) {
                System.err.println("Error loading CSV file: " + filename);
                e.printStackTrace();
            }
        }
    }

    private static List<Product> searchProduct(String colour, String size, String gender, String outputPreference) {
        Session session = factory.openSession();
        session.beginTransaction();
        String query = "SELECT Product FROM Product Product WHERE Product.colour = :colour AND Product.size = :size AND Product.genderRecommendation = :gender AND Product.availability = 'Y' ";
        if (outputPreference.equalsIgnoreCase("price")) {
            query += " ORDER BY price";
        } else if (outputPreference.equalsIgnoreCase("rating")) {
            query += " ORDER BY rating";
        } else if (outputPreference.equalsIgnoreCase("both")) {
            query += " ORDER BY price, rating";
        }
        List<Product> matchedResult = session.createQuery(query, Product.class).setParameter("colour", colour).setParameter("size", size)
                .setParameter("gender", gender).list();
        session.getTransaction().commit();
        session.close();
        return matchedResult;
    }

    //Display
    private static void displayOutput(List<Product> productList) {
        if (productList.isEmpty()) {
            System.out.println("No matching products found.");
        } else {
            System.out.println("\nMatching Products:\n");
            for (Product product : productList) {
                System.out.println("ID: " + product.getId()
                        + "\nName: " + product.getName()
                        + "\nColor: " + product.getColor()
                        + "\nGender: " + product.getGender()
                        + "\nSize: " + product.getSize()
                        + "\nPrice: $" + product.getPrice()
                        + "\nRating: " + product.getRating()
                        + "\nAvailability: " + product.getAvailability() + "\n");
            }
        }
    }
}