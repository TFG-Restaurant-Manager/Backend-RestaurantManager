package com.tfg_rm.backend_restaurantmanager.script;

// Source code is decompiled from a .class file using FernFlower decompiler (from Intellij IDEA).
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class SeedInserter {
   private static final String DEFAULT_URL = "jdbc:postgresql://host.docker.internal:5432/restaurant_host";
   private static final String DEFAULT_USER = "admin";
   private static final String DEFAULT_PASS = "admin";
   private final Connection conn;
   private final Random rnd = ThreadLocalRandom.current();

   public SeedInserter(Connection var1) {
      this.conn = var1;
   }

   public static void main(String[] var0) throws Exception {
      String var1 = (var0.length > 0 ? var0[0] : DEFAULT_URL).trim();
      String var2 = (var0.length > 1 ? var0[1] : DEFAULT_USER).trim();
      String var3 = var0.length > 2 ? var0[2] : DEFAULT_PASS;
      Connection var4 = DriverManager.getConnection(var1, var2, var3);

      try {
         var4.setAutoCommit(false);
         SeedInserter var5 = new SeedInserter(var4);
         var5.runSeed();
         var4.commit();
         System.out.println("Seeding completed.");
      } catch (Throwable var8) {
         if (var4 != null) {
            try {
               var4.close();
            } catch (Throwable var7) {
               var8.addSuppressed(var7);
            }
         }

         throw var8;
      }

      if (var4 != null) {
         var4.close();
      }

   }

   public void runSeed() throws SQLException {
      byte var1 = 5;

      for(int var2 = 1; var2 <= var1; ++var2) {
         int var3 = this.insertRestaurant("Restaurante " + var2, "Descripción del restaurante " + var2, "rest" + var2 + "@example.com", "+34 600 000 " + String.format("%03d", var2), "Calle Falsa " + var2, "/logos/rest" + var2 + ".png");
         int[] var4 = new int[]{this.insertCategory(var3, "Entrantes"), this.insertCategory(var3, "Principales"), this.insertCategory(var3, "Postres"), this.insertCategory(var3, "Bebidas")};

         for(int var5 = 1; var5 <= 12; ++var5) {
            this.insertIngredient(var3, "Ingrediente_" + var2 + "_" + var5, "unidad", (double)this.round((double)10.0F + this.rnd.nextDouble() * (double)100.0F, 3), (double)this.round((double)0.5F + this.rnd.nextDouble() * (double)10.0F, 2));
         }

         for(int var23 = 1; var23 <= 8; ++var23) {
            int var6 = var4[this.rnd.nextInt(var4.length)];
            int var7 = this.insertDish(var3, var6, "Plato_" + var2 + "_" + var23, "Descripción coherente del plato " + var23, (double)this.round((double)3.0F + this.rnd.nextDouble() * (double)25.0F, 2), true);
            int var8 = 2 + this.rnd.nextInt(3);

            for(int var9 = 0; var9 < var8; ++var9) {
               int var10 = this.selectRandomIngredientId(var3);
               if (var10 > 0) {
                  this.insertDishIngredient(var7, var10, (double)this.round(0.05 + this.rnd.nextDouble() * (double)0.5F, 3));
               }
            }
         }

         byte var24 = 20;

         for(int var25 = 1; var25 <= var24; ++var25) {
            String var27 = "Cliente_" + var2 + "_" + var25;
            String var29 = "cliente" + var2 + "_" + var25 + "@example.com";
            this.insertClient(var3, var27, var29, "pw_hash_dummy");
         }

         int var26 = this.insertTableSection(var3, "Interior");
         int var28 = this.insertTableSection(var3, "Terraza");

         for(int var30 = 1; var30 <= 8; ++var30) {
            int var32 = var30 % 2 == 0 ? var26 : var28;
            this.insertTable(var3, var32, "T-" + var2 + "-" + var30, 2 + this.rnd.nextInt(6), this.rnd.nextInt(100), this.rnd.nextInt(100));
         }

         byte var31 = 30;

         for(int var33 = 0; var33 < var31; ++var33) {
            int var34 = this.selectRandomClientId(var3);
            boolean var11 = this.rnd.nextBoolean();
            Integer var12 = var11 ? null : this.selectRandomTableId(var3);
            int var13 = var11 ? this.selectOrderTypeIdByName("WEB") : this.selectOrderTypeIdByName("TABLE");
            int var14 = this.selectOrderStatusByName(Math.random() < 0.2 ? "PAGADO" : "CREADO");
            double var15 = (double)0.0F;
            int var17 = this.insertOrder(var3, var12, var13, (String)null, var34, var14, (double)0.0F, "Seed order");
            int var18 = 1 + this.rnd.nextInt(4);

            for(int var19 = 0; var19 < var18; ++var19) {
               int var20 = this.selectRandomDishId(var3);
               double var21 = this.selectDishPrice(var20);
               if (var21 <= (double)0.0F) {
                  var21 = (double)this.round((double)5.0F + this.rnd.nextDouble() * (double)20.0F, 2);
               }

               this.insertOrderItem(var17, var20, "", var21);
               var15 += var21;
            }

            this.updateOrderTotal(var17, (double)this.round(var15, 2));
            if (Math.random() < (double)0.5F) {
               this.insertPayment(var17, Math.random() < (double)0.5F ? "EFECTIVO" : "TARJETA", (double)this.round(var15, 2));
            }
         }

         this.conn.commit();
         System.out.println("Seeded restaurant " + var3);
      }

   }

   private int insertRestaurant(String var1, String var2, String var3, String var4, String var5, String var6) throws SQLException {
      String var7 = "INSERT INTO restaurants(name, description, email, phone, address, logo_url) VALUES(?,?,?,?,?,?) RETURNING id";
      PreparedStatement var8 = this.conn.prepareStatement(var7);

      int var10;
      try {
         var8.setString(1, var1);
         var8.setString(2, var2);
         var8.setString(3, var3);
         var8.setString(4, var4);
         var8.setString(5, var5);
         var8.setString(6, var6);
         ResultSet var9 = var8.executeQuery();

         try {
            var9.next();
            var10 = var9.getInt(1);
         } catch (Throwable var14) {
            if (var9 != null) {
               try {
                  var9.close();
               } catch (Throwable var13) {
                  var14.addSuppressed(var13);
               }
            }

            throw var14;
         }

         if (var9 != null) {
            var9.close();
         }
      } catch (Throwable var15) {
         if (var8 != null) {
            try {
               var8.close();
            } catch (Throwable var12) {
               var15.addSuppressed(var12);
            }
         }

         throw var15;
      }

      if (var8 != null) {
         var8.close();
      }

      return var10;
   }

   private int insertCategory(int var1, String var2) throws SQLException {
      String var3 = "INSERT INTO categories(restaurant_id, name) VALUES(?,?) RETURNING id";
      PreparedStatement var4 = this.conn.prepareStatement(var3);

      int var6;
      try {
         var4.setInt(1, var1);
         var4.setString(2, var2);
         ResultSet var5 = var4.executeQuery();

         try {
            var5.next();
            var6 = var5.getInt(1);
         } catch (Throwable var10) {
            if (var5 != null) {
               try {
                  var5.close();
               } catch (Throwable var9) {
                  var10.addSuppressed(var9);
               }
            }

            throw var10;
         }

         if (var5 != null) {
            var5.close();
         }
      } catch (Throwable var11) {
         if (var4 != null) {
            try {
               var4.close();
            } catch (Throwable var8) {
               var11.addSuppressed(var8);
            }
         }

         throw var11;
      }

      if (var4 != null) {
         var4.close();
      }

      return var6;
   }

   private int insertIngredient(int var1, String var2, String var3, double var4, double var6) throws SQLException {
      String var8 = "INSERT INTO ingredients(restaurant_id, name, unit, stock_quantity, cost_unit) VALUES(?,?,?,?,?) RETURNING id";
      PreparedStatement var9 = this.conn.prepareStatement(var8);

      int var11;
      try {
         var9.setInt(1, var1);
         var9.setString(2, var2);
         var9.setString(3, var3);
         var9.setDouble(4, var4);
         var9.setDouble(5, var6);
         ResultSet var10 = var9.executeQuery();

         try {
            var10.next();
            var11 = var10.getInt(1);
         } catch (Throwable var15) {
            if (var10 != null) {
               try {
                  var10.close();
               } catch (Throwable var14) {
                  var15.addSuppressed(var14);
               }
            }

            throw var15;
         }

         if (var10 != null) {
            var10.close();
         }
      } catch (Throwable var16) {
         if (var9 != null) {
            try {
               var9.close();
            } catch (Throwable var13) {
               var16.addSuppressed(var13);
            }
         }

         throw var16;
      }

      if (var9 != null) {
         var9.close();
      }

      return var11;
   }

   private int insertDish(int var1, int var2, String var3, String var4, double var5, boolean var7) throws SQLException {
      String var8 = "INSERT INTO dishes(restaurant_id, category_id, name, description, price, available) VALUES(?,?,?,?,?,?) RETURNING id";
      PreparedStatement var9 = this.conn.prepareStatement(var8);

      int var11;
      try {
         var9.setInt(1, var1);
         var9.setInt(2, var2);
         var9.setString(3, var3);
         var9.setString(4, var4);
         var9.setDouble(5, var5);
         var9.setBoolean(6, var7);
         ResultSet var10 = var9.executeQuery();

         try {
            var10.next();
            var11 = var10.getInt(1);
         } catch (Throwable var15) {
            if (var10 != null) {
               try {
                  var10.close();
               } catch (Throwable var14) {
                  var15.addSuppressed(var14);
               }
            }

            throw var15;
         }

         if (var10 != null) {
            var10.close();
         }
      } catch (Throwable var16) {
         if (var9 != null) {
            try {
               var9.close();
            } catch (Throwable var13) {
               var16.addSuppressed(var13);
            }
         }

         throw var16;
      }

      if (var9 != null) {
         var9.close();
      }

      return var11;
   }

   private void insertDishIngredient(int var1, int var2, double var3) throws SQLException {
      String var5 = "INSERT INTO dish_ingredients(dish_id, ingredient_id, quantity) VALUES(?,?,?) ON CONFLICT DO NOTHING";
      PreparedStatement var6 = this.conn.prepareStatement(var5);

      try {
         var6.setInt(1, var1);
         var6.setInt(2, var2);
         var6.setDouble(3, var3);
         var6.executeUpdate();
      } catch (Throwable var10) {
         if (var6 != null) {
            try {
               var6.close();
            } catch (Throwable var9) {
               var10.addSuppressed(var9);
            }
         }

         throw var10;
      }

      if (var6 != null) {
         var6.close();
      }

   }

   private int insertClient(int var1, String var2, String var3, String var4) throws SQLException {
      String var5 = "INSERT INTO clients(restaurant_id, name, email, password_hash) VALUES(?,?,?,?) RETURNING id";

      try {
         PreparedStatement var6 = this.conn.prepareStatement(var5);

         int var8;
         try {
            var6.setInt(1, var1);
            var6.setString(2, var2);
            var6.setString(3, var3);
            var6.setString(4, var4);
            ResultSet var7 = var6.executeQuery();

            try {
               var7.next();
               var8 = var7.getInt(1);
            } catch (Throwable var12) {
               if (var7 != null) {
                  try {
                     var7.close();
                  } catch (Throwable var11) {
                     var12.addSuppressed(var11);
                  }
               }

               throw var12;
            }

            if (var7 != null) {
               var7.close();
            }
         } catch (Throwable var13) {
            if (var6 != null) {
               try {
                  var6.close();
               } catch (Throwable var10) {
                  var13.addSuppressed(var10);
               }
            }

            throw var13;
         }

         if (var6 != null) {
            var6.close();
         }

         return var8;
      } catch (SQLException var14) {
         System.err.println("Warning inserting client: " + var14.getMessage());
         return -1;
      }
   }

   private int insertTableSection(int var1, String var2) throws SQLException {
      String var3 = "INSERT INTO table_sections(restaurant_id, title) VALUES(?,?) RETURNING id";
      PreparedStatement var4 = this.conn.prepareStatement(var3);

      int var6;
      try {
         var4.setInt(1, var1);
         var4.setString(2, var2);
         ResultSet var5 = var4.executeQuery();

         try {
            var5.next();
            var6 = var5.getInt(1);
         } catch (Throwable var10) {
            if (var5 != null) {
               try {
                  var5.close();
               } catch (Throwable var9) {
                  var10.addSuppressed(var9);
               }
            }

            throw var10;
         }

         if (var5 != null) {
            var5.close();
         }
      } catch (Throwable var11) {
         if (var4 != null) {
            try {
               var4.close();
            } catch (Throwable var8) {
               var11.addSuppressed(var8);
            }
         }

         throw var11;
      }

      if (var4 != null) {
         var4.close();
      }

      return var6;
   }

   private int insertTable(int var1, int var2, String var3, int var4, Integer var5, Integer var6) throws SQLException {
      String var7 = "INSERT INTO tables_restaurant(restaurant_id, section_id, name, capacity, pos_x, pos_y) VALUES(?,?,?,?,?,?) RETURNING id";
      PreparedStatement var8 = this.conn.prepareStatement(var7);

      int var10;
      try {
         var8.setInt(1, var1);
         var8.setInt(2, var2);
         var8.setString(3, var3);
         var8.setInt(4, var4);
         var8.setObject(5, var5);
         var8.setObject(6, var6);
         ResultSet var9 = var8.executeQuery();

         try {
            var9.next();
            var10 = var9.getInt(1);
         } catch (Throwable var14) {
            if (var9 != null) {
               try {
                  var9.close();
               } catch (Throwable var13) {
                  var14.addSuppressed(var13);
               }
            }

            throw var14;
         }

         if (var9 != null) {
            var9.close();
         }
      } catch (Throwable var15) {
         if (var8 != null) {
            try {
               var8.close();
            } catch (Throwable var12) {
               var15.addSuppressed(var12);
            }
         }

         throw var15;
      }

      if (var8 != null) {
         var8.close();
      }

      return var10;
   }

   private int insertOrder(int var1, Integer var2, int var3, String var4, Integer var5, int var6, double var7, String var9) throws SQLException {
      String var10 = "INSERT INTO orders(restaurant_id, table_id, order_type_id, delivery_address, client_id, status_id, total, notes) VALUES(?,?,?,?,?,?,?,?) RETURNING id";
      PreparedStatement var11 = this.conn.prepareStatement(var10);

      int var13;
      try {
         var11.setInt(1, var1);
         var11.setObject(2, var2);
         var11.setInt(3, var3);
         var11.setString(4, var4);
         var11.setObject(5, var5);
         var11.setInt(6, var6);
         var11.setDouble(7, var7);
         var11.setString(8, var9);
         ResultSet var12 = var11.executeQuery();

         try {
            var12.next();
            var13 = var12.getInt(1);
         } catch (Throwable var17) {
            if (var12 != null) {
               try {
                  var12.close();
               } catch (Throwable var16) {
                  var17.addSuppressed(var16);
               }
            }

            throw var17;
         }

         if (var12 != null) {
            var12.close();
         }
      } catch (Throwable var18) {
         if (var11 != null) {
            try {
               var11.close();
            } catch (Throwable var15) {
               var18.addSuppressed(var15);
            }
         }

         throw var18;
      }

      if (var11 != null) {
         var11.close();
      }

      return var13;
   }

   private void insertOrderItem(int var1, int var2, String var3, double var4) throws SQLException {
      String var6 = "INSERT INTO order_items(order_id, dish_id, notes, unit_price) VALUES(?,?,?,?)";
      PreparedStatement var7 = this.conn.prepareStatement(var6);

      try {
         var7.setInt(1, var1);
         var7.setInt(2, var2);
         var7.setString(3, var3);
         var7.setDouble(4, var4);
         var7.executeUpdate();
      } catch (Throwable var11) {
         if (var7 != null) {
            try {
               var7.close();
            } catch (Throwable var10) {
               var11.addSuppressed(var10);
            }
         }

         throw var11;
      }

      if (var7 != null) {
         var7.close();
      }

   }

   private void updateOrderTotal(int var1, double var2) throws SQLException {
      PreparedStatement var4 = this.conn.prepareStatement("UPDATE orders SET total = ? WHERE id = ?");

      try {
         var4.setDouble(1, var2);
         var4.setInt(2, var1);
         var4.executeUpdate();
      } catch (Throwable var8) {
         if (var4 != null) {
            try {
               var4.close();
            } catch (Throwable var7) {
               var8.addSuppressed(var7);
            }
         }

         throw var8;
      }

      if (var4 != null) {
         var4.close();
      }

   }

   private void insertPayment(int var1, String var2, double var3) throws SQLException {
      String var5 = "INSERT INTO payments(order_id, method, amount) VALUES(?,?,?)";
      PreparedStatement var6 = this.conn.prepareStatement(var5);

      try {
         var6.setInt(1, var1);
         var6.setString(2, var2);
         var6.setDouble(3, var3);
         var6.executeUpdate();
      } catch (Throwable var10) {
         if (var6 != null) {
            try {
               var6.close();
            } catch (Throwable var9) {
               var10.addSuppressed(var9);
            }
         }

         throw var10;
      }

      if (var6 != null) {
         var6.close();
      }

   }

   private int selectRandomIngredientId(int var1) throws SQLException {
      PreparedStatement var2 = this.conn.prepareStatement("SELECT id FROM ingredients WHERE restaurant_id = ? ORDER BY random() LIMIT 1");

      int var4;
      label67: {
         try {
            var2.setInt(1, var1);
            ResultSet var3 = var2.executeQuery();

            label69: {
               try {
                  if (!var3.next()) {
                     break label69;
                  }

                  var4 = var3.getInt(1);
               } catch (Throwable var8) {
                  if (var3 != null) {
                     try {
                        var3.close();
                     } catch (Throwable var7) {
                        var8.addSuppressed(var7);
                     }
                  }

                  throw var8;
               }

               if (var3 != null) {
                  var3.close();
               }
               break label67;
            }

            if (var3 != null) {
               var3.close();
            }
         } catch (Throwable var9) {
            if (var2 != null) {
               try {
                  var2.close();
               } catch (Throwable var6) {
                  var9.addSuppressed(var6);
               }
            }

            throw var9;
         }

         if (var2 != null) {
            var2.close();
         }

         return -1;
      }

      if (var2 != null) {
         var2.close();
      }

      return var4;
   }

   private int selectRandomDishId(int var1) throws SQLException {
      PreparedStatement var2 = this.conn.prepareStatement("SELECT id FROM dishes WHERE restaurant_id = ? ORDER BY random() LIMIT 1");

      int var4;
      label67: {
         try {
            var2.setInt(1, var1);
            ResultSet var3 = var2.executeQuery();

            label69: {
               try {
                  if (!var3.next()) {
                     break label69;
                  }

                  var4 = var3.getInt(1);
               } catch (Throwable var8) {
                  if (var3 != null) {
                     try {
                        var3.close();
                     } catch (Throwable var7) {
                        var8.addSuppressed(var7);
                     }
                  }

                  throw var8;
               }

               if (var3 != null) {
                  var3.close();
               }
               break label67;
            }

            if (var3 != null) {
               var3.close();
            }
         } catch (Throwable var9) {
            if (var2 != null) {
               try {
                  var2.close();
               } catch (Throwable var6) {
                  var9.addSuppressed(var6);
               }
            }

            throw var9;
         }

         if (var2 != null) {
            var2.close();
         }

         return -1;
      }

      if (var2 != null) {
         var2.close();
      }

      return var4;
   }

   private double selectDishPrice(int var1) throws SQLException {
      if (var1 <= 0) {
         return (double)0.0F;
      } else {
         PreparedStatement var2 = this.conn.prepareStatement("SELECT price FROM dishes WHERE id = ?");

         double var4;
         label71: {
            try {
               var2.setInt(1, var1);
               ResultSet var3 = var2.executeQuery();

               label73: {
                  try {
                     if (!var3.next()) {
                        break label73;
                     }

                     var4 = var3.getDouble(1);
                  } catch (Throwable var8) {
                     if (var3 != null) {
                        try {
                           var3.close();
                        } catch (Throwable var7) {
                           var8.addSuppressed(var7);
                        }
                     }

                     throw var8;
                  }

                  if (var3 != null) {
                     var3.close();
                  }
                  break label71;
               }

               if (var3 != null) {
                  var3.close();
               }
            } catch (Throwable var9) {
               if (var2 != null) {
                  try {
                     var2.close();
                  } catch (Throwable var6) {
                     var9.addSuppressed(var6);
                  }
               }

               throw var9;
            }

            if (var2 != null) {
               var2.close();
            }

            return (double)0.0F;
         }

         if (var2 != null) {
            var2.close();
         }

         return var4;
      }
   }

   private int selectRandomClientId(int var1) throws SQLException {
      PreparedStatement var2 = this.conn.prepareStatement("SELECT id FROM clients WHERE restaurant_id = ? ORDER BY random() LIMIT 1");

      int var4;
      label67: {
         try {
            var2.setInt(1, var1);
            ResultSet var3 = var2.executeQuery();

            label69: {
               try {
                  if (!var3.next()) {
                     break label69;
                  }

                  var4 = var3.getInt(1);
               } catch (Throwable var8) {
                  if (var3 != null) {
                     try {
                        var3.close();
                     } catch (Throwable var7) {
                        var8.addSuppressed(var7);
                     }
                  }

                  throw var8;
               }

               if (var3 != null) {
                  var3.close();
               }
               break label67;
            }

            if (var3 != null) {
               var3.close();
            }
         } catch (Throwable var9) {
            if (var2 != null) {
               try {
                  var2.close();
               } catch (Throwable var6) {
                  var9.addSuppressed(var6);
               }
            }

            throw var9;
         }

         if (var2 != null) {
            var2.close();
         }

         return -1;
      }

      if (var2 != null) {
         var2.close();
      }

      return var4;
   }

   private Integer selectRandomTableId(int var1) throws SQLException {
      PreparedStatement var2 = this.conn.prepareStatement("SELECT id FROM tables_restaurant WHERE restaurant_id = ? ORDER BY random() LIMIT 1");

      Integer var4;
      label67: {
         try {
            var2.setInt(1, var1);
            ResultSet var3 = var2.executeQuery();

            label69: {
               try {
                  if (!var3.next()) {
                     break label69;
                  }

                  var4 = var3.getInt(1);
               } catch (Throwable var8) {
                  if (var3 != null) {
                     try {
                        var3.close();
                     } catch (Throwable var7) {
                        var8.addSuppressed(var7);
                     }
                  }

                  throw var8;
               }

               if (var3 != null) {
                  var3.close();
               }
               break label67;
            }

            if (var3 != null) {
               var3.close();
            }
         } catch (Throwable var9) {
            if (var2 != null) {
               try {
                  var2.close();
               } catch (Throwable var6) {
                  var9.addSuppressed(var6);
               }
            }

            throw var9;
         }

         if (var2 != null) {
            var2.close();
         }

         return null;
      }

      if (var2 != null) {
         var2.close();
      }

      return var4;
   }

   private int selectOrderTypeIdByName(String var1) throws SQLException {
      PreparedStatement var2 = this.conn.prepareStatement("SELECT id FROM order_type WHERE name = ? LIMIT 1");

      int var4;
      label67: {
         try {
            var2.setString(1, var1);
            ResultSet var3 = var2.executeQuery();

            label69: {
               try {
                  if (!var3.next()) {
                     break label69;
                  }

                  var4 = var3.getInt(1);
               } catch (Throwable var8) {
                  if (var3 != null) {
                     try {
                        var3.close();
                     } catch (Throwable var7) {
                        var8.addSuppressed(var7);
                     }
                  }

                  throw var8;
               }

               if (var3 != null) {
                  var3.close();
               }
               break label67;
            }

            if (var3 != null) {
               var3.close();
            }
         } catch (Throwable var9) {
            if (var2 != null) {
               try {
                  var2.close();
               } catch (Throwable var6) {
                  var9.addSuppressed(var6);
               }
            }

            throw var9;
         }

         if (var2 != null) {
            var2.close();
         }

         return 1;
      }

      if (var2 != null) {
         var2.close();
      }

      return var4;
   }

   private int selectOrderStatusByName(String var1) throws SQLException {
      PreparedStatement var2 = this.conn.prepareStatement("SELECT id FROM order_status WHERE name = ? LIMIT 1");

      int var4;
      label67: {
         try {
            var2.setString(1, var1);
            ResultSet var3 = var2.executeQuery();

            label69: {
               try {
                  if (!var3.next()) {
                     break label69;
                  }

                  var4 = var3.getInt(1);
               } catch (Throwable var8) {
                  if (var3 != null) {
                     try {
                        var3.close();
                     } catch (Throwable var7) {
                        var8.addSuppressed(var7);
                     }
                  }

                  throw var8;
               }

               if (var3 != null) {
                  var3.close();
               }
               break label67;
            }

            if (var3 != null) {
               var3.close();
            }
         } catch (Throwable var9) {
            if (var2 != null) {
               try {
                  var2.close();
               } catch (Throwable var6) {
                  var9.addSuppressed(var6);
               }
            }

            throw var9;
         }

         if (var2 != null) {
            var2.close();
         }

         return 1;
      }

      if (var2 != null) {
         var2.close();
      }

      return var4;
   }

   private int round(double var1, int var3) {
      double var4 = Math.pow((double)10.0F, (double)var3);
      return (int)Math.round(var1 * var4) / (int)var4;
   }

   private double round(double var1, double var3) {
      return (double)Math.round(var1 * (double)100.0F) / (double)100.0F;
   }
}
