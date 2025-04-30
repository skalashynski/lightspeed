package com.lightspeed.skalashynski;

import com.lightspeed.skalashynski.model.Query;

import java.util.List;

import static com.lightspeed.skalashynski.parser.impl.SelectClauseParser.parseFullQuery;

/**
 * Company: https://www.lightspeedhq.com/
 * Company task: https://github.com/Ecwid/new-job/blob/master/SQL-parser.md
 * todo: предусмотреть что SQL может быть некорректный
 * todo: написать README документацию также нужно.
 * todo: написать документацию к классам
 */

public class SQLParser {


    private static final List<String> queries = List.of(
            """
                    select * from (select * from A) a_alias
                    """,
            """
                    SELECT * FROM products WHERE category = 'electronics' AND price > 100
                    """,
            """
                       SELECT author.name, count(book.id), sum(book.cost)
                       FROM author a
                       LEFT JOIN book b ON (a.id = b.author_id)
                       GROUP BY a.name HAVING COUNT(*) > 1 AND SUM(b.cost) > 500
                       LIMIT 10
                    """,
            """
                        SELECT author.name, count(book.id), sum(book.cost)
                        FROM author
                        LEFT JOIN book ON (author.id = book.author_id)
                        WHERE book.cost > 100
                        GROUP BY author.name
                        HAVING COUNT(*) > 1
                        ORDER BY sum(book.cost) DESC
                        LIMIT 10
                        OFFSET 5
                    """,
            """
                    
                    """
    );

    public static void main(String[] args) {

        queries.forEach(sql -> {
            Query query = parseFullQuery(sql.trim());
            System.out.println("SQL: " + sql);
            System.out.println("Columns: " + query.getColumns());
            System.out.println("From Sources: " + query.getFromSources());
            System.out.println("Joins: " + query.getJoins());
            System.out.println("Where Clauses: " + query.getWhere());
            System.out.println("Group By Columns: " + query.getGroupBy());
            System.out.println("Sort Columns: " + query.getSortColumns());
            System.out.println("Limit: " + query.getLimit());
            System.out.println("Offset: " + query.getOffset());
            System.out.println("------------------");
        });


    }
}