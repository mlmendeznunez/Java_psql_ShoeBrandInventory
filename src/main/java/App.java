import java.util.HashMap;
import spark.ModelAndView;
import spark.template.velocity.VelocityTemplateEngine;
import static spark.Spark.*;
import java.util.List;
import java.util.Arrays;
import java.util.Set;

 public class App {
    public static void main(String[] args) {
    staticFileLocation("/public");
    String layout = "templates/layout.vtl";

    get("/", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      model.put("template", "templates/index.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

        //gets Add Brand Form
    get("/add-brand", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      model.put("template", "templates/add-brand.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/add-brand", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      String brand_name = request.queryParams("brand_name");

      Brand brand = new Brand(brand_name);
      brand.save();

      model.put("brand", brand);
      model.put("stores", Store.all());
      model.put("template", "templates/add-brand.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/brands/:id/addstore", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      Brand brand = Brand.find(Integer.parseInt(request.params(":id")));

      Store store = Store.find(Integer.parseInt(request.queryParams("store_id"))); //inputing name from html
      brand.addStore(store);

      model.put("brand", brand); //b/c it is rerendered each time, must put in all info
      model.put("stores", Store.all());
      model.put("template", "templates/newbook.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    //gets Add Store form
    get("/add-store", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      model.put("template", "templates/add-store.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());


    post("/add-store", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      String store_name = request.queryParams("store_name");

      Store newStore = new Store(store_name);
      newStore.save();

      model.put("template", "templates/new-store.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    //re-renders Add book to author form so author can be added to multiple books
    post("/stores/:id/addbook", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      Store store = Store.find(Integer.parseInt(request.params(":id")));

      Brand brand = Brand.find(Integer.parseInt(request.queryParams("brand_id")));
      store.addBrand(brand);

      model.put("store", store);
      model.put("brands", Brand.all());
      model.put("template", "templates/new-store.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    //Get page to view and update stores in db
    get("/all-stores", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      model.put("stores", Store.all());
      model.put("template", "templates/all-stores.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    //Get page to view and update brands in db
    get("/all-brands", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      model.put("books", Brand.all());
      model.put("template", "templates/all-brands.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    //render page with form for editing a brand in the inventory
    get("/brands/:id/editbrand", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      Brand brand = Brand.find(Integer.parseInt(request.params(":id")));

      model.put("brand", brand);
      model.put("stores", Store.all());
      model.put("template", "templates/edit-brands.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/brands/:id/editbrand", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      Brand brand = Brand.find(Integer.parseInt(request.params(":id")));
      String brand_name = request.queryParams("brand_name");

      brand.update(brand_name);

      Store store = Store.find(Integer.parseInt(request.queryParams("store_id"))); //inputing name from html
      brand.addStore(store);

      model.put("brand", brand);
      model.put("brands", Brand.all());
      model.put("template", "templates/all-brands.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    //render page with form for deleting a brand from the inventory
    get("/brands/:id/delete", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      Brand brand = Brand.find(Integer.parseInt(request.params(":id")));

      model.put("brand", brand);
      model.put("template", "templates/delete-brands.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    //deletes book and renders list of all a brand from the inventory
    post("/brands/:id/delete", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      Brand brand = Brand.find(Integer.parseInt(request.params(":id")));
      brand.delete();

      //model.put("book", book);
      model.put("brands", Brand.all());
      model.put("template", "templates/all-brands.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/stores/:id/editstore", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      Store store = Store.find(Integer.parseInt(request.params(":id")));

      model.put("store", store);
      model.put("brands", Brand.all());
      model.put("template", "templates/edit-books.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/stores/:id/editstore", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      Store store = Store.find(Integer.parseInt(request.params(":id")));
      String store_name = request.queryParams("store_name");
      store.update(store_name);

      Brand brand = Brand.find(Integer.parseInt(request.queryParams("brand_id")));
      store.addBrand(brand);

      model.put("store", store);
      model.put("stores", Store.all());
      model.put("template", "template/all-stores.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/stores/:id/delete", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      Store store = Store.find(Integer.parseInt(request.params(":id")));

      model.put("stores", Store.all());
      model.put("template", "templates/delete-stores.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/stores/:id/delete", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      Store store = Store.find(Integer.parseInt(request.params(":id")));
      store.delete();

      model.put("stores", Store.all());
      model.put("template", "template/all-stores.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());


  }//end of main

}//end of app
