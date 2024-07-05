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
import labs.pm.app.Drink;
import labs.pm.app.Food;

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

//        Product p1 = new Product();
//        p1.setId(101);
//        p1.setName("Tea");
//        p1.setPrice(BigDecimal.valueOf(1.99));
//        System.out.println(p1.getId() + " " + p1.getName() + " " + p1.getPrice() + " " + p1.getDiscount());
        Product p2 = new Drink(102, "Coffee", BigDecimal.valueOf(1.99));
        Product p3 = new Food(103, "Cake", BigDecimal.valueOf(3.99), LocalDate.now().plusDays(2));

        System.out.println(p2);
        System.out.println(p3);

        
        Product p6 = new Drink(104, "Chocolate", BigDecimal.valueOf(2.99));
        Product p7 = new Food(104, "Chocolate", BigDecimal.valueOf(2.99), LocalDate.now().plusDays(2));
        System.out.println(p6.equals(p7));
    }
}
