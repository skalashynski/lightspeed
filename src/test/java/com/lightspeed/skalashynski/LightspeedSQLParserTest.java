package com.lightspeed.skalashynski;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lightspeed.skalashynski.model.Query;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static com.lightspeed.skalashynski.parser.impl.SelectClauseParser.parseFullQuery;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class LightspeedSQLParserTest {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    static List<Arguments> sqlTestCases() throws Exception {
        ClassLoader classLoader = LightspeedSQLParserTest.class.getClassLoader();
        Path path = Paths.get(classLoader.getResource("sql_test_data.json").toURI());
        String read = Files.readString(path);
        var testCases = objectMapper.readValue(read, new TypeReference<List<TestCase>>() {
        });
        return testCases.stream().map(e -> Arguments.of(e.name, e)).toList();
    }

    @ParameterizedTest(name = "Parsing SQL: [{0}]")
    @MethodSource("sqlTestCases")
    void testParser(String name, TestCase testCase) {
        var actual = parseFullQuery(testCase.input);
        assertEquals(testCase.expected, actual, "Mismatch for SQL: " + testCase.input);
    }

    static class TestCase {
        public String name;
        public String input;
        public Query expected;
    }
}
