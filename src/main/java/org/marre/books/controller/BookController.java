package org.marre.books.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.hibernate.validator.constraints.Length;
import org.marre.books.domain.Book;
import org.marre.books.domain.BookPrototype;
import org.marre.books.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/v1/books")
@Validated
public class BookController {
    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @RequestMapping(
            value = "/",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseBody
    @ApiOperation(value = "Add book")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Book added", response = Book.class)
    })
    public Book add(@ApiParam(value = "book", required = true) @RequestBody @Valid BookPrototype bookPrototype) {
        return bookService.add(bookPrototype);
    }

    @RequestMapping(
            value = "/",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseBody
    @ApiOperation(value = "Retrieve all books")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Books retrieved", response = Book.class)
    })
    public List<Book> findAll() {
        return bookService.findAll();
    }

    @RequestMapping(
            value = "/id/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiOperation(value = "Find books by ID")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Book retrieved", response = Book.class),
    })
    public ResponseEntity<Book> findById(
            @PathVariable(value="id") UUID id
    ) {
        Optional<Book> book = bookService.findById(id);
        return ResponseEntity.of(book);
    }

    @RequestMapping(
            value = "/isbn/{isbn}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseBody
    @ApiOperation(value = "Find books by ISBN")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Books retrieved", response = Book.class),
    })
    public List<Book> findByIsdn(
            @Valid
            @Length(min = 10, max = 10)
            @Pattern(regexp = "[0-9]+")
            @PathVariable(value="isbn") String isbn
    ) {
        return bookService.findByIsbn(isbn);
    }

    @ExceptionHandler({
            HttpMessageNotReadableException.class,
            MethodArgumentNotValidException.class,
            MethodArgumentTypeMismatchException.class,
            ConstraintViolationException.class,
    })
    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Invalid input")
    public void handleInputException() {
    }
}
