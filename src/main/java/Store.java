import java.util.List;
import org.sql2o.*;
import java.util.ArrayList;

public class Store {
  private int id;
  private String store_name;

  public int getId() {
    return id;
  }

  public String getStoreName() {
    return store_name;
  }

  public Store(String store_name) {
    this.store_name = store_name;
  }

  @Override
  public boolean equals(Object otherStore) {
    if(!(otherStore instanceof Store)) {
      return false;
    } else {
      Store newStore = (Store) otherStore;
      return this.getStoreName().equals(newStore.getStoreName());
    }
  }

  public static List<Store> all() {
    String sql = "SELECT * FROM stores ORDER BY store_name ASC";
    try(Connection con = DB.sql2o.open()) {
      return con.createQuery(sql).executeAndFetch(Store.class);
    }
  }

  public void save() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO stores (store_name) VALUES (:store_name)";
      this.id = (int) con.createQuery(sql, true)
      .addParameter("store_name", this.store_name)
      .executeUpdate()
      .getKey();
    }
  }

  public static Store find(int id) {
      try(Connection con = DB.sql2o.open()) {
        String sql = "SELECT * FROM stores WHERE id=:id ORDER BY store_name ASC";
        Store store = con.createQuery(sql)
          .addParameter("id", id)
          .executeAndFetchFirst(Store.class);
        return store;
      }
    }

    public void update(String store_name) {
      this.store_name = store_name;
        try(Connection con = DB.sql2o.open()) {
          String sql = "UPDATE stores SET store_name=:store_name WHERE id=:id";
          con.createQuery(sql)
            .addParameter("store_name", store_name)
            .addParameter("id", id) //why do we need the id here but not in others?
            .executeUpdate();
        }
    }

    public void addBrand(Brand brand) {
      try(Connection con = DB.sql2o.open()) {
        String sql = "INSERT INTO stores_brands (brand_id, store_id) VALUES ( :brand_id, :store_id)";
        con.createQuery(sql)
          .addParameter("brand_id", brand.getId())
          .addParameter("store_id", this.getId())
          .executeUpdate();
      }
    }

    public ArrayList<Brand> getBrands() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT brand_id FROM stores_brands WHERE store_id =:store_id";
      List<Integer> brandIds = con.createQuery(sql)
        .addParameter("store_id", this.getId())
        .executeAndFetch(Integer.class);

        ArrayList<Brand> brands = new ArrayList<Brand>();

      for(Integer index : brandIds) {
        String brandQuery = "SELECT * FROM brands WHERE id=:index";
        Brand brand = con.createQuery(brandQuery)
          .addParameter("index", index)
          .executeAndFetchFirst(Brand.class);
        brands.add(brand);
      }return brands;
    }
  }
    public void delete() {
    try(Connection con = DB.sql2o.open()) {
      String deleteQuery = "DELETE FROM stores WHERE id=:id";
      con.createQuery(deleteQuery)
      .addParameter("id", id)
      .executeUpdate();

      String joinDeleteQuery = "DELETE from stores_brands WHERE store_id =:store_id";
      con.createQuery(joinDeleteQuery)
        .addParameter("store_id", this.getId())
        .executeUpdate();
    }
  }


}//ends class Author
