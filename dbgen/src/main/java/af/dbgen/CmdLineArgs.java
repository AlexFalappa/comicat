package af.dbgen;

import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.Option;

/**
 * Command line arguments for af.dbgen.CreateApp.
 * Uses args4j library.
 * Created by sasha on 01/06/15.
 */
public class CmdLineArgs {
    @Option(name = "d", metaVar = "DRIVER_CLASS", usage = "JDBC driver class")
    String jdbcDriver;
    @Option(name = "l", metaVar = "DB_URL", usage = "JDBC database URL")
    String jdbcUrl;
    @Option(name = "u", metaVar = "DB_USER", usage = "JDBC database user name")
    String jdbcUser;
    @Option(name = "p", metaVar = "DB_PASSW", usage = "JDBC database user password")
    String jdbcPassw;
    @Option(name = "x", usage = "Test export as XML")
    boolean xmlExport = false;
    @Argument(metaVar = "REC_NUM", usage = "Number of comic record to generate. Default 10.")
    int recNum = 10;
}
