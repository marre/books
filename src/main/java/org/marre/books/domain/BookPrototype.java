package org.marre.books.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.UUID;

/**
 * Used in the REST API when creating a new book.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "Book")
public class BookPrototype {
    @ApiModelProperty(value = "ISBN-10", example = "1449373321", required = true)
    @NotNull
    @Length(min = 10, max = 10)
    @Pattern(regexp = "[0-9]+")
    private String isbn;

    @ApiModelProperty(value = "Book title", example = "Designing Data-Intensive Applications", required = true)
    @NotBlank
    private String title;

    @ApiModelProperty(value = "Authors", required = true)
    @NotNull
    @Size(min=1)
    private List<String> authors;

    /**
     * Create a new Book by assigning a unique ID to it.
     */
    public Book createBook() {
        return new Book(
                UUID.randomUUID(),
                isbn,
                title,
                authors);
    }
}
