import java.util.List;
import org.sql2o.*;
import java.util.ArrayList;


public class Brand {
  private int id;
  private String brand_name;


  public int getId() {
    return id;
  }

  public String getBrand() {
    return brand_name;
  }

  public Brand(String brand_name) {
    this.brand_name = brand_name;
  }

  @Override
  public boolean equals(Object otherBrand) {
    if(!(otherBrand instanceof Brand)) {
      return false;
    } else {
      Brand newBrand = (Brand) otherBrand;
      return this.getBrand().equals(newBrand.getBrand());
    }
  }

  public static List<Brand> all() {
    String sql = "SELECT id, brand_name FROM brands";
    try(Connection con = DB.sql2o.open()) {
      return con.createQuery(sql).executeAndFetch(Brand.class);
    }
  }

  public void save() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO brands (brand_name) VALUES (:brand_name)";
      this.id = (int) con.createQuery(sql, true)
        .addParameter("brand_name", brand_name)
        .executeUpdate()
        .getKey();
    }
  }

  public static Brand find(int id) {
    try(Connection con = DB.sql2o.open()){
      String sql ="SELECT * FROM brands WHERE id=:id ORDER BY brand_name ASC";
      Brand brand = con.createQuery(sql)
      .addParameter("id", id)
      .executeAndFetchFirst(Brand.class);
      return brand;
    }
  }

  public void update(String brand_name) {
  this.brand_name = brand_name;
  try(Connection con = DB.sql2o.open()){
    String sql = "UPDATE brands SET brand_name=:brand_name WHERE id=:id";
    con.createQuery(sql)
      .addParameter("brand_name", brand_name)
      .addParameter("id", id)
      .executeUpdate();
  }
}

  public void addStore(Store store) {
      try(Connection con = DB.sql2o.open()) {
        String sql = "INSERT INTO stores_brands (brand_id, store_id) VALUES (:brand_id, :store_id)";
        con.createQuery(sql)
          .addParameter("brand_id", this.getId())
          .addParameter("store_id", store.getId())
          .executeUpdate();
      }
    }


public ArrayList<Store> getStores() {
  //grabs student ids from a course
  try(Connection con = DB.sql2o.open()) {
    String sql = "SELECT store_id FROM stores_brands WHERE brand_id=:brand_id";
    List<Integer> storeIds = con.createQuery(sql)
    .addParameter("brand_id", this.getId())
    .executeAndFetch(Integer.class);
    if(storeIds == null)
      return null;

    //declare empty array to push all students ids that match to the courseid
    ArrayList<Store> stores = new ArrayList<Store>();

    //looping through the student index in order to grab all students that match course_id
    
    for(Integer index : storeIds) { //for index in student Ids
      String storeQuery = "SELECT * FROM stores WHERE Id = :index";
      Store store = con.createQuery(storeQuery)
        .addParameter("index", index)
        .executeAndFetchFirst(Store.class);
        stores.add(store);
    }
    return stores;
  }
}

public void delete() {
  try(Connection con = DB.sql2o.open()) {
    String deleteQuery = "DELETE FROM brands WHERE id=:id";
      con.createQuery(deleteQuery)
        .addParameter("id", id)
        .executeUpdate();

    String joinDeleteQuery = "DELETE FROM stores_brands WHERE brand_id =:brand_id";
      con.createQuery(joinDeleteQuery)
        .addParameter("brand_id", this.getId())
        .executeUpdate();
  }
}


}//ends class Course
