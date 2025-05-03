package com.example.bookstore.controller

import com.example.bookstore.model.Book
import com.example.bookstore.repository.BookRepository
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.transaction.annotation.Transactional
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class BookControllerIntegrationTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Autowired
    private lateinit var bookRepository: BookRepository

    @BeforeEach
    fun setUp() {
        bookRepository.deleteAll()
        bookRepository.saveAll(
            listOf(
                Book(title = "Book 1", author = "Author 1", price = 10.0),
                Book(title = "Book 2", author = "Author 2", price = 15.0)
            )
        )
    }

    @Test
    fun `should return all books`() {
        mockMvc.perform(get("/api/books"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.size()").value(2))
    }

    @Test
    fun `should return a book by id`() {
        val book = bookRepository.findAll().first()

        mockMvc.perform(get("/api/books/${book.id}"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(book.id))
            .andExpect(jsonPath("$.title").value(book.title))
    }

    @Test
    fun `should create a new book`() {
        val book = Book(title = "New Book", author = "New Author", price = 20.0)

        mockMvc.perform(
            post("/api/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(book))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").isNotEmpty)
            .andExpect(jsonPath("$.title").value("New Book"))
    }

    @Test
    fun `should update an existing book`() {
        val book = bookRepository.findAll().first()
        val updatedBook = book.copy(title = "Updated Book", author = "Updated Author", price = 25.0)

        mockMvc.perform(
            put("/api/books/${book.id}")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedBook))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.title").value("Updated Book"))
    }

    @Test
    fun `should delete a book`() {
        val book = bookRepository.findAll().first()

        mockMvc.perform(delete("/api/books/${book.id}"))
            .andExpect(status().isNoContent)

        assert(bookRepository.findById(book.id).isEmpty)
    }
}