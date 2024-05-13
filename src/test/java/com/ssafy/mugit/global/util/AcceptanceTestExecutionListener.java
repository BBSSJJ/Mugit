package com.ssafy.mugit.global.util;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;

import java.util.List;

public class AcceptanceTestExecutionListener extends AbstractTestExecutionListener {

    @Override
    public void afterTestMethod(final TestContext testContext) {
        final JdbcTemplate jdbcTemplate = testContext.getApplicationContext().getBean(JdbcTemplate.class);
        final List<String> truncateQueries = getTruncateQuery(jdbcTemplate);
        final List<String> populateQueries = getPopulateQuery(jdbcTemplate);
        truncateTables(jdbcTemplate, truncateQueries);
        populateTables(jdbcTemplate, populateQueries);
    }

    private List<String> getTruncateQuery(final JdbcTemplate jdbcTemplate) {
        return jdbcTemplate.queryForList("SELECT CONCAT('TRUNCATE TABLE ', TABLE_NAME, ';') AS list FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = 'mugit_test_db'", String.class);
    }

    private List<String> getPopulateQuery(final JdbcTemplate jdbcTemplate) {
        return jdbcTemplate.queryForList("SELECT CONCAT('INSERT INTO ', TABLE_NAME, ' VALUES ( 50 );') AS list FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = 'mugit_test_db' AND TABLE_NAME LIKE '%_seq'", String.class);
    }

    private void truncateTables(final JdbcTemplate jdbcTemplate, final List<String> truncateQueries) {
        execute(jdbcTemplate, "SET FOREIGN_KEY_CHECKS = 0");
        truncateQueries.forEach(v -> execute(jdbcTemplate, v));
        execute(jdbcTemplate, "SET FOREIGN_KEY_CHECKS = 1");
    }

    private void populateTables(JdbcTemplate jdbcTemplate, List<String> populateQueries) {
        populateQueries.forEach(v -> execute(jdbcTemplate, v));
    }

    private void execute(final JdbcTemplate jdbcTemplate, final String query) {jdbcTemplate.execute(query);}

}
