/*
 * Copyright (C) 2024 Deepika
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package labs.pm.app;

import java.math.BigDecimal;
import java.time.LocalDate;
import labs.pm.data.Product;
import labs.pm.data.Drink;
import labs.pm.data.Food;
import labs.pm.data.Rating;

/**
 * {@code Shop} Class represents an application that manages Products
 *
 * @version 4.0
 * @author Bhawuk
 */
public class Shop {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

//          Product p1 = new Product();
//          p1.setId(101);
//          p1.setName("Tea");
//          p1.setPrice(BigDecimal.valueOf(1.99));      
//        Product p1 = new Product(101, "Tea", BigDecimal.valueOf(1.99));
//        Product p2 = new Product(102, "Coffee", BigDecimal.valueOf(1.99), Rating.FOUR_STAR);
//        Product p3 = new Product(103, "Cake", BigDecimal.valueOf(3.99), Rating.FIVE_STAR);
//        Product p4 = new Product();
//        Product p5 = p3.applyRating(Rating.THREE_STAR);
//        
//        System.out.println(p1.getId() + " " + p1.getName() + " " + p1.getPrice() + " " + p1.getDiscount() + " "
//                + p1.getRating().getStars());
//        System.out.println(p2.getId() + " " + p2.getName() + " " + p2.getPrice() + " " + p2.getDiscount() + " "
//                + p1.getRating().getStars());
//        System.out.println(p3.getId() + " " + p3.getName() + " " + p3.getPrice() + " " + p3.getDiscount() + " "
//                + p3.getRating().getStars());
//        System.out.println(p4.getId() + " " + p4.getName() + " " + p4.getPrice() + " " + p4.getDiscount() + " "
//                + p4.getRating().getStars());
//        System.out.println(p5.getId() + " " + p5.getName() + " " + p5.getPrice() + " " + p5.getDiscount() + " "
//                + p5.getRating().getStars());
//        
        Product p2 = new Drink(102, "Coffee", BigDecimal.valueOf(1.99), Rating.FOUR_STAR);
        Product p3 = new Food(103, "Cake", BigDecimal.valueOf(3.99), Rating.FIVE_STAR, LocalDate.now().plusDays(2));
        Product p4 = new Food(104, "Cookie", BigDecimal.valueOf(3.99), Rating.THREE_STAR, LocalDate.now());

        System.out.println(p2);
        System.out.println(p3);
//
        Product p6 = new Drink(104, "Chocolate", BigDecimal.valueOf(2.99), Rating.FIVE_STAR);
        Product p7 = new Food(104, "Chocolate", BigDecimal.valueOf(2.99), Rating.FIVE_STAR, LocalDate.now().plusDays(2));
        System.out.println(p6.equals(p7));
        
         if (p3 instanceof Food) {
            Food food = (Food) p3;
            System.out.println("Discounted price for " + food.getName() + ": " + food.getDiscount());
        }

        if (p2 instanceof Drink) {
            Drink drink = (Drink) p2;
            System.out.println("Discounted price for " + drink.getName() + ": " + drink.getDiscount());
        }

    }
}
