package labs.pm.data;

import java.math.BigDecimal;
import java.nio.file.Path;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class ProductManager {

//    private Product product;
//    private Review review;
//    private Review[] review = new Review[5];
//    private Locale locale;
//    private ResourceBundle resources;
//    private DateTimeFormatter dateFormat;
//    private NumberFormat moneyFormat;l
    private Map<Product, List<Review>> products = new HashMap<>();
    private static final Logger logger = Logger.getLogger(ProductManager.class.getName());
    private final ResourceBundle config = ResourceBundle.getBundle("labs.pm.data.config");
    private final MessageFormat reviewFormat = new MessageFormat(config.getString("labs.pm.data.config"));
//    private final MessageFormat productFormat = new MessageFormat(config.getString("labs.pm.data.config"));

//    Path where reports will be created
    private Path reportsFolder = Path.of(config.getString("reports.folder"));
    private Path dataFolder = Path.of(config.getString("data.folder"));
    private Path tempFolder = Path.of(config.getString("temp.folder"));

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

    private static Map<String, ResourceFormatter> formatters
            = Map.of("en-GB", new ResourceFormatter(Locale.UK));

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
        try {
            return reviewProduct(findProduct(id), rating, comments);
        } catch (ProductManagerException ex) {
            Logger.getLogger(ProductManager.class.getName()).log(Level.INFO, ex.getMessage(), ex);
//            logger.log(Level.INFO, ex.getMessage()); ---> Error
            return null;
        }
    }

    public Product reviewProduct(Product product, Rating rating, String comments) {
//        review = new Review(rating, comments);
//        if (review[review.length - 1] != null) {
//            review = Arrays.copyOf(review, review.length + 5);
//        }
        List<Review> reviews = products.get(product);
        products.remove(product, reviews);
        reviews.add(new Review(rating, comments));
//        int sum = 0;
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
//        for (Review review : reviews) {
//            sum += review.getRating().ordinal();
//        }
//        this.product = product.applyRating(Rateable.convert(Math.round((float) sum / i)));
//        return this.product;
//        product = product.applyRating(Rateable.convert(Math.round((float) sum / reviews.size())));

        product = product.applyRating(
                Rateable.convert(
                        (int) Math.round(
                                reviews.stream()
                                        .mapToInt(r -> r.getRating().ordinal())
                                        .average()
                                        .orElse(0) // Provide a default value if the stream is empty
                        )
                )
        );

        products.put(product, reviews);
        return product;
    }

    public void printProductReport(int id) {
        try {
            printProductReport(findProduct(id));
        } catch (ProductManagerException ex) {
            Logger.getLogger(ProductManager.class.getName()).log(Level.SEVERE, null, ex);
//            logger.log(Level.INFO, ex.getMessage()); ---> Error
        }
    }

    public void printProductReport(Product product) {
        List<Review> reviews = products.get(product);
        Collections.sort(reviews);
//        StringBuilder txt = new StringBuilder();

//      Printing the product reports to a file
        Path productFile = reportsFolder.resolve(
                MessageFormat.format(
                        config.getString("report.file"), product.getId()
                )
        );

//        txt.append(MessageFormat.format(resources.getString("product"),
//                product.getName(),
//                moneyFormat.format(product.getPrice()),
//                product.getRating().getStars(),
//                dateFormat.format(product.getBestBefore())));
        try (PritnWriter out = new PrintWriter(
                new OutputStreamWriter(
                        Files.newOutputStream(productFile,
                                StandardOpenOption.CREATE),
                        "UTF-8"))) {
            txt.append(formatter.formatProduct(product));
            txt.append('\n');

            if (reviews.isEmpty()) {
                txt.append(formatter.getText("no.review"));
            } else {
                txt.append(reviews.stream()
                        .map(r -> formatter.formatReview(r) + '\n')
                        .collect(Collectors.joining()));
            }
            System.out.println(txt);
        }
//        for (Review review : reviews) {
//            if (review == null) {
//                break;
//            }
//            txt.append(MessageFormat.format(resources.getString("review"),
//                    review.getRating().getStars(),
//                    review.getComments()));
//            txt.append(formatter.formatProduct(product));
//            txt.append('\n');
//    }
//        if (review[0] == null) {
//            txt.append(resources.getString("no.review"));
//            txt.append('\n');
//        }
//        if (reviews.isEmpty()) {
//            txt.append(resources.getString("no.review"));
//            txt.append(formatter.getText("no.review"));
//            txt.append('\n');
//        }
//    System.out.println (txt);
    }

    public Product findProduct(int id) throws ProductManagerException {
//        Product result = null;
//        for(Product product: products.keySet()){
//            if(product.getId() == id) {
//                result = product;
//                break;
//            }
//        }
//        return result; 

//      Method implementation by streams and lambda expressions
        return products.keySet()
                .stream()
                .filter(p -> p.getId() == id)
                .findFirst()
                //                .orElseGet(() -> null);
                //                .get(); 
                .orElseThrow(()
                        -> new ProductManagerException("Product with id " + id + " not found"));
    }

    public void printProducts(Predicate<Product> filter, Comparator<Product> sorter) {
//        List<Product> productList = new ArrayList<>(products.keySet());
//        productList.sort(sorter);
        StringBuilder txt = new StringBuilder();

//        for (Product product : productList) {
//            txt.append(formatter.formatProduct(product));
//            txt.append('\n');
//        }
        products.keySet()
                .stream()
                .sorted(sorter)
                .filter(filter)
                .forEach(p -> txt.append(formatter.formatProduct(p) + '\n'));

        System.out.println(txt);
    }

    public Map<String, String> getDiscounts() {
        return products.keySet()
                .stream()
                .collect(
                        Collectors.groupingBy(product -> product.getRating().getStars(),
                                Collectors.collectingAndThen(
                                        Collectors.summingDouble(
                                                product -> product.getDiscount().doubleValue()),
                                        discount -> formatter.moneyFormat.format(discount)
                                )));

    }

    /**
     * 12-2 : Text Parsing Operations
     */
    public void parseReview(String text) {
        try {
            Object[] values = reviewFormat.parse(text);
            reviewProduct(
                    Integer.parseInt((String) values[0]),
                    Rateable.convert(Integer.parseInt((String) values[1])),
                    (String) values[2]);
        } catch (ParseException ex) {
            logger.log(Level.WARNING, "Error parsing review " + text, ex);
        }
    }

}
