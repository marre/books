package org.marre.books.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Value;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.UUID;

/**
 * Represents a Book in the REST API.
 *
 * It is also used as the document stored in Mongo. Not ideal, but
 * good enough for this project.
 */
@Value
@ApiModel(description = "Book")
@Document
public class Book {
    @Id
    @ApiModelProperty(value = "The book identity", example = "42dd4370-f04f-45f1-8c95-dd5d1563c786", required = true)
    private final UUID id;

    @ApiModelProperty(value = "ISBN-10", example = "1449373321", required = true)
    private final String isbn;

    @ApiModelProperty(value = "Book title", example = "Designing Data-Intensive Applications", required = true)
    private final String title;

    @ApiModelProperty(value = "Authors", required = true)
    private final List<String> authors;
}
