package org.marre.books.controller;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.marre.books.domain.Book;
import org.marre.books.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebAppConfiguration
@SpringBootTest
public class BooksControllerTests {
	@Autowired
	private WebApplicationContext ctx;

	@Autowired
	private BookRepository repository;

	private MockMvc mockMvc;

	@Before
	public void before() {
		// Clear the list of books for every test run
		repository.deleteAll();

		mockMvc = MockMvcBuilders.webAppContextSetup(ctx).build();
	}

    @Test
    public void addBook() throws Exception {
        // Add book via JSON API
        String json = "{" +
                "  \"isbn\": \"1449373321\"," +
                "  \"title\": \"Designing Data-Intensive Applications\"," +
                "  \"authors\": [\"Martin Kleppman\"]" +
                "}";

        mockMvc.perform(post("/v1/books/").contentType(MediaType.APPLICATION_JSON_VALUE).content(json))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));

        // Verify that it actually was stored
        List<Book> books = repository.findByIsbn("1449373321");
        Assert.assertEquals(1, books.size());
        Book book = books.get(0);
        Assert.assertEquals("1449373321", book.getIsbn());
    }

    @Test
    public void addBookWithInvalidIsbn() throws Exception {
	    // ISBN with an 'A'
        String json = "{" +
                "  \"isbn\": \"144937332A\"," +
                "  \"title\": \"Designing Data-Intensive Applications\"," +
                "  \"authors\": [\"Martin Kleppman\"]" +
                "}";

        mockMvc.perform(post("/v1/books/").contentType(MediaType.APPLICATION_JSON_VALUE).content(json))
                .andExpect(status().is4xxClientError());
    }

    @Test
	public void lookupAll() throws Exception {
		// Add two copies of the same book
		storeBook(UUID.fromString("42dd4370-f04f-45f1-8c95-dd5d1563c786"), "1449373321", "Designing Data-Intensive Applications", "Martin Kleppman");
		storeBook(UUID.fromString("42dd4370-f04f-45f1-8c95-dd5d1563c787"), "1449373321", "Designing Data-Intensive Applications", "Martin Kleppman");

		// Verify that it is possible to retrieve via REST API
		mockMvc.perform(get("/v1/books/"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(content().json(
						"[{" +
								"  'id' = '42dd4370-f04f-45f1-8c95-dd5d1563c786'," +
								"  'isbn' = '1449373321'," +
								"  'title' = 'Designing Data-Intensive Applications'," +
								"  'authors' = ['Martin Kleppman']" +
								"}," +
								"{" +
								"  'id' = '42dd4370-f04f-45f1-8c95-dd5d1563c787'," +
								"  'isbn' = '1449373321'," +
								"  'title' = 'Designing Data-Intensive Applications'," +
								"  'authors' = ['Martin Kleppman']" +
								"}]"));
	}

	@Test
	public void lookupByIsbn() throws Exception {
		// Add some books
		storeBook(UUID.fromString("42dd4370-f04f-45f1-8c95-dd5d1563c786"), "1449373321", "Designing Data-Intensive Applications", "Martin Kleppman");
		storeBook(UUID.fromString("42dd4370-f04f-45f1-8c95-dd5d1563c787"), "1449373321", "Designing Data-Intensive Applications", "Martin Kleppman");
		storeBook(UUID.fromString("42dd4370-f04f-45f1-8c95-dd5d1563c788"), "1539692876", "Game Engine Black Book: Wolfenstein 3D", "Fabien Sanglard");

		// Lookup by ISBN
		mockMvc.perform(get("/v1/books/isbn/1539692876"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(content().json(
						"[{" +
								"  'id' = '42dd4370-f04f-45f1-8c95-dd5d1563c788'," +
								"  'isbn' = '1539692876'," +
								"  'title' = 'Game Engine Black Book: Wolfenstein 3D'," +
								"  'authors' = ['Fabien Sanglard']" +
								"}]"));
	}

	@Test
	public void lookupById() throws Exception {
		// Add some books
		storeBook(UUID.fromString("42dd4370-f04f-45f1-8c95-dd5d1563c786"), "1449373321", "Designing Data-Intensive Applications", "Martin Kleppman");
		storeBook(UUID.fromString("42dd4370-f04f-45f1-8c95-dd5d1563c787"), "1449373321", "Designing Data-Intensive Applications", "Martin Kleppman");
		storeBook(UUID.fromString("42dd4370-f04f-45f1-8c95-dd5d1563c788"), "1539692876", "Game Engine Black Book: Wolfenstein 3D", "Fabien Sanglard");

		// Lookup by ISBN
		mockMvc.perform(get("/v1/books/id/42dd4370-f04f-45f1-8c95-dd5d1563c786"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(content().json(
						"{" +
								"  'id' = '42dd4370-f04f-45f1-8c95-dd5d1563c786'," +
								"  'isbn' = '1449373321'," +
								"  'title' = 'Designing Data-Intensive Applications'," +
								"  'authors' = ['Martin Kleppman']" +
								"}"));
	}

    @Test
    public void lookupNonexistentBookById() throws Exception {
        // Lookup by ISBN
        mockMvc.perform(get("/v1/books/id/42dd4370-f04f-45f1-8c95-dd5d1563c786"))
                .andExpect(status().isNotFound());
    }

	/**
	 * Store a book into mongo.
	 */
    private void storeBook(UUID id, String isbn, String title, String author) {
    	Book book = new Book(
    			id,
				isbn,
				title,
				Collections.singletonList(author));

    	repository.save(book);
    }
}
