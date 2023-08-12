Annotations. Working with Java 17. (May work with other versions, but it was not tested).

## Available annotations

### @ValidPagebleSortParam

The annotation `@ValidPagebleSortParam` can be used on Pageable objects,
to restrict values that can be passed as a sort order, which are passed e.g. as a query param under the value "sort".

### @NoForbiddenCharSequences

The annotation `@NoForbiddenCharSequences` checks if a String contains disallowed CharSequences.
By default, separate chars < > % $ & are forbidden, but this can be overwritten by passing arguments to the annotation
e.g.  `@NoForbiddenCharSequences(field = {"a","bc","123"})`. The annotation can also be used with objects. If so, the
fields with the type String of the objects are validated.

### @NullOrNotBlank

Validates a String parameter or field. A String is valid, if it is null or not blank.

## Usage

### Annotations

Feel free to copy the code. You can test it in your project by building it with `mvn clean install` and then
import it from your local maven repository as a dependency to your project:

```
<dependencies>
    <!-- other dependencies -->
    <!--  ...  -->
    <dependency>
        <groupId>xyz.adolphium</groupId>
        <artifactId>adolphium-annotations</artifactId>
        <version>1.0.0-SNAPSHOT</version
    </dependency>
</dependencies>
```

## todo

- cleanup project
- write a more readable README.md; add more annotations
- release version and upload to maven repository
