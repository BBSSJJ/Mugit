package com.ssafy.mugit.global.util;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;

import java.util.List;

public class AcceptanceTestExecutionListener extends AbstractTestExecutionListener {

    @Override
    public void afterTestMethod(final TestContext testContext) {
        final JdbcTemplate jdbcTemplate = getJdbcTemplate(testContext);
        final List<String> truncateQueries = getTruncateQueries(jdbcTemplate);
        truncateTables(jdbcTemplate, truncateQueries);
    }

    private List<String> getTruncateQueries(final JdbcTemplate jdbcTemplate) {
        return jdbcTemplate.queryForList("SELECT CONCAT('TRUNCATE TABLE ', TABLE_NAME, ';') AS list FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = 'mugit_test_db'", String.class);
    }

    private JdbcTemplate getJdbcTemplate(final TestContext testContext) {
        return testContext.getApplicationContext().getBean(JdbcTemplate.class);
    }

    private void truncateTables(final JdbcTemplate jdbcTemplate, final List<String> truncateQueries) {
        execute(jdbcTemplate, "SET FOREIGN_KEY_CHECKS = 0");
        truncateQueries.forEach(v -> execute(jdbcTemplate, v));
        execute(jdbcTemplate, "SET FOREIGN_KEY_CHECKS = 1");
    }

    private void execute(final JdbcTemplate jdbcTemplate, final String query) {
        jdbcTemplate.execute(query);
    }

}
