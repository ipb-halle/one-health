package ipbhalle.de.ontologymanagerserver.utils.security;

public class StringProcessing {

    private static final String[] SQL_INJECTION_PATTERNS = {
            "('.+--)|(--)|(%7C)",       // Basic patterns
            "' OR '1'='1",              // Common injection
            "UNION(.*?)SELECT",         // UNION-based injection
            "SELECT(.*?)FROM",          // Generic SELECT query
            "INSERT(.*?)INTO",          // Generic INSERT query
            "UPDATE(.*?)SET",           // Generic UPDATE query
            "DELETE FROM",              // Generic DELETE query
            "DROP TABLE",               // Generic DROP TABLE query
            "' OR 'x'='x",              // Another common injection
            "EXEC(.*?)xp_",             // Execution of stored procedures
            "SLEEP\\(",                 // Time-based injection
            "--",                       // Comment in SQL
            ";"                         // End of SQL statement
    };

    public static boolean isSQLInjection(String input) {
        for (String pattern : SQL_INJECTION_PATTERNS) {
            if (input.toUpperCase().matches(pattern)) {
                return true;
            }
        }
        return false;
    }

}
