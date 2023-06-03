Annotations. Working with Java 17. (May workt with other versions, but it was not tested).

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
## todo
- cleanup project
- write a more readable README.md; add more annotations
