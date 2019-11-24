package com.garethhenriksen.IOT.model;

//import java.util.HashSet;
//import java.util.Set;
//import java.util.UUID;
//
//
//import org.springframework.data.cassandra.core.cql.Ordering;
//import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
//import org.springframework.data.cassandra.core.mapping.Column;
//import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
//import org.springframework.data.cassandra.core.mapping.Table;
//
//@Table
//public class Book {
//    @PrimaryKeyColumn(
//            name = "isbn",
//            ordinal = 2,
//            type = PrimaryKeyType.CLUSTERED,
//            ordering = Ordering.DESCENDING)
//    private UUID id;
//    @PrimaryKeyColumn(
//            name = "title", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
//    private String title;
//    @PrimaryKeyColumn(
//            name = "publisher", ordinal = 1, type = PrimaryKeyType.PARTITIONED)
//    private String publisher;
//    @Column
//    private Set<String> tags = new HashSet<>();
//    // standard getters and setters
//}