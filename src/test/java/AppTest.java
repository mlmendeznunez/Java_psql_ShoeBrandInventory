import org.fluentlenium.adapter.FluentTest;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.junit.rules.ExternalResource;
import org.sql2o.*;

import static org.fluentlenium.core.filter.FilterConstructor.*;
import static org.assertj.core.api.Assertions.assertThat;

public class AppTest extends FluentTest{
  public WebDriver webDriver = new HtmlUnitDriver();
  public WebDriver getDefaultDriver(){
    return webDriver;
  }

  @ClassRule
  public static ServerRule server = new ServerRule();

  @Rule
  public DatabaseRule database = new DatabaseRule();

  @Test
  public void rootTest() {
    goTo("http://localhost:4567/");
    assertThat(pageSource().contains("Shoe Stores"));
  }

  void AddBrand()
  {
    goTo("http://localhost:4567/");
    click("a", withText("Add Brand"));
    assertThat(pageSource().contains("Add Shoe Brand"));
    fill("#brand_name").with("Nike");
    submit("#submit");
  }

  @Test
  public void addBrandOnlyTest() {
    AddBrand();
    assertThat(pageSource().contains("Nike"));
    assertThat(pageSource().contains("You cannot add"));

    goTo("http://localhost:4567/");
    click("a", withText("Search All Brands"));
    assertThat(pageSource()).contains("Nike");

  }

  void AddStore()
  {
    goTo("http://localhost:4567/");
    click("a", withText("Add Store"));
    assertThat(pageSource().contains("Add Store"));
    fill("#store_name").with("Stylish");
    submit("#submit");
  }

  @Test
  public void addStoreOnlyTest() {
    AddStore();
    assertThat(pageSource().contains("Stylish"));
    assertThat(pageSource().contains("You cannot add"));

    goTo("http://localhost:4567/");
    click("a", withText("Search All Stores"));
    assertThat(pageSource().contains("Stylish"));

  }

  void AddBrandAddStore()
  { 
    AddBrand();
    AddStore();
    assertThat(pageSource().contains("Nike"));
    click("option", withText("Nike"));
    submit("#submit");
  }

  void AddStoreAddBrand()
  {
    AddStore();
    AddBrand();
    assertThat(pageSource().contains("Stylish"));
    click("option", withText("Stylish"));
    submit("#submit");
  }

  void AddBothTests()
  {
    goTo("http://localhost:4567/");
    click("a", withText("Search All Brands"));
    assertThat(pageSource().contains("Nike"));
    assertThat(pageSource().contains("Stylish"));

    click("a", withText("Nike"));
    assertThat(pageSource().contains("Stylish"));

    goTo("http://localhost:4567/");
    click("a", withText("Search All Stores"));
    assertThat(pageSource().contains("Nike"));
    assertThat(pageSource().contains("Stylish"));
    
    click("a", withText("Stylish"));
    assertThat(pageSource().contains("Nike"));
  }

  @Test
  public void addBrandAddStoreTest() {
    AddBrandAddStore();

    AddBothTests();
  }

  @Test
  public void addStoreAddBrandTest() {
    AddStoreAddBrand();

    AddBothTests();
  }


  void RemoveBrand()
  {
    goTo("http://localhost:4567/");
    click("a", withText("Search All Brands"));
    find("tr", withText().contains("Nike")).find("a", withName("delete")).click();
  }

  @Test
  public void addBrandRemoveBrandTest() {
    AddBrand();
    RemoveBrand();
    goTo("http://localhost:4567/");
    click("a", withText("Search All Brands"));
    assertThat(!(pageSource().contains("Nike")));
  }

  @Test
  public void editBrandTest(){
    AddBrand();
    goTo("http://localhost:4567/");
    click("a", withText("Search All Brands"));
    find("tr", withText().contains("Nike")).find("a", withName("edit")).click();
    fill("#brand_name").with("Adidas");
    submit("#update");
    assertThat(pageSource().contains("Adidas"));
  }

}
