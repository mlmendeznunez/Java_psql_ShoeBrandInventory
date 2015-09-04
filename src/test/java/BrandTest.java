import java.util.Arrays;
import org.junit.*;
import static org.junit.Assert.*;
import org.junit.Rule;
import java.util.List;
import java.util.ArrayList;

public class BrandTest {

  @Rule
  public DatabaseRule database = new DatabaseRule();

  @Test
  public void all_emptyAtFirst() {
    assertEquals(Brand.all().size(), 0);
  }

  @Test
  public void equals_returnsTrueIfBrandsAreTheSame() {
    Brand firstBrand = new Brand ("Nike");
    Brand secondBrand = new Brand ("Nike");
    assertTrue(firstBrand.equals(secondBrand));
  }

  @Test
  public void save_savesBrandIntoDatabase_true () {
    Brand newBrand = new Brand ("Adidas");
    newBrand.save();
    assertTrue(Brand.all().get(0).equals(newBrand));
  }

  @Test
  public void find_findsBrandInDatabase_true() {
    Brand myBrand = new Brand("Sketchers");
    myBrand.save();
    Brand savedBrand = Brand.find(myBrand.getId());
    assertTrue(myBrand.equals(savedBrand));
  }

  @Test
  public void update_updatesBrandNameInDatabase_true(){
    Brand myBrand = new Brand("Puma");
    myBrand.save();
    String brand_name = "Saucony";
    myBrand.update(brand_name);
    assertTrue(Brand.all().get(0).getBrand().equals(brand_name));
  }

  @Test
  public void getStores_returnsAllStores_List() {
    Store myStore = new Store("FootFactory");
    myStore.save();
    assertTrue(myStore.equals(Store.all().get(0)));
  }

  @Test
  public void delete_deletesBrandsAndListAssociations() {
    Store myStore = new Store("FootFactory");
    myStore.save();

    Brand myBrand = new Brand("Asics");
    myBrand.save();

    myBrand.addStore(myStore);
    myBrand.delete();
    assertEquals(myStore.getBrands().size(), 0);
  }



}//end BookTest class
