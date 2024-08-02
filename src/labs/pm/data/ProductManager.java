package labs.pm.data;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

public class ProductManager {

//    private Product product;
//    private Review review;
//    private Review[] review = new Review[5];
//    private Locale locale;
//    private ResourceBundle resources;
//    private DateTimeFormatter dateFormat;
//    private NumberFormat moneyFormat;l
    private Map<Product, List<Review>> products = new HashMap<>();
    
    private static class ResourceFormatter {
        private Locale locale;
        private ResourceBundle resources;
        private DateTimeFormatter dateFormat;
        private NumberFormat moneyFormat;
                
        private ResourceFormatter(Locale locale) {
            this.locale = locale;
            resources = ResourceBundle.getBundle("labs.pm.data.resources", locale);
            dateFormat = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT).localizedBy(locale);
            moneyFormat = NumberFormat.getCurrencyInstance(locale); 
        }
        
        private String formatProduct(Product product) {
            return MessageFormat.format(resources.getString("product"),
                product.getName(),
                moneyFormat.format(product.getPrice()),
                product.getRating().getStars(),
                dateFormat.format(product.getBestBefore()));
        }
        
        private String formatReview(Review review) {
            return MessageFormat.format(resources.getString("review"),
                    review.getRating().getStars(),
                    review.getComments());
        }
        
        private String getText(String key) {
            return resources.getString(key);
        }
    }
    
    private static Map<String, ResourceFormatter> formatters = 
                Map.of("en-GB", new ResourceFormatter(Locale.UK));

    private ResourceFormatter formatter;
        
    
    public void changeLocale(String languageTag) {
        formatter = formatters.getOrDefault(languageTag, formatters.get("en-GB"));
    }
    
    public static Set<String> getSupportLocales() {
        return formatters.keySet();
    }
    
    public ProductManager(String languageTag) {
        changeLocale(languageTag);
    }
    
    public ProductManager(Locale locale) {
//        this.locale = locale;
//        resources = ResourceBundle.getBundle("labs.pm.data.resources", locale);
//        dateFormat = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT).localizedBy(locale);
//        moneyFormat = NumberFormat.getCurrencyInstance(locale);
        this(locale.toLanguageTag());
    }
    

    public Product createProduct(int id, String name, BigDecimal price, Rating rating, LocalDate bestBefore) {
        Product product = new Food(id, name, price, rating, bestBefore);
        products.putIfAbsent(product, new ArrayList<>());
        return product;
    }

    public Product createProduct(int id, String name, BigDecimal price, Rating rating) {
        Product product = new Drink(id, name, price, rating);
        products.putIfAbsent(product, new ArrayList<>());
        return product;
    }
    
    public Product reviewProduct(int id, Rating rating, String comments) {
        return reviewProduct(findProduct(id), rating, comments);
    }

    public Product reviewProduct(Product product, Rating rating, String comments) {
//        review = new Review(rating, comments);
//        if (review[review.length - 1] != null) {
//            review = Arrays.copyOf(review, review.length + 5);
//        }
        List<Review> reviews = products.get(product);
        products.remove(product, reviews);
        reviews.add(new Review(rating, comments));
        int sum = 0;
//        int i = 0;
//        boolean reviewed = false;
//        while (i < review.length && !reviewed) {
//            if (review[i] == null) {
//                review[i] = new Review(rating, comments);
//                reviewed = true;
//            }
//            sum += review[i].getRating().ordinal();
//            i++;
//        }
        for(Review review: reviews) {
            sum+=review.getRating().ordinal();
        }
//        this.product = product.applyRating(Rateable.convert(Math.round((float) sum / i)));
//        return this.product;
        product = product.applyRating(Rateable.convert(Math.round((float) sum / reviews.size())));
        products.put(product, reviews);
        return product;
    }
    
    public void printProductReport(int id) {
        printProductReport(findProduct(id));
    }

    public void printProductReport(Product product) {
        List<Review> reviews = products.get(product);
        Collections.sort(reviews);
        StringBuilder txt = new StringBuilder();
//        txt.append(MessageFormat.format(resources.getString("product"),
//                product.getName(),
//                moneyFormat.format(product.getPrice()),
//                product.getRating().getStars(),
//                dateFormat.format(product.getBestBefore())));
        txt.append(formatter.formatProduct(product));
        txt.append('\n');

        for (Review review : reviews) {
//            if (review == null) {
//                break;
//            }
//            txt.append(MessageFormat.format(resources.getString("review"),
//                    review.getRating().getStars(),
//                    review.getComments()));
            txt.append(formatter.formatProduct(product));
            txt.append('\n');
        }
//        if (review[0] == null) {
//            txt.append(resources.getString("no.review"));
//            txt.append('\n');
//        }
        if(reviews.isEmpty()) {
//            txt.append(resources.getString("no.review"));
            txt.append(formatter.getText("no.review"));
            txt.append('\n');
        }
        System.out.println(txt);
    }
    
    public Product findProduct(int id) {
        Product result = null;
        for(Product product: products.keySet()){
            if(product.getId() == id) {
                result = product;
                break;
            }
        }
        return result; 
    }
    
    public void printProducts(Comparator<Product> sorter) {
        List<Product> productList = new ArrayList<>(products.keySet());
        productList.sort(sorter);
        StringBuilder txt = new StringBuilder();
        
        for(Product product: productList) {
            txt.append(formatter.formatProduct(product));
            txt.append('\n');
        }
        
        System.out.println(txt);
    }
    
    
}

