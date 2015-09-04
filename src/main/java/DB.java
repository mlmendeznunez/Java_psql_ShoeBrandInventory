import org.sql2o.*;

public class DB {
  public static Sql2o sql2o = new Sql2o("jdbc:postgresql://127.0.0.1:5432/shoe_stores", null, null);
}
