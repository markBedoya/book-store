package com.example.bookstore.controller

import com.example.bookstore.model.Book
import com.example.bookstore.service.BookService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/books")
class BookController(private val bookService: BookService) {

    @GetMapping
    fun getAllBooks(): List<Book> = bookService.getAllBooks()

    @GetMapping("/{id}")
    fun getBookById(@PathVariable id: Long): ResponseEntity<Book> =
        ResponseEntity.ok(bookService.getBookById(id))

    @PostMapping
    fun createBook(@RequestBody book: Book): ResponseEntity<Book> =
        ResponseEntity.ok(bookService.createBook(book))

    @PutMapping("/{id}")
    fun updateBook(@PathVariable id: Long, @RequestBody book: Book): ResponseEntity<Book> =
        ResponseEntity.ok(bookService.updateBook(id, book))

    @DeleteMapping("/{id}")
    fun deleteBook(@PathVariable id: Long): ResponseEntity<Unit> {
        bookService.deleteBook(id)
        return ResponseEntity.noContent().build()
    }
}