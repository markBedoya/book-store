package com.example.bookstore.service

import com.example.bookstore.model.Book
import com.example.bookstore.repository.BookRepository
import com.example.bookstore.exception.BookNotFoundException
import org.springframework.stereotype.Service

@Service
class BookService(private val bookRepository: BookRepository) {

    fun getAllBooks(): List<Book> = bookRepository.findAll()

    fun getBookById(id: Long): Book =
        bookRepository.findById(id).orElseThrow { BookNotFoundException("Book not found with id: $id") }

    fun createBook(book: Book): Book = bookRepository.save(book)

    fun updateBook(id: Long, updatedBook: Book): Book {
        val existingBook = getBookById(id)
        val bookToSave = existingBook.copy(
            title = updatedBook.title,
            author = updatedBook.author,
            price = updatedBook.price
        )
        return bookRepository.save(bookToSave)
    }

    fun deleteBook(id: Long) {
        if (!bookRepository.existsById(id)) {
            throw BookNotFoundException("Book not found with id: $id")
        }
        bookRepository.deleteById(id)
    }

    //create a method that takes a list of books and orders them by price
    fun orderBooksByPrice(books: List<Book>): List<Book> {
        return books.sortedBy { it.price }
    }

}