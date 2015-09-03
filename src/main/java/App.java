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

    //Admin page for Adding/Searching/Editing Catalog
    get("/adminhome", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      model.put("template", "templates/admin.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    //gets Add Book form
    get("/add-book", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();

      model.put("template", "templates/newbook.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/add-book", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      String title = request.queryParams("title");
      String genre = request.queryParams("genre");
      int copies = Integer.parseInt(request.queryParams("copies"));

      Book book = new Book(title, genre, copies);
      book.save();

      model.put("authors", Author.all());
      model.put("book", book); //you may have one object of an array without having the whole array
      model.put("template", "templates/newbook.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/books/:id/addauthor", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      Book book = Book.find(Integer.parseInt(request.params(":id")));

      Author author = Author.find(Integer.parseInt(request.queryParams("author_id"))); //inputing name from html
      book.addAuthor(author);

      model.put("book", book); //b/c it is rerendered each time, must put in all info
      model.put("authors", Author.all());
      model.put("template", "templates/newbook.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    //gets Add Author form
    get("/add-author", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();

      model.put("template", "templates/newauthor.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());


    post("/add-author", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      String name = request.queryParams("name");

      Author newAuthor = new Author(name);
      newAuthor.save();

      model.put("template", "templates/newauthor.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());


    //Get page to view and update authors in db
    get("/all-authors", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      model.put("authors", Author.all());
      model.put("template", "templates/all-authors.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/all-books", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      model.put("books", Book.all());
      model.put("template", "templates/all-books.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());


    // get("/authors/:id/addbook", (request, response) -> {
    //   HashMap<String, Object> model = new HashMap<String, Object>();
    //
    //   Author author = Author.find(Integer.parseInt(request.params(":id")));
    //
    //   model.put("author", author);
    //   model.put("template", "templates/newauthor.vtl");
    //   return new ModelAndView(model, layout);
    // }, new VelocityTemplateEngine());

    post("/authors/:id/addbook", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      Author author = Author.find(Integer.parseInt(request.params(":id")));
      int bookId = Integer.parseInt(request.queryParams("book_id"));
      Book newBook = Book.find(bookId);
      author.addBook(newBook);

      response.redirect("/authors/" + author.getId() + "/addbook");
      return null;
    });

    // get("/books/:id/addauthor", (request, response) -> {
    //   HashMap<String, Object> model = new HashMap<String, Object>();
    //
    //   Book book = Book.find(Integer.parseInt(request.params(":id")));
    //
    //
    //   // model.put("book", newBook);
    //   model.put("books", Book.all());
    //   model.put("authors", Author.all());
    //   model.put("template", "templates/newbook.vtl");
    //   return new ModelAndView(model, layout);
    // }, new VelocityTemplateEngine());






  }//end of main

}//end of app
