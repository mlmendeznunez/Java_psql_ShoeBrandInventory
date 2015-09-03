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

    //renders input for Add Author form and displays Add book to author form
    post("/add-author", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      String name = request.queryParams("name");

      Author author = new Author(name);
      author.save();

      model.put("author", author);
      model.put("books", Book.all());
      model.put("template", "templates/newauthor.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    //re-renders Add book to author form so author can be added to multiple books
    post("/authors/:id/addbook", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      Author author = Author.find(Integer.parseInt(request.params(":id")));

      Book book = Book.find(Integer.parseInt(request.queryParams("book_id")));
      author.addBook(book);

      model.put("author", author);
      model.put("books", Book.all());
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

    get("/all-patrons", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      model.put("patrons", Patron.all());
      model.put("template", "templates/all-patrons.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());


    //render page with form for editing a book in the catalog
    get("/books/:id/editbook", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      Book book = Book.find(Integer.parseInt(request.params(":id")));

      model.put("book", book);
      model.put("authors", Author.all());
      model.put("template", "templates/edit-books.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/books/:id/editbook", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      Book book = Book.find(Integer.parseInt(request.params(":id")));
      String title = request.queryParams("title");
      String genre = request.queryParams("genre");
      int copies = Integer.parseInt(request.queryParams("copies"));

      book.update(title, genre, copies);

      Author author = Author.find(Integer.parseInt(request.queryParams("author_id"))); //inputing name from html
      book.addAuthor(author);

      model.put("book", book);
      model.put("books", Book.all());
      model.put("template", "templates/all-books.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    //render page with form for deleting a book from the catalog
    get("/books/:id/delete", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      Book book = Book.find(Integer.parseInt(request.params(":id")));

      model.put("book", book);
      model.put("template", "templates/delete-books.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    //deletes book and renders list of all books in catalog
    post("/books/:id/delete", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      Book book = Book.find(Integer.parseInt(request.params(":id")));
      book.delete();

      //model.put("book", book);
      model.put("books", Book.all());
      model.put("template", "templates/all-books.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    //render page with form for editing an author in the catalog
    get("/authors/:id/editauthor", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      Author author = Author.find(Integer.parseInt(request.params(":id")));

      model.put("author", author);
      model.put("books", Book.all());
      model.put("template", "templates/edit-books.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());


    post("/authors/:id/editauthor", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      Author author = Author.find(Integer.parseInt(request.params(":id")));
      String name = request.queryParams("name");

      author.update(name);

      Book book = Book.find(Integer.parseInt(request.queryParams("book_id")));
      author.addBook(book);

      model.put("author", author);
      model.put("authors", Author.all());
      model.put("template", "template/all-authors.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/authors/:id/delete", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      Author author = Author.find(Integer.parseInt(request.params(":id")));

      //model.put("book", book);
      model.put("authors", Author.all());
      model.put("template", "templates/delete-authors.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/authors/:id/delete", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      Author author = Author.find(Integer.parseInt(request.params(":id")));
      author.delete();

      //model.put("author", author);
      model.put("authors", Author.all());
      model.put("template", "template/all-authors.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/add-patron", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();

      model.put("template", "templates/add-patron.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/add-patron", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      String name = request.queryParams("name");

      Patron patron = new Patron(name);
      patron.save();

      model.put("patrons", Patron.all());
      model.put("template", "templates/admin.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/delete-patron", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();

      model.put("template", "templates/delete-patron.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/delete-patron", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      String name = request.queryParams("name");

      Patron.search(name).delete(); //ask for explanation

      model.put("template", "templates/admin.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/patrons/:id/delete", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      Patron patron = Patron.find(Integer.parseInt(request.params(":id")));

      //model.put("book", book);
      model.put("patron", patron);
      model.put("patrons", Patron.all());
      model.put("template", "templates/delete-patronsearchpg.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/patrons/:id/delete", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      Patron patron = Patron.find(Integer.parseInt(request.params(":id")));

      patron.delete();

      //model.put("author", author);
      model.put("patrons", Patron.all());
      model.put("template", "templates/all-patrons.vtl");
      return new ModelAndView(model, layout);
      }, new VelocityTemplateEngine());

    get("/patrons/:id/editpatron", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      Patron patron = Patron.find(Integer.parseInt(request.params(":id")));

      model.put("patron", patron);
      model.put("template", "templates/edit-books.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/authors/:id/editauthor", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      Author author = Author.find(Integer.parseInt(request.params(":id")));
      String name = request.queryParams("patronname");

      patron.update(name);

      model.put("patron", patron);
      model.put("authors", Patron.all());
      model.put("template", "template/all-patrons.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());
  }//end of main

}//end of app
