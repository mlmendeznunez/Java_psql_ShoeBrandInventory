import org.junit.*;
import static org.junit.Assert.*;
import org.junit.Rule;
import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;

public class StoreTest {

     @Rule
     public DatabaseRule database = new DatabaseRule();

     @Test
     public void all_emptyAtFirst() {
       assertEquals(Store.all().size(), 0);
     }

     @Test
     public void equals_returnTrueIfStoresAreTheSame() {
       Store newStore = new Store("Shakespeare");
       Store secondStore = new Store("Shakespeare");
       assertTrue(newStore.equals(secondStore));
     }

     @Test
     public void save_savesIntoDatabase_true() {
       Store newStore = new Store("Shakespeare");
       newStore.save();
       assertTrue(Store.all().get(0).equals(newStore));
     }

     @Test
     public void find_findsStoreInDatabase_true() {
       Store myStore = new Store("Tolstoy");
       myStore.save();
       Store savedStore = Store.find(myStore.getId());
       assertTrue(myStore.equals(savedStore));
     }

     @Test
     public void update_changesStoreNameAndEnrollmentInDatabase_true() {
       Store myStore = new Store ("Tolstoy");
       myStore.save();
       String store_name = "Doestoevsky";
       myStore.update(store_name);
       assertTrue(Store.all().get(0).getStoreName().equals(store_name));
     }

     @Test
     public void addBrand_addsBrandToStore() {
       Brand myBrand = new Brand("JimmyChoo");
       myBrand.save();

       Store myStore = new Store("StylistFeet");
       myStore.save();

       myStore.addBrand(myBrand);
       Brand savedBrand = myStore.getBrands().get(0);
       assertTrue(myBrand.equals(savedBrand));
     }

     @Test
     public void getStores_returnsAllBrands_ArrayList() {
       Brand myBrand = new Brand ("JimmyChoo");
       myBrand.save();

       Store myStore = new Store("StylistFeet");
       myStore.save();

       myStore.addBrand(myBrand);
       List savedBrands = myStore.getBrands();
       assertEquals(savedBrands.size(), 1);
     }

     @Test
     public void delete_deletesAllStoresAndListAssociation () {
       Brand myBrand = new Brand("Gucci");
       myBrand.save();

       Store myStore = new Store("Tolstoy");
       myStore.save();

       myStore.addBrand(myBrand);
       myStore.delete();
       assertEquals(myBrand.getStores().size(), 0);
     }

 }//end StoreTest class
